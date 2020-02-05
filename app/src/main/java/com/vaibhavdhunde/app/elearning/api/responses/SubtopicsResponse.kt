package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Subtopic

data class SubtopicsResponse(
    val error: Boolean,
    val message: String?,
    val subtopics: List<Subtopic>?
)