package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterPriceTest {

    @Test
    fun `check if filters all prices up to and including 0`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(-111.2, 0.0))

        assertEquals(expected, prices.filterPriceTo(0.0))
    }

    @Test
    fun `check if filters all prices up to 250`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 250.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(11.3, 14.0, -111.2, 33.0, 0.0, 0.001, 250.0))

        assertEquals(expected, prices.filterPriceTo(250.0))
    }

    @Test
    fun `check if filters all prices from 30`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(115153.31, 33.0, 200.0, 305.11))

        assertEquals(expected, prices.filterPriceFrom(30.0))
    }

    @Test
    fun `check if filters all prices from minus 30`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, -1.0, -14.11, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0))
        val expected = createGoldItemsWithPrices(listOf(11.3, -1.0, -14.11, 14.0, 33.0, 0.0, 0.001, 200.0))

        assertEquals(expected, prices.filterPriceFrom(-30.0))
    }

    @Test
    fun `check if filters all prices from and including 0`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, 33.0, 0.0, 0.001, 200.0, 305.11))

        assertEquals(expected, prices.filterPriceFrom(0.0))
    }

    @Test
    fun `check if filters all prices from 0 to 20`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 20.0))
        val expected = createGoldItemsWithPrices(listOf(11.3, 14.0, 0.0, 0.001, 20.0))

        assertEquals(expected, prices.filterPriceFrom(0.0).filterPriceTo(20.0))
    }

    private fun createGoldItemsWithPrices(prices: List<Double>): List<GoldItem> {
        return prices.map { fakeGoldItem().copy(priceDouble = it) }.toList()
    }
}
