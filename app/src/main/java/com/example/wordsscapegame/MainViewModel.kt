package com.example.wordsscapegame

import androidx.lifecycle.ViewModel
import com.example.wordsscapegame.data.GameRepository
import com.example.wordsscapegame.ui.Position
import com.example.wordsscapegame.ui.Word
import com.example.wordsscapegame.ui.WordsUiState
import com.example.wordsscapegame.ui.screens.GameStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val gameRepository: GameRepository): ViewModel() {

    private val _caughtScoreCount = MutableStateFlow(0)
    val caughtScoreCount = _caughtScoreCount.asStateFlow()

    private val _lostScoreCount = MutableStateFlow(0)
    val lostScoreCount = _lostScoreCount.asStateFlow()

    private val _gameStatus = MutableStateFlow(GameStatus.ReadyToPlay)
    val gameStatus = _gameStatus.asStateFlow()

    private val _wordsUiState = MutableStateFlow(WordsUiState(movingWords = gameRepository.getMovingWords(), caughtWords = emptySet()))
    val wordsUiState = _wordsUiState.asStateFlow()

    private fun incrementRightScore() {
        _caughtScoreCount.value += 1
    }

    private fun incrementWrongScore() {
        _lostScoreCount.value += 1
    }

    fun onGameStatusChanged() {
        if (_gameStatus.value == GameStatus.ReadyToPlay) {
            _gameStatus.value = GameStatus.Playing
            wordsUiState.value.updateAllMovingWordsPositions(Position.Moving)
        } else {
            _wordsUiState.value = WordsUiState(
                movingWords = gameRepository.getMovingWords(),
                caughtWords = emptySet()
            )
            _gameStatus.value = GameStatus.ReadyToPlay
            _caughtScoreCount.value = 0
            _lostScoreCount.value = 0
        }
    }

    fun onLostWord(word: Pair<Int, Word>) {
        wordsUiState.value.updateWordPosition(word.first, Position.End)
        incrementWrongScore()
    }

    fun onCaughtWord(word: Pair<Int, Word>) {
        wordsUiState.value.addCaughtWord(word)
        incrementRightScore()
    }

}