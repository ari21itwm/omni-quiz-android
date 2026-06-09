package com.omni.quiz.feature.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omni.quiz.core.data.repository.QuizRepository
import com.omni.quiz.core.model.QuizQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        loadQuiz()
    }

    fun loadQuiz() {
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                val questions = repository.getQuestions()
                if (questions.isNotEmpty()) {
                    _uiState.value = QuizUiState.Success(questions = questions)
                } else {
                    _uiState.value = QuizUiState.Error("No questions found.")
                }
            } catch (e: Exception) {
                _uiState.value = QuizUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun selectOption(index: Int) {
        _uiState.update { state ->
            if (state is QuizUiState.Success) {
                state.copy(selectedOptionIndex = index)
            } else state
        }
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        if (currentState is QuizUiState.Success) {
            val question = currentState.questions.getOrNull(currentState.currentQuestionIndex)
            val isCorrect = question?.correctOptionIndex == currentState.selectedOptionIndex
            
            val newScore = if (isCorrect) currentState.currentScore + 1 else currentState.currentScore
            val nextIndex = currentState.currentQuestionIndex + 1
            
            if (nextIndex < currentState.questions.size) {
                _uiState.value = currentState.copy(
                    currentQuestionIndex = nextIndex,
                    currentScore = newScore,
                    selectedOptionIndex = null
                )
            } else {
                _uiState.value = QuizUiState.GameOver(newScore)
                viewModelScope.launch {
                    try {
                        repository.submitFinalScore("user_123", "Quiz Master", newScore)
                    } catch (e: Exception) {
                        // Log or handle error if submission fails
                    }
                }
            }
        }
    }
}
