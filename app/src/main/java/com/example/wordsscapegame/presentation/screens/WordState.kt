package com.example.wordsscapegame.presentation.screens

import androidx.compose.ui.graphics.Color
import com.example.wordsscapegame.domain.data.Word

/**
 * Represents a word in the game.
 *
 * @property word The word data.
 * @property position The initial position of the word.
 * @property color The color of the word.
 * @property caught Indicates if the word has been caught by the player.
 * @property wordAnimationState The animation properties of the word.
 *
 */
data class WordState(
    val word: Word,
    val position: Position,
    val color: Color,
    val caught: Boolean,
    val wordAnimationState: WordAnimationState
)

/**
 * Represents the animation properties of a word.
 *
 * @property duration The duration of the word's animation.
 * @property delay The delay before the word starts animating.
 */
data class WordAnimationState(
    val duration: Int,
    val delay: Int
)

/**
 * Represents the position of a word.
 */
enum class Position {
    Start,
    Moving,
    End
}