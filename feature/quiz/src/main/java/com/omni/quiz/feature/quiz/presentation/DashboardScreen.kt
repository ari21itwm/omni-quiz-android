package com.omni.quiz.feature.quiz.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.omni.quiz.core.model.QuizType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onCategorySelected: (QuizType) -> Unit,
    onSeedDatabase: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text("Cześć!", style = MaterialTheme.typography.headlineMedium)
                        Text("Ready to learn?", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.secondary)
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                CategoryCard(
                    title = "Polnisch-Vokabeln",
                    subtitle = "Master the language",
                    icon = Icons.Default.Language,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    onClick = { onCategorySelected(QuizType.VOCABULARY) }
                )
            }
            item {
                CategoryCard(
                    title = "Erdkunde-Karten",
                    subtitle = "Explore the world",
                    icon = Icons.Default.Map,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { onCategorySelected(QuizType.GEOGRAPHY) }
                )
            }
            item {
                CategoryCard(
                    title = "Tägliche Motivation",
                    subtitle = "Stay inspired",
                    icon = Icons.Default.Lightbulb,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    onClick = { onCategorySelected(QuizType.MOTIVATION) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                LeaderboardPlaceholder()
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onSeedDatabase,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("SEED DATABASE (One-Time)")
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun CategoryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium)
            }
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LeaderboardPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Leaderboard", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Friends Sync", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Fake scores
            listOf("Neo" to 1250, "Morpheus" to 980, "Trinity" to 840).forEach { (name, score) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = name, style = MaterialTheme.typography.bodyLarge)
                    Text(text = "$score pts", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
