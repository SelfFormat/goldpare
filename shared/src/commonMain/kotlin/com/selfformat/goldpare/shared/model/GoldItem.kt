package com.selfformat.goldpare.shared.model

data class GoldItem(
    val id: Long,
    val price: String?,
    val title: String,
    val link: String,
    val website: String,
    val img_url: String?,
    val weight: String?,
    val quantity: Long,
    val type: String,
    val priceDouble: Double?,
    val mintFullName: String,
    val weightInGrams: Double?,
    val pricePerGram: Double?,
    val pricePerOunce: Double?
) {

    fun priceMarkup(stockPrice: Double): Double? {
        return ((pricePerOunce?.div(stockPrice))?.minus(1.0))?.times(100)
    }

    companion object {
        const val OZ_TROY = 31.1034768
        val fakeGoldItem = GoldItem(
            1,
            "3000zł",
            "Gold 1/2 oz",
            "www.gold.com/1oz",
            "gold.com",
            "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
            weight = "1/4oz",
            quantity = 1,
            type = "coin",
            priceDouble = 3000.0,
            mintFullName = "Gold Mint",
            weightInGrams = 15.55,
            pricePerGram = 100.0,
            pricePerOunce = 6000.0
        )
    }
}
