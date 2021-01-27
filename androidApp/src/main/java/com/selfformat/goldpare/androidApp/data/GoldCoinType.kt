package com.selfformat.goldpare.androidApp.data

import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.utils.regexIgnoreCase

enum class GoldCoinType(val coinName: Int, val regex: Regex) {

    ALL(R.string.all_coins_type, Regex("")),
    KRUGERRAND(R.string.krugerrand, regexIgnoreCase("krugerrand")),
    DUCATS(R.string.ducats, regexIgnoreCase("dukat|ducats")),
    BRITANNIA(R.string.britannia, regexIgnoreCase("britannia")),
    MAPLE_LEAF(R.string.maple_leaf, regexIgnoreCase("maple|klon|liść|lisc")),
    AUSTRALIAN_KANGAROO(R.string.australian_kangaroo, regexIgnoreCase("kangur|kangaroo")),
    CHINESE_PANDA(R.string.chinese_panda, regexIgnoreCase("panda")),
    AMERICAN_BUFFALO(R.string.american_buffalo, regexIgnoreCase("bizon|buffalo")),
    AMERICAN_EAGLE(R.string.american_eagle, regexIgnoreCase("eagle|orze")),
    VIENNA_PHILHARMONIC(R.string.vienna_philharmonic, regexIgnoreCase("vienna|philharmoni|filchar|wiede")),
    QUEENS_BEASTS(R.string.queens_bests, regexIgnoreCase("bestie|beasts"))
}
