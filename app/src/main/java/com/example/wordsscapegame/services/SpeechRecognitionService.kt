package com.example.wordsscapegame.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.example.wordsscapegame.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface SpeechService {
    fun startListening()
    fun stopListening()
}

class SpeechServiceImpl(@ApplicationContext private val context: Context): SpeechService, RecognitionListener {

    private val _speechState = MutableStateFlow(SpeechState())
    val speechState = _speechState.asStateFlow()

    val recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    override fun startListening() {
        _speechState.update { SpeechState() }

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _speechState.update {
                it.copy(
                    error = context.getString(R.string.speech_service_error)
                )
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }

        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)

        _speechState.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    override fun stopListening() {
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _speechState.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onEndOfSpeech() {
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _speechState.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                _speechState.update {
                    it.copy(
                        spokenText = result
                    )
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit

}

data class SpeechState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)