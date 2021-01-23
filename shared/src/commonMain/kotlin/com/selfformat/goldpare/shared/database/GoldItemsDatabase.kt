package com.selfformat.goldpare.shared.database

import com.selfformat.goldpare.shared.cache.AppDatabase
import com.selfformat.goldpare.shared.mappers.GoldItemMapper
import com.selfformat.goldpare.shared.models.APIGoldItem
import com.selfformat.goldpare.shared.models.GoldItem

internal class GoldItemsDatabase(databaseDriverFactory: DatabaseDriverFactory) {
    private val db = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = db.appDatabaseQueries
    private val mapper = GoldItemMapper()

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
                image = it.image,
                weight = it.weight,
                quantity = it.quantity,
                type = it.type,
                priceDouble = mapper.priceDouble(it.price),
                weightInGrams = mapper.weightInGrams(it.weight),
                pricePerGram = mapper.pricePerGram(
                    weightInGrams = mapper.weightInGrams(it.weight),
                    priceDouble = mapper.priceDouble(it.price),
                    quantity = it.quantity
                ),
                pricePerOunce = mapper.pricePerOunce(
                    mapper.pricePerGram(
                        weightInGrams = mapper.weightInGrams(it.weight),
                        priceDouble = mapper.priceDouble(it.price),
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
            image = goldItem.image,
            weight = goldItem.weight,
            quantity = goldItem.quantity,
            type = goldItem.type
        )
    }
}
