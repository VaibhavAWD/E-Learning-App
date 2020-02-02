package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse

interface UsersRemoteDataSource {

    suspend fun loginUser(email: String, password: String): AuthResponse

    suspend fun registerUser(name: String, email: String, password: String): AuthResponse

    suspend fun updateProfileName(name: String, apiKey: String): DefaultResponse

    suspend fun updatePassword(password: String, newPassword: String, apiKey: String): DefaultResponse

    suspend fun deactivateAccount(apiKey: String): DefaultResponse

}