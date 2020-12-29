package com.selfformat.goldpare.androidApp.compose.enums

import com.selfformat.goldpare.androidApp.R

enum class GoldType(val typeCode: String, val typeName: Int) {
    ALL("all", R.string.gold),
    COIN("coin", R.string.coins),
    BAR("bar", R.string.bars)
}
