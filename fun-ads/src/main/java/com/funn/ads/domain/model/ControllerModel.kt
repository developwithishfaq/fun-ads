package com.funn.ads.domain.model

import com.monetization.bannerads.BannerAdType
import com.monetization.core.ad_units.core.AdType

data class ControllerModel(
    val adKey: String,
    val adType: AdType,
    val adIds: List<String>,
    val bannerAdType: BannerAdType? = null,
)