package com.selfformat.goldpare.shared.model

enum class WeightRange(
    val rangeName: String,
    val labelRangeName: String,
    val weightFromInGrams: Double,
    val weightToInGrams: Double
) {
    ALL("Wszystkie wagi", "ALL", 0.0, weightToInGrams = 10000.0),
    OZ_2("2 uncje", "2 oz", weightFromInGrams = 62.0, weightToInGrams = 68.0),
    OZ_1("1 uncja", "1 oz", weightFromInGrams = 31.0, weightToInGrams = 34.0),
    OZ_1_2("1/2 uncji", "1/2 oz", weightFromInGrams = 15.0, weightToInGrams = 17.0),
    OZ_1_4("1/4 uncji", "1/4 oz", weightFromInGrams = 7.5, weightToInGrams = 8.5),
    OZ_1_10("1/10 uncji lub mniej", "1/10 oz", weightFromInGrams = 0.1, weightToInGrams = 3.4),
}
