package com.omni.quiz.core.data.repository

import com.omni.quiz.core.data.mapper.asEntity
import com.omni.quiz.core.data.mapper.asExternalModel
import com.omni.quiz.core.database.dao.LeaderboardDao
import com.omni.quiz.core.model.LeaderboardEntry
import com.omni.quiz.core.model.QuizCategory
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealQuizRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val leaderboardDao: LeaderboardDao
) : QuizRepository {

    override fun getLeaderboard(): Flow<List<LeaderboardEntry>> = flow {
        emitAll(leaderboardDao.getLeaderboard().map { entities ->
            entities.map { it.asExternalModel() }
        })

        networkDataSource.getLeaderboardStream()
            .onEach { networkEntries ->
                leaderboardDao.clearLeaderboard()
                leaderboardDao.insertLeaderboard(networkEntries.map { it.asEntity() })
            }
            .collect {}
    }

    override suspend fun getQuestions(): List<QuizQuestion> {
        // Return dummy data for initial testing on emulator
        return listOf(
            QuizQuestion(
                id = "1",
                text = "What is the capital of France?",
                options = listOf("London", "Berlin", "Paris", "Madrid"),
                correctOptionIndex = 2,
                category = QuizCategory.GEOGRAPHY
            ),
            QuizQuestion(
                id = "2",
                text = "Which planet is known as the Red Planet?",
                options = listOf("Venus", "Mars", "Jupiter", "Saturn"),
                correctOptionIndex = 1,
                category = QuizCategory.GEOGRAPHY
            ),
            QuizQuestion(
                id = "3",
                text = "What is 5 + 7?",
                options = listOf("10", "11", "12", "13"),
                correctOptionIndex = 2,
                category = QuizCategory.VOCABULARY
            )
        )
    }

    override suspend fun submitFinalScore(userId: String, username: String, score: Int) {
        val entry = LeaderboardEntry(
            userId = userId,
            username = username,
            score = score,
            timestamp = System.currentTimeMillis()
        )
        leaderboardDao.insertLeaderboard(listOf(entry.asEntity()))
        networkDataSource.submitScore(entry)
    }
}
