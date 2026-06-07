package com.omni.quiz.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.omni.quiz.core.database.dao.LeaderboardDao
import com.omni.quiz.core.database.dao.QuizDao
import com.omni.quiz.core.database.model.LeaderboardEntity
import com.omni.quiz.core.database.model.QuizQuestionEntity
import com.omni.quiz.core.database.util.Converters

@Database(
    entities = [
        QuizQuestionEntity::class,
        LeaderboardEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class OmniDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
    abstract fun leaderboardDao(): LeaderboardDao
}
