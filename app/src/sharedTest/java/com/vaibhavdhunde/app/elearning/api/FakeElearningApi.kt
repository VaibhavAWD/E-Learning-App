package com.vaibhavdhunde.app.elearning.api

import com.vaibhavdhunde.app.elearning.api.responses.*
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.data.entities.User
import retrofit2.Response

class FakeElearningApi : ElearningApi {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    // test user data
    private val testUser = User(
        1,
        "Test Name",
        "test@email.com",
        "password_hash",
        "api_key",
        "created_at",
        1
    )

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

    // test topics data
    private val testTopic1 = Topic(
        1,
        1,
        "Test Subject 1",
        "Test Subtitle 1"
    )
    private val testTopic2 = Topic(
        2,
        1,
        "Test Subject 2",
        "Test Subtitle 2"
    )

    // test subtopics data
    private val testSubtopic1 = Subtopic(
        1,
        1,
        "Test Subtopic 1",
        "Test Body 1",
        "https://test.com/url",
        "https://test.com/image.jpg",
        "2:44"
    )

    private val testSubtopic2 = Subtopic(
        2,
        1,
        "Test Subtopic 2",
        "Test Body 2",
        "https://test.com/url2",
        "https://test.com/image2.jpg",
        "3:17"
    )

    override suspend fun loginUser(email: String, password: String): Response<AuthResponse> {
        return if (shouldReturnError) {
            Response.success(AuthResponse(true, "Test exception", null))
        } else {
            Response.success(AuthResponse(false, null, testUser))
        }
    }

    override suspend fun registerUser(
        name: String,
        email: String,
        password: String
    ): Response<AuthResponse> {
        return if (shouldReturnError) {
            Response.success(AuthResponse(true, "Test exception", null))
        } else {
            Response.success(AuthResponse(false, null, testUser))
        }
    }

    override suspend fun updateProfileName(
        name: String,
        apiKey: String
    ): Response<DefaultResponse> {
        return if (shouldReturnError) {
            Response.success(DefaultResponse(true, "Test exception"))
        } else {
            Response.success(DefaultResponse(false, "Success"))
        }
    }

    override suspend fun updatePassword(
        password: String,
        newPassword: String,
        apiKey: String
    ): Response<DefaultResponse> {
        return if (shouldReturnError) {
            Response.success(DefaultResponse(true, "Test exception"))
        } else {
            Response.success(DefaultResponse(false, "Success"))
        }
    }

    override suspend fun deactivateAccount(apiKey: String): Response<DefaultResponse> {
        return if (shouldReturnError) {
            Response.success(DefaultResponse(true, "Test exception"))
        } else {
            Response.success(DefaultResponse(false, "Success"))
        }
    }

    override suspend fun getSubjects(): Response<SubjectsResponse> {
        return if (shouldReturnError) {
            Response.success(SubjectsResponse(true, "Test exception", null))
        } else {
            Response.success(SubjectsResponse(false, null, listOf(testSubject1, testSubject2)))
        }
    }

    override suspend fun getTopics(subjectId: Long): Response<TopicsResponse> {
        return if (shouldReturnError) {
            Response.success(TopicsResponse(true, "Test exception", null))
        } else {
            Response.success(TopicsResponse(false, null, listOf(testTopic1, testTopic2)))
        }
    }

    override suspend fun getSubtopics(topicId: Long): Response<SubtopicsResponse> {
        return if (shouldReturnError) {
            Response.success(SubtopicsResponse(true, "Test exception", null))
        } else {
            Response.success(SubtopicsResponse(false, null, listOf(testSubtopic1, testSubtopic2)))
        }
    }

    override suspend fun getSubtopic(subtopicId: Long, apiKey: String): Response<SubtopicResponse> {
        return if (shouldReturnError) {
            Response.success(SubtopicResponse(true, "Test exception", null))
        } else {
            Response.success(SubtopicResponse(false, null, testSubtopic1))
        }
    }

    override suspend fun addFeedback(message: String, apiKey: String): Response<DefaultResponse> {
        return if (shouldReturnError) {
            Response.success(DefaultResponse(true, "Test exception"))
        } else {
            Response.success(DefaultResponse(false, "Success"))
        }
    }

    override suspend fun addReport(message: String, apiKey: String): Response<DefaultResponse> {
        return if (shouldReturnError) {
            Response.success(DefaultResponse(true, "Test exception"))
        } else {
            Response.success(DefaultResponse(false, "Success"))
        }
    }
}