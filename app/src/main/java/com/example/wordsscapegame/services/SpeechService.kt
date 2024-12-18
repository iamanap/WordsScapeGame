package com.example.wordsscapegame.services

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow

//interface SpeechService {
//    fun startListening()
//    fun stopListening()
//
//}
//
//class SpeechServiceImpl(@ApplicationContext private val context: Context) : SpeechService {
//    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
////    private val _recognizerText = MutableStateFlow("")
////    override val recognizedText: StateFlow<String> = _recognizedText
////
////    private val _isListening = MutableStateFlow(false)
////    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
//
//    private val recognitionListener = object : RecognitionListener {
//        override fun onReadyForSpeech(params: Bundle?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onBeginningOfSpeech() {
//            TODO("Not yet implemented")
//        }
//
//        override fun onRmsChanged(rmsdB: Float) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onBufferReceived(buffer: ByteArray?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onResults(results: Bundle?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onPartialResults(partialResults: Bundle?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onEvent(eventType: Int, params: Bundle?) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onError(error: Int) {
//            TODO("Not yet implemented")
//        }
//
//        override fun onEndOfSpeech() {
//            TODO("Not yet implemented")
//        }
//    }
//
//    init {
//        speechRecognizer.setRecognitionListener(recognitionListener)
//    }
//
//    override fun startListening() {
////        val intent = RecognizerIntent().apply {
////            action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
////            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
////            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // Customize as needed
////        }
////        speechRecognizer.startListening(intent)
//    }
//
//    override fun stopListening() {
//        speechRecognizer.stopListening()
//    }
//}