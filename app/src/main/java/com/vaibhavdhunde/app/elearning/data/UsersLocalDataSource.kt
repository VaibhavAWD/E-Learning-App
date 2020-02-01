package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.entities.User

interface UsersLocalDataSource {

    suspend fun getUser(): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser()

}