package com.funn.ads.models.jsonModel.action

import kotlinx.serialization.Serializable

@Serializable
data class ActionsModel(
    val actionKey: String? = null,
    val enabled: Int = 1,
    val loadAds: List<String>? = null,
    val showAds: ShowAdModel? = null,
)
