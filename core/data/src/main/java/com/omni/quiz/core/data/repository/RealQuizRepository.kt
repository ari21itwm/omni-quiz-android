package com.omni.quiz.core.data.repository

import com.omni.quiz.core.data.mapper.asEntity
import com.omni.quiz.core.data.mapper.asExternalModel
import com.omni.quiz.core.database.dao.LeaderboardDao
import com.omni.quiz.core.database.dao.QuizDao
import com.omni.quiz.core.model.LeaderboardEntry
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
    private val leaderboardDao: LeaderboardDao,
    private val quizDao: QuizDao
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
        return try {
            val networkQuestions = networkDataSource.getQuestions()
            quizDao.clearQuestions()
            quizDao.insertQuestions(networkQuestions.map { it.asEntity() })
            networkQuestions
        } catch (e: Exception) {
            quizDao.getQuestions().map { it.asExternalModel() }
        }
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
