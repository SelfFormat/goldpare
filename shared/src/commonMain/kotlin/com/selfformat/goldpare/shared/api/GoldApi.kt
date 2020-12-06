package com.selfformat.goldpare.shared.api

import com.selfformat.goldpare.shared.model.APIGoldItem
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.serialization.json.Json

class GoldApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { ignoreUnknownKeys = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun fetchGoldItems(): List<APIGoldItem> {
        return httpClient.get(GOLD_ENDPOINT)
    }

    companion object {
        private val GOLD_ENDPOINT = Url("https://selfformat.com/coin_quantity_int.json")
    }
}
