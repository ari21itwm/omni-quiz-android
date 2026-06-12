package com.omni.quiz.feature.quiz.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omni.quiz.core.data.repository.QuizRepository
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType
import com.omni.quiz.core.network.FirestoreSeeder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository,
    private val seeder: FirestoreSeeder
) : ViewModel() {

    private val _uiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private var currentType: QuizType? = null

    fun loadQuiz(type: QuizType? = null) {
        currentType = type
        viewModelScope.launch {
            _uiState.value = QuizUiState.Loading
            try {
                var questions = repository.getQuestions()
                if (type != null) {
                    questions = questions.filter { it.type == type }
                }
                
                if (questions.isNotEmpty()) {
                    _uiState.value = QuizUiState.Success(questions = questions)
                } else {
                    _uiState.value = QuizUiState.Error("No questions found for this category.")
                }
            } catch (e: Exception) {
                _uiState.value = QuizUiState.Error(e.message ?: "Unknown Error")
            }
        }
    }

    fun retry() {
        loadQuiz(currentType)
    }

    fun selectOption(index: Int) {
        _uiState.update { state ->
            if (state is QuizUiState.Success && !state.isAnswerRevealed) {
                state.copy(selectedOptionIndex = index)
            } else state
        }
    }
    
    fun toggleHint() {
        _uiState.update { state ->
            if (state is QuizUiState.Success && !state.isAnswerRevealed) {
                state.copy(isHintVisible = !state.isHintVisible)
            } else state
        }
    }

    fun submitAnswer() {
        _uiState.update { state ->
            if (state is QuizUiState.Success && !state.isAnswerRevealed) {
                val question = state.questions.getOrNull(state.currentQuestionIndex)
                val isCorrect = question?.correctOptionIndex == state.selectedOptionIndex
                
                val newScore = if (isCorrect && question?.isEngagementOnly != true) state.currentScore + 1 else state.currentScore
                
                state.copy(
                    currentScore = newScore,
                    isAnswerRevealed = true
                )
            } else state
        }
    }

    fun moveToNextQuestion() {
        val currentState = _uiState.value
        if (currentState is QuizUiState.Success && currentState.isAnswerRevealed) {
            val nextIndex = currentState.currentQuestionIndex + 1
            
            if (nextIndex < currentState.questions.size) {
                _uiState.value = currentState.copy(
                    currentQuestionIndex = nextIndex,
                    selectedOptionIndex = null,
                    isHintVisible = false,
                    isAnswerRevealed = false
                )
            } else {
                _uiState.value = QuizUiState.GameOver(currentState.currentScore)
                viewModelScope.launch {
                    try {
                        repository.submitFinalScore("user_123", "Quiz Master", currentState.currentScore)
                    } catch (e: Exception) {
                        // Log or handle error if submission fails
                    }
                }
            }
        }
    }

    fun seedDatabase() {
        viewModelScope.launch {
            try {
                seeder.seedDatabase()
            } catch (e: Exception) {
                // Log or handle error
            }
        }
    }
}
