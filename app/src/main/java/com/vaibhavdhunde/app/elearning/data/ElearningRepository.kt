package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.entities.User

interface ElearningRepository {

    suspend fun loginUser(email: String, password: String): Result<*>

    suspend fun registerUser(name: String, email: String, password: String): Result<*>

    suspend fun updateProfileName(name: String): Result<*>

    suspend fun updatePassword(password: String, newPassword: String): Result<*>

    suspend fun deactivateAccount(): Result<*>

    suspend fun getUser(): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}