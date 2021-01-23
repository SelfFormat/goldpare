package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.api.GoldApi
import com.selfformat.goldpare.shared.models.XauPln
import com.selfformat.goldpare.shared.database.GoldItemsDatabase
import com.selfformat.goldpare.shared.database.DatabaseDriverFactory
import com.selfformat.goldpare.shared.database.XauPlnDatabase
import com.selfformat.goldpare.shared.models.GoldItem

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
