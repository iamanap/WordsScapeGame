package com.example.wordsscapegame.presentation.screens

/**
 * Represents the state of the words in the game.
 *
 * Holds the list of moving words.
 *
 * @property movingWords The list of words currently moving in the game.
 */
data class WordsViewState(
    val movingWords: List<WordState>,
    val caughtWords: Set<WordState> = emptySet()
) {

    fun updateWordPosition(index: Int, position: Position): WordsViewState {
        val updatedWords = movingWords.toMutableList()
        updatedWords[index] = updatedWords[index].copy(position = position)
        return copy(movingWords = updatedWords)
    }

    fun addCaughtWord(index: Int): WordsViewState {
        val updatedWords = movingWords.toMutableList()
        val caughtWord = updatedWords[index].copy(caught = true)
        updatedWords[index] = caughtWord

        return copy(
            movingWords = updatedWords,
            caughtWords = caughtWords + caughtWord
        )
    }

    fun resetWords(newWordStates: List<WordState>): WordsViewState {
        return WordsViewState(movingWords = newWordStates, caughtWords = emptySet())
    }
}