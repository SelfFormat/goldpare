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

    @Test
    fun `when weight is available as fraction with uncji keyword convert it to grams`() {
        val expected = ozTroy
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "1 uncja")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }

    @Test
    fun `when weight is available as fraction with uncja keyword convert it to grams`() {
        val expected = ozTroy / 7
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "1/7 uncji")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }

    @Test
    fun `when weight is available with g keyword convert it to grams`() {
        val expected = 132.0
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "132   g")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }

    @Test
    fun `when weight is available with gramy keyword convert it to grams`() {
        val expected = 3.0
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "3 gramy")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }

    @Test
    fun `when weight is double with gram keyword convert it to grams`() {
        val expected = 15.55
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "15.55 gram")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.weightInGrams)
    }

    @Test
    fun `when weight is not available then weightInGrams will be null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = null)
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.weightInGrams)
    }

    @Test
    fun `when weight is not a number with oz or uncj keyword weightInGrams will be null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "4 word 23")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.weightInGrams)
    }

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
