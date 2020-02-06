package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.FeedbacksRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [FeedbacksRemoteDataSource].
 */
class FeedbacksRemoteDataSourceTest {

    // SUT
    private lateinit var feedbacksRemoteDataSource: FeedbacksRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        feedbacksRemoteDataSource = IFeedbacksRemoteDataSource(api)
    }

    @Test
    fun addFeedback_success() = runBlocking {
        // WHEN - adding feedback
        val response = feedbacksRemoteDataSource.addFeedback("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNotNull()
    }

    @Test
    fun addFeedback_error() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - adding feedback
        val response = feedbacksRemoteDataSource.addFeedback("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
    }
}