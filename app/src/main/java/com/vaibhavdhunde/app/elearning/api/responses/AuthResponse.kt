package com.vaibhavdhunde.app.elearning.api.responses

import com.vaibhavdhunde.app.elearning.data.entities.User

data class AuthResponse(
    val error: Boolean,
    val message: String?,
    val user: User?
)