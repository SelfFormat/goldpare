package com.selfformat.goldpare.shared.cache

import com.selfformat.goldpare.shared.model.APIGoldItem
import com.selfformat.goldpare.shared.model.GoldItem

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
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
                weight = it.weight
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
            weight = goldItem.weight
        )
    }
}
