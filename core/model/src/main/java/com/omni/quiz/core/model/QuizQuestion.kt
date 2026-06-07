package com.omni.quiz.core.model

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val category: QuizCategory,
    val imageUrl: String? = null
)
