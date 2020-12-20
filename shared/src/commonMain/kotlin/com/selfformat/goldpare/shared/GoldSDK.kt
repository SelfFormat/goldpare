package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.api.GoldApi
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.cache.Database
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem

class GoldSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = GoldApi()

    @Throws(Exception::class)
    suspend fun getGoldItems(forceReload: Boolean): List<GoldItem> {
        val cachedGoldItems = database.getAllGoldItems()
        return if (cachedGoldItems.isNotEmpty() && !forceReload) {
            cachedGoldItems
        } else {
            api.fetchGoldItems().also {
                database.clearGoldDatabase()
                database.createGoldItems(it)
            }
            database.getAllGoldItems()
        }
    }

    @Throws(Exception::class)
    suspend fun getXauPln(forceReload: Boolean): List<XauPln> {
        val cachedXauPln = database.getXauPln()
        return if (cachedXauPln.isNotEmpty() && !forceReload) {
            database.getXauPln()
        } else {
            api.fetchXauPln().also {
                database.clearPlnXauDatabase()
                database.createGoldXau(it)
            }
            database.getXauPln()
        }
    }
}
