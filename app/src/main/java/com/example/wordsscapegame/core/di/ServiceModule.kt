package com.example.wordsscapegame.core.di

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.wordsscapegame.core.services.SoundEffectService
import com.example.wordsscapegame.core.services.SoundEffectServiceImpl
import com.example.wordsscapegame.core.services.SpeechRecognitionService
import com.example.wordsscapegame.core.services.SpeechRecognitionServiceImpl
import com.example.wordsscapegame.core.services.TextToSpeechService
import com.example.wordsscapegame.core.services.TextToSpeechServiceImpl
import com.example.wordsscapegame.core.services.VibrationService
import com.example.wordsscapegame.core.services.VibrationServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideVibrationService(@ApplicationContext context: Context): VibrationService {
        return VibrationServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSoundEffectService(
        @ApplicationContext context: Context,
        soundPool: SoundPool
    ): SoundEffectService {
        return SoundEffectServiceImpl(context, soundPool)
    }

    @Provides
    @Singleton
    fun provideSoundPool(): SoundPool {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        return SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
    }

    @Provides
    @Singleton
    fun provideTextToSpeechService(
        @ApplicationContext context: Context
    ): TextToSpeechService {
        return TextToSpeechServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSpeechService(@ApplicationContext context: Context): SpeechRecognitionService {
        return SpeechRecognitionServiceImpl(context)
    }

}