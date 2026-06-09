package com.omni.quiz.feature.quiz.presentation

import com.omni.quiz.core.model.QuizQuestion

sealed interface QuizUiState {
    data object Loading : QuizUiState
    
    data class Success(
        val questions: List<QuizQuestion>,
        val currentQuestionIndex: Int = 0,
        val currentScore: Int = 0,
        val selectedOptionIndex: Int? = null,
        val isHintVisible: Boolean = false
    ) : QuizUiState
    
    data class Error(val message: String) : QuizUiState
    
    data class GameOver(val finalScore: Int) : QuizUiState
}
