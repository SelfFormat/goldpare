package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.androidApp.data.GoldCoinType
import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterByCoinTypeTest {

    @Test
    fun `check if filters by krugerrand returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.KRUGERRAND,
        listOf("Krugerrand 123g", "Nice Gold 24k Krugerrand 1 oz", "Krugerrandy", "krugerrand"),
        listOf("Kru gerrand 123g", "Vienna 1/4 oz", "Panda 2 uncje")
    )

    @Test
    fun `check if filters by ducats returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.DUKAT,
        listOf("Dukat 123g", "Nice Gold 24k Ducats 1 oz", "3 dukaty 6g", "Dukat"),
        listOf("Duncan kat 123g", "Vienna 1/4 oz", "Panda 2 uncje")
    )

    private fun testCoinTypeFiltering(
        type: GoldCoinType,
        correctTitles: List<String>,
        wrongTitles: List<String>
    ) {
        val proper = createListOfTitles(correctTitles)
        val wrong = createListOfTitles(wrongTitles)
        val allItems = proper + wrong
        assertEquals(proper, allItems.filterByCoinType(type))
    }

    private fun fakeGoldItem() = GoldItem(
        id = 1,
        price = "3000zł",
        title = "Krugerrand 1/2 oz",
        link = "www.gold.com/1oz",
        website = "gold.com",
        image = "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin",
        priceDouble = 3000.0,
        weightInGrams = 15.55,
        pricePerGram = 100.0,
        pricePerOunce = 6000.0
    )

    private fun createListOfTitles(titles: List<String>): List<GoldItem> {
        return titles.map { fakeGoldItem().copy(title = it) }.toList()
    }
}
