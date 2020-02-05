package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Subject

data class SubjectsResponse(
    val error: Boolean,
    val message: String?,
    val subjects: List<Subject>?
)