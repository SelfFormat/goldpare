package com.selfformat.goldpare.androidApp.compose.enums

enum class SortingType(val sortingName: String = "") {
    NONE("Brak sortowania"),
    PRICE_ASC("Cena rosnąco"),
    PRICE_DESC("Cena malejąco"),
    PRICE_PER_OZ_ASC("Cena za uncję rosnąco"),
    PRICE_PER_OZ_DESC("Cena za uncję malejąco")
}
