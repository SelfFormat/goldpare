package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.model.GoldItem
import org.junit.Assert.assertEquals
import org.junit.Test

class AndroidGreetingTest {

    @Test
    fun checkIfFractionWithOzKeywordIsParsedToGrams() {
        val weight = "1/2 oz"
        val goldItem = GoldItem(
            id = 0,
            price = null,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = weight,
            quantity = 0,
            type = "",
        )
        val ozTroy = 31.1034768
        val expected = ozTroy / 2
        val result = goldItem.weightInGrams(weight)
        assertEquals(expected, result)
    }

    @Test
    fun checkIfFractionWithUncjiKeywordIsParsedToGrams() {
        val ozTroy = 31.1034768
        val weight = "1/4 uncji"
        val goldItem = GoldItem(
            id = 0,
            price = null,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = weight,
            quantity = 0,
            type = ""
        )
        val expected = ozTroy / 4
        val result = goldItem.weightInGrams(weight)
        assertEquals(expected, result)
    }


    @Test
    fun checkIfWeightWithGKeywordIsParsedToGrams() {
        val weight = "8g"
        val goldItem = GoldItem(
            id = 0,
            price = null,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = weight,
            quantity = 0,
            type = ""
        )
        val expected = 8.0
        val result = goldItem.weightInGrams(weight)
        assertEquals(expected, result)
    }


    @Test
    fun checkIfWeightWithDecimalPointAndWithGKeywordIsParsedToGrams() {
        val weight = "35.9 g"
        val goldItem = GoldItem(
            id = 0,
            price = null,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = weight,
            quantity = 0,
            type = ""
        )
        val expected = 35.9
        val result = goldItem.weightInGrams(weight)
        assertEquals(expected, result)
    }

    @Test
    fun checkIfWeightWithGramKeywordIsParsedToGrams() {
        val weight = "88 gram"
        val goldItem = GoldItem(
            id = 0,
            price = null,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = weight,
            quantity = 0,
            type = ""
        )
        val expected = 88.0
        val result = goldItem.weightInGrams(weight)
        assertEquals(expected, result)
    }

    @Test
    fun checkIfMarkupPriceIsCalculated() {
        val stockPrice = 6000.0
        val itemPrice = "7800.22"
        val goldItem = GoldItem(
            id = 0,
            price = itemPrice,
            title = "",
            link = "",
            website = "",
            img_url = null,
            weight = "31.10",
            quantity = 1,
            type = ""
        )
        assertEquals(30.0, goldItem.priceMarkup(stockPrice)!!, 0.1)
    }
}