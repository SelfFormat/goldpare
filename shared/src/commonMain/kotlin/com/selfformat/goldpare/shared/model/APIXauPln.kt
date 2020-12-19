package com.selfformat.goldpare.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class APIXauPln(
    val timestamp: Long,
    val metal: String,
    val currency: String,
    val exchange: String,
    val symbol: String,
    val open_time: String,
    val price: Double,
    val ch: Double,
    val ask: Double,
    val bid: Double
)
