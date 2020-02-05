package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.SubtopicsRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [SubtopicsRemoteDataSource].
 */
class SubtopicsRemoteDataSourceTest {

    // SUT
    private lateinit var subtopicsRemoteDataSource: SubtopicsRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        subtopicsRemoteDataSource = ISubtopicsRemoteDataSource(api)
    }

    @Test
    fun getSubtopics_success_subtopicsReturned() = runBlocking {
        // WHEN - getting subtopics
        val response = subtopicsRemoteDataSource.getSubtopics(1)

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.subtopics).isNotNull()
    }

    @Test
    fun getSubtopics_error_subtopicsNotReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - getting subtopics
        val response = subtopicsRemoteDataSource.getSubtopics(1)

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.subtopics).isNull()
    }
}