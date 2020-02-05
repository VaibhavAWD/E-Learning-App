package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Topic

data class TopicsResponse (
    val error: Boolean,
    val message: String?,
    val topics: List<Topic>?
)