package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.User

class FakeElearningRepository : ElearningRepository {

    private var user: User? = null

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun loginUser(email: String, password: String): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            Success(Unit)
        }
    }

    override suspend fun registerUser(name: String, email: String, password: String): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            Success(Unit)
        }
    }

    override suspend fun getUser(): Result<User> {
        return if (user != null) {
            Success(user!!)
        } else {
            Error(Exception("User not found"))
        }
    }

    override suspend fun saveUser(user: User) {
        this.user = user
    }

    override suspend fun deleteUser() {
        user = null
    }
}