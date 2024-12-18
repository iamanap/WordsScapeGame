package com.example.wordsscapegame.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordsscapegame.R
import com.example.wordsscapegame.data.GameRepository
import com.example.wordsscapegame.services.ReactionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val reactionService: ReactionService
) : ViewModel() {
    private val _wordsUiState = MutableStateFlow(WordsUiState(movingWords = gameRepository.getMovingWords()))
    val wordsUiState = _wordsUiState.asStateFlow()

    private val _gameUiState = MutableStateFlow(
        GameUiState(
        status = GameStatus.ReadyToPlay,
        caughtScore = 0,
        lostScore = 0)
    )
    val gameUiState = _gameUiState.asStateFlow()

    init {
        reactionService.loadSound(R.raw.catch_sound)
    }

    override fun onCleared() {
        super.onCleared()
        reactionService.releaseSoundPool()
    }

    fun changeGameStatus() {
        if (gameUiState.value.status == GameStatus.ReadyToPlay) {
            startGame()
        } else {
            stopGame()
        }
    }

    private fun startGame() {
        _gameUiState.value = _gameUiState.value.changeGameToPlaying()
        val initialWords =
            gameRepository.getMovingWords().map { it.copy(position = Position.Moving) }
        _wordsUiState.value = _wordsUiState.value.resetWords(initialWords)
    }

    private fun stopGame() {
        _gameUiState.value = _gameUiState.value.changeGameToReadyToPlay()
        _wordsUiState.value = WordsUiState(movingWords = gameRepository.getMovingWords())
    }

    fun onWordLost(index: Int, word: Word) {
        if (word.position == Position.Moving && !word.caught) {
            _wordsUiState.value = _wordsUiState.value.updateWordPosition(index, Position.End)
            _gameUiState.value = _gameUiState.value.incrementLostScore()
            reactionService.vibrate(100L)
        }
    }

    fun onWordCaught(index: Int) {
        if (gameUiState.value.status == GameStatus.Playing
            && _wordsUiState.value.movingWords[index].position == Position.Moving
        ) {
            _wordsUiState.value = _wordsUiState.value.addCaughtWord(index)
            _gameUiState.value = _gameUiState.value.incrementCaughtScore()
            viewModelScope.launch {
                reactionService.playSound(R.raw.catch_sound)
            }
        }
    }
}