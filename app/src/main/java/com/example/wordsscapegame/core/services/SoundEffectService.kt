package com.example.wordsscapegame.core.services

import android.content.Context
import android.media.SoundPool
import javax.inject.Inject

interface SoundEffectService {
    fun preloadSound(soundResId: Int)
    fun playEffect(soundResId: Int)
    fun release()
}

class SoundEffectServiceImpl @Inject constructor(
    private val context: Context,
    private val soundPool: SoundPool
) : SoundEffectService {
    private val soundMap = mutableMapOf<Int, Int>()

    override fun preloadSound(soundResId: Int) {
        val soundId = soundPool.load(context, soundResId, 1)
        soundMap[soundResId] = soundId
    }

    override fun playEffect(soundResId: Int) {
        soundMap[soundResId]?.let { soundId ->
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f)
        }
    }

    override fun release() {
        soundPool.release()
    }
}