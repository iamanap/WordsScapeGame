package com.example.wordsscapegame.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordsscapegame.R
import com.example.wordsscapegame.domain.data.GameData
import com.example.wordsscapegame.domain.data.Position
import com.example.wordsscapegame.domain.data.Word
import com.example.wordsscapegame.services.ReactionService
import com.example.wordsscapegame.services.SpeechRecognitionOptions
import com.example.wordsscapegame.services.SpeechRecognitionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameData: GameData,
    private val reactionService: ReactionService,
    private val speechRecognitionService: SpeechRecognitionService
) : ViewModel() {

    private val _permissionIsGranted = MutableStateFlow(true)
    val isVoicePermissionGranted: StateFlow<Boolean> = _permissionIsGranted

    private val _wordsUiState =
        MutableStateFlow(WordsUiState(movingWords = gameData.getMovingWords()))
    val wordsUiState = _wordsUiState.asStateFlow()

    private val _gameUiState = MutableStateFlow(
        GameUiState(
            status = GameStatus.ReadyToPlay,
            caughtScore = 0,
            lostScore = 0
        )
    )
    val gameUiState = _gameUiState.asStateFlow()

    init {
        reactionService.loadSound(R.raw.catch_sound)
        viewModelScope.launch {
            speechRecognitionService.speechState.collect {
                if (it.spokenWords.isNotEmpty())
                    processRecognizedWord()
            }
        }
        viewModelScope.launch {
            gameUiState.collect {
                if (isVoicePermissionGranted.value) {
                    if (it.status == GameStatus.Playing) {
                        startSpeechRecognition()
                    } else {
                        stopSpeechRecognition()
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        reactionService.release()
        stopSpeechRecognition()
    }

    fun changeGameStatus() {
        if (gameUiState.value.status == GameStatus.ReadyToPlay) {
            startGame()
        } else {
            stopGame()
        }
    }

    private fun startGame() {
        _gameUiState.update { _gameUiState.value.changeGameToPlaying() }
        val initialWords =
            gameData.getMovingWords().map { it.copy(position = Position.Moving) }
        _wordsUiState.update { wordsUiState.value.resetWords(initialWords) }
    }

    private fun stopGame() {
        _gameUiState.update { _gameUiState.value.changeGameToReadyToPlay() }
        _wordsUiState.update { WordsUiState(movingWords = gameData.getMovingWords()) }
        stopSpeechRecognition()
    }

    fun onWordLost(index: Int, word: Word) {
        if (word.position == Position.Moving && !word.caught) {
            reactionService.playText(word.text)
            _wordsUiState.update {
                _wordsUiState.value.updateWordPosition(
                    index,
                    Position.End
                )
            }
            _gameUiState.update { _gameUiState.value.incrementLostScore() }
            reactionService.vibrate(100L)
        }
    }

    fun onWordCaught(index: Int) {
        if (gameUiState.value.status == GameStatus.Playing
            && _wordsUiState.value.movingWords[index].position == Position.Moving
            && !_wordsUiState.value.movingWords[index].caught
        ) {
            _wordsUiState.update { _wordsUiState.value.addCaughtWord(index) }
            _gameUiState.update { _gameUiState.value.incrementCaughtScore() }
            viewModelScope.launch {
                reactionService.playEffect(R.raw.catch_sound)
            }
        }
    }

    fun onPermissionGranted(isGranted: Boolean) {
        _permissionIsGranted.value = isGranted
    }

    fun dismissDialog() {
        _permissionIsGranted.value = true
    }

    private fun startSpeechRecognition() {
        val wordTexts: List<String> = _wordsUiState.value.movingWords.map { it.text }
        speechRecognitionService.startListening(SpeechRecognitionOptions(wordTexts))
    }

    private fun stopSpeechRecognition() {
        speechRecognitionService.stopListening()
    }

    private fun processRecognizedWord() {
        val recognizedText = speechRecognitionService.speechState.value.spokenWords
        Log.i(GameViewModel::class.simpleName, "Recognized text: $recognizedText")
        recognizedText.forEach { recognizedWord ->
            _wordsUiState.value.movingWords.indexOfFirst { it.text.lowercase() == recognizedWord.lowercase() }
                .let { wordIndex ->
                    if (wordIndex != -1) {
                        Log.i(GameViewModel::class.simpleName, "Found Word: $recognizedText")
                        onWordCaught(wordIndex)
                    }
                }
        }

    }
}