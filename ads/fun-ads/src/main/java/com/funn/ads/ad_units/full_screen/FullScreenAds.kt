package com.funn.ads.ad_units.full_screen

import android.app.Activity
import com.funn.ads.domain.model.ActionFiltered
import com.funn.ads.sdk.FunAdsSdk
import com.monetization.adsmain.showRates.full_screen_ads.FullScreenAdsShowManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.msgs.MessagesType


object FullScreenAdsShowManagerFun {

    fun showFullScreenAd(
        placementKey: String,
        key: String,
        onAdDismiss: (Boolean, MessagesType?) -> Unit,
        activity: Activity,
        isInstantAd: Boolean = false,
        uiAdsListener: UiAdsListener? = null,
        adType: AdType,
        requestNewIfNotAvailable: Boolean = false,
        requestNewIfAdShown: Boolean = false,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        counterKey: String? = null,
        onRewarded: ((Boolean) -> Unit)? = null,
        onCounterUpdate: ((Int) -> Unit)? = null,
    ) {
        val model: ActionFiltered? = FunAdsSdk.getActionByKey(placementKey)
        if (model?.adToShow != null && model.canOverride) {
            com.funn.ads.units.interAds.showFullScreenAd(activity, model.adToShow) {
                onAdDismiss.invoke(it,null)
            }
        } else {
            FullScreenAdsShowManager.showFullScreenAd(
                placementKey = placementKey,
                key = key,
                onAdDismiss = onAdDismiss,
                activity = activity,
                isInstantAd = isInstantAd,
                uiAdsListener = uiAdsListener,
                adType = adType,
                requestNewIfNotAvailable = requestNewIfNotAvailable,
                requestNewIfAdShown = requestNewIfAdShown,
                normalLoadingTime = normalLoadingTime,
                instantLoadingTime = instantLoadingTime,
                counterKey = counterKey,
                onRewarded = onRewarded,
                onCounterUpdate = onCounterUpdate
            )
        }
    }

}