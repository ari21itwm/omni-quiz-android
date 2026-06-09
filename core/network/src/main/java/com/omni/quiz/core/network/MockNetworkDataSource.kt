package com.omni.quiz.core.network

import com.omni.quiz.core.model.LeaderboardEntry
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType
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

    override suspend fun getQuestions(): List<QuizQuestion> {
        return listOf(
            // 3 Vocabulary Items (Polish)
            QuizQuestion(
                id = "v1",
                text = "Dzień dobry",
                options = listOf("Good morning", "Good night", "Goodbye", "Hello"),
                correctOptionIndex = 0,
                type = QuizType.VOCABULARY,
                translationHint = "Used in the morning"
            ),
            QuizQuestion(
                id = "v2",
                text = "Dziękuję",
                options = listOf("Please", "Thank you", "Sorry", "Yes"),
                correctOptionIndex = 1,
                type = QuizType.VOCABULARY
            ),
            QuizQuestion(
                id = "v3",
                text = "Przepraszam",
                options = listOf("Excuse me / Sorry", "Goodbye", "Welcome", "No"),
                correctOptionIndex = 0,
                type = QuizType.VOCABULARY
            ),
            
            // 3 Geography Items
            QuizQuestion(
                id = "g1",
                text = "Identify this European country",
                options = listOf("France", "Germany", "Poland", "Italy"),
                correctOptionIndex = 2,
                type = QuizType.GEOGRAPHY,
                imageUrl = "https://example.com/maps/poland.png"
            ),
            QuizQuestion(
                id = "g2",
                text = "What is the capital of this country?",
                options = listOf("Berlin", "Paris", "Warsaw", "Prague"),
                correctOptionIndex = 2,
                type = QuizType.GEOGRAPHY,
                imageUrl = "https://example.com/maps/poland_outline.png"
            ),
            QuizQuestion(
                id = "g3",
                text = "Which river flows through this city?",
                options = listOf("Vistula", "Oder", "Danube", "Rhine"),
                correctOptionIndex = 0,
                type = QuizType.GEOGRAPHY,
                imageUrl = "https://example.com/cities/warsaw.png"
            ),

            // 3 Motivation Items
            QuizQuestion(
                id = "m1",
                text = "The only way to do great work is to love what you do.",
                options = listOf("True", "False"),
                correctOptionIndex = 0,
                type = QuizType.MOTIVATION,
                isEngagementOnly = true
            ),
            QuizQuestion(
                id = "m2",
                text = "Success is not final, failure is not fatal: it is the courage to continue that counts.",
                options = listOf("Inspiring", "Very Inspiring"),
                correctOptionIndex = 1,
                type = QuizType.MOTIVATION,
                isEngagementOnly = true
            ),
            QuizQuestion(
                id = "m3",
                text = "Vibe Coding is the future of human-AI collaboration.",
                options = listOf("Agree", "Disagree"),
                correctOptionIndex = 0,
                type = QuizType.MOTIVATION,
                isEngagementOnly = true
            )
        )
    }

    override suspend fun submitScore(entry: LeaderboardEntry) {
        // No-op for mock
    }
}
