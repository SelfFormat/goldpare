package com.selfformat.goldpare.shared.cache

import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.model.APIGoldItem
import com.selfformat.goldpare.shared.model.APIXauPln
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.Mint

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearGoldDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllGoldItems()
        }
    }

    internal fun getAllGoldItems(): List<GoldItem> {
        return dbQuery.selectAllGoldItems().executeAsList().map {
            GoldItem(
                id = it.id,
                price = it.price,
                title = it.title,
                link = it.link,
                website = it.website,
                img_url = it.image_url,
                weight = it.weight,
                quantity = it.quantity,
                type = it.type,
                priceDouble = priceDouble(it.price),
                mintFullName = mintFullName(it.website),
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
        }
    }

    internal fun createGoldItems(goldItems: List<APIGoldItem>) {
        dbQuery.transaction {
            goldItems.forEach { item: APIGoldItem ->
                insertGoldItem(item)
            }
        }
    }

    private fun insertGoldItem(goldItem: APIGoldItem) {
        dbQuery.insertGoldItem(
            price = goldItem.price,
            title = goldItem.title,
            link = goldItem.link,
            website = goldItem.website,
            image_url = goldItem.image_url,
            weight = goldItem.weight,
            quantity = goldItem.quantity,
            type = goldItem.type
        )
    }

    private fun priceDouble(price: String?): Double? {
        return price?.replace("\\s".toRegex(), "")?.replace("zł", "")?.replace("PLN", "")
            ?.replace("/szt.", "")?.replace(",", ".")?.toDoubleOrNull()
    }

    private fun mintFullName(website: String): String {
        Mint.values().forEach {
            if (website == it.name) {
                return it.fullName
            }
        }
        return "Uknown provider"
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

    fun clearPlnXauDatabase() {
        dbQuery.transaction {
            dbQuery.removeXauPln()
        }
    }

    fun createGoldXau(it: APIXauPln) {
        dbQuery.transaction {
            insertXauPln(it)
        }
    }

    private fun insertXauPln(it: APIXauPln) {
        dbQuery.insertXauPln(
            timestamp = it.timestamp,
            metal = it.metal,
            currency = it.currency,
            exchange = it.exchange,
            symbol = it.symbol,
            open_time = it.open_time,
            price = it.price,
            ch = it.ch,
            ask = it.ask,
            bid = it.bid
        )
    }

    fun getXauPln(): List<XauPln> {
        return dbQuery.selectAllXauPln().executeAsList().map {
            XauPln(
                id = it.id,
                timestamp = it.timestamp,
                metal = it.metal,
                currency = it.currency,
                exchange = it.exchange,
                symbol = it.symbol,
                open_time = it.open_time,
                price = it.price,
                ch = it.ch,
                ask = it.ask,
                bid = it.bid
            )
        }
    }
}
