package com.selfformat.goldpare.androidApp.compose.enums

import com.selfformat.goldpare.androidApp.R

enum class GoldCoinType(val coinName: Int, val regex: Regex) {
    // TODO Change each coinName to be regexp based on results
    ALL(R.string.all_coins_type, Regex("")),
    KRUGERRAND(R.string.krugerrand, Regex("")),
    DUKAT(R.string.ducats, Regex("")),
    BRITANNIA(R.string.britannia, Regex("")),
    LISC_KLONOWY(R.string.maple_leaf, Regex("")),
    AUSTRALIJSKI_KANGUR(R.string.australian_kangaroo, Regex("")),
    CHINSKA_PANDA(R.string.chinese_panda, Regex("")),
    AMERYKANSKI_BIZON(R.string.american_buffalo, Regex("")),
    AMERYKANSKI_ORZEL(R.string.american_eagle, Regex("")),
    FILCHARMONICY_WIEDENSCY(R.string.vienna_philharmonic, Regex("")),
    BESTIE_KROLOWEJ(R.string.queens_bests, Regex(""))
}
