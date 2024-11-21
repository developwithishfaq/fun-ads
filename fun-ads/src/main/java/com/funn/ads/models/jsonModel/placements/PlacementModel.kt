package com.funn.ads.models.jsonModel.placements

import com.monetization.core.ui.AdsWidgetData
import kotlinx.serialization.Serializable

@Serializable
data class PlacementModel(
    val adName: String,
    val controller: String,
    val layout: String,
    val requestNewOnShow: Int,
    val shimmer: String,
    val showNewAdEveryTime: Int,
    val showOnlyIfAdAvailable: Int,
    val adsWidgetData: AdsWidgetDataDomain? = null,
)

@Serializable
data class AdsWidgetDataDomain(
    var enabled: Int? = null,
    var adCtaHeight: Float? = null,
    var ctaRoundness: Int? = null,
    var adCtaTextSize: Float? = null,
    var adHeadlineTextSize: Float? = null,
    var adBodyTextSize: Float? = null,
    var adMediaViewHeight: Float? = null,
    var adIconHeight: Float? = null,
    var adIconWidth: Float? = null,
    var adLayout: String? = null,
    var margings: String? = null,
    var adCtaBgColor: String? = null,
    var adCtaTextColor: String? = null,
    var adHeadLineTextColor: String? = null,
    var adBodyTextColor: String? = null,
    var adAttrTextColor: String? = null,
    var adAttrBgColor: String? = null,
)

fun AdsWidgetDataDomain.toAdsWidgetData(): AdsWidgetData {
    return AdsWidgetData(
        enabled = enabled,
        adCtaHeight = adCtaHeight,
        ctaRoundness = ctaRoundness,
        adCtaTextSize = adCtaTextSize,
        adHeadlineTextSize = adHeadlineTextSize,
        adBodyTextSize = adBodyTextSize,
        adMediaViewHeight = adMediaViewHeight,
        adIconHeight = adIconHeight,
        adIconWidth = adIconWidth,
        adLayout = adLayout,
        margings = margings,
        adCtaBgColor = adCtaBgColor,
        adCtaTextColor = adCtaTextColor,
        adHeadLineTextColor = adHeadLineTextColor,
        adBodyTextColor = adBodyTextColor,
        adAttrTextColor = adAttrTextColor,
        adAttrBgColor = adAttrBgColor,
    )
}
