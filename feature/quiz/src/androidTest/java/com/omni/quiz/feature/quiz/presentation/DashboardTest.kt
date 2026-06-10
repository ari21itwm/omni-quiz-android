package com.omni.quiz.feature.quiz.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class DashboardTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun dashboard_displaysCategoryCards() {
        // Given
        composeTestRule.setContent {
            DashboardScreen(onCategorySelected = {})
        }

        // Then
        composeTestRule.onNodeWithText("Polnisch-Vokabeln").assertIsDisplayed()
        composeTestRule.onNodeWithText("Erdkunde-Karten").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tägliche Motivation").assertIsDisplayed()
    }
}
