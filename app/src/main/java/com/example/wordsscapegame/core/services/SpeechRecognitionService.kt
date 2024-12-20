package com.example.wordsscapegame.core.services

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.example.wordsscapegame.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * An interface for providing speech recognition functionality.
 *
 * Implementations of this interface are responsible for managing and triggering
 * speech recognition events, handling results, and providing state updates.
 */
interface SpeechRecognitionService {
    fun startListening()
    fun stopListening()
    /**
     * Provides a flow of the current speech recognition state.
     */
    val speechState: StateFlow<SpeechState>
}
/**
 * An implementation of the `SpeechRecognitionService` interface that uses the
 * Android SpeechRecognizer API.
 *
 * This class handles speech recognition events, processes results, and updates
 * the speech state flow.
 *
 * @param context The application context.
 */
class SpeechRecognitionServiceImpl @Inject constructor(
    private val context: Context
) :
    SpeechRecognitionService, RecognitionListener {
    private val _speechState = MutableStateFlow(SpeechState())
    override val speechState = _speechState.asStateFlow()
    private var isListening = false
    private var recognizer = SpeechRecognizer.createSpeechRecognizer(context)

    private val workerScope = CoroutineScope(Dispatchers.Default + Job())


    override fun startListening() {
        if (isListening) return // Avoid multiple listeners

        recognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer.setRecognitionListener(this)

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _speechState.update {
                it.copy(
                    error = context.getString(R.string.speech_service_error)
                )
            }
            return
        }

        createSpeechRecognitionIntent().run {
            recognizer.startListening(this)
        }
    }

    private fun createSpeechRecognitionIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }

    override fun stopListening() {
        recognizer.stopListening()
        recognizer.destroy()
        isListening = false
    }

    override fun onReadyForSpeech(params: Bundle?) {
        isListening = true
        _speechState.update { SpeechState() }
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

    override fun onPartialResults(partialResults: Bundle?) {
        updateResultState(partialResults)
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

    override fun onEndOfSpeech() = Unit
    override fun onEvent(eventType: Int, params: Bundle?) = Unit
    override fun onBeginningOfSpeech() = Unit
    override fun onRmsChanged(rmsdB: Float) = Unit
    override fun onBufferReceived(buffer: ByteArray?) = Unit
}

data class SpeechState(
    val spokenWords: List<String> = emptyList(),
    val error: String? = null
)