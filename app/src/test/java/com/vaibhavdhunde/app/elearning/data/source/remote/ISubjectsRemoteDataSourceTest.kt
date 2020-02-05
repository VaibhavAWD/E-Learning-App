package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.SubjectsRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [SubjectsRemoteDataSource].
 */
class ISubjectsRemoteDataSourceTest {

    // SUT
    private lateinit var subjectsRemoteDataSource: SubjectsRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        subjectsRemoteDataSource = ISubjectsRemoteDataSource(api)
    }

    @Test
    fun getSubjects_success_subjectsReturned() = runBlocking {
        // WHEN - getting subjects
        val response = subjectsRemoteDataSource.getSubjects()

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.subjects).isNotNull()
    }

    @Test
    fun getSubjects_error_subjectsNotReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - getting subjects
        val response = subjectsRemoteDataSource.getSubjects()

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.subjects).isNull()
    }
}