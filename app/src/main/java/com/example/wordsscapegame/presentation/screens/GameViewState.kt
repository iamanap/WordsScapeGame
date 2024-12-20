package com.example.wordsscapegame.presentation.screens

import androidx.annotation.StringRes
import com.example.wordsscapegame.R

data class GameUiState(
    val status: GameStatus,
    val caughtScore: Int,
    val lostScore: Int
) {

    fun changeGameToPlaying(): GameUiState {
        return copy(status = GameStatus.Playing)
    }

    fun changeGameToReadyToPlay(): GameUiState {
        return copy(status = GameStatus.ReadyToPlay, caughtScore = 0, lostScore = 0)
    }

    fun incrementCaughtScore(): GameUiState {
        return copy(caughtScore = caughtScore + 1)
    }

    fun incrementLostScore(): GameUiState {
        return copy(lostScore = lostScore + 1)
    }
}

enum class GameStatus(@StringRes val text: Int) {
    ReadyToPlay(R.string.start),
    Playing(R.string.reset)
}