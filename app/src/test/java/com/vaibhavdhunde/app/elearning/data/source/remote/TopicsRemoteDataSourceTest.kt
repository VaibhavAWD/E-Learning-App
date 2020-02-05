package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.TopicsRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [TopicsRemoteDataSource].
 */
class TopicsRemoteDataSourceTest {

    // SUT
    private lateinit var topicsRemoteDataSource: TopicsRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        topicsRemoteDataSource = ITopicsRemoteDataSource(api)
    }

    @Test
    fun getTopics_success_topicsReturned() = runBlocking {
        // WHEN - getting topics
        val response = topicsRemoteDataSource.getTopics(1)

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.topics).isNotNull()
    }

    @Test
    fun getTopics_error_subjectsNotReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - getting subjects
        val response = topicsRemoteDataSource.getTopics(1)

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.topics).isNull()
    }
}