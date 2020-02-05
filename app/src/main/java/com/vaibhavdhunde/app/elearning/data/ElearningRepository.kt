package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.data.entities.User

interface ElearningRepository {

    suspend fun loginUser(email: String, password: String): Result<*>

    suspend fun registerUser(name: String, email: String, password: String): Result<*>

    suspend fun updateProfileName(name: String): Result<*>

    suspend fun updatePassword(password: String, newPassword: String): Result<*>

    suspend fun deactivateAccount(): Result<*>

    suspend fun getSubjects(forceUpdate: Boolean = false): Result<List<Subject>>

    suspend fun getTopics(subjectId: Long, forceUpdate: Boolean = false): Result<List<Topic>>

    suspend fun getUser(): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}