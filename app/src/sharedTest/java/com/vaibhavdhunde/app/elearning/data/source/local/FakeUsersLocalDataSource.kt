package com.vaibhavdhunde.app.elearning.data.source.local

import com.vaibhavdhunde.app.elearning.data.Result
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.UsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.entities.User

class FakeUsersLocalDataSource : UsersLocalDataSource {

    private var user: User? = null

    override suspend fun getUser(): Result<User> {
        user?.let { return Success(it) }
        return Error(Exception("User not found"))
    }

    override suspend fun saveUser(user: User) {
        this.user = user
    }

    override suspend fun deleteUser() {
        user = null
    }
}