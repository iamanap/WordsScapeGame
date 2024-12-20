package com.example.wordsscapegame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordsscapegame.components.ActionButton
import com.example.wordsscapegame.components.GameScoreBar
import com.example.wordsscapegame.components.TextOutlinedAndFilled
import com.example.wordsscapegame.domain.data.Position
import com.example.wordsscapegame.domain.data.Word
import com.example.wordsscapegame.presentation.theme.WordsScapeGameTheme
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel()
) {
    val wordsUiState by viewModel.wordsUiState.collectAsState()
    val gameUiState by viewModel.gameUiState.collectAsState()

    Box(
        modifier = modifier
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
            val (backgroundColor, shadowColor) = when (gameUiState.status) {
                GameStatus.ReadyToPlay -> {
                    Pair(
                        MaterialTheme.colorScheme.primary,
                        WordsScapeGameTheme.extraColors.greenContainerShadowBackground
                    )
                }

                else -> {
                    Pair(
                        WordsScapeGameTheme.extraColors.resetButtonBackground,
                        WordsScapeGameTheme.extraColors.resetButtonBackgroundShadow
                    )
                }
            }
            ActionButton(
                onClicked = { viewModel.changeGameStatus() },
                backgroundColor = backgroundColor,
                shadowColor = shadowColor,
                text = stringResource(gameUiState.status.text),
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
            .padding(top = 13.dp, start = 20.dp, end = 20.dp),
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height = 34.dp)
            .onGloballyPositioned { layoutCoordinates ->
                parentWidth = layoutCoordinates.size.width.toFloat()
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
        AnimatedVisibility(
            visible = !word.caught,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 50)
            ),
            exit = fadeOut(
                animationSpec = tween(durationMillis = 0)
            )
        ) {
            WordBox(
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        wordBoxWidth = layoutCoordinates.size.width.toFloat()
                    },
                targetOffset = (parentWidth - wordBoxWidth),
                word = word,
                onWordCaught = onWordCaught,
                onWordLost = onWordLost
            )
        }
    }
}

@Composable
private fun WordBox(
    word: Word,
    modifier: Modifier,
    targetOffset: Float = 0f,
    onWordCaught: () -> Unit,
    onWordLost: () -> Unit
) {
    var clickable by rememberSaveable { mutableStateOf(false) }
    val wordBoxAnimation = onPosition(word = word, targetOffset = targetOffset)

    LaunchedEffect(word.position) {
        if (word.position == Position.Moving) {
            delay(wordBoxAnimation.delay.toLong())
            clickable = true
        } else clickable = false
    }

    val backAnimation = animateColorAsState(
        targetValue = wordBoxAnimation.backgroundColor,
        animationSpec = tween(
            durationMillis = if (word.position == Position.Start) 0 else 200,
            delayMillis = wordBoxAnimation.delay
        ), label = "wordBackgroundAnimation"
    )

    val offsAnimation = animateFloatAsState(
        targetValue = wordBoxAnimation.targetOffset,
        animationSpec = tween(
            durationMillis = wordBoxAnimation.duration,
            delayMillis = wordBoxAnimation.delay,
            easing = LinearEasing,
        ),
        label = "wordOffsetAnimation",
        finishedListener = {
            onWordLost()
        }
    )

    WordBoxComponent(modifier, word, backAnimation.value, offsAnimation.value) {
        if (clickable) onWordCaught()
    }
}

@Composable
private fun WordBoxComponent(
    modifier: Modifier,
    word: Word,
    backgroundAnimation: Color,
    offsetAnimation: Float,
    onWordCaught: () -> Unit
) {
    OutlinedButton(
        onClick = { onWordCaught() },
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundAnimation),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline),
        contentPadding = PaddingValues(horizontal = 6.dp),
        modifier = modifier
            .wrapContentSize()
            .offset(
                x = with(LocalDensity.current) { offsetAnimation.toDp() },
                y = 0.dp
            )
    ) {
        TextOutlinedAndFilled(
            text = word.text,
            style = MaterialTheme.typography.labelMedium,
            strokeWidth = 4f,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
fun onPosition(word: Word, targetOffset: Float): WordBoxAnimation {
    return when (word.position) {
        Position.Start -> {
            WordBoxAnimation(
                backgroundColor = WordsScapeGameTheme.extraColors.resetButtonBackground,
                targetOffset = 0f,
                duration = 0,
                delay = 0
            )
        }

        Position.Moving -> {
            WordBoxAnimation(
                backgroundColor = word.color,
                targetOffset = targetOffset,
                duration = word.animation.duration,
                delay = word.animation.delay
            )
        }

        else ->
            WordBoxAnimation(
                backgroundColor = if (!word.caught) WordsScapeGameTheme.extraColors.wordLost
                    else word.color
                ,
                targetOffset = targetOffset,
                duration = 0,
                delay = 0
            )
    }
}

@OptIn(ExperimentalLayoutApi::class)
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
        FlowRow(
            maxItemsInEachRow = 3,
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            caughtWords.forEach { word ->
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

@Preview
@Composable
private fun GameScreenPreview() {
    GameScreen()
}

data class WordBoxAnimation(
    val backgroundColor: Color,
    val targetOffset: Float,
    val duration: Int,
    val delay: Int
)