package com.selfformat.goldpare.shared.model

enum class WeightRanges(
    val rangeName: String,
    val weightFromInGrams: Double,
    val weightToInGrams: Double
) {
    ALL("Wszystkie wagi", 0.0, weightToInGrams = 10000.0),
    OZ_2("2 uncje", weightFromInGrams = 62.0, weightToInGrams = 68.0),
    OZ_1("1 uncja", weightFromInGrams = 31.0, weightToInGrams = 34.0),
    OZ_1_2("1/2 uncji", weightFromInGrams = 15.0, weightToInGrams = 17.0),
    OZ_1_4("1/4 uncji", weightFromInGrams = 7.5, weightToInGrams = 8.5),
    OZ_1_10("1/10 uncji lub mniej", weightFromInGrams = 0.1, weightToInGrams = 3.4),
}
