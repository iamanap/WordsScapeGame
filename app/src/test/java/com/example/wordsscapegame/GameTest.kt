package com.example.wordsscapegame

import androidx.compose.animation.core.Animation
import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.core.services.ReactionService
import com.example.wordsscapegame.domain.data.GameData
import com.example.wordsscapegame.presentation.screens.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.mockito.kotlin.mock

import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class GameUnitTest {
    private lateinit var viewModel: GameViewModel
    private val gameData: GameData = mock()
    private val reactionService: ReactionService = mock()
    private val speechRecognitionService: SpeechRecognitionService = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        whenever(gameData.getMovingWords()).thenReturn(
            listOf(
                Word(text = "test1", position = androidx.room.parser.expansion.Position.Start, color = Color.Red, caught = false, animation = Animation(1000, 100)),
                Word(text = "test2", position = androidx.room.parser.expansion.Position.Start, color = Color.Red, caught = false, animation = Animation(1000, 100))
            )
        )
        viewModel = GameViewModel(gameData, reactionService, speechRecognitionService)
    }
}