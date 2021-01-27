package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.androidApp.data.GoldType
import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterByGoldType {

    @Test
    fun `check if filter bars`() {
        val prices = createGoldItemsWithQuantity(listOf("coin", "coin", "coin", "bar", "coin", "bar"))
        val expected = createGoldItemsWithQuantity(listOf("bar", "bar"))

        assertEquals(expected, prices.filterGoldType(GoldType.BAR))
    }

    @Test
    fun `check if filter coins`() {
        val prices = createGoldItemsWithQuantity(listOf("coin", "coin", "coin", "bar", "coin", "bar"))
        val expected = createGoldItemsWithQuantity(listOf("coin", "coin", "coin", "coin"))

        assertEquals(expected, prices.filterGoldType(GoldType.COIN))
    }

    @Test
    fun `check if gold type is all then no filtering is applied`() {
        val prices = createGoldItemsWithQuantity(listOf("coin", "coin", "coin", "bar", "coin", "bar"))

        assertEquals(prices, prices.filterGoldType(GoldType.ALL))
    }

    private fun createGoldItemsWithQuantity(type: List<String>): List<GoldItem> {
        return type.map { fakeGoldItem().copy(type = it) }.toList()
    }
}
