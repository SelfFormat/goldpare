package com.selfformat.goldpare.shared.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class APIXauPln(
    val timestamp: Long,
    val metal: String,
    val currency: String,
    val exchange: String,
    val symbol: String,
    @SerialName("open_time")
    val openTime: String,
    val price: Double,
    val ch: Double,
    val ask: Double,
    val bid: Double
)
