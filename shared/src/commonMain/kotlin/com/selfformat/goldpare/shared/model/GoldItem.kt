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
    val type: String
) {
    val weightInGrams: Double? = weightInGrams(weight)

    private val priceDouble: Double? = price?.
        replace("\\s".toRegex(), "")?.
        replace("zł", "")?.
        replace("PLN", "")?.
        replace("/szt.", "")?.
        replace(",", ".")?.
        toDoubleOrNull()

    private val pricePerGram: Double? = (weightInGrams?.let { priceDouble?.div(it) })?.div(quantity)

    val pricePerOunce: Double? = pricePerGram?.times(OZ_TROY)

    val mintFullName: String =
        when (website) {
            "goldenmark" -> "Goldenmark"
            "79element" -> "79th element"
            "mennicacompl" -> "Mennica Polska"
            "mennicakapitalowa" -> "Mennica Kapitałowa"
            "mennicakrajowa" -> "Mennica Krajowa"
            "mennicamazovia" -> "Mennica Mazovia"
            "metalelokacyjne" -> "Metale Lokacyjne"
            "metalmarketu" -> "Metal Market Europe"
            "wyrobymennicze" -> "Wyroby Mennicze"
            "mennicaskarbowa" -> "Mennica Skarbowa"
            "coininvest" -> "Coininvest"
            "travex" -> "Travex"
            "mennica24" -> "Mennica24"
            "zlotauncja" -> "Złota Uncja"
            "mennicakrakowska" -> "Mennica Krakowska"
            "mennicagdanska" -> "Mennica Gdańska"
            "mennicazielona" -> "Mennica Zielona"
            "goldco" -> "Goldco"
            "idfmetale" -> "IDF metale"
            "chojnackiikwiecien" -> "Chojnacki i Kwiecień"
            "flyingatomgold" -> "Flyingatom"
            else -> "Sklep"
        }

    fun weightInGrams(weight: String?): Double? {
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
                return convertOzToGram(weightInOz)
            }
            weightWithoutWhitespace.contains(gramRegex) -> {
                return weightWithoutWhitespace.replace(gramRegex, "").toDoubleOrNull()
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

    private fun parseFraction(ratio: String): Double {
        return if (ratio.contains("/")) {
            val rat = ratio.split("/").toTypedArray()
            rat[0].toDouble() / rat[1].toDouble()
        } else {
            ratio.toDouble()
        }
    }

    private fun convertOzToGram(ozQuantity: Double?): Double? {
        return ozQuantity?.times(OZ_TROY)
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
            type = "coin"
        )
    }
}
