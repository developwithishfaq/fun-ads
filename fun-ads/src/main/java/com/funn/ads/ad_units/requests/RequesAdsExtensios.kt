package com.funn.ads.ad_units.requests

import android.app.Activity
import com.funn.ads.domain.model.ActionFiltered
import com.funn.ads.sdk.FunAdsSdk
import com.monetization.adsmain.commons.loadAd
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.managers.AdsLoadingStatusListener


fun String.loadAdFun(
    placementKey: String,
    activity: Activity,
    adType: AdType,
    listener: AdsLoadingStatusListener? = null
) {
    val model = FunAdsSdk.getPreloadByPlacementKey(placementKey)
    if (model != null) {
        model.adsToLoad.forEach {adToLoad->
            adToLoad.adKey.loadAd(
                placementKey = placementKey,
                activity = activity,
                adType = adToLoad.adType,
                listener = listener
            )
        }
    } else {
        loadAd(
            placementKey = placementKey,
            activity = activity,
            adType = adType,
            listener = listener
        )
    }
}