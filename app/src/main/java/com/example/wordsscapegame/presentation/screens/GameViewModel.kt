package com.example.wordsscapegame.presentation.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordsscapegame.R
import com.example.wordsscapegame.domain.data.GameData
import com.example.wordsscapegame.core.services.ReactionService
import com.example.wordsscapegame.core.services.SpeechRecognitionService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Word Scape game.
 *
 * Manages the game state, handles user interactions, and interacts with
 * services like speech recognition and sound effects.
 *
 * @constructor Creates a new GameViewModel instance.
 *
 * @property gameData The GameData instance to access game data.
 * @property reactionService The ReactionService instance to handle sound effects.
 * @property speechRecognitionService The SpeechRecognitionService instance to handle speech-to-text transforming.
 *
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameData: GameData,
    private val reactionService: ReactionService,
    private val speechRecognitionService: SpeechRecognitionService
) : ViewModel() {

    private val _isVoidPermissionIsGranted = MutableStateFlow(true)
    val isVoicePermissionGranted: StateFlow<Boolean> = _isVoidPermissionIsGranted

    private val _wordsViewState =
        MutableStateFlow(WordsViewState(movingWords = gameData.getMovingWords()))
    val wordsUiState = _wordsViewState.asStateFlow()

    private val _gameViewState = MutableStateFlow(
        GameViewState(
            status = GameStatus.ReadyToPlay,
            caughtScore = 0,
            lostScore = 0
        )
    )
    val gameUiState = _gameViewState.asStateFlow()

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
        _gameViewState.update { _gameViewState.value.changeGameToPlaying() }
        val initialWords =
            gameData.getMovingWords().map { it.copy(position = Position.Moving) }
        _wordsViewState.update { wordsUiState.value.resetWords(initialWords) }
    }

    private fun stopGame() {
        _gameViewState.update { _gameViewState.value.changeGameToReadyToPlay() }
        _wordsViewState.update { WordsViewState(movingWords = gameData.getMovingWords()) }
        stopSpeechRecognition()
    }

    /**
     * Handles the event when a word is lost.
     *
     * Updates the word's position, increments the lost score, and plays sound effects.
     *
     * @param index The index of the lost word in the list.
     * @param wordState The lost word object.
     */
    fun onWordLost(index: Int, wordState: WordState) {
        if (wordState.position == Position.Moving && !wordState.caught) {
            reactionService.playText(wordState.word.text)
            _wordsViewState.update {
                _wordsViewState.value.updateWordPosition(
                    index,
                    Position.End
                )
            }
            _gameViewState.update { _gameViewState.value.incrementLostScore() }
            reactionService.vibrate(100L)
        }
    }

    /**
     * Handles the event when a word is caught.
     *
     * Updates the word's state, increments the caught score, and plays sound effects.
     *
     * @param index The index of the caught word in the list.
     */
    fun onWordCaught(index: Int) {
        if (gameUiState.value.status == GameStatus.Playing
            && _wordsViewState.value.movingWords[index].position == Position.Moving
            && !_wordsViewState.value.movingWords[index].caught
        ) {
            _wordsViewState.update { _wordsViewState.value.addCaughtWord(index) }
            _gameViewState.update { _gameViewState.value.incrementCaughtScore() }
            viewModelScope.launch {
                reactionService.playEffect(R.raw.catch_sound)
            }
        }
    }

    fun onPermissionGranted(isGranted: Boolean) {
        _isVoidPermissionIsGranted.value = isGranted
    }

    fun dismissDialog() {
        _isVoidPermissionIsGranted.value = true
    }

    private fun startSpeechRecognition() {
        val wordTexts: List<String> = _wordsViewState.value.movingWords.map { it.word.text }
        speechRecognitionService.startListening()
    }

    private fun stopSpeechRecognition() {
        speechRecognitionService.stopListening()
    }

    /**
     * Processes the recognized word from speech recognition.
     *
     * Checks if the recognized word matches any of the moving words and handles
     * the word catching event.
     */
    fun processRecognizedWord() {
        val recognizedText = speechRecognitionService.speechState.value.spokenWords
        Log.i(GameViewModel::class.simpleName, "Recognized text: $recognizedText")
        recognizedText.forEach { recognizedWord ->
            _wordsViewState.value.movingWords.indexOfFirst { it.word.text.lowercase() == recognizedWord.lowercase() }
                .let { wordIndex ->
                    if (wordIndex != -1) {
                        Log.i(GameViewModel::class.simpleName, "Found Word: $recognizedText")
                        onWordCaught(wordIndex)
                    }
                }
        }

    }
}