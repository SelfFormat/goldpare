package com.selfformat.goldpare.androidApp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = gray200,
    primaryVariant = gray900,
    secondary = secondaryGold,
    background = darkBackground,
    onBackground = lightBackground,
    surface = darkBackground,
)

private val LightColorPalette = lightColors(
    primary = gray400,
    primaryVariant = gray100,
    secondary = secondaryGold,
    background = lightBackground,
    onBackground = darkBackground,
    surface = lightBackground
)

@Composable
fun GoldpareTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
