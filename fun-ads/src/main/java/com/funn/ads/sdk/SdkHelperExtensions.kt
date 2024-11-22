package com.funn.ads.sdk

import com.funn.ads.domain.model.ActionFiltered
import com.funn.ads.domain.model.AdKeyWithType
import com.funn.ads.domain.model.ControllerModel
import com.funn.ads.domain.model.ShowFiltered
import com.funn.ads.models.jsonModel.AdsJsonModel
import com.funn.ads.models.jsonModel.CountersEntryModel
import com.funn.ads.models.jsonModel.action.ActionsModel
import com.funn.ads.models.jsonModel.action.ShowAdModel
import com.funn.ads.models.jsonModel.toBannerAdType
import com.funn.ads.models.jsonModel.toEnumAdType
import com.funn.ads.units.interAds.toBoolean
import com.monetization.core.ad_units.core.AdType

fun AdsJsonModel?.getAllFilteredActions(controllers: List<ControllerModel>): HashMap<String, ActionFiltered> {
    val filteredActions = HashMap<String, ActionFiltered>()
    this?.actions?.forEach { action: ActionsModel ->
        val filteredModel = action.toActionFiltered(controllers, this)
        if (filteredModel != null) {
            filteredActions[action.actionKey!!] = filteredModel
        }
    }
    return filteredActions
}

fun ActionsModel.toActionFiltered(
    controllers: List<ControllerModel>,
    adsJsonModel: AdsJsonModel
): ActionFiltered? {
    val action = this
    return if (action.enabled.toBoolean() && action.actionKey.isNullOrBlank().not()) {
        val adsToLoad = mutableListOf<AdKeyWithType>()
        val filteredAction = ActionFiltered()

        if (action.loadAds.isNullOrEmpty().not()) {
            action.loadAds?.forEach {
                val controller = controllers.firstOrNull { cont ->
                    cont.adKey == it
                }
                controller?.let {
                    adsToLoad.add(
                        AdKeyWithType(adKey = it.adKey, adType = it.adType)
                    )
                }
            }
            filteredAction.adsToLoad = adsToLoad
        }
        action.showAds?.let { adToShow: ShowAdModel ->
            val template = adsJsonModel.templates?.list ?: emptyList()
            val index = template.indexOfFirst {
                it.templateKey == adToShow.templateKey
            }
            if (index != -1) {
                filteredAction.adToShow = ShowFiltered(
                    adKey = adToShow.controller,
                    adType = controllers.firstOrNull { it.adKey == adToShow.controller }?.adType
                        ?: AdType.INTERSTITIAL,
                    template = template[index],
                )
                filteredAction.canOverride = action.canOverride.toBoolean()
            }
        }
        if (filteredAction.adsToLoad.isNotEmpty() || filteredAction.adToShow != null) {
            filteredAction
        } else {
            null
        }
    } else {
        null
    }
}

fun AdsJsonModel?.getControllers(): List<ControllerModel> {
    return this?.let {
        val controllers = (controllers?.list ?: emptyList()).mapNotNull {
            if (it.enabled.toBoolean()) {
                it.type.toEnumAdType()?.let { type ->
                    ControllerModel(
                        adKey = it.key,
                        adType = type,
                        adIds = it.adIds,
                        bannerAdType = it.bannerAdType.toBannerAdType()
                    )
                }
            } else {
                null
            }
        }
        controllers
    } ?: emptyList()
}

fun AdsJsonModel?.getCounters(): List<CountersEntryModel> {
    return this?.let {
        counters?.list ?: emptyList()
    } ?: emptyList()
}