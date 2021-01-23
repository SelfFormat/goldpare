package com.selfformat.goldpare.androidApp.compose.data

import com.selfformat.goldpare.androidApp.R

enum class SortingType(val sortingName: Int) {
    NONE(R.string.no_sorting),
    PRICE_ASC(R.string.sort_price_ascending),
    PRICE_DESC(R.string.sort_price_descending),
    PRICE_PER_OZ_ASC(R.string.sort_price_per_oz_ascending),
    PRICE_PER_OZ_DESC(R.string.sort_price_per_oz_descending)
}
