package com.funn.ads.ad_units.natives

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import com.funn.ads.models.jsonModel.placements.toAdsWidgetData
import com.funn.ads.sdk.FunAdsSdk
import com.funn.ads.units.interAds.logFunSdk
import com.funn.ads.units.interAds.toBoolean
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.composeviews.GenericViewModelFactory
import com.monetization.composeviews.SdkBanner
import com.monetization.composeviews.SdkNativeAd
import com.monetization.composeviews.rememberNativeAdUiWidget
import com.monetization.composeviews.statefull.nativeAd.OnScreenAdsViewModel
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.NativeTemplates
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo


@Composable
fun InitPlacement(
    placementKey: String
) {
    val activity = LocalContext.current
    (activity as? Activity)?.let {
        val model = FunAdsSdk.getUiAdByKey(placementKey)
        logFunSdk("Placement(placementKey=$placementKey,controller=${model?.controller})=$model")
        Toast.makeText(activity, "$placementKey = $model", Toast.LENGTH_SHORT).show()
        if (model?.placement != null) {
            val placement = model.placement!!
            when (model.adType) {
                AdType.NATIVE -> {
                    SdkNativeAd(
                        adLayout = LayoutInfo.LayoutByName(placement.layout ?: NativeTemplates.SmallNative),
                        adKey = placement.controller,
                        placementKey = placementKey,
                        activity = activity,
                        showShimmerLayout = placement.shimmer.funAdsShimmer(activity),
                        requestNewOnShow = placement.requestNewOnShow.toBoolean(),
                        showNewAdEveryTime = placement.showNewAdEveryTime.toBoolean(),
                        adsWidgetData = placement.adsWidgetData?.toAdsWidgetData()
                    )
                }

                AdType.BANNER -> {
                    SdkBanner(
                        adKey = placement.controller,
                        placementKey = placementKey,
                        activity = activity,
                        showShimmerLayout = placement.shimmer.funAdsShimmer(activity),
                        requestNewOnShow = placement.requestNewOnShow.toBoolean(),
                        showNewAdEveryTime = placement.showNewAdEveryTime.toBoolean()
                    )
                }

                else -> {

                }
            }
        }
    } ?: run {
        logFunSdk("Activity Is Null On Placement Key of $placementKey", true)
    }
}


@Composable
fun SdkNativeAdFun(
    activity: Activity,
    placementKey: String,
    adKey: String,
    adLayout: LayoutInfo,
    modifier: Modifier = Modifier,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    adsWidgetData: AdsWidgetData? = null,
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    listener: UiAdsListener? = null,
): AdsUiWidget? {
    val model by remember {
        mutableStateOf(FunAdsSdk.getUiAdByKey(placementKey))
    }
    if (model != null) {
        InitPlacement(
            placementKey = placementKey
        )
        return null
    } else {
        return SdkNativeAd(
            activity = activity,
            placementKey = placementKey,
            adKey = adKey,
            adLayout = adLayout,
            modifier = modifier,
            showShimmerLayout = showShimmerLayout,
            adsWidgetData = adsWidgetData,
            requestNewOnShow = requestNewOnShow,
            showNewAdEveryTime = showNewAdEveryTime,
            listener = listener
        )
    }
}


