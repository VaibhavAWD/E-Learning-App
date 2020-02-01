package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import com.vaibhavdhunde.app.elearning.data.UsersRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.entities.User
import retrofit2.Response

class FakeUsersRemoteDataSource : UsersRemoteDataSource {

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

    override suspend fun loginUser(email: String, password: String): AuthResponse {
        return if (shouldReturnError) {
            AuthResponse(true, "Test exception", null)
        } else {
            AuthResponse(false, null, testUser)
        }
    }

    override suspend fun registerUser(name: String, email: String, password: String): AuthResponse {
        return if (shouldReturnError) {
            AuthResponse(true, "Test exception", null)
        } else {
            AuthResponse(false, null, testUser)
        }
    }
}