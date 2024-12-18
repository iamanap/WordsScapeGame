package com.example.wordsscapegame

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.wordsscapegame.ui.screens.GameScreen
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordsScapeGameTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { padding ->
                    GameScreen(
                        modifier = Modifier
                            .padding(
                                start = padding.calculateStartPadding(LayoutDirection.Ltr),
                                end = padding.calculateEndPadding(LayoutDirection.Rtl),
                                bottom = padding.calculateBottomPadding() - 10.dp,
                                top = padding.calculateTopPadding() - 18.dp
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WordsScapeGameTheme {
        GameScreen()
    }
}