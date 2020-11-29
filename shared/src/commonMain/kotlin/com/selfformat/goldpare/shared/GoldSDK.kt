package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.api.GoldApi
import com.selfformat.goldpare.shared.cache.Database
import com.selfformat.goldpare.shared.cache.DatabaseDriverFactory
import com.selfformat.goldpare.shared.model.GoldItem

class GoldSDK(databaseDriverFactory: DatabaseDriverFactory) {
    private val database = Database(databaseDriverFactory)
    private val api = GoldApi()

    @Throws(Exception::class) suspend fun getLaunches(forceReload: Boolean): List<GoldItem> {
        val cachedLaunches = database.getAllLaunches()
        return if (cachedLaunches.isNotEmpty() && !forceReload) {
            cachedLaunches
        } else {
            api.getAllGoldItems().also {
                database.clearDatabase()
                database.createLaunches(it)
            }
        }
    }
}
