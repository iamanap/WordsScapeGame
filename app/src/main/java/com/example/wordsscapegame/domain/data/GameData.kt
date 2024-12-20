package com.example.wordsscapegame.domain.data

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.presentation.screens.Position
import com.example.wordsscapegame.presentation.screens.WordAnimationState
import com.example.wordsscapegame.presentation.screens.WordState
import com.example.wordsscapegame.presentation.theme.Green
import com.example.wordsscapegame.presentation.theme.Orange
import com.example.wordsscapegame.presentation.theme.Peach
import com.example.wordsscapegame.presentation.theme.Purple
import com.example.wordsscapegame.presentation.theme.Red
import javax.inject.Inject

/**
 * A data class that holds the game data.
 *
 * This class provides a list of moving words for the game.
 *
 * @constructor Creates a new instance of GameData.
 */
class GameData @Inject constructor() {
    fun getMovingWords(): List<WordState> {
        return listOf(
            WordState(
                word = Word(text = "Apple"),
                position = Position.Start,
                color = Red,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 4000,
                    delay = 0
                )
            ),
            WordState(
                word = Word(text = "Banana"),
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 3500,
                    delay = 500
                )
            ),
            WordState(
                word = Word(text = "Grape"),
                position = Position.Start,
                color = Purple,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 3000,
                    delay = 0
                )
            ),
            WordState(
                word = Word(text = "Orange"),
                position = Position.Start,
                color = Orange,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 2500,
                    delay = 1500
                )
            ),
            WordState(
                word = Word(text = "Watermelon"),
                position = Position.Start,
                color = Green,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 2000,
                    delay = 1500
                )
            ),
            WordState(
                word = Word(text = "Lemon"),
                position = Position.Start,
                color = Color.Yellow,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 1500,
                    delay = 500
                )
            ),
            WordState(
                word = Word(text = "Peach"),
                position = Position.Start,
                color = Peach,
                caught = false,
                wordAnimationState = WordAnimationState(
                    duration = 1500,
                    delay = 2500
                )
            )
        )
    }
}