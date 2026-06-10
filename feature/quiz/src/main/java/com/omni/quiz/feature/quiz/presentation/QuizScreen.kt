package com.omni.quiz.feature.quiz.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("OmniQuiz") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState) {
                is QuizUiState.Loading -> LoadingContent()
                is QuizUiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = viewModel::retry
                )
                is QuizUiState.Success -> QuizContent(
                    questions = state.questions,
                    currentIndex = state.currentQuestionIndex,
                    score = state.currentScore,
                    selectedOption = state.selectedOptionIndex,
                    isHintVisible = state.isHintVisible,
                    isAnswerRevealed = state.isAnswerRevealed,
                    onOptionSelected = viewModel::selectOption,
                    onToggleHint = viewModel::toggleHint,
                    onSubmit = viewModel::submitAnswer,
                    onNext = viewModel::moveToNextQuestion
                )
                is QuizUiState.GameOver -> GameOverContent(
                    score = state.finalScore,
                    onPlayAgain = viewModel::retry,
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun QuizContent(
    questions: List<QuizQuestion>,
    currentIndex: Int,
    score: Int,
    selectedOption: Int?,
    isHintVisible: Boolean,
    isAnswerRevealed: Boolean,
    onOptionSelected: (Int) -> Unit,
    onToggleHint: () -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit
) {
    val question = questions.getOrNull(currentIndex) ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress and Score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Step ${currentIndex + 1}/${questions.size}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            if (!question.isEngagementOnly) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Text(
                        text = "Score: $score",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Main Content Area
        Box(modifier = Modifier.weight(1f)) {
            when (question.type) {
                QuizType.VOCABULARY -> VocabularyCard(
                    question = question,
                    isHintVisible = isHintVisible,
                    onToggleHint = onToggleHint
                )
                QuizType.GEOGRAPHY -> GeographyCard(question = question)
                QuizType.MOTIVATION -> MotivationCard(question = question)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Actions Area
        if (!question.isEngagementOnly) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                question.options.forEachIndexed { index, option ->
                    val isCorrect = index == question.correctOptionIndex
                    val isSelected = selectedOption == index
                    
                    val containerColor by animateColorAsState(
                        targetValue = when {
                            isAnswerRevealed && isCorrect -> Color(0xFFC8E6C9) // Light Green
                            isAnswerRevealed && isSelected && !isCorrect -> Color(0xFFFFCDD2) // Light Red
                            isSelected -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        },
                        label = "option_color"
                    )
                    
                    val contentColor = when {
                        isAnswerRevealed && (isCorrect || isSelected) -> Color.Black
                        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurface
                    }

                    Surface(
                        onClick = { if (!isAnswerRevealed) onOptionSelected(index) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        color = containerColor,
                        contentColor = contentColor,
                        tonalElevation = 2.dp,
                        border = if (isSelected || (isAnswerRevealed && isCorrect)) {
                            BorderStroke(2.dp, if (isCorrect && isAnswerRevealed) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary)
                        } else {
                            BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        }
                    ) {
                        Text(
                            text = option,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = if (isAnswerRevealed) onNext else onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = selectedOption != null || question.isEngagementOnly,
            colors = if (question.isEngagementOnly) ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary) else ButtonDefaults.buttonColors()
        ) {
            Text(
                text = when {
                    isAnswerRevealed -> if (currentIndex < questions.size - 1) "Next Question" else "Finish Quiz"
                    question.isEngagementOnly -> "Inspired!"
                    else -> "Check Answer"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun VocabularyCard(
    question: QuizQuestion,
    isHintVisible: Boolean,
    onToggleHint: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Translate this word:",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = question.text,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            if (question.translationHint != null) {
                TextButton(onClick = onToggleHint) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Show Hint",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(if (isHintVisible) "Hide Hint" else "Show Hint")
                }
                
                AnimatedVisibility(visible = isHintVisible) {
                    Text(
                        text = question.translationHint ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun GeographyCard(question: QuizQuestion) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                // In a real app, use Coil to load question.imageUrl
                Text(
                    text = "[ Map Image Placeholder ]\n${question.imageUrl}",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MotivationCard(question: QuizQuestion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info, // Placeholder for a quote icon
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "\"${question.text}\"",
                style = MaterialTheme.typography.headlineMedium,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Daily Motivation",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
private fun GameOverContent(score: Int, onPlayAgain: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Congratulations!",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "You've finished the quiz!",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Final Score",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = "$score",
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = onPlayAgain,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Play Again", fontSize = 18.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Back to Dashboard", fontSize = 18.sp)
        }
    }
}
