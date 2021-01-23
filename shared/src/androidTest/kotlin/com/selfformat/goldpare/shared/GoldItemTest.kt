package com.selfformat.goldpare.shared

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class GoldItemTest {
    private val fakeGoldItem = GoldItem(
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
    fun `when gold stock price available then calculate price markup in percentage`() {
        val stockPrice = 4000.0
        assertEquals(50.0, fakeGoldItem.priceMarkupInPercentage(stockPrice))
    }

    @Test
    fun `when price per ounce is null then price markup will be null`() {
        val goldItem = fakeGoldItem.copy(pricePerOunce = null)
        val stockPrice = 4000.0
        assertEquals(null, goldItem.priceMarkupInPercentage(stockPrice))
    }
}
