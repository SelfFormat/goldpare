package com.selfformat.goldpare.shared.model

data class GoldItem(
    val id: Long,
    val price: String?,
    val title: String,
    val link: String,
    val website: String,
    val img_url: String?,
    val weight: String?,
    val quantity: Long
) {
    val pricePerOunce: String = "we need to calculate that"

    val calculatedWeight: CoinWeight = when {
        title.contains("1/25") -> CoinWeight.OZ1_25
        title.contains("1/10") -> CoinWeight.OZ1_10
        title.contains("1/4") -> CoinWeight.OZ1_4
        title.contains("1/2") -> CoinWeight.OZ1_2
        title.contains("2") -> CoinWeight.OZ2
        title.contains("1") -> CoinWeight.OZ1
        else -> CoinWeight.Unknown
    }
    val priceDouble: Double?
        get() {
            return price?.replace("\\s".toRegex(), "")?.replace("zł", "")?.replace(",", ".")
                ?.toDoubleOrNull()
        }

    val weightInGrams: Double?
        get() {
            val weightWithoutWhitespace =
                weight?.replace("\\s".toRegex(), "")?.replace(",", ".") ?: return null
            return if (weightWithoutWhitespace.contains("oz", ignoreCase = true)) {
                weightWithoutWhitespace.replace("oz", "").toDoubleOrNull()
            } else {
                1.0
            }
        }

    fun String.convertOzToGram(quantity: Int): Double {
        return this.toDouble() / quantity
    }
}
