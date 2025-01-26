package com.example.wordsscapegame.core.services

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale
import javax.inject.Inject

interface TextToSpeechService {
    fun playText(text: String)
    fun release()
}

class TextToSpeechServiceImpl @Inject constructor(
    private val context: Context
) : TextToSpeechService, TextToSpeech.OnInitListener {
    private val tts: TextToSpeech = TextToSpeech(context, this)

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("en-US")
            tts.setSpeechRate(1.5f)
        }
    }

    override fun playText(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun release() {
        tts.shutdown()
    }
}