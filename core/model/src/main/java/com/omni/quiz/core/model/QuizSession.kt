package com.omni.quiz.core.model

data class QuizSession(
    val sessionId: String,
    val questions: List<QuizQuestion>,
    val currentQuestionIndex: Int,
    val score: Int,
    val isCompleted: Boolean
)
