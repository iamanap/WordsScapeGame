package com.example.wordsscapegame.ui.screens

import androidx.compose.ui.graphics.Color

data class WordsUiState(
    val movingWords: List<Word>,
    val caughtWords: Set<Word> = emptySet()
) {

    fun updateWordPosition(index: Int, position: Position): WordsUiState {
        val updatedWords = movingWords.toMutableList()
        updatedWords[index] = updatedWords[index].copy(position = position)
        return copy(movingWords = updatedWords)
    }

    fun addCaughtWord(index: Int): WordsUiState {
        val updatedWords = movingWords.toMutableList()
        val caughtWord = updatedWords[index].copy(caught = true)
        updatedWords[index] = caughtWord

        return copy(
            movingWords = updatedWords,
            caughtWords = caughtWords + caughtWord
        )
    }

    fun resetWords(newWords: List<Word>): WordsUiState {
        return WordsUiState(movingWords = newWords, caughtWords = emptySet())
    }
}

data class Word(
    val text: String,
    val position: Position,
    val color: Color,
    val caught: Boolean,
    val animation: Animation
)

data class Animation(
    val duration: Int,
    val delay: Int
)

enum class Position {
    Start,
    Moving,
    End
}