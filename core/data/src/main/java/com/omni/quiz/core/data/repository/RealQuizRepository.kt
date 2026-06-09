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
        return listOf(
            QuizQuestion(
                id = "1",
                text = "What is Vibe Coding?",
                options = listOf("Coding by feel", "A new music genre", "Debugging with bass", "Silent coding"),
                correctOptionIndex = 0,
                category = QuizCategory.MOTIVATION
            ),
            QuizQuestion(
                id = "2",
                text = "Which language did Morpheus use?",
                options = listOf("Python", "C++", "Binary", "The Matrix"),
                correctOptionIndex = 2,
                category = QuizCategory.VOCABULARY
            ),
            QuizQuestion(
                id = "3",
                text = "What is the primary goal of OmniQuiz?",
                options = listOf("Infinite scrolling", "Knowledge mastery", "Watching ads", "Mining crypto"),
                correctOptionIndex = 1,
                category = QuizCategory.MOTIVATION
            ),
            QuizQuestion(
                id = "4",
                text = "In 'The Matrix', what color is the pill Neo takes?",
                options = listOf("Blue", "Green", "Red", "Yellow"),
                correctOptionIndex = 2,
                category = QuizCategory.VOCABULARY
            ),
            QuizQuestion(
                id = "5",
                text = "What does 'Offline-First' mean?",
                options = listOf("No internet needed", "App only works in caves", "Manual data entry", "Data is never saved"),
                correctOptionIndex = 0,
                category = QuizCategory.MOTIVATION
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
