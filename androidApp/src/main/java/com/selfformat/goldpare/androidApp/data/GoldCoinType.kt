package com.selfformat.goldpare.androidApp.data

import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.utils.regexIgnoreCase

enum class GoldCoinType(val coinName: Int, val regex: Regex) {

    ALL(R.string.all_coins_type, Regex("")),
    KRUGERRAND(R.string.krugerrand, regexIgnoreCase("krugerrand")),
    DUKAT(R.string.ducats, regexIgnoreCase("dukat|ducats")),
    BRITANNIA(R.string.britannia, regexIgnoreCase("britannia")),
    LISC_KLONOWY(R.string.maple_leaf, regexIgnoreCase("maple|klon|liść|lisc")),
    AUSTRALIJSKI_KANGUR(R.string.australian_kangaroo, regexIgnoreCase("kangur|kangaroo")),
    CHINSKA_PANDA(R.string.chinese_panda, regexIgnoreCase("panda")),
    AMERYKANSKI_BIZON(R.string.american_buffalo, regexIgnoreCase("bizon|buffalo")),
    AMERYKANSKI_ORZEL(R.string.american_eagle, regexIgnoreCase("eagle|orze")),
    FILCHARMONICY_WIEDENSCY(R.string.vienna_philharmonic, regexIgnoreCase("vienna|philharmoni|filchar|wiede")),
    BESTIE_KROLOWEJ(R.string.queens_bests, regexIgnoreCase("bestie|beasts"))
}
