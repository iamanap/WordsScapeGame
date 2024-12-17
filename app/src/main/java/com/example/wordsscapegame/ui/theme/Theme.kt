package com.example.wordsscapegame.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80
//)
//
//private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)

private val ColorScheme = lightColorScheme(
    primary = LightGreen,
    secondary = Amber,
    tertiary = Orange,
    background = DarkBrown,
    outline = DarkGray
)

@Immutable
data class WordsScapeGameExtraColors(
    val incorrectScoreContainerBackground: Color = Color.Unspecified,
    val incorrectScoreContainerShadowBackground: Color = Color.Unspecified,
    val rightScoreContainerBackground: Color = Color.Unspecified,
    val rightScoreContainerShadowBackground: Color = Color.Unspecified,
    val wordStart: Color = Color.Unspecified,
    val trailBackground: Color = Color.Unspecified,
    val startButtonBackgroundShadow: Color = Color.Unspecified,
)

val LocalExtraColors = staticCompositionLocalOf {
    WordsScapeGameExtraColors()
}

private val ExtraColors = WordsScapeGameExtraColors(
    incorrectScoreContainerBackground = Red,
    incorrectScoreContainerShadowBackground = DarkRed,
    rightScoreContainerBackground = Green,
    rightScoreContainerShadowBackground = DarkGreen,
    wordStart = Amber,
    trailBackground = LightBrown,
    startButtonBackgroundShadow = DarkGreen
)

@Composable
fun WordsScapeGameTheme(
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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