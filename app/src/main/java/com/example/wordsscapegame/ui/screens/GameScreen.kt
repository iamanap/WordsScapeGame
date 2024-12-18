package com.example.wordsscapegame.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordsscapegame.components.ActionButton
import com.example.wordsscapegame.components.GameScoreBar
import com.example.wordsscapegame.components.TextOutlinedAndFilled
import com.example.wordsscapegame.ui.theme.WordsScapeGameTheme

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel()
) {
    val wordsUiState by viewModel.wordsUiState.collectAsState()
    val gameUiState by viewModel.gameUiState.collectAsState()

    Box(modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .padding(vertical = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .offset(y = (-10).dp)
                .align(Alignment.TopCenter),
        ) {
            GameScoreBar(
                caughtScore = gameUiState.caughtScore,
                lostScore = gameUiState.lostScore
            )
            WordTrails(
                movingWords = wordsUiState.movingWords,
                onWordLost = { index, word -> viewModel.onWordLost(index, word) },
                onWordCaught = { index -> viewModel.onWordCaught(index) }
            )
            CaughtWordsBox(
                caughtWords = wordsUiState.caughtWords,
                modifier = Modifier
                    .weight(1f)
            )
            ActionButton(
                gameStatus = gameUiState.status,
                onGameStatusChanged = { viewModel.changeGameStatus() },
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun WordTrails(
    movingWords: List<Word>,
    onWordCaught: (Int) -> Unit,
    onWordLost: (Int, Word) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, bottom = 7.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        movingWords.forEachIndexed { i, word ->
            WordTrail(
                word = word,
                onWordCaught = { onWordCaught(i) },
                onWordLost = { onWordLost(i, word) }
            )
        }
    }
}

@Composable
private fun WordTrail(
    word: Word,
    onWordCaught: () -> Unit,
    onWordLost: () -> Unit
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
                    wordBoxWidth = layoutCoordinates.size.width / density
                },
            targetOffset = (parentWidth - wordBoxWidth).dp,
            word = word,
            onWordCaught = onWordCaught,
            onWordLost = onWordLost
        )
    }
}

@Composable
private fun WordBox(
    word: Word,
    modifier: Modifier,
    targetOffset: Dp = 0.dp,
    onWordCaught: () -> Unit,
    onWordLost: () -> Unit
) {
    val wordOffsetAnimation = offsetAnimation(word, targetOffset, onWordLost)
    val backgroundAnimation = backgroundAnimation(word)

    OutlinedButton(
        onClick = { onWordCaught() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundAnimation),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        contentPadding = PaddingValues(horizontal = 6.dp),
        modifier = modifier
            .wrapContentSize()
            .offset(x = wordOffsetAnimation)
            .alpha(if (word.caught && targetOffset == 0.dp) 0f else 1f)
    ) {
        TextOutlinedAndFilled(
            text = word.name,
            style = MaterialTheme.typography.labelMedium,
            strokeWidth = 4f,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
private fun backgroundAnimation(word: Word): Color {
    val backColor = when (word.position) {
        Position.Start -> WordsScapeGameTheme.extraColors.wordStart
        Position.Moving -> word.color
        else -> WordsScapeGameTheme.extraColors.wordLost
    }
    val backgroundAnimation by animateColorAsState(
        targetValue = backColor,
        animationSpec = tween(
            durationMillis = if (word.position == Position.Start) 0 else 200
        ),
        label = "backgroundColorAnimation"
    )
    return backgroundAnimation
}

@Composable
private fun offsetAnimation(
    word: Word,
    targetOffset: Dp,
    onWordLost: () -> Unit
): Dp {
    val (targetValue, duration, delay) = when (word.position) {
        Position.Start -> Triple(0.dp, 0, 0)
        Position.Moving -> Triple(targetOffset, word.animation.duration, word.animation.delay)
        else -> Triple(targetOffset, 0, 0)
    }

    val wordOffsetAnimation by animateDpAsState(
        targetValue = targetValue,
        animationSpec = tween(
            durationMillis = duration,
            delayMillis = delay,
            easing = LinearEasing
        ),
        finishedListener = {
            onWordLost()
        },
        label = "wordOffsetAnimation"
    )
    return wordOffsetAnimation
}

@Composable
private fun CaughtWordsBox(
    modifier: Modifier = Modifier,
    caughtWords: Set<Word>
) {
    val cornerShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
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
            caughtWords.chunked(3)
                .forEach { rowWords ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        rowWords.forEach { word ->
                            WordBox(
                                modifier = Modifier,
                                word = word,
                                onWordCaught = {},
                                onWordLost = {}
                            )
                        }
                    }
                }
        }
    }
}

@Preview
@Composable
private fun GameScreenPreview() {
    GameScreen()
}