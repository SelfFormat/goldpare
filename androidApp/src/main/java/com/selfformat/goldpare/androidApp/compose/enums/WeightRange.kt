package com.selfformat.goldpare.androidApp.compose.enums

import com.selfformat.goldpare.androidApp.R

enum class WeightRange(
    val rangeName: Int,
    val labelRangeName: Int,
    val weightFromInGrams: Double,
    val weightToInGrams: Double
) {
    ALL(R.string.all, R.string.all, 0.0, weightToInGrams = 10000.0),
    OZ_2(R.string.ounce_2, R.string.oz_2, weightFromInGrams = 62.0, weightToInGrams = 68.0),
    OZ_1(R.string.ounce_1, R.string.oz_1, weightFromInGrams = 31.0, weightToInGrams = 34.0),
    OZ_1_2(R.string.ounce_1_2, R.string.oz_1_2, weightFromInGrams = 15.0, weightToInGrams = 17.0),
    OZ_1_4(R.string.ounce_1_4, R.string.oz_1_4, weightFromInGrams = 7.5, weightToInGrams = 8.5),
    OZ_1_10(R.string.ounce_1_10, R.string.oz_1_10, weightFromInGrams = 0.1, weightToInGrams = 3.4),
}
