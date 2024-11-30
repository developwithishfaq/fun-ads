package com.monetization.composeviews

import android.app.Activity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import com.monetization.adsmain.widgets.AdsUiWidget
import com.monetization.composeviews.statefull.nativeAd.OnScreenAdsViewModel
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.listeners.UiAdsListener
import com.monetization.core.ui.AdsWidgetData
import com.monetization.core.ui.LayoutInfo
import com.monetization.core.ui.ShimmerInfo

@Composable
fun rememberNativeAdUiWidget(
    activity: Activity,
    placementKey: String,
    adKey: String,
    adLayout: LayoutInfo,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    adsWidgetData: AdsWidgetData? = null,
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    showOnlyIfAdAvailable: Boolean = false,
    onScreenAdsViewModel: OnScreenAdsViewModel = viewModel(
        factory = GenericViewModelFactory(OnScreenAdsViewModel::class.java) {
            OnScreenAdsViewModel()
        }
    ),
    listener: UiAdsListener? = null,
): AdsUiWidget {
    val lifecycleOwner = LocalSavedStateRegistryOwner.current
    val state by onScreenAdsViewModel.state.collectAsState()
    val view = if (state.adPlacements[placementKey] == null) {
        AdsUiWidget(activity).apply {
            attachWithLifecycle(
                lifecycle = lifecycleOwner.lifecycle,
                forBanner = false,
                isJetpackCompose = true
            )
            setWidgetKey(
                placementKey = placementKey, adKey = adKey, isNativeAd = true,
                model = adsWidgetData,
                defEnabled = true
            )
            showNativeAdmob(
                activity = activity,
                adLayout = adLayout,
                adKey = adKey,
                shimmerInfo = showShimmerLayout,
                oneTimeUse = showNewAdEveryTime,
                requestNewOnShow = requestNewOnShow,
                listener = listener,
                showOnlyIfAdAvailable = showOnlyIfAdAvailable
            )
        }
    } else {
        state.adPlacements[placementKey]?.widget
    }
    return view!!
}


@Composable
fun SdkNativeAd(
    activity: Activity,
    placementKey: String,
    adKey: String,
    adLayout: LayoutInfo,
    modifier: Modifier = Modifier,
    showShimmerLayout: ShimmerInfo = ShimmerInfo.GivenLayout(),
    adsWidgetData: AdsWidgetData? = null,
    requestNewOnShow: Boolean = false,
    showNewAdEveryTime: Boolean = true,
    showOnlyIfAdAvailable: Boolean = false,
    listener: UiAdsListener? = null,
    onScreenAdsViewModel: OnScreenAdsViewModel = viewModel(
        factory = GenericViewModelFactory(OnScreenAdsViewModel::class.java) {
            OnScreenAdsViewModel()
        }
    ),
    widget: AdsUiWidget = rememberNativeAdUiWidget(
        activity = activity,
        placementKey = placementKey,
        adKey = adKey,
        adLayout = adLayout,
        requestNewOnShow = requestNewOnShow,
        showShimmerLayout = showShimmerLayout,
        listener = listener,
        showNewAdEveryTime = showNewAdEveryTime,
        showOnlyIfAdAvailable = showOnlyIfAdAvailable,
        onScreenAdsViewModel = onScreenAdsViewModel,
        adsWidgetData = adsWidgetData
    ),
): AdsUiWidget {
    val lifecycleOwner = LocalSavedStateRegistryOwner.current
    var stateUpdated by rememberSaveable {
        mutableStateOf(false)
    }
    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 2.dp,
                top = 0.dp,
                end = 2.dp,
                bottom = 0.dp
            ),
        factory = {
            widget
        },
    ) { view ->
        view.requestLayout()
        if (stateUpdated.not()) {
            logAds("Native Ad on Update View Called, is=${view is AdsUiWidget} View=$view")
            onScreenAdsViewModel.updateState(view, placementKey, adKey)
            stateUpdated = true
        }
    }
    DisposableEffect(Unit) {
        onScreenAdsViewModel.setInPause(false, placementKey, false)
        onDispose {
            onScreenAdsViewModel.setInPause(true, placementKey, false)
        }
    }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    onScreenAdsViewModel.setInPause(false, placementKey, false)
                }

                Lifecycle.Event.ON_STOP -> {
                    onScreenAdsViewModel.setInPause(true, placementKey, false)
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            println("MyComposable: Disposed")
        }
    }
    return widget
}
