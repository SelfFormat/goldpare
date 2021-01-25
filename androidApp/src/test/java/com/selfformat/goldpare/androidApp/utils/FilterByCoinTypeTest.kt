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
        GoldCoinType.DUCATS,
        listOf("Dukat 123g", "Nice Gold 24k Ducats 1 oz", "3 dukaty 6g", "Dukat"),
        listOf("Duncan kat 123g", "Vienna 1/4 oz", "Panda 2 uncje")
    )

    @Test
    fun `check if filters by britannia returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.BRITANNIA,
        listOf("Britannia 123g", "Nice Gold 24k britannia 1 oz", "3 britannia 6g", "Britannia"),
        listOf("brita 123g", "Britain 1/4 oz", "Panda 2 uncje")
    )

    @Test
    fun `check if filters by maple leaf returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.MAPLE_LEAF,
        listOf("Maple leaf 123g", "Nice Gold 24k maple 1 oz", "Lisc klonu 6g", "liść klonowy"),
        listOf("listki kat 123g", "map 1/4 oz", "lsc 2 uncje")
    )

    @Test
    fun `check if filters by australian kangaroo returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.AUSTRALIAN_KANGAROO,
        listOf("australian kangaroo 123g", "Nice Gold 24k Australijski kangur 1 oz", "3 australijskie kangury 6g"),
        listOf("australian cat 123g", "kan 1/4 oz", "Panda 2 uncje")
    )

    @Test
    fun `check if filters by chinese panda returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.CHINESE_PANDA,
        listOf("panda 123g", "Nice Gold 24k Chinese panda 1 oz", "3 Nice pandas 6g", "pandas"),
        listOf("pan da 123g", "chinese 1/4 oz", "vienna 2 uncje")
    )

    @Test
    fun `check if filters by american buffalo returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.AMERICAN_BUFFALO,
        listOf("amerykański bizon 123g", "Nice Gold 24k american BUFFALO 1 oz", "3 buffalo 6g", "bizon"),
        listOf("bison 123g", "amerykański 1/4 oz", "american 2 uncje")
    )

    @Test
    fun `check if filters by american eagle returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.AMERICAN_EAGLE,
        listOf("american eagle 123g", "Nice Gold 24k American Eagles 1 oz", "3 orzeł amerykański 6g"),
        listOf("Duncan american 123g", "orły 1/4 oz", "panda 2 uncje")
    )

    @Test
    fun `check if filters by vienna philharmonic returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.VIENNA_PHILHARMONIC,
        listOf("vienna 123g", "Nice Gold 24k philharmonic 1 oz", "3 filharmoniki wiedeńskie 6g", "filcharmonik"),
        listOf("Duncan kat 123g", "eagle 1/4 oz", "Panda 2 uncje")
    )

    @Test
    fun `check if filters by queens beasts returns correct items regardless case`() = testCoinTypeFiltering(
        GoldCoinType.QUEENS_BEASTS,
        listOf("Bestie królowej 123g", "Nice Gold 24k queen's beasts 1 oz", "beasts"),
        listOf("best kat 123g", "Vienna 1/4 oz", "queen 2 uncje")
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
