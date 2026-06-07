package com.omni.quiz.core.data.repository

import com.omni.quiz.core.data.mapper.asEntity
import com.omni.quiz.core.data.mapper.asExternalModel
import com.omni.quiz.core.database.dao.LeaderboardDao
import com.omni.quiz.core.model.LeaderboardEntry
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
        // 1. Emit local cache instantly
        emitAll(leaderboardDao.getLeaderboard().map { entities ->
            entities.map { it.asExternalModel() }
        })

        // 2. collection from Firebase stream
        networkDataSource.getLeaderboardStream()
            .onEach { networkEntries ->
                // 3. Automatically overwrite local Room DB cache when fresh data arrives
                leaderboardDao.clearLeaderboard()
                leaderboardDao.insertLeaderboard(networkEntries.map { it.asEntity() })
            }
            .collect { /* Firestore snapshots are handled in onEach and emitted via Flow */ }
    }

    override suspend fun submitFinalScore(userId: String, username: String, score: Int) {
        val entry = LeaderboardEntry(
            userId = userId,
            username = username,
            score = score,
            timestamp = System.currentTimeMillis()
        )

        // 1. Save locally to Room first
        leaderboardDao.insertLeaderboard(listOf(entry.asEntity()))

        // 2. Try to push to Firebase
        networkDataSource.submitScore(entry)
    }
}
