package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse

interface UsersRemoteDataSource {

    suspend fun loginUser(email: String, password: String): AuthResponse

    suspend fun registerUser(name: String, email: String, password: String): AuthResponse

}