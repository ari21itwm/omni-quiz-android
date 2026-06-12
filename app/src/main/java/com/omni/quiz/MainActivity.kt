package com.omni.quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.omni.quiz.core.model.QuizType
import com.omni.quiz.feature.quiz.presentation.DashboardScreen
import com.omni.quiz.feature.quiz.presentation.QuizScreen
import com.omni.quiz.feature.quiz.presentation.QuizViewModel
import com.omni.quiz.ui.theme.OmniQuizTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OmniQuizTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
                val viewModel: QuizViewModel = hiltViewModel()

                when (currentScreen) {
                    is Screen.Dashboard -> {
                        DashboardScreen(
                            onCategorySelected = { type ->
                                viewModel.loadQuiz(type)
                                currentScreen = Screen.Quiz(type)
                            },
                            onSeedDatabase = viewModel::seedDatabase
                        )
                    }
                    is Screen.Quiz -> {
                        QuizScreen(
                            viewModel = viewModel,
                            onBack = { currentScreen = Screen.Dashboard }
                        )
                        BackHandler {
                            currentScreen = Screen.Dashboard
                        }
                    }
                }
            }
        }
    }
}

sealed interface Screen {
    data object Dashboard : Screen
    data class Quiz(val type: QuizType) : Screen
}
