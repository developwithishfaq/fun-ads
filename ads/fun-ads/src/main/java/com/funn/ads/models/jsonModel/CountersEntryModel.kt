package com.funn.ads.models.jsonModel

import kotlinx.serialization.Serializable

@Serializable
data class CountersEntryModel(
    val name: String,
    val currentPoint: String,
    val enabled: Int,
    val maxPoint: String,
    val notShownStrategy: String,
    val adShownStrategy: String,
)