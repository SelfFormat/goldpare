package com.selfformat.goldpare.androidApp.compose.data

import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.data.WeightRange.Companion.MAX_WEIGHT
import com.selfformat.goldpare.androidApp.compose.data.WeightRange.Companion.MIN_WEIGHT

sealed class WeightRange(
    open val weightFrom: Double = MIN_WEIGHT,
    open val weightTo: Double = MAX_WEIGHT,
) {
    companion object {
        const val MIN_WEIGHT = 0.0
        const val MAX_WEIGHT = 10000.0
    }
}

data class CustomWeightRange(
    override val weightFrom: Double = MIN_WEIGHT,
    override val weightTo: Double = MAX_WEIGHT
) : WeightRange(weightFrom, weightTo)

data class PredefinedWeightRange constructor(val predefinedWeightRanges: PredefinedWeightRanges) : WeightRange(
    weightFrom = predefinedWeightRanges.weightFromInGrams,
    weightTo = predefinedWeightRanges.weightToInGrams,
)

enum class PredefinedWeightRanges(
    val rangeName: Int,
    val labelRangeName: Int,
    val weightFromInGrams: Double,
    val weightToInGrams: Double
) {
    ALL(R.string.all, R.string.all, MIN_WEIGHT, weightToInGrams = MAX_WEIGHT),
    OZ_2(R.string.ounce_2, R.string.oz_2, weightFromInGrams = 62.0, weightToInGrams = 68.0),
    OZ_1(R.string.ounce_1, R.string.oz_1, weightFromInGrams = 31.0, weightToInGrams = 34.0),
    OZ_1_2(R.string.ounce_1_2, R.string.oz_1_2, weightFromInGrams = 15.0, weightToInGrams = 17.0),
    OZ_1_4(R.string.ounce_1_4, R.string.oz_1_4, weightFromInGrams = 7.5, weightToInGrams = 8.5),
    OZ_1_10(R.string.ounce_1_10, R.string.oz_1_10, weightFromInGrams = 0.1, weightToInGrams = 3.4),
}
