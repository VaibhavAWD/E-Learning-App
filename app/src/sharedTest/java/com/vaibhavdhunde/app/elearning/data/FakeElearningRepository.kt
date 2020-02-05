package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.User

class FakeElearningRepository : ElearningRepository {

    private var user: User? = null

    private var subjects: List<Subject>? = null

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    // test subject data
    private val testSubject1 = Subject(
        1,
        "Test Subject1 ",
        "Test subtitle 1",
        "https://testapi.com/image1.jpg"
    )
    private val testSubject2 = Subject(
        2,
        "Test Subject 2",
        "Test subtitle 2",
        "https://testapi.com/image2.jpg"
    )

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

    override suspend fun getSubjects(forceUpdate: Boolean): Result<*> {
        return if (shouldReturnError) {
            Error(Exception("Test exception"))
        } else {
            subjects?.let { return Success(it) }
            Success(listOf(testSubject1, testSubject2))
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