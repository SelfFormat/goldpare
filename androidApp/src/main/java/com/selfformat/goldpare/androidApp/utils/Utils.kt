package com.selfformat.goldpare.androidApp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.data.Mint
import com.selfformat.goldpare.shared.models.GoldItem

fun openWebPage(url: String, context: Context) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    ContextCompat.startActivity(context, intent, null)
}

fun mintFullName(website: String, context: Context): String {
    Mint.values().forEach {
        if (website == it.name) {
            return it.fullName
        }
    }
    return context.getString(R.string.unknown_provider)
}

fun regexIgnoreCase(pattern: String): Regex {
    return Regex(pattern = pattern, RegexOption.IGNORE_CASE)
}

fun fakeGoldItem(): GoldItem {
    return GoldItem(
        id = 1,
        price = "3000zł",
        title = "Krugerrand 1/2 oz",
        link = "www.gold.com/1oz",
        website = "gold.com",
        image = "https://79element.pl/1382-home_default/australijski-lunar-lii-rok-myszy-2020-1oz.jpg",
        weight = "1/4oz",
        quantity = 1,
        type = "coin",
        priceDouble = 3000.0,
        weightInGrams = 15.55,
        pricePerGram = 100.0,
        pricePerOunce = 6000.0
    )
}
