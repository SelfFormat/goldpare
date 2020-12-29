package com.selfformat.goldpare.androidApp.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = gray200,
    primaryVariant = gray900,
    secondary = blue200,
    background = darkBackground,
    onBackground = lightBackground,
    surface = darkBackground,
)

private val LightColorPalette = lightColors(
    primary = gray400,
    primaryVariant = gray100,
    secondary = blue200,
    background = lightBackground,
    onBackground = darkBackground,
    surface = lightBackground
    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
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
