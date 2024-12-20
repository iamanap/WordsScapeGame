package com.example.wordsscapegame

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.core.services.ReactionService
import com.example.wordsscapegame.core.services.SpeechRecognitionService
import com.example.wordsscapegame.core.services.SpeechState
import com.example.wordsscapegame.domain.data.GameData
import com.example.wordsscapegame.domain.data.Word
import com.example.wordsscapegame.presentation.screens.GameStatus
import com.example.wordsscapegame.presentation.screens.GameViewModel
import com.example.wordsscapegame.presentation.screens.Position
import com.example.wordsscapegame.presentation.screens.WordAnimationState
import com.example.wordsscapegame.presentation.screens.WordState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GameTest {
    private lateinit var viewModel: GameViewModel
    private val gameData: GameData = mock()
    private val reactionService: ReactionService = mock()
    private val speechRecognitionService: SpeechRecognitionService = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        whenever(speechRecognitionService.speechState).thenReturn(
            MutableStateFlow(SpeechState())
        )
        whenever(gameData.getMovingWords()).thenReturn(
            listOf(
                WordState(
                    word = Word(text = "test1"),
                    position = Position.Start,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                ),
                WordState(
                    word = Word(text = "test2"),
                    position = Position.Start,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                )
            )
        )
        viewModel = GameViewModel(gameData, reactionService, speechRecognitionService)
    }

    @Test
    fun `startGame updates game state to Playing and resets words`() = runTest {
        viewModel.changeGameStatus()

        assertEquals(GameStatus.Playing, viewModel.gameUiState.value.status)
        assertEquals(
            listOf(
                WordState(
                    word = Word(text = "test1"),
                    position = Position.Moving,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                ),
                WordState(
                    word = Word(text = "test2"),
                    position = Position.Moving,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                )
            ),
            viewModel.wordsUiState.value.movingWords
        )
    }

    @Test
    fun `stopGame updates game state to ReadyToPlay and resets words`() = runTest {
        // Start the game first
        viewModel.changeGameStatus()

        // Then stop the game
        viewModel.changeGameStatus()

        assertEquals(GameStatus.ReadyToPlay, viewModel.gameUiState.value.status)
        assertEquals(
            listOf(
                WordState(
                    word = Word(text = "test1"),
                    position = Position.Start,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                ),
                WordState(
                    word = Word(text = "test2"),
                    position = Position.Start,
                    color = Color.Red,
                    caught = false,
                    wordAnimationState = WordAnimationState(1000, 100)
                )
            ),
            viewModel.wordsUiState.value.movingWords
        )
    }

    @Test
    fun `onWordLost updates word position, lost score, and calls reactionService`() = runTest {
        viewModel.changeGameStatus()

        viewModel.onWordLost(0, viewModel.wordsUiState.value.movingWords[0])

        assertEquals(Position.End, viewModel.wordsUiState.value.movingWords[0].position)
        assertEquals(1, viewModel.gameUiState.value.lostScore)

        verify(reactionService).playText("test1")
        verify(reactionService).vibrate()
    }

    @Test
    fun `onWordCaught updates word state, caught score, and calls reactionService`() = runTest {
        viewModel.changeGameStatus()
        viewModel.onWordCaught(0)

        assertEquals(true, viewModel.wordsUiState.value.movingWords[0].caught)
        assertEquals(1, viewModel.gameUiState.value.caughtScore)
        verify(reactionService).playEffect(R.raw.catch_sound)
    }


}