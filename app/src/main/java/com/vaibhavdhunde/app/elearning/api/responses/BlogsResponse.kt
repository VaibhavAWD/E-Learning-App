package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.Blog

data class BlogsResponse(
    val error: Boolean,
    val message: String?,
    val blogs: List<Blog>?
)