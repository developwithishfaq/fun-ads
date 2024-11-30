package com.funn.ads.ad_units.banner

import android.app.Activity
import androidx.lifecycle.Lifecycle
import com.funn.ads.ad_units.natives.initPlacement
import com.funn.ads.sdk.FunAdsSdk
import com.monetization.adsmain.commons.sdkBannerAd
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.ui.ShimmerInfo


fun AdsUiWidget.sdkBannerAdFun(
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    activity: Activity,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    showOnlyIfAdAvailable: Boolean = false,
    defaultEnable: Boolean = true,
    listener: UiAdsListener? = null
) {
    val model = FunAdsSdk.getUiAdByKey(placementKey)
    if (model != null) {
        initPlacement(
            activity = activity,
            placementKey = placementKey,
            lifecycle = lifecycle
        )
    } else {
        sdkBannerAd(
            adKey = adKey,
            placementKey = placementKey,
            lifecycle = lifecycle,
            activity = activity,
            showShimmerLayout = showShimmerLayout,
            requestNewOnShow = requestNewOnShow,
            showNewAdEveryTime = showNewAdEveryTime,
            showOnlyIfAdAvailable = showOnlyIfAdAvailable,
            defaultEnable = defaultEnable,
            listener = listener
        )
    }
}