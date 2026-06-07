package com.omni.quiz.core.data.di

import com.omni.quiz.core.data.repository.QuizRepository
import com.omni.quiz.core.data.repository.RealQuizRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindQuizRepository(
        impl: RealQuizRepository
    ): QuizRepository
}
