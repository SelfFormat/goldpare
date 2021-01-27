package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.androidApp.data.CustomWeightRange
import com.selfformat.goldpare.androidApp.data.WeightRange
import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterByWeight {

    @Test
    fun `check if filters all weights up to and including 1`() {
        val weights = createGoldItemsWithWeight(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithWeight(listOf(0.0, 0.001))

        assertEquals(expected, weights.filterByWeight(CustomWeightRange(WeightRange.MIN_WEIGHT, 1.0)))
    }

    @Test
    fun `check if filters all weights up to 250`() {
        val weights = createGoldItemsWithWeight(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 250.0, 305.11))
        val expected = createGoldItemsWithWeight(listOf(11.3, 14.0, 33.0, 0.0, 0.001, 250.0))

        assertEquals(expected, weights.filterByWeight(CustomWeightRange(WeightRange.MIN_WEIGHT, 250.0)))
    }

    @Test
    fun `check if filters all weights from 30 up to max weight allowed`() {
        val weights = createGoldItemsWithWeight(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithWeight(listOf(33.0, 200.0, 305.11))

        assertEquals(expected, weights.filterByWeight(CustomWeightRange(30.0, WeightRange.MAX_WEIGHT)))
    }

    @Test
    fun `when range min and max are set then should return all items within that range`() {
        val weights = createGoldItemsWithWeight(listOf(11.3, 115153.31, 14.0, -111.2, 33.0, 0.0, 0.001, 200.0, 305.11))
        val expected = createGoldItemsWithWeight(listOf(11.3, 14.0, 33.0, 0.0, 0.001, 200.0, 305.11))

        assertEquals(
            expected,
            weights.filterByWeight(CustomWeightRange(WeightRange.MIN_WEIGHT, WeightRange.MAX_WEIGHT))
        )
    }

    private fun createGoldItemsWithWeight(weights: List<Double>): List<GoldItem> {
        return weights.map { fakeGoldItem().copy(weightInGrams = it) }.toList()
    }
}
