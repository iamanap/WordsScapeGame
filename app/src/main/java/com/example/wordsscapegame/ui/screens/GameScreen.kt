package com.example.wordsscapegame.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordsscapegame.MainViewModel
import com.example.wordsscapegame.components.GameScoreBar
import com.example.wordsscapegame.components.ShadowedBox
import com.example.wordsscapegame.components.TextOutlinedAndFilled
import com.example.wordsscapegame.ui.Position
import com.example.wordsscapegame.ui.Word
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme

@Composable
fun GameScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 26.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GameScoreBar()
        WordTrails()
        CaughtWordsBox()
        ActionButton()
    }
}

@Composable
private fun WordTrails(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val wordsUiState = mainViewModel.wordsUiState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, bottom = 7.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        wordsUiState.movingWords.forEachIndexed { i, word ->
            WordTrail(Pair(i, word)) {
                if (mainViewModel.gameStatus.value == GameStatus.Playing
                    && word.position == Position.Moving)
                    mainViewModel.onCaughtWord(Pair(i, word))
            }
        }
    }
}

@Composable
private fun WordTrail(
    word: Pair<Int,Word>,
    onWordClicked: (Word) -> Unit
) {
    var parentWidth by remember { mutableFloatStateOf(0f) }
    var wordBoxWidth by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current.density

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 34.dp)
            .onGloballyPositioned { layoutCoordinates ->
                parentWidth = layoutCoordinates.size.width / density
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(AbsoluteAlignment.CenterLeft)
                .height(height = 34.dp)
                .padding(vertical = 2.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(WordsScapeGameTheme.extraColors.trailBackground)
        )
        WordBox(
            modifier = Modifier
                .onGloballyPositioned { layoutCoordinates ->
                    wordBoxWidth = layoutCoordinates.size.width  / density
                },
            targetOffset = (parentWidth - wordBoxWidth).dp,
            word = word
        ) { word ->
            Log.i("Game Screen", "Clicked $word")
            onWordClicked(word)
        }
    }
}

//@Preview
@Composable
private fun WordBox(
    word: Pair<Int, Word>,
    modifier: Modifier,
    targetOffset: Dp = 0.dp,
    mainViewModel: MainViewModel = hiltViewModel(),
    onWordClicked: (Word) -> Unit
) {
    val status = mainViewModel.gameStatus.collectAsState().value
    val wordsUiState = mainViewModel.wordsUiState.collectAsState()

    val animationDuration = maxOf(6000 / (1..wordsUiState.value.movingWords.size).random(), 1500)

    val wordOffsetAnimation by animateDpAsState(
        targetValue = if (status == GameStatus.Playing) targetOffset else 0.dp,
        animationSpec = tween(
            durationMillis = if (status == GameStatus.Playing) animationDuration else 100,
            easing = LinearEasing
        ),
        finishedListener = {
            if (status == GameStatus.Playing)
                mainViewModel.onLostWord(word)
        },
        label = "wordOffsetAnimation"
    )
    val wordVisibilityAnimation by animateFloatAsState(
        targetValue = if (!word.second.caught) 1f else 0f,
        animationSpec = tween(
            durationMillis = 200
        ),
    )

    val backgroundColor = when(word.second.position) {
        Position.Start -> WordsScapeGameTheme.extraColors.wordStart
        Position.Moving -> word.second.color
        else -> WordsScapeGameTheme.extraColors.wordLost
    }
    OutlinedButton(
        onClick = { onWordClicked(word.second) },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        contentPadding = PaddingValues(horizontal = 6.dp),
        modifier = modifier
            .wrapContentSize()
            .offset(x = wordOffsetAnimation)
    ) {
        TextOutlinedAndFilled(
            text = word.second.name,
            style = MaterialTheme.typography.labelMedium,
            strokeWidth = 4f,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
private fun CaughtWordsBox(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val wordsUiState = mainViewModel.wordsUiState.collectAsState().value
    val cornerShape = RoundedCornerShape(8.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 110.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = cornerShape)
            .shadow(elevation = 4.dp)
            .clip(shape = cornerShape)
            .background(WordsScapeGameTheme.extraColors.trailBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            wordsUiState.caughtWords.chunked(wordsUiState.movingWords.size / 2).forEachIndexed { i, rowWords ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    rowWords.forEachIndexed { j, word ->
                        WordBox(
                            modifier = Modifier,
                            word = Pair((i * 3) + j, word)
                        ) { }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButton(mainViewModel: MainViewModel = hiltViewModel()) {
    var pressed by remember { mutableStateOf(false) }
    val status = mainViewModel.gameStatus.collectAsState()

    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "actionButtonScaleAnimation"
    )

    val (backgroundColor, shadowColor) = when (status.value) {
        GameStatus.ReadyToPlay -> {
            Pair(MaterialTheme.colorScheme.primary,
            WordsScapeGameTheme.extraColors.rightScoreContainerShadowBackground)
        }
        else -> {
            Pair(WordsScapeGameTheme.extraColors.resetButtonBackground,
            WordsScapeGameTheme.extraColors.resetButtonBackgroundShadow)
        }
    }

    ShadowedBox(
        backgroundColor = backgroundColor,
        shadowColor = shadowColor,
        borderWidth = 2.dp,
        roundedCornerShape = RoundedCornerShape(14.dp),
        elevation = 6.dp,
        modifier = Modifier
            .size(width = 160.dp, height = 82.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    pressed = true
                    tryAwaitRelease()
                    pressed = false
                    mainViewModel.onGameStatusChanged()
                })
            }
    ) {
        TextOutlinedAndFilled(
            text = status.value.text,
            style = MaterialTheme.typography.labelLarge,
            strokeWidth = 8f,
            modifier = Modifier
                .wrapContentSize()
        )
    }
}

enum class GameStatus(val text: String) {
    ReadyToPlay("START"),
    Playing("RESET")
}

@Preview
@Composable
private fun GameScreenPreview() {
    GameScreen()
}