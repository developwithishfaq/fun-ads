package com.funn.ads.models.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class CountersModel(
    val list: List<CountersEntryModel>? = null,
)
