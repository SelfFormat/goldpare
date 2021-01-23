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
        weight = "15 g",
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
        weight = "15 g",
        quantity = 1,
        type = "coin",
        priceDouble = 3000.0,
        weightInGrams = 15.00,
        pricePerGram = 100.0,
        pricePerOunce = 6000.0
    )

    // region weightInGrams conversions

    @Test
    fun `when weight is available as fraction with oz keyword convert it to grams`() {
        val expected = ozTroy / 4
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "1/4oz")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
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

    // endregion

    // region price convesions

    @Test
    fun `when price is number with zl keyword priceDouble will be null`() {
        val expectedPrice = 10.33
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "10.33 zł")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expectedPrice, result.priceDouble)
    }

    @Test
    fun `when price is number with PLN keyword priceDouble will be null`() {
        val expectedPrice = 334.2
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "334.2PLN")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expectedPrice, result.priceDouble)
    }

    @Test
    fun `when price is number with comma instead of dot for floating point priceDouble will be parsed to double`() {
        val expectedPrice = 11.2
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "11,2 PLN")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expectedPrice, result.priceDouble)
    }

    @Test
    fun `when price is using both comma and dot for number priceDouble will be parsed to double`() {
        val expectedPrice = 1171.24
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "1,171.24 PLN")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expectedPrice, result.priceDouble)
    }

    @Test
    fun `when price is not a number with zl or PLN keyword priceDouble will be null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "3 something")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.priceDouble)
    }

    @Test
    fun `when price is just a number priceDouble will be null`() {
        val expectedPrice = 334.0
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "334")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expectedPrice, result.priceDouble)
    }

    // endregion

    // region price per gram conversion

    @Test
    fun `when price and weight in grams are not null and quantity is greater then 1 then calculate price per gram`() {
        val expected = 200.0
        val result = mapper.mapToDomain(fakeDatabaseGoldItem)
        assertEquals(expected, result.pricePerGram)
    }

    @Test
    fun `when price and weight in grams are not null then calculate price per gram`() {
        val expected = 50.0
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(quantity = 4)
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(expected, result.pricePerGram)
    }

    @Test
    fun `when price is negative then price per gram is null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = "-300")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.pricePerGram)
    }

    @Test
    fun `when weight is negative then price per gram is null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = "-300 gram")
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.pricePerGram)
    }

    @Test
    fun `when weight is null then price per gram is null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(weight = null)
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.pricePerGram)
    }

    @Test
    fun `when price is null then price per gram is null`() {
        val customDatabaseGoldItem = fakeDatabaseGoldItem.copy(price = null)
        val result = mapper.mapToDomain(customDatabaseGoldItem)
        assertEquals(null, result.pricePerGram)
    }

    // endregion
}
