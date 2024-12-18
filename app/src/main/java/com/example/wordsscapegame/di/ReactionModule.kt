package com.example.wordsscapegame.di

import android.content.Context
import com.example.wordsscapegame.services.ReactionService
import com.example.wordsscapegame.services.ReactionServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReactionModule {

    @Provides
    @Singleton
    fun provideReactionService(@ApplicationContext context: Context): ReactionService {
        return ReactionServiceImpl(context)
    }

}