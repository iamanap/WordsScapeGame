package com.example.wordsscapegame.presentation.screens

import com.example.wordsscapegame.domain.data.Position
import com.example.wordsscapegame.domain.data.Word

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