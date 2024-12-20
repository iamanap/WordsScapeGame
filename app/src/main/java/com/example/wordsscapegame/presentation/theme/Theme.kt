package com.example.wordsscapegame.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val ColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = Amber,
    tertiary = Orange,
    background = DarkBrown,
    outline = DarkGray
)

@Immutable
data class WordsScapeGameExtraColors(
    val redContainerBackground: Color = Color.Unspecified,
    val redContainerShadowBackground: Color = Color.Unspecified,
    val greenContainerBackground: Color = Color.Unspecified,
    val greenContainerShadowBackground: Color = Color.Unspecified,
    val wordStart: Color = Color.Unspecified,
    val wordLost: Color = Color.Unspecified,
    val trailBackground: Color = Color.Unspecified,
    val startButtonBackgroundShadow: Color = Color.Unspecified,
    val resetButtonBackground: Color = Color.Unspecified,
    val resetButtonBackgroundShadow: Color = Color.Unspecified
)

val LocalExtraColors = staticCompositionLocalOf {
    WordsScapeGameExtraColors()
}

private val ExtraColors = WordsScapeGameExtraColors(
    redContainerBackground = Red,
    redContainerShadowBackground = DarkRed,
    greenContainerBackground = Green,
    greenContainerShadowBackground = DarkGreen,
    wordStart = Amber,
    wordLost = Gray,
    trailBackground = LightBrown,
    startButtonBackgroundShadow = DarkGreen,
    resetButtonBackground = Amber,
    resetButtonBackgroundShadow = DarkAmber
)

@Composable
fun WordsScapeGameTheme(
    content: @Composable () -> Unit
) {
    val colorScheme =  ColorScheme
    val extraColors = ExtraColors

    CompositionLocalProvider(LocalExtraColors provides extraColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

object WordsScapeGameTheme {
    val extraColors: WordsScapeGameExtraColors
        @Composable
        get() = LocalExtraColors.current
}