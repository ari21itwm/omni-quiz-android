package com.omni.quiz.core.database.di

import android.content.Context
import androidx.room.Room
import com.omni.quiz.core.database.OmniDatabase
import com.omni.quiz.core.database.dao.LeaderboardDao
import com.omni.quiz.core.database.dao.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideOmniDatabase(
        @ApplicationContext context: Context
    ): OmniDatabase {
        return Room.databaseBuilder(
            context,
            OmniDatabase::class.java,
            "omni-database"
        ).build()
    }

    @Provides
    fun provideQuizDao(database: OmniDatabase): QuizDao = database.quizDao()

    @Provides
    fun provideLeaderboardDao(database: OmniDatabase): LeaderboardDao = database.leaderboardDao()
}
