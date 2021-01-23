package com.selfformat.goldpare.shared.api

import com.selfformat.goldpare.shared.models.APIGoldItem
import com.selfformat.goldpare.shared.models.APIXauPln
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.Url
import kotlinx.serialization.json.Json

class GoldApi {
    private val httpClient = HttpClient {
        install(JsonFeature) {
            val json = Json { isLenient = true }
            serializer = KotlinxSerializer(json)
        }
    }

    suspend fun fetchGoldItems(): List<APIGoldItem> {
        return httpClient.get(GOLD_ENDPOINT)
    }

    suspend fun fetchXauPln(): APIXauPln {
        return httpClient.get(XAU_PLN_ENDPOINT)
    }

    companion object {
        private val GOLD_ENDPOINT = Url("https://selfformat.com/coin_image.json")
        private val XAU_PLN_ENDPOINT = Url("https://selfformat.com/xau_pln.json")
    }
}
