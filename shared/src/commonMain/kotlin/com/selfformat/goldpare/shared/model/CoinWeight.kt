package com.selfformat.goldpare.shared.model

sealed class CoinWeight(val multiplier: Int, val name: String) {
    object OZ1_4 : CoinWeight(4, "1/4 oz")
    object OZ1 : CoinWeight(1, "1 oz")
    object OZ1_2 : CoinWeight(2, "1/2 oz")
    object OZ1_10 : CoinWeight(10, "1/10 oz")
    object OZ2 : CoinWeight(2, "2 oz")
    object OZ1_25 : CoinWeight(25, "1/25 oz")
    object Unknown : CoinWeight(1, "Unknown")
}
