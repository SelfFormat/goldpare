package com.selfformat.goldpare.androidApp.utils

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterSearchResults {

    @Test
    fun `check if results contains search phrase noo matter case`() {
        testSearchPhrases(
            phrase = "kruge",
            items = listOf(
                "Krugerrand 123g",
                "Nice Gold 24k Krugerrand 1 oz",
                "Krugerrandy",
                "krugerrand",
                "Kru gerrand 123g",
                "Vienna 1/4 oz",
                "Panda 2 uncje"
            ),
            searchedItems = listOf("Krugerrand 123g", "Nice Gold 24k Krugerrand 1 oz", "Krugerrandy", "krugerrand"),
        )
        testSearchPhrases(
            phrase = "1 DUKAT",
            items = listOf(
                "Krugerrand i 1 dukat 123g",
                "Nice Gold 24k Krugerrand 1 oz",
                "Dukat",
                "One ducat",
                "1 nice dukat 123g",
                "   1 DUKAT   ",
            ),
            searchedItems = listOf("Krugerrand i 1 dukat 123g", "   1 DUKAT   ")
        )
    }

    private fun testSearchPhrases(
        phrase: String,
        items: List<String>,
        searchedItems: List<String>
    ) {
        assertEquals(
            createGoldItemsWithTitles(searchedItems),
            createGoldItemsWithTitles(items).searchFor(phrase)
        )
    }

    private fun createGoldItemsWithTitles(titles: List<String>): List<GoldItem> {
        return titles.map { fakeGoldItem().copy(title = it) }.toList()
    }
}
