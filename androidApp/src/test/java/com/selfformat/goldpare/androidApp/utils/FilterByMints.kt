import com.selfformat.goldpare.androidApp.data.Mint
import com.selfformat.goldpare.androidApp.utils.fakeGoldItem
import com.selfformat.goldpare.androidApp.utils.filterByMint

import com.selfformat.goldpare.shared.models.GoldItem
import org.junit.Test
import kotlin.test.assertEquals

class FilterByMints {

    @Test
    fun `should filter all items that have website same as enum name case sensitive`() {
        var mintFilter = Mint.MENNICACOMPL
        var prices = createGoldItemsWithWebsite(listOf(
            "MENNICACOMPL", "MENNICA", "COM", "PL", "super test Name", "mennicacompl"
        ))
        var expected = createGoldItemsWithWebsite(listOf("MENNICACOMPL"))

        assertEquals(expected, prices.filterByMint(mintFilter))

        mintFilter = Mint.MENNICASKARBOWA
        prices = createGoldItemsWithWebsite(listOf(
            "MENNICASKARBOWA", "MENNICA", "SKARBOWA", "PL", " MENNICASKARBOWA    X", "mennicaskarbowa"
        ))
        expected = createGoldItemsWithWebsite(listOf("MENNICASKARBOWA"))

        assertEquals(expected, prices.filterByMint(mintFilter))
    }

    private fun createGoldItemsWithWebsite(mints: List<String>): List<GoldItem> {
        return mints.map { fakeGoldItem().copy(website = it) }.toList()
    }
}
