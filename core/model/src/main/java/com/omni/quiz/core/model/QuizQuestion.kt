package com.omni.quiz.core.model

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val type: QuizType,
    val imageUrl: String? = null,
    val translationHint: String? = null,
    val isEngagementOnly: Boolean = false
)
