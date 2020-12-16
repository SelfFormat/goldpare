package com.selfformat.goldpare.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class APIGoldItem(
    val price: String?,
    val title: String,
    val link: String,
    val website: String,
    val image_url: String?,
    val weight: String?,
    val quantity: Long,
    val type: String
)