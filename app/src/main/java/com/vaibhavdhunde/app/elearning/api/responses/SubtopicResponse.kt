package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Subtopic

data class SubtopicResponse(
    val error: Boolean,
    val message: String?,
    val subtopic: Subtopic?
)