package com.funn.ads.sdk

import android.util.Log
import androidx.annotation.RawRes
import com.example.rewadedad.AdmobRewardedAdsController
import com.example.rewadedad.AdmobRewardedAdsManager
import com.example.rewardedinterads.AdmobRewardedInterAdsController
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.funn.ads.configs.FunConfigs
import com.funn.ads.constants.toCounterStrategy
import com.funn.ads.constants.toIntOrZero
import com.funn.ads.domain.model.ActionFiltered
import com.funn.ads.domain.model.ControllerModel
import com.funn.ads.domain.model.UiAdFiltered
import com.funn.ads.models.jsonModel.AdsJsonModel
import com.funn.ads.models.jsonModel.CountersEntryModel
import com.funn.ads.models.jsonModel.UiAdsModel
import com.funn.ads.units.interAds.logFunSdk
import com.funn.ads.units.interAds.toBoolean
import com.monetization.adsmain.commons.addNewController
import com.monetization.appopen.AdmobAppOpenAdsController
import com.monetization.appopen.AdmobAppOpenAdsManager
import com.monetization.bannerads.AdmobBannerAdsController
import com.monetization.bannerads.AdmobBannerAdsManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.counters.CounterInfo
import com.monetization.core.counters.CounterManager
import com.monetization.core.counters.CounterStrategies
import com.monetization.core.listeners.ControllersListener
import com.monetization.interstitials.AdmobInterstitialAdsController
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsController
import com.monetization.nativeads.AdmobNativeAdsManager
import kotlinx.serialization.json.Json

object FunAdsSdk {


    private val json = Json {
        explicitNulls = true
        this.prettyPrint = true
        this.ignoreUnknownKeys = true
        this.isLenient = true
    }

    private var adsData: AdsJsonModel? = null

    private var registeredActions = HashMap<String, ActionFiltered>()
    private var registeredUiAds = HashMap<String, UiAdFiltered>()

    fun getAdsJsonModel(): AdsJsonModel? {
        return adsData
    }

    fun getActionByKey(key: String) = registeredActions[key]
    fun getUiAdByKey(key: String) = registeredUiAds[key]


    fun String.toAdsJsonModel(): AdsJsonModel? {
        return try {
            return json.decodeFromString<AdsJsonModel?>(this)
        } catch (e: Exception) {
            logFunSdk("Exception toAdsJsonModel=${e.message}")
            null
        }
    }

    fun initialize(
        jsonModel: AdsJsonModel,
        localControllers: List<ControllerModel>,
        controllersListener: ControllersListener? = null,
        onInitialized: () -> Unit
    ) {
        logFunSdk("remoteConfigsFetched: $jsonModel")
        adsData = jsonModel
        val controllers = localControllers.plus(adsData?.getControllers() ?: emptyList())
        addControllers(controllers, controllersListener)

        registerActions(controllers)
        registeredUiAds = registerPlacements()
        onInitialized.invoke()
    }

    private fun registerPlacements(): HashMap<String, UiAdFiltered> {
        val placements = HashMap<String, UiAdFiltered>()
        adsData?.uiAds?.let {
            val allAds = it.ads ?: emptyList()
            it.registered?.forEach { registeredAd ->
                if (
                    registeredAd.placementKey.isNotBlank() &&
                    registeredAd.enabled.toBoolean() &&
                    registeredAd.adName.isNotBlank()
                ) {
                    val model = allAds.firstOrNull { it.adName == registeredAd.adName }
                    if (model != null) {
                        placements[registeredAd.placementKey] = UiAdFiltered(
                            placement = model,
                            adType = if (model.layout.isBlank()) {
                                AdType.BANNER
                            } else {
                                AdType.NATIVE
                            },
                            controller = model.controller,
                        )
                    }
                }
            }
        }
        return placements
    }

    private fun registerActions(controllers: List<ControllerModel>) {
        val actions = adsData.getAllFilteredActions(controllers)
        val countersList = adsData.getCounters()
        addCounters(countersList)
        registeredActions = actions
    }

    private fun addCounters(countersList: List<CountersEntryModel>) {
        countersList.forEach { counter ->
            if (counter.name.isNotBlank() && counter.enabled.toBoolean()) {
                CounterManager.createACounter(
                    key = counter.name, info = CounterInfo(
                        maxPoint = counter.maxPoint.toIntOrZero(),
                        currentPoint = counter.currentPoint.toIntOrZero(),
                        adShownStrategy = counter.adShownStrategy.toCounterStrategy(),
                        adNotShownStrategy = counter.notShownStrategy.toCounterStrategy()
                    )
                )
            }
        }
    }

    private fun addControllers(list: List<ControllerModel>, listener: ControllersListener? = null) {
        list.forEach { model ->
            when (model.adType) {
                AdType.NATIVE -> {
                    AdmobNativeAdsManager.addNewController(
                        AdmobNativeAdsController(
                            adKey = model.adKey,
                            adIdsList = model.adIds,
                            listener = listener,
                        ),
                        true
                    )
                }

                AdType.INTERSTITIAL -> {
                    AdmobInterstitialAdsManager.addNewController(
                        controller = AdmobInterstitialAdsController(
                            model.adKey, model.adIds, listener
                        ),
                        replace = true
                    )
                }

                AdType.REWARDED -> AdmobRewardedAdsManager.addNewController(
                    AdmobRewardedAdsController(
                        adKey = model.adKey,
                        adIdsList = model.adIds,
                        listener = listener
                    ),
                    true
                )

                AdType.REWARDED_INTERSTITIAL -> AdmobRewardedInterAdsManager.addNewController(
                    AdmobRewardedInterAdsController(
                        adKey = model.adKey,
                        adIdsList = model.adIds,
                        listener = listener
                    ),
                    true
                )

                AdType.BANNER -> {
                    model.bannerAdType?.let {
                        AdmobBannerAdsManager.addNewController(
                            AdmobBannerAdsController(
                                adKey = model.adKey, adIdsList = model.adIds,
                                bannerAdType = it, listener = listener
                            ),
                            true
                        )
                    } ?: run {
                        throw IllegalArgumentException("Banner Ad Type Cannot Be Null For Key ${model.adKey}")
                    }
                }

                AdType.AppOpen -> AdmobAppOpenAdsManager.addNewController(
                    AdmobAppOpenAdsController(
                        adKey = model.adKey,
                        adIdsList = model.adIds,
                        listener = listener
                    ),
                    true
                )
            }
        }
    }


}
