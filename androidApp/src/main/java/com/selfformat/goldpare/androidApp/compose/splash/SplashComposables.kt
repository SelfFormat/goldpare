package com.selfformat.goldpare.androidApp.compose.splash

import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.fontFamily
import com.selfformat.goldpare.androidApp.R
import com.selfformat.goldpare.androidApp.compose.theme.logoDescriptionFontSize
import com.selfformat.goldpare.androidApp.compose.theme.logoFontSize

@Composable
fun SplashWithLoading() {
    Column(
        modifier = Modifier.fillMaxSize().fillMaxHeight().background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(id = R.string.app_name),
            color = Color.White,
            fontSize = logoFontSize,
            fontFamily = fontFamily(Typeface.SERIF)
        )
        Text(stringResource(R.string.loading), color = Color.White, fontSize = logoDescriptionFontSize)
    }
}
