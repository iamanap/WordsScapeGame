package com.example.wordsscapegame.di

import android.content.Context
import com.example.wordsscapegame.services.ReactionService
import com.example.wordsscapegame.services.ReactionServiceImpl
import com.example.wordsscapegame.services.SpeechRecognitionService
import com.example.wordsscapegame.services.SpeechRecognitionServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideReactionService(@ApplicationContext context: Context): ReactionService {
        return ReactionServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSpeechService(@ApplicationContext context: Context): SpeechRecognitionService {
        return SpeechRecognitionServiceImpl(context)
    }

}