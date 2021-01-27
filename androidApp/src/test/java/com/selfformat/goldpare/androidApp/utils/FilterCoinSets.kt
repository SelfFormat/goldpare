package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterCoinSets {

    @Test
    fun `check if show sets will show all items`() {
        val prices = createGoldItemsWithQuantity(listOf(1, 3, 2, 1, 1, 55, 20, 1, 4))
        val expected = createGoldItemsWithQuantity(listOf(1, 3, 2, 1, 1, 55, 20, 1, 4))

        assertEquals(expected, prices.showCoinSets(true))
    }

    @Test
    fun `check if show sets is false will show only single items`() {
        val prices = createGoldItemsWithQuantity(listOf(1, 3, 2, 1, 1, 55, 20, 1, 4))
        val expected = createGoldItemsWithQuantity(listOf(1, 1, 1, 1))

        assertEquals(expected, prices.showCoinSets(false))
    }

    private fun createGoldItemsWithQuantity(quantity: List<Long>): List<GoldItem> {
        return quantity.map { fakeGoldItem().copy(quantity = it) }.toList()
    }
}
