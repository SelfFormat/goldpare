package com.selfformat.goldpare.androidApp.compose

enum class GoldCoinType(coinName: String = "") {
    //TODO: Change coinName to be regexp based on results
    KRUGERRAND("Krugerrand"),
    DUKAT("Dukat"),
    BRITANNIA("Britannia"),
    LISC_KLONOWY("Klonowy"),
    AUSTRALIJSKI_KANGUR("Kangur"),
    CHINSKA_PANDA("Panda"),
    AMERYKANSKI_BIZON("Bizon"),
    AMERYKANSKI_ORZEŁ("Orzeł"),
    FILCHARMONICY_WIEDENSCY("Filharmonik"),
    ALL()
}