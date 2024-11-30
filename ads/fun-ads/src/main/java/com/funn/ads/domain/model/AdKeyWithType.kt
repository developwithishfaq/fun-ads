package com.funn.ads.domain.model

import com.monetization.core.ad_units.core.AdType

data class AdKeyWithType(
    val adKey: String,
    val adType: AdType
)