package com.funn.ads.models.jsonModel

import com.monetization.bannerads.BannerAdSize
import com.monetization.bannerads.BannerAdType
import com.monetization.bannerads.BannerCollapsable
import com.monetization.core.ad_units.core.AdType
import kotlinx.serialization.Serializable

@Serializable
data class ControllerEntryModel(
    val adIds: List<String> = emptyList(),
    val key: String = "",
    val type: String = "",
    val enabled: Int = 0,
    val bannerAdType: String? = null,
)

fun String?.toBannerAdType(): BannerAdType {
    return if (this == null) {
        BannerAdType.Normal(BannerAdSize.AdaptiveBanner)
    } else if (contains("adaptive")) {
        BannerAdType.Normal(BannerAdSize.AdaptiveBanner)
    } else if (contains("medium")) {
        BannerAdType.Normal(BannerAdSize.MediumRectangle)
    } else if (contains("collapsetop", true)) {
        BannerAdType.Collapsible(BannerCollapsable.CollapseTop)
    } else if (contains("collapsebot", true)) {
        BannerAdType.Collapsible(BannerCollapsable.CollapseBottom)
    } else {
        BannerAdType.Normal(BannerAdSize.AdaptiveBanner)
    }
}

fun String.toEnumAdType(): AdType? {
    return if (equals("inter", true)) {
        AdType.INTERSTITIAL
    } else if (equals("appOpen", true)) {
        AdType.AppOpen
    } else if (equals("native", true)) {
        AdType.NATIVE
    } else if (equals("banner", true)) {
        AdType.BANNER
    } else if (equals("rewarded", true)) {
        AdType.REWARDED
    } else if (equals("rewardedInter", true)) {
        AdType.REWARDED
    } else {
        null
    }
}