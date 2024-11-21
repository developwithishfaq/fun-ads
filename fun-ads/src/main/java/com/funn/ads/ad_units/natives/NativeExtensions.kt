package com.funn.ads.ad_units.natives

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.funn.ads.models.jsonModel.placements.toAdsWidgetData
import com.funn.ads.sdk.FunAdsSdk
import com.funn.ads.units.interAds.logFunSdk
import com.funn.ads.units.interAds.toBoolean
import com.monetization.adsmain.commons.sdkBannerAd
import com.monetization.adsmain.commons.sdkNativeAdd
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.NativeConstants.getAdLayout
import com.monetization.core.commons.Utils.resToView
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo


fun String.funAdsShimmer(context: Context): ShimmerInfo {
    return if (contains("given", true)) {
        ShimmerInfo.GivenLayout()
    } else {
        if (isNotBlank()) {
            try {
                val view = this.getAdLayout(context)
                ShimmerInfo.ShimmerByView(view)
            } catch (_: Exception) {
                ShimmerInfo.None
            }
        } else {
            ShimmerInfo.None
        }
    }
}

fun AdsUiWidget.sdkNativeAdFun(
    adLayout: LayoutInfo,
    adKey: String,
    placementKey: String,
    lifecycle: Lifecycle,
    activity: Activity,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    showOnlyIfAdAvailable: Boolean = false,
    showFromHistory: Boolean = false,
    defaultEnable: Boolean = true,
    adsWidgetData: AdsWidgetData? = null,
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
        sdkNativeAdd(
            adLayout = adLayout,
            adKey = adKey,
            placementKey = placementKey,
            lifecycle = lifecycle,
            activity = activity,
            showShimmerLayout = showShimmerLayout,
            requestNewOnShow = requestNewOnShow,
            showNewAdEveryTime = showNewAdEveryTime,
            showOnlyIfAdAvailable = showOnlyIfAdAvailable,
            showFromHistory = showFromHistory,
            defaultEnable = defaultEnable,
            adsWidgetData = adsWidgetData,
            listener = listener
        )
    }
}


fun AdsUiWidget.initPlacement(activity: Activity, placementKey: String, lifecycle: Lifecycle) {
    val model = FunAdsSdk.getUiAdByKey(placementKey)
    logFunSdk("Placement(placementKey=$placementKey,controller=${model?.controller})=$model")
    Toast.makeText(activity, "$placementKey = $model", Toast.LENGTH_SHORT).show()
    if (model?.placement != null) {
        val placement = model.placement!!
        when (model.adType) {
            AdType.NATIVE -> {
                sdkNativeAdd(
                    adLayout = LayoutInfo.LayoutByName(placement.layout),
                    adKey = placement.controller,
                    placementKey = placementKey,
                    lifecycle = lifecycle,
                    activity = activity,
                    showShimmerLayout = placement.shimmer.funAdsShimmer(activity),
                    requestNewOnShow = placement.requestNewOnShow.toBoolean(),
                    showNewAdEveryTime = placement.showNewAdEveryTime.toBoolean(),
                    showOnlyIfAdAvailable = placement.showOnlyIfAdAvailable.toBoolean(),
                    adsWidgetData = placement.adsWidgetData?.toAdsWidgetData()
                )
            }

            AdType.BANNER -> {
                sdkBannerAd(
                    adKey = placement.controller,
                    placementKey = placementKey,
                    lifecycle = lifecycle,
                    activity = activity,
                    showShimmerLayout = placement.shimmer.funAdsShimmer(activity),
                    requestNewOnShow = placement.requestNewOnShow.toBoolean(),
                    showNewAdEveryTime = placement.showNewAdEveryTime.toBoolean(),
                    showOnlyIfAdAvailable = placement.showOnlyIfAdAvailable.toBoolean()
                )
            }

            else -> {
                logFunSdk("This Ad Type Is Not Suitable For On Ui Ads", true)
            }
        }
    }
}