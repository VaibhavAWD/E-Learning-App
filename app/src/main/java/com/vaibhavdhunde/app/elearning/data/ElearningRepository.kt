package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.entities.*

interface ElearningRepository {

    suspend fun loginUser(email: String, password: String): Result<*>

    suspend fun registerUser(name: String, email: String, password: String): Result<*>

    suspend fun updateProfileName(name: String): Result<*>

    suspend fun updatePassword(password: String, newPassword: String): Result<*>

    suspend fun deactivateAccount(): Result<*>

    suspend fun getSubjects(): Result<List<Subject>>

    suspend fun getTopics(subjectId: Long): Result<List<Topic>>

    suspend fun getSubtopics(topicId: Long): Result<List<Subtopic>>

    suspend fun getSubtopic(subtopicId: Long): Result<Subtopic>

    suspend fun sendFeedback(message: String): Result<String>

    suspend fun sendReport(message: String): Result<String>

    suspend fun getBlogs(): Result<List<Blog>>

    suspend fun getBlog(blogId: Long): Result<Blog>

    suspend fun getUser(): Result<User>

    suspend fun saveUser(user: User)

    suspend fun deleteUser()
}