package com.chinchillateam.chinchillatune.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color(0xFF880e4f),
    primaryVariant = Color(0xFF560027),
    secondary = Color(0xFF33691e)
)

private val LightColorPalette = lightColors(
    primary = Color(0xFFFFE0EA),
    primaryVariant = Color(0xFFccb9bc),
    secondary = Color(0xFFf1f8e9),

    // Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
)

@Composable
fun ChinchillaTunerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    // TODO fix dark mode (jan anton?)
//    val colors = if (darkTheme) DarkColorPalette else LightColorPalette
    val colors = LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content,

    )
}