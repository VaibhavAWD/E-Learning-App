package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Blog

data class BlogResponse(
    val error: Boolean,
    val message: String?,
    val blog: Blog?
)