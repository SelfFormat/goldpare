package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.androidApp.data.SortingType
import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class SortByTest {

    @Test
    fun `check if sorts by prices ascending`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(-111.2, 0.0, 0.001, 11.3, 14.0, 33.0, 200.0, 305.11, 115153.31))

        assertEquals(expected, prices.sortBy(SortingType.PRICE_ASC))
    }

    @Test
    fun `check if sorts by prices descending`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 250.0, 305.11))
        val expected = createGoldItemsWithPrices(listOf(115153.31, 305.11, 250.0, 33.0, 14.0, 11.3, 0.001, 0.0, -111.2))

        assertEquals(expected, prices.sortBy(SortingType.PRICE_DESC))
    }

    @Test
    fun `check if sorts by prices per oz ascending`() {
        val prices = createGoldItemsWithPricesPerOunce(
            listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11)
        )
        val expected = createGoldItemsWithPricesPerOunce(
            listOf(-111.2, 0.0, 0.001, 11.3, 14.0, 33.0, 200.0, 305.11, 115153.31)
        )

        assertEquals(expected, prices.sortBy(SortingType.PRICE_PER_OZ_ASC))
    }

    @Test
    fun `check if sorts by prices per oz descending`() {
        val prices = createGoldItemsWithPricesPerOunce(
            listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 250.0, 305.11)
        )
        val expected = createGoldItemsWithPricesPerOunce(
            listOf(115153.31, 305.11, 250.0, 33.0, 14.0, 11.3, 0.001, 0.0, -111.2)
        )

        assertEquals(expected, prices.sortBy(SortingType.PRICE_PER_OZ_DESC))
    }

    @Test
    fun `check if no sorting then returns unchanged list`() {
        val prices = createGoldItemsWithPrices(listOf(11.3, -1.0, -14.11, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0))

        assertEquals(prices, prices.sortBy(SortingType.NONE))
    }

    private fun createGoldItemsWithPrices(prices: List<Double>): List<GoldItem> {
        return prices.map { fakeGoldItem().copy(priceDouble = it) }.toList()
    }

    private fun createGoldItemsWithPricesPerOunce(pricesPerOunce: List<Double>): List<GoldItem> {
        return pricesPerOunce.map { fakeGoldItem().copy(pricePerOunce = it) }.toList()
    }
}
