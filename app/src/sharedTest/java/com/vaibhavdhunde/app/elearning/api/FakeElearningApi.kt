package com.vaibhavdhunde.app.elearning.api

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import com.vaibhavdhunde.app.elearning.data.entities.User
import retrofit2.Response

class FakeElearningApi : ElearningApi {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    // test user data
    private val testUser = User(
        1,
        "Test Name",
        "test@email.com",
        "password_hash",
        "api_key",
        "created_at",
        1
    )

    override suspend fun loginUser(email: String, password: String): Response<AuthResponse> {
        return if (shouldReturnError) {
            Response.success(AuthResponse(true, "Test exception", null))
        } else {
            Response.success(AuthResponse(false, null, testUser))
        }
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Response<AuthResponse> {
        return if (shouldReturnError) {
            Response.success(AuthResponse(true, "Test exception", null))
        } else {
            Response.success(AuthResponse(false, null, testUser))
        }
    }
}