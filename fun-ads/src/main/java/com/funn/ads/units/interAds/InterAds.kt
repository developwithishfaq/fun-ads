package com.funn.ads.units.interAds

import android.app.Activity
import android.util.Log
import android.view.View
import com.funn.ads.domain.model.AdKeyWithType
import com.funn.ads.domain.model.ShowFiltered
import com.funn.ads.sdk.FunAdsSdk
import com.monetization.adsmain.commons.getAdController
import com.monetization.adsmain.commons.getAdTypeByKey
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.showRates.full_screen_ads.FullScreenAdsShowManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.counters.CounterManager.isCounterRegistered


fun View.setOnActionClickListener(
    activity: Activity, actionKey: String, onClicked: (adShown: Boolean) -> Unit
) {
    setOnClickListener {
        val model = FunAdsSdk.getActionByKey(actionKey)
        logFunSdk("Action($actionKey) = $model")
        if (model != null) {
            loadAds(model.adsToLoad, activity)
            if (model.adToShow != null && model.canOverride.not()) {
                showFullScreenAd(activity, model.adToShow) {
                    onClicked.invoke(it)
                }
            } else {
                onClicked.invoke(false)
            }
        } else {
            onClicked.invoke(false)
        }
    }
}

fun loadAds(ads: List<AdKeyWithType>, activity: Activity) {
    ads.forEach {
        it.adKey.loadAd(
            "SDK_TRUE",
            activity = activity,
            adType = it.adType
        )
    }
}

fun showFullScreenAd(
    activity: Activity,
    showFiltered: ShowFiltered?,
    onDismiss: (Boolean) -> Unit
) {
    logFunSdk("Showing Ad ${showFiltered?.adKey} = $showFiltered")
    showFiltered?.let {
        val template = it.template
        FullScreenAdsShowManager.showFullScreenAd(
            placementKey = "SDK_TRUE",
            key = showFiltered.adKey,
            adType = showFiltered.adType,
            isInstantAd = template.isInstant.toBoolean(),
            activity = activity,
            requestNewIfAdShown = template.requestNewOnShow.toBoolean(),
            requestNewIfNotAvailable = template.loadNewIfNotAvailable.toBoolean(),
            normalLoadingTime = template.normalLoadingTime.toLong(),
            instantLoadingTime = template.instantLoadingTime.toLong(),
            counterKey = if (template.counterKey?.isCounterRegistered() == true) {
                template.counterKey
            } else {
                null
            },
            onAdDismiss = { it, msg ->
                onDismiss.invoke(it)
            }
        )
    } ?: run {
        onDismiss.invoke(false)
    }
}

fun Int.toBoolean() = this == 1


fun logFunSdk(msg: String, error: Boolean = false) {
    if (error) {
        Log.e("funxads", "logFunSdk:$msg")
    } else {
        Log.d("funxads", "logFunSdk:$msg")
    }
}