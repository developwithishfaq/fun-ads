package com.funn.ads.models.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class ControllersModel(
    val list: List<ControllerEntryModel>? = null,
)
