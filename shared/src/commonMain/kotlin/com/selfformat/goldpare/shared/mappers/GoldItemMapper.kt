package com.selfformat.goldpare.shared.mappers

import com.selfformat.goldpare.shared.models.GoldItem

internal class GoldItemMapper {

    fun mapToDomain(it: com.selfformat.goldpare.shared.cache.GoldItem) = GoldItem(
        id = it.id,
        price = it.price,
        title = it.title,
        link = it.link,
        website = it.website,
        image = it.image,
        weight = it.weight,
        quantity = it.quantity,
        type = it.type,
        priceDouble = priceDouble(it.price),
        weightInGrams = weightInGrams(it.weight),
        pricePerGram = pricePerGram(
            weightInGrams = weightInGrams(it.weight),
            priceDouble = priceDouble(it.price),
            quantity = it.quantity
        ),
        pricePerOunce = pricePerOunce(
            pricePerGram(
                weightInGrams = weightInGrams(it.weight),
                priceDouble = priceDouble(it.price),
                quantity = it.quantity
            )
        )
    )

    private fun priceDouble(price: String?): Double? {
        return price?.replace("\\s".toRegex(), "")?.replace("zł", "")?.replace("PLN", "")
            ?.replace("/szt.", "")?.replace(",", ".")?.toDoubleOrNull()
    }

    private fun pricePerGram(
        weightInGrams: Double?,
        priceDouble: Double?,
        quantity: Long
    ): Double? {
        return weightInGrams?.let { priceDouble?.div(it) }?.div(quantity)
    }

    private fun pricePerOunce(pricePerGram: Double?): Double? {
        return pricePerGram?.times(GoldItem.OZ_TROY)
    }

    private fun weightInGrams(weight: String?): Double? {
        val weightWithoutWhitespace =
            weight?.replace("\\s".toRegex(), "")?.replace(",", ".") ?: return null
        val ozRegex = "(?:uncj\\w)|(?:oz)".toRegex()
        val gramRegex = "(?:gram?\\w)|(?:g)".toRegex()
        return when {
            weightWithoutWhitespace.contains(ozRegex) -> {
                val weightWithoutUnitText = weightWithoutWhitespace.replace(ozRegex, "")
                val weightInOz: Double? = if (weightWithoutUnitText.contains('/')) {
                    parseFraction(weightWithoutUnitText)
                } else {
                    weightWithoutUnitText.toDoubleOrNull()
                }
                convertOzToGram(weightInOz)
            }
            weightWithoutWhitespace.contains(gramRegex) -> {
                weightWithoutWhitespace.replace(gramRegex, "").toDoubleOrNull()
            }
            else -> {
                val toDouble = weightWithoutWhitespace.toDoubleOrNull()
                toDouble ?: 1.0
            }
        }
    }

    private fun parseFraction(ratio: String): Double {
        return if (ratio.contains("/")) {
            val rat = ratio.split("/").toTypedArray()
            rat[0].toDouble() / rat[1].toDouble()
        } else {
            ratio.toDouble()
        }
    }

    private fun convertOzToGram(ozQuantity: Double?): Double? {
        return ozQuantity?.times(GoldItem.OZ_TROY)
    }
}
