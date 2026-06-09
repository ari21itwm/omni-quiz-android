package com.omni.quiz.core.network

import com.omni.quiz.core.model.LeaderboardEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A mock implementation of [NetworkDataSource] for UI testing without Firebase dependencies.
 */
@Singleton
class MockNetworkDataSource @Inject constructor() : NetworkDataSource {

    override fun getLeaderboardStream(): Flow<List<LeaderboardEntry>> {
        return flowOf(
            listOf(
                LeaderboardEntry("1", "VibeCoder", 1000, System.currentTimeMillis()),
                LeaderboardEntry("2", "Morpheus", 850, System.currentTimeMillis() - 10000),
                LeaderboardEntry("3", "Neo", 720, System.currentTimeMillis() - 20000)
            )
        )
    }

    override suspend fun submitScore(entry: LeaderboardEntry) {
        // No-op for mock
    }
}
