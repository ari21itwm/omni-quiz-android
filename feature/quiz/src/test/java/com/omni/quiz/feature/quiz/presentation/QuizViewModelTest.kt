package com.omni.quiz.feature.quiz.presentation

import com.omni.quiz.core.data.repository.QuizRepository
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val repository: QuizRepository = mockk()
    private val seeder: com.omni.quiz.core.network.FirestoreSeeder = mockk()
    private lateinit var viewModel: QuizViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Default mock behavior
        coEvery { repository.getQuestions() } returns listOf(
            QuizQuestion("1", "V1", listOf("A", "B"), 0, QuizType.VOCABULARY),
            QuizQuestion("2", "G1", listOf("A", "B"), 0, QuizType.GEOGRAPHY),
            QuizQuestion("3", "M1", listOf("A", "B"), 0, QuizType.MOTIVATION)
        )
        
        viewModel = QuizViewModel(repository, seeder)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadQuiz with VOCABULARY type filters questions correctly`() = runTest {
        // When
        viewModel.loadQuiz(QuizType.VOCABULARY)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is QuizUiState.Success)
        val successState = state as QuizUiState.Success
        assertEquals(1, successState.questions.size)
        assertEquals(QuizType.VOCABULARY, successState.questions[0].type)
    }

    @Test
    fun `loadQuiz with GEOGRAPHY type filters questions correctly`() = runTest {
        // When
        viewModel.loadQuiz(QuizType.GEOGRAPHY)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is QuizUiState.Success)
        val successState = state as QuizUiState.Success
        assertEquals(1, successState.questions.size)
        assertEquals(QuizType.GEOGRAPHY, successState.questions[0].type)
    }

    @Test
    fun `loadQuiz with no questions returns error state`() = runTest {
        // Given
        coEvery { repository.getQuestions() } returns emptyList()
        
        // When
        viewModel.loadQuiz()

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is QuizUiState.Error)
        assertEquals("No questions found for this category.", (state as QuizUiState.Error).message)
    }
}
