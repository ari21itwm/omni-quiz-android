package com.omni.quiz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.omni.quiz.core.database.model.QuizQuestionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions")
    fun getQuestionsStream(): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions")
    suspend fun getQuestions(): List<QuizQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Query("DELETE FROM quiz_questions")
    suspend fun clearQuestions()
}
