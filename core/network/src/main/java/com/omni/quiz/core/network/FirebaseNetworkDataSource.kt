package com.omni.quiz.core.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import com.omni.quiz.core.model.LeaderboardEntry
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [NetworkDataSource] using Firebase Firestore.
 */
@Singleton
class FirebaseNetworkDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : NetworkDataSource {

    override fun getLeaderboardStream(): Flow<List<LeaderboardEntry>> {
        return firestore.collection("leaderboard")
            .orderBy("score", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.documents.map { document ->
                    LeaderboardEntry(
                        userId = document.id,
                        username = document.getString("username") ?: "Unknown",
                        score = document.getLong("score")?.toInt() ?: 0,
                        timestamp = document.getLong("timestamp") ?: 0L
                    )
                }
            }
    }

    override suspend fun getQuestions(): List<QuizQuestion> {
        return firestore.collection("questions")
            .get()
            .await()
            .documents.map { document ->
                QuizQuestion(
                    id = document.id,
                    text = document.getString("text") ?: "",
                    options = (document.get("options") as? List<String>) ?: emptyList(),
                    correctOptionIndex = document.getLong("correctOptionIndex")?.toInt() ?: 0,
                    type = QuizType.valueOf(document.getString("type") ?: "VOCABULARY"),
                    imageUrl = document.getString("imageUrl"),
                    translationHint = document.getString("translationHint"),
                    isEngagementOnly = document.getBoolean("isEngagementOnly") ?: false
                )
            }
    }

    override suspend fun submitScore(entry: LeaderboardEntry) {
        val scoreData = mapOf(
            "username" to entry.username,
            "score" to entry.score,
            "timestamp" to entry.timestamp
        )
        firestore.collection("leaderboard")
            .document(entry.userId)
            .set(scoreData)
            .await()
    }
}
