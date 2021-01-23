package com.selfformat.goldpare.shared.models

data class GoldItem(
    val id: Long,
    val price: String?,
    val title: String,
    val link: String,
    val website: String,
    val image: String?,
    val weight: String?,
    val quantity: Long,
    val type: String,
    val priceDouble: Double?,
    val weightInGrams: Double?,
    val pricePerGram: Double?,
    val pricePerOunce: Double?
) {

    @Suppress("MagicNumber")
    fun priceMarkupInPercentage(stockPrice: Double): Double? {
        return ((pricePerOunce?.div(stockPrice))?.minus(1.0))?.times(100)
    }
}
