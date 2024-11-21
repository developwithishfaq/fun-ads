package com.example.myapplication

import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.core.ad_units.core.AdType

enum class AdKeys(
    val adType: AdType,
    val adIds: List<String> = emptyList(),
    val bannerAdType: BannerAdType? = null
) {
    Test1(AdType.NATIVE, listOf(""), bannerAdType = BannerAdType.Normal(BannerAdSize.Banner)),
//    TestNew(AdType.INTERSTITIAL, listOf(""))
}