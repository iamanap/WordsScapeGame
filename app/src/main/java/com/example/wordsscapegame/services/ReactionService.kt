package com.example.wordsscapegame.services

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext

interface ReactionService {
    fun vibrate(duration: Long = 100L)
    fun loadSound(soundResId: Int)
    fun playSound(soundResId: Int)
    fun releaseSoundPool()
}

class ReactionServiceImpl(@ApplicationContext private val context: Context) : ReactionService {
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

    override fun vibrate(duration: Long) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        }
    }

    override fun playSound(resourceId: Int) {
        val soundId = soundMap[resourceId]
        if (soundId != null) {
            soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    override fun loadSound(resourceId: Int) {
        val soundId = soundPool.load(context, resourceId, 1)
        soundMap[resourceId] = soundId
    }

    override fun releaseSoundPool() {
        soundPool.release()
    }

}