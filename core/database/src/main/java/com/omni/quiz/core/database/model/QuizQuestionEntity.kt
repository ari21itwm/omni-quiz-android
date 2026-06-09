package com.omni.quiz.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.omni.quiz.core.model.QuizType

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val type: QuizType,
    val imageUrl: String?,
    val translationHint: String?,
    val isEngagementOnly: Boolean
)
