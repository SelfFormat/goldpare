package com.selfformat.goldpare.shared.cache

internal class Database(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = AppDatabase(databaseDriverFactory.createDriver())
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllGoldItems()
        }
    }

    internal fun getAllLaunches(): List<GoldItem> {
        return dbQuery.selectAllGoldItems(::mapGoldItemSelecting).executeAsList()
    }

    private fun mapGoldItemSelecting(
        id: Long,
        price: String,
        title: String,
        link: String,
        website: String
    ): GoldItem {
        return GoldItem(
            id = id,
            price = price,
            title = title,
            link = link,
            website = website
        )
    }

    internal fun createLaunches(goldItems: List<GoldItem>) {
        dbQuery.transaction {
            goldItems.forEach { item ->
                val goldItem = dbQuery.selectGoldItemById(item.id).executeAsOneOrNull()
                if (goldItem != null) {
                    insertGoldItem(goldItem)
                }
            }
        }
    }

    private fun insertGoldItem(goldItem: GoldItem) {
        dbQuery.insertGoldItem(
            id = goldItem.id,
            price = goldItem.price,
            title = goldItem.title,
            link = goldItem.link,
            website = goldItem.website
        )
    }
}
