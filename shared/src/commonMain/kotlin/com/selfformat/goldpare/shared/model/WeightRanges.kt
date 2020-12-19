package com.selfformat.goldpare.shared.model

enum class WeightRanges(
    val rangeName: String,
    val weightFromInGrams: Double,
    val weightToInGrams: Double
) {
    ALL("Wszystkie wagi", 0.0, 10000.0),
    OZ_2("2 uncje", 62.0, 68.0),
    OZ_1("1 uncja", 31.0, 34.0),
    OZ_1_2("1/2 uncji", 15.0, 17.0),
    OZ_1_4("1/4 uncji", 7.5, 8.5),
    OZ_1_10("1/10 uncji lub mniej", 0.1, 3.4),
}
