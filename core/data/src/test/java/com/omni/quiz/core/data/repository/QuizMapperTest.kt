package com.omni.quiz.core.data.repository

import com.omni.quiz.core.data.mapper.asEntity
import com.omni.quiz.core.data.mapper.asExternalModel
import com.omni.quiz.core.database.model.QuizQuestionEntity
import com.omni.quiz.core.model.QuizQuestion
import com.omni.quiz.core.model.QuizType
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizMapperTest {

    @Test
    fun `QuizQuestionEntity asExternalModel maps all fields correctly`() {
        // Given
        val entity = QuizQuestionEntity(
            id = "1",
            text = "Test Question",
            options = listOf("A", "B"),
            correctOptionIndex = 1,
            type = QuizType.VOCABULARY,
            imageUrl = "http://image.com",
            translationHint = "Hint",
            isEngagementOnly = true
        )

        // When
        val domain = entity.asExternalModel()

        // Then
        assertEquals(entity.id, domain.id)
        assertEquals(entity.text, domain.text)
        assertEquals(entity.options, domain.options)
        assertEquals(entity.correctOptionIndex, domain.correctOptionIndex)
        assertEquals(entity.type, domain.type)
        assertEquals(entity.imageUrl, domain.imageUrl)
        assertEquals(entity.translationHint, domain.translationHint)
        assertEquals(entity.isEngagementOnly, domain.isEngagementOnly)
    }

    @Test
    fun `QuizQuestion asEntity maps all fields correctly`() {
        // Given
        val domain = QuizQuestion(
            id = "1",
            text = "Test Question",
            options = listOf("A", "B"),
            correctOptionIndex = 1,
            type = QuizType.GEOGRAPHY,
            imageUrl = "http://image.com",
            translationHint = "Hint",
            isEngagementOnly = false
        )

        // When
        val entity = domain.asEntity()

        // Then
        assertEquals(domain.id, entity.id)
        assertEquals(domain.text, entity.text)
        assertEquals(domain.options, entity.options)
        assertEquals(domain.correctOptionIndex, entity.correctOptionIndex)
        assertEquals(domain.type, entity.type)
        assertEquals(domain.imageUrl, entity.imageUrl)
        assertEquals(domain.translationHint, entity.translationHint)
        assertEquals(domain.isEngagementOnly, entity.isEngagementOnly)
    }
}
