package com.omni.quiz.core.data.repository

import com.omni.quiz.core.model.LeaderboardEntry
import com.omni.quiz.core.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getLeaderboard(): Flow<List<LeaderboardEntry>>
    suspend fun getQuestions(): List<QuizQuestion>
    suspend fun submitFinalScore(userId: String, username: String, score: Int)
}
