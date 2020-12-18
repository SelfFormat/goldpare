package com.selfformat.goldpare.androidApp.compose

enum class GoldCoinType(val coinName: String = "") {
    //TODO: Change coinName to be regexp based on results
    ALL("Wszystkie"),
    KRUGERRAND("Krugerrand"),
    DUKAT("Dukat"),
    BRITANNIA("Britannia"),
    LISC_KLONOWY("Klonowy"),
    AUSTRALIJSKI_KANGUR("Kangur"),
    CHINSKA_PANDA("Pad"),
    AMERYKANSKI_BIZON("Bizon"),
    AMERYKANSKI_ORZEL("Orzeł"),
    FILCHARMONICY_WIEDENSCY("Filharmonik")
}