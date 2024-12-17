package com.example.wordsscapegame.ui

import androidx.compose.ui.graphics.Color

class WordsUiState(
    movingWords: List<Word>,
    caughtWords: Set<Word>
) {
    private val _movingWords: MutableList<Word> = movingWords.toMutableList()
    val movingWords: List<Word> = _movingWords

    private val _caughtWords: MutableSet<Word> = caughtWords.toMutableSet()
    val caughtWords: Set<Word> = _caughtWords

    fun updateAllMovingWordsPositions(position: Position) {
        _movingWords.forEachIndexed { index, _ ->
            updateWordPosition(index, position)
        }
    }

    fun updateWordPosition(index: Int, position: Position) {
        _movingWords[index] = _movingWords[index].copy(position = position)
    }

    fun addCaughtWord(word: Pair<Int, Word>) {
        if (!_caughtWords.contains(word.second)) {
            val caughtWord = word.second.copy(caught = true)
            _caughtWords.add(caughtWord)
            _movingWords[word.first] = caughtWord
        }
    }

    fun clearCaughtWords() {
        _caughtWords.clear()
    }
}

data class Word(
    val name: String,
    val position: Position,
    val color: Color,
    val caught: Boolean
)

enum class Position {
    Start,
    Moving,
    End
}