package com.funn.ads.domain.model

import com.funn.ads.models.jsonModel.Template
import com.funn.ads.models.jsonModel.placements.PlacementModel
import com.monetization.core.ad_units.core.AdType


data class FilteredPreloads(
    val placementKey: String,
    val adsToLoad: List<AdKeyWithType>
)

data class ActionFiltered(
    var actionKey: String = "",
    var adsToLoad: List<AdKeyWithType> = emptyList(),
    var adToShow: ShowFiltered? = null,
    var canOverride: Boolean = false,
)

data class UiAdFiltered(
    var placement: PlacementModel? = null,
    val adType: AdType,
    val controller: String
)

data class ShowFiltered(
    val adKey: String,
    val adType: AdType,
    val template: Template
)