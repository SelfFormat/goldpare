package com.selfformat.goldpare.shared.api

data class XauPln(
    val id: Long,
    val timestamp: Long,
    val metal: String,
    val currency: String,
    val exchange: String,
    val symbol: String,
    val openTime: String,
    val price: Double,
    val ch: Double,
    val ask: Double,
    val bid: Double
)
