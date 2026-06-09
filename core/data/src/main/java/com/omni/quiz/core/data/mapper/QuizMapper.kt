package com.omni.quiz.core.data.mapper

import com.omni.quiz.core.database.model.QuizQuestionEntity
import com.omni.quiz.core.model.QuizQuestion

fun QuizQuestionEntity.asExternalModel() = QuizQuestion(
    id = id,
    text = text,
    options = options,
    correctOptionIndex = correctOptionIndex,
    type = type,
    imageUrl = imageUrl,
    translationHint = translationHint,
    isEngagementOnly = isEngagementOnly
)

fun QuizQuestion.asEntity() = QuizQuestionEntity(
    id = id,
    text = text,
    options = options,
    correctOptionIndex = correctOptionIndex,
    type = type,
    imageUrl = imageUrl,
    translationHint = translationHint,
    isEngagementOnly = isEngagementOnly
)
