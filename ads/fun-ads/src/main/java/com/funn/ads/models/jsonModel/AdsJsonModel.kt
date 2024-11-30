package com.funn.ads.models.jsonModel

import com.funn.ads.models.jsonModel.action.ActionsModel
import com.funn.ads.models.jsonModel.placements.PlacementModel
import kotlinx.serialization.Serializable

@Serializable
data class AdsJsonModel(
    val controllers: ControllersModel? = null,
    val counters: CountersModel? = null,
    val actions: List<ActionsModel>? = null,
    val templates: TemplatesModel? = null,
    val uiAds: UiAdsModel? = null,
    val preloadRequests: PreloadRequestsMain? = null,
)

@Serializable
data class PreloadRequestsMain(
    val list: List<PreloadRequest> = emptyList()
)

@Serializable
data class PreloadRequest(
    val enabled: Int,
    val placementKey: String,
    val loadAds: List<String>,
)

@Serializable
data class UiAdsModel(
    val ads: List<PlacementModel>? = null,
    val registered: List<RegisteredPlacement>? = null,
)

@Serializable
data class RegisteredPlacement(
    val enabled: Int,
    val placementKey: String,
    val adName: String,
)

@Serializable
data class TemplatesModel(
    val list: List<Template>
)

@Serializable
data class Template(
    val templateKey: String? = null,
    val isInstant: Int = 0,
    val requestNewOnShow: Int = 0,
    val loadNewIfNotAvailable: Int = 0,
    val normalLoadingTime: Int = 0,
    val instantLoadingTime: Int = 0,
    val counterKey: String? = null,
)
/*{
    "templateKey" : "MyInstantAd",
    "isInstant" : 1,
    "requestNewOnShow" : 1,
    "loadNewIfNotAvailable" : 1,
    "normalLoadingTime" : 1000,
    "instantLoadingTime" :10000,
    "counterKey" : "MainScreenCounter"
}*/

