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
    companion object {
        const val OZ_TROY = 31.1034768
    }

    val pricePerOunce: Double?
        get() = pricePerGram?.times(OZ_TROY)

    val weightInGrams: Double?
        get() = weightInGrams(weight)

    fun weightInGrams(weight: String?): Double? {
        val weightWithoutWhitespace =
            weight?.replace("\\s".toRegex(), "")?.replace(",", ".") ?: return null
        val ozRegex = "(?:uncj\\w)|(?:oz)".toRegex()
        val gramRegex = "(?:gram?\\w)|(?:g)".toRegex()
        return when {
            weightWithoutWhitespace.contains(ozRegex) -> {
                val weightWithoutUnitText = weightWithoutWhitespace.replace(ozRegex, "")
                val weightInOz: Double = if (weightWithoutUnitText.contains('/')) {
                    parseFraction(weightWithoutUnitText)
                } else {
                    weightWithoutUnitText.toDouble()
                }
                return convertOzToGram(weightInOz)
            }
            weightWithoutWhitespace.contains(gramRegex) -> {
                return weightWithoutWhitespace.replace(gramRegex, "").toDouble()
            }
            else -> {
                val toDouble = weightWithoutWhitespace.toDoubleOrNull()
                toDouble ?: 1.0
            }
        }
    }

    fun priceMarkup(stockPrice: Double): Double? {
        return ((pricePerOunce?.div(stockPrice))?.minus(1.0))?.times(100)
    }

    private val priceDouble: Double?
        get() {
            return price?.replace("\\s".toRegex(), "")?.replace("zł", "")?.replace(",", ".")
                ?.toDoubleOrNull()
        }

    private val pricePerGram: Double?
        get() = (weightInGrams?.let { priceDouble?.div(it) })?.div(quantity)

    private fun parseFraction(ratio: String): Double {
        return if (ratio.contains("/")) {
            val rat = ratio.split("/").toTypedArray()
            rat[0].toDouble() / rat[1].toDouble()
        } else {
            ratio.toDouble()
        }
    }

    private fun convertOzToGram(ozQuantity: Double): Double {
        return OZ_TROY * ozQuantity
    }
}
