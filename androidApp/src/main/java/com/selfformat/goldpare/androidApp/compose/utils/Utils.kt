package com.selfformat.goldpare.androidApp.compose.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.data.Mint

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
