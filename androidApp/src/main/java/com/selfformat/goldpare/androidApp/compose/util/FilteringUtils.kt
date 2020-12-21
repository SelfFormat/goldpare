package com.selfformat.goldpare.androidApp.compose.util

import com.selfformat.goldpare.shared.model.GoldCoinType
import com.selfformat.goldpare.shared.model.GoldItem
import com.selfformat.goldpare.shared.model.GoldType
import com.selfformat.goldpare.shared.model.Mint
import com.selfformat.goldpare.shared.model.SortingType
import com.selfformat.goldpare.shared.model.WeightRange

internal const val NO_PRICE_FILTERING = -1.0
internal const val SHOW_GOLD_SETS = true
internal const val SINGLE_ITEM = 1L

internal fun List<GoldItem>.filterByCoinType(goldCoinType: GoldCoinType): List<GoldItem> {
    if (goldCoinType == GoldCoinType.ALL) return this
    return this.filter {
        it.title.contains(goldCoinType.name, ignoreCase = true)
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
    return this.filter { it.type == type.typeName }
}

internal fun List<GoldItem>.filterByWeight(weight: WeightRange): List<GoldItem> {
    if (weight == WeightRange.ALL) return this
    return this
        .filter { it.weightInGrams != null } // first filter out items which doesn't have calculated weight in grams
        .filter { it.weightInGrams!! >= weight.weightFromInGrams && it.weightInGrams!! <= weight.weightToInGrams }
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
