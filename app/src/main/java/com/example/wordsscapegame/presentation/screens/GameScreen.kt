package com.example.wordsscapegame.presentation.screens

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
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wordsscapegame.presentation.components.ActionButton
import com.example.wordsscapegame.presentation.components.GameScoreBar
import com.example.wordsscapegame.presentation.components.TextOutlinedAndFilled
import com.example.wordsscapegame.presentation.theme.WordsScapeGameTheme
import kotlinx.coroutines.delay

/**
 * Represents the game screen where the word-catching game is played.
 *
 * This composable displays the game elements, including the score bar,
 * word trails, caught words box, and action button. It handles
 * game logic and user interactions.
 *
 * @param viewModel The GameViewModel instance used to manage game state.
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val wordsUiState by viewModel.wordsUiState.collectAsState()
    val gameUiState by viewModel.gameUiState.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    end = padding.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = padding.calculateBottomPadding() - 18.dp,
                    top = padding.calculateTopPadding() - 4.dp
                )
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
                    movingWordStates = wordsUiState.movingWords,
                    onWordLost = { index, word -> viewModel.onWordLost(index, word) },
                    onWordCaught = { index -> viewModel.onWordCaught(index) }
                )
                CaughtWordsBox(
                    caughtWordStates = wordsUiState.caughtWords,
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
}

/**
 * Displays the trails for the moving words in the game.
 *
 * This composable iterates through the list of moving words and
 * renders a WordTrail for each word. It handles word catching
 * and word loss events.
 *
 * @param movingWordStates The list of words currently moving in the game.
 * @param onWordCaught Callback function triggered when a word is caught.
 * @param onWordLost Callback function triggered when a word is lost.
 */
@Composable
private fun WordTrails(
    movingWordStates: List<WordState>,
    onWordCaught: (Int) -> Unit,
    onWordLost: (Int, WordState) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 13.dp, start = 20.dp, end = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        movingWordStates.forEachIndexed { i, word ->
            WordTrail(
                wordState = word,
                onWordCaught = { onWordCaught(i) },
                onWordLost = { onWordLost(i, word) }
            )
        }
    }
}

/**
 * Displays the trail for a single moving word.
 *
 * This composable renders the visual representation of a word's
 * trail and handles its movement and interactions.
 *
 * @param wordState The word object to be displayed.
 * @param onWordCaught Callback function triggered when the word is caught.
 * @param onWordLost Callback function triggered when the word is lost.
 */
@Composable
private fun WordTrail(
    wordState: WordState,
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
            visible = !wordState.caught,
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
                wordState = wordState,
                onWordCaught = onWordCaught,
                onWordLost = onWordLost
            )
        }
    }
}

/**
 * Displays the visual representation of a word box.
 *
 * This composable renders the word box with its text and handles
 * its animation and interactions.
 *
 * @param wordState The word object to be displayed in the box.
 * @param modifier The modifier to be applied to the layout.
 * @param targetOffset The target offset for the word box animation.
 * @param onWordCaught Callback function triggered when the word is caught.
 * @param onWordLost Callback function triggered when the word is lost.
 */
@Composable
private fun WordBox(
    wordState: WordState,
    modifier: Modifier,
    targetOffset: Float = 0f,
    onWordCaught: () -> Unit,
    onWordLost: () -> Unit
) {
    var clickable by rememberSaveable { mutableStateOf(false) }
    val wordBoxAnimation = onPosition(wordState = wordState, targetOffset = targetOffset)

    LaunchedEffect(wordState.position) {
        if (wordState.position == Position.Moving) {
            delay(wordBoxAnimation.delay.toLong())
            clickable = true
        } else clickable = false
    }

    val backAnimation = animateColorAsState(
        targetValue = wordBoxAnimation.backgroundColor,
        animationSpec = tween(
            durationMillis = if (wordState.position == Position.Start) 0 else 200,
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

    WordBoxComponent(modifier, wordState.word.text, backAnimation.value, offsAnimation.value) {
        if (clickable) onWordCaught()
    }
}

/**
 * Renders the visual component of a word box.
 *
 * This composable displays the word box with its text, background,
 * and border. It handles the click event for catching the word.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param text The text to be displayed in the box.
 * @param backgroundAnimation The background color of the word box.
 * @param offsetAnimation The offset of the word box animation.
 * @param onWordCaught Callback function triggered when the word is caught.
 */
@Composable
private fun WordBoxComponent(
    modifier: Modifier,
    text: String,
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
            text = text,
            style = MaterialTheme.typography.labelMedium,
            strokeWidth = 4f,
            modifier = Modifier
                .padding(horizontal = 6.dp)
        )
    }
}

/**
 * Determines the animation parameters for a word box based on its position.
 *
 * This function returns a WordBoxAnimation object containing the
 * background color, target offset, duration, and delay for the
 * word box animation.
 *
 * @param wordState The word object for which to determine animation parameters.
 * @param targetOffset The target offset for the word box animation.
 * @return A WordBoxAnimation object containing the animation parameters.
 */
@Composable
fun onPosition(wordState: WordState, targetOffset: Float): WordBoxAnimation {
    return when (wordState.position) {
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
                backgroundColor = wordState.color,
                targetOffset = targetOffset,
                duration = wordState.wordAnimationState.duration,
                delay = wordState.wordAnimationState.delay
            )
        }

        else ->
            WordBoxAnimation(
                backgroundColor = if (!wordState.caught) WordsScapeGameTheme.extraColors.wordLost
                else wordState.color,
                targetOffset = targetOffset,
                duration = 0,
                delay = 0
            )
    }
}

/**
 * Displays the box containing the caught words.
 *
 * This composable renders a box that displays the words that have
 * been caught during the game.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param caughtWordStates The set of words that have been caught.
 */

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CaughtWordsBox(
    modifier: Modifier = Modifier,
    caughtWordStates: Set<WordState>
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
            caughtWordStates.forEach { word ->
                WordBox(
                    modifier = Modifier,
                    wordState = word,
                    onWordCaught = {},
                    onWordLost = {}
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
private fun GameScreenPreview() {
    GameScreen()
}

/**
 * Data class representing the animation parameters for a word box.
 *
 * This class holds the background color, target offset, duration,
 * and delay for the animation of a word box.
 *
 * @property backgroundColor The background color of the word box.
 * @property targetOffset The target offset for the word box animation.
 * @property duration The duration of the animation in milliseconds.
 * @property delay The delay before the animation starts in milliseconds.
 */
data class WordBoxAnimation(
    val backgroundColor: Color,
    val targetOffset: Float,
    val duration: Int,
    val delay: Int
)