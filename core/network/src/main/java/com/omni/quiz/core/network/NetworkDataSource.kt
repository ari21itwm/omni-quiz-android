package com.omni.quiz.core.network

import com.omni.quiz.core.model.LeaderboardEntry
import com.omni.quiz.core.model.QuizQuestion
import kotlinx.coroutines.flow.Flow

/**
 * Interface representing the network data source for the OmniQuiz application.
 */
interface NetworkDataSource {
    /**
     * Streams real-time leaderboard updates.
     */
    fun getLeaderboardStream(): Flow<List<LeaderboardEntry>>

    /**
     * Fetches all available quiz questions.
     */
    suspend fun getQuestions(): List<QuizQuestion>

    /**
     * Submits a user's score to the leaderboard.
     */
    suspend fun submitScore(entry: LeaderboardEntry)
}
