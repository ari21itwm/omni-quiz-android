package com.omni.quiz.core.model

data class LeaderboardEntry(
    val userId: String,
    val username: String,
    val score: Int,
    val timestamp: Long
)
