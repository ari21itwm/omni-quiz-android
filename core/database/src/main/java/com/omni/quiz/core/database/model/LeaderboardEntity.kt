package com.omni.quiz.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leaderboard")
data class LeaderboardEntity(
    @PrimaryKey val userId: String,
    val username: String,
    val score: Int,
    val timestamp: Long
)
