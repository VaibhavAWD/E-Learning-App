package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.data.entities.User

class FakeElearningRepository : ElearningRepository {

    private var user: User? = null

    var subjects: List<Subject> = emptyList()

    private var cachedSubjects: MutableList<Subject>? = null

    var topics: List<Topic> = emptyList()

    private var cachedTopics: MutableList<Topic>? = null

    var subtopics: List<Subtopic> = emptyList()

    private var cachedSubtopics: MutableList<Subtopic>? = null

    var subtopic: Subtopic? = null

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

    override suspend fun updateProfileName(name: String): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            user!!.name = name
            Success("Success")
        }
    }

    override suspend fun updatePassword(password: String, newPassword: String): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            Success("Success")
        }
    }

    override suspend fun deactivateAccount(): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            Success("Success")
        }
    }

    override suspend fun getSubjects(forceUpdate: Boolean): Result<List<Subject>> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            if (!forceUpdate) {
                cachedSubjects?.let { return Success(it.toList()) }
            }
            cachedSubjects?.clear()
            cachedSubjects = subjects.toMutableList()
            Success(cachedSubjects!!.toList())
        }
    }

    override suspend fun getTopics(subjectId: Long, forceUpdate: Boolean): Result<List<Topic>> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            if (!forceUpdate) {
                cachedTopics?.let { return Success(it.toList()) }
            }
            cachedTopics?.clear()
            cachedTopics = topics.toMutableList()
            Success(cachedTopics!!.toList())
        }
    }

    override suspend fun getSubtopics(topicId: Long, forceUpdate: Boolean): Result<List<Subtopic>> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            if (!forceUpdate) {
                cachedSubtopics?.let { return Success(it.toList()) }
            }
            cachedSubtopics?.clear()
            cachedSubtopics = subtopics.toMutableList()
            Success(cachedSubtopics!!.toList())
        }
    }

    override suspend fun getSubtopic(subtopicId: Long): Result<Subtopic> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            Success(subtopic!!)
        }
    }

    override suspend fun getUser(): Result<User> {
        if (shouldReturnError) {
            return Error(Exception("Test exception"))
        }
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