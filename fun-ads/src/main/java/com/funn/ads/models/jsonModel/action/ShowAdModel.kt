package com.funn.ads.models.jsonModel.action

import kotlinx.serialization.Serializable

@Serializable
data class ShowAdModel(
    val controller: String,
    val templateKey: String
)
