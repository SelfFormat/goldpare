package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.api.GoldApi
import com.selfformat.goldpare.shared.api.XauPln
import com.selfformat.goldpare.shared.cache.GoldItemsDatabase
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.cache.XauPlnDatabase
import com.selfformat.goldpare.shared.model.GoldItem

class GoldSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = GoldItemsDatabase(databaseDriverFactory)
    private val xauPlnDatabase = XauPlnDatabase(databaseDriverFactory)
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
        val cachedXauPln = xauPlnDatabase.getXauPln()
        return if (cachedXauPln.isNotEmpty() && !forceReload) {
            xauPlnDatabase.getXauPln()
        } else {
            api.fetchXauPln().also {
                xauPlnDatabase.clearPlnXauDatabase()
                xauPlnDatabase.createGoldXau(it)
            }
            xauPlnDatabase.getXauPln()
        }
    }
}
