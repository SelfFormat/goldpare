package com.selfformat.goldpare.androidApp.compose

enum class SortingType(val sortingName: String = "") {
    NONE("Brak sortowania"),
    PRICE_ASC("Ascending by price"),
    PRICE_DESC("Descending by price"),
    PRICE_PER_OZ_ASC("Ascending by ounce price"),
    PRICE_PER_OZ_DESC("Descending by ounce price")
}
