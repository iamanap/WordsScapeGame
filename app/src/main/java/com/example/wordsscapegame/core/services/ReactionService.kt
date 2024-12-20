package com.example.wordsscapegame.core.services

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * An interface for providing feedback reactions, such as vibration, sound effects, and text-to-speech.
 *
 * Implementations of this interface are responsible for managing and triggering various
 * feedback mechanisms to enhance the user experience.
 */
interface ReactionService {
    /**
     * Triggers a vibration effect.
     *
     * @param duration The duration of the vibration in milliseconds. Defaults to 100L.
     */
    fun vibrate(duration: Long = 100L)
    /**
     * Loads a sound effect into memory.
     *
     * @param soundResId The resource ID of the sound effect to load.
     */
    fun loadSound(soundResId: Int)
    /**
     * Releases resources held by the service.
     *
     * This should be called when the service is no longer needed to avoid memory leaks.
     */
    fun playEffect(soundResId: Int)
    /**
     * Releases resources held by the service.
     *
     * This should be called when the service is no longer needed to avoid memory leaks.
     */
    fun release()
    /**
     * Plays text using text-to-speech.
     *
     * @param text The text to be spoken.
     */
    fun playText(text: String)
}

/**
 * An implementation of the `ReactionService` interface that provides feedback reactions
 * using system services.
 *
 * This class uses the `Vibrator`, `SoundPool`, and `TextToSpeech` system services to
 * provide vibration, sound effects, and text-to-speech feedback, respectively.
 *
 * @param context The application context.
 */
class ReactionServiceImpl(@ApplicationContext private val context: Context) : ReactionService, TextToSpeech.OnInitListener {
    private var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    private val tts: TextToSpeech = TextToSpeech(context, this)
    private val workerScope = CoroutineScope(Dispatchers.Default + Job())

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale("en-US")
            tts.setSpeechRate(1.2f)
            tts.speak(" ", TextToSpeech.QUEUE_FLUSH, null, "warmup")
        }
    }

    override fun vibrate(duration: Long) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    }

    override fun playEffect(soundResId: Int) {
        workerScope.launch {
            val soundId = soundMap[soundResId]
            if (soundId != null) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            }
        }
    }

    override fun loadSound(soundResId: Int) {
        val soundId = soundPool.load(context, soundResId, 1)
        soundMap[soundResId] = soundId
    }

    override fun release() {
        soundPool.release()
        tts.shutdown()
    }

    override fun playText(text: String) {
        workerScope.launch {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            Log.i(ReactionService::class.simpleName, "Playing $text")
        }
    }

}