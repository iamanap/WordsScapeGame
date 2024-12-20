package com.example.wordsscapegame.presentation.screens

import androidx.annotation.StringRes
import com.example.wordsscapegame.R

/**
 * Represents the state of the game.
 *
 * Holds information about the game status, caught score, and lost score.
 *
 * @property status The current status of the game (ReadyToPlay, Playing).
 * @property caughtScore The number of words caught by the player.
 * @property lostScore The number of words lost by the player.
 */
data class GameViewState(
    val status: GameStatus,
    val caughtScore: Int,
    val lostScore: Int
) {

    fun changeGameToPlaying(): GameViewState {
        return copy(status = GameStatus.Playing)
    }

    fun changeGameToReadyToPlay(): GameViewState {
        return copy(status = GameStatus.ReadyToPlay, caughtScore = 0, lostScore = 0)
    }

    fun incrementCaughtScore(): GameViewState {
        return copy(caughtScore = caughtScore + 1)
    }

    fun incrementLostScore(): GameViewState {
        return copy(lostScore = lostScore + 1)
    }
}

enum class GameStatus(@StringRes val text: Int) {
    ReadyToPlay(R.string.start),
    Playing(R.string.reset)
}