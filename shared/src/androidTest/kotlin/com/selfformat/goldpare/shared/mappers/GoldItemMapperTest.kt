package com.selfformat.goldpare.shared.mappers

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals
import com.selfformat.goldpare.shared.cache.GoldItem as DatabaseGoldItem

class GoldItemMapperTest {

    private val mapper = GoldItemMapper()
    private val ozTroy = 31.1034768

    private val fakeDatabaseGoldItem = DatabaseGoldItem(
        1,
        "3000zł",
        "Gold 1/2 oz",
        "www.gold.com/1oz",
        "gold.com",
        "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin"
    )

    private val fakeDomainGoldItem = GoldItem(
        1,
        "3000zł",
        "Gold 1/2 oz",
        "www.gold.com/1oz",
        "gold.com",
        "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin",
        priceDouble = 3000.0,
        weightInGrams = 15.55,
        pricePerGram = 100.0,
        pricePerOunce = 6000.0
    )

    @Test
    fun `when weight is available as fraction with oz keyword convert it to grams`() {
        val expected = ozTroy / 4
        val result = mapper.mapToDomain(fakeDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }
//
//    @Test
//    fun checkIfFractionWithUncjiKeywordIsParsedToGrams() {
//        val ozTroy = 31.1034768
//        val weight = "1/4 uncji"
//        val goldItem = GoldItem(
//            id = 0,
//            price = null,
//            title = "",
//            link = "",
//            website = "",
//            image = null,
//            weight = weight,
//            quantity = 0,
//            type = ""
//        )
//        val expected = ozTroy / 4
//        val result = goldItem.weightInGrams(weight)
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun checkIfWeightWithGKeywordIsParsedToGrams() {
//        val weight = "8g"
//        val goldItem = GoldItem(
//            id = 0,
//            price = null,
//            title = "",
//            link = "",
//            website = "",
//            image = null,
//            weight = weight,
//            quantity = 0,
//            type = ""
//        )
//        val expected = 8.0
//        val result = goldItem.weightInGrams(weight)
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun checkIfWeightWithDecimalPointAndWithGKeywordIsParsedToGrams() {
//        val weight = "35.9 g"
//        val goldItem = GoldItem(
//            id = 0,
//            price = null,
//            title = "",
//            link = "",
//            website = "",
//            image = null,
//            weight = weight,
//            quantity = 0,
//            type = ""
//        )
//        val expected = 35.9
//        val result = goldItem.weightInGrams(weight)
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun checkIfWeightWithGramKeywordIsParsedToGrams() {
//        val weight = "88 gram"
//        val goldItem = GoldItem(
//            id = 0,
//            price = null,
//            title = "",
//            link = "",
//            website = "",
//            image = null,
//            weight = weight,
//            quantity = 0,
//            type = ""
//        )
//        val expected = 88.0
//        val result = goldItem.weightInGrams(weight)
//        assertEquals(expected, result)
//    }
}
