package com.example.wordsscapegame.core.services

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import javax.inject.Inject

interface VibrationService {
    fun vibrate(duration: Long = 100L)
}

@Suppress("DEPRECATION")
class VibrationServiceImpl @Inject constructor(private val context: Context) : VibrationService {
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        context.getSystemService(VIBRATOR_SERVICE) as Vibrator
    }

    override fun vibrate(duration: Long) {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
    }
}