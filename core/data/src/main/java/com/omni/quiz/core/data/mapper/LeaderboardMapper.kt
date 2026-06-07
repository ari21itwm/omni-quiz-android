package com.omni.quiz.core.data.mapper

import com.omni.quiz.core.database.model.LeaderboardEntity
import com.omni.quiz.core.model.LeaderboardEntry

fun LeaderboardEntity.asExternalModel() = LeaderboardEntry(
    userId = userId,
    username = username,
    score = score,
    timestamp = timestamp
)

fun LeaderboardEntry.asEntity() = LeaderboardEntity(
    userId = userId,
    username = username,
    score = score,
    timestamp = timestamp
)
