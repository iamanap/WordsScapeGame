package com.example.wordsscapegame.domain.data

import androidx.compose.ui.graphics.Color

/**
 * Represents a word in the game.
 *
 * @property text The text of the word.
 * @property position The initial position of the word.
 * @property color The color of the word.
 * @property caught Indicates if the word has been caught by the player.
 * @property animation The animation properties of the word.
 *
 */
data class WordState(
    val text: String,
    val position: Position,
    val color: Color,
    val caught: Boolean,
    val animation: Animation
)

/**
 * Represents the animation properties of a word.
 *
 * @property duration The duration of the word's animation.
 * @property delay The delay before the word starts animating.
 */
data class Animation(
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