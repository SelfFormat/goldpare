package com.selfformat.goldpare.androidApp.compose.utils

import com.selfformat.goldpare.androidApp.compose.data.GoldCoinType
import com.selfformat.goldpare.androidApp.compose.data.GoldType
import com.selfformat.goldpare.androidApp.compose.data.Mint
import com.selfformat.goldpare.androidApp.compose.data.PredefinedWeightRange
import com.selfformat.goldpare.androidApp.compose.data.PredefinedWeightRanges
import com.selfformat.goldpare.androidApp.compose.data.SortingType
import com.selfformat.goldpare.androidApp.compose.data.WeightRange
import com.selfformat.goldpare.shared.models.GoldItem

internal const val NO_PRICE_FILTERING = -1.0
internal const val SHOW_GOLD_SETS = true
internal const val SINGLE_ITEM = 1L

internal fun List<GoldItem>.filterByCoinType(goldCoinType: GoldCoinType): List<GoldItem> {
    if (goldCoinType == GoldCoinType.ALL) return this
    return this.filter {
        it.title.contains(goldCoinType.regex)
    }
}

@Suppress("MaxLineLength")
internal fun List<GoldItem>.filterPriceFrom(priceFrom: Double): List<GoldItem> {
    if (priceFrom == NO_PRICE_FILTERING) return this
    return this
        .filter { it.priceDouble != null } // first filter out items which doesn't have calculated double from price string
        .filter { it.priceDouble!! >= priceFrom }
}

@Suppress("MaxLineLength")
internal fun List<GoldItem>.filterPriceTo(priceTo: Double): List<GoldItem> {
    if (priceTo == NO_PRICE_FILTERING) return this
    return this
        .filter { it.priceDouble != null } // first filter out items which doesn't have calculated double from price string
        .filter { it.priceDouble!! <= priceTo }
}

internal fun List<GoldItem>.showCoinSets(show: Boolean): List<GoldItem> {
    return if (show) {
        return this
    } else {
        this.filter {
            it.quantity == SINGLE_ITEM
        }
    }
}

internal fun List<GoldItem>.sortBy(sortingType: SortingType): List<GoldItem> {
    return when (sortingType) {
        SortingType.PRICE_ASC -> this.sortedBy { it.price }
        SortingType.PRICE_DESC -> this.sortedByDescending { it.price }
        SortingType.PRICE_PER_OZ_ASC -> this.sortedBy { it.pricePerOunce }
        SortingType.PRICE_PER_OZ_DESC -> this.sortedByDescending { it.pricePerOunce }
        SortingType.NONE -> this
    }
}

internal fun List<GoldItem>.filterGoldType(type: GoldType): List<GoldItem> {
    if (type == GoldType.ALL) return this
    return this.filter { it.type == type.typeCode }
}

internal fun List<GoldItem>.filterByWeight(range: WeightRange): List<GoldItem> {
    if (range is PredefinedWeightRange) {
        if (range.predefinedWeightRanges == PredefinedWeightRanges.ALL) return this
    }
    return this
        .filter { it.weightInGrams != null }
        .filter { it.weightInGrams!! >= range.weightFrom &&
                it.weightInGrams!! <= range.weightTo
        }
}

internal fun List<GoldItem>.filterByMint(currentMint: Mint): List<GoldItem> {
    if (currentMint == Mint.ALL) return this
    return this.filter { it.website == currentMint.name }
}

internal fun List<GoldItem>.searchFor(phrase: String?): List<GoldItem> {
    if (phrase.isNullOrEmpty()) return this
    return this.filter {
        it.title.contains(phrase, ignoreCase = true)
    }
}
