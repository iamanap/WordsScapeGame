package com.example.wordsscapegame.services

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.example.wordsscapegame.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

interface SpeechRecognitionService {
    fun startListening(options: SpeechRecognitionOptions)
    fun stopListening()
    val speechState: StateFlow<SpeechState>
}

data class SpeechRecognitionOptions(
    val expectedWords: List<String> = emptyList(),
    val maxAlternatives: Int = 2
)

class SpeechRecognitionServiceImpl @Inject constructor(
    private val context: Context
) :
    SpeechRecognitionService, RecognitionListener {
    private val _speechState = MutableStateFlow(SpeechState())
    override val speechState = _speechState.asStateFlow()
    private var currentOptions: SpeechRecognitionOptions? = null
    private var isListening = false
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
    private val mutex = Mutex()

    private val workerScope = CoroutineScope(Dispatchers.Default + Job())

    init {
        recognizer.setRecognitionListener(this)
    }

    override fun startListening(options: SpeechRecognitionOptions) {
        if (isListening) return // Avoid multiple listeners

        currentOptions = options
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
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }

        recognizer.startListening(intent)
        isListening = true

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
        isListening = false
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
        Log.e(SpeechRecognitionService::class.simpleName, "Error: $error")
    }

    override fun onResults(results: Bundle?) {
        updateResultState(results)
    }

    private fun updateResultState(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.let { alternatives ->
                workerScope.launch {
                    _speechState.update {
                        it.copy(
                            spokenWords = alternatives.split(" ")
                        )
                    }
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        updateResultState(partialResults)
    }

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit

    private data class ProcessedResult(
        val bestMatch: String,
        val alternatives: List<String>
    )
}

data class SpeechState(
    val spokenWords: List<String> = emptyList(),
    val isSpeaking: Boolean = false,
    val error: String? = null
)