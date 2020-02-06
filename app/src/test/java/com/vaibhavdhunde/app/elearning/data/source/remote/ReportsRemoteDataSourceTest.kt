package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.ReportsRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [ReportsRemoteDataSource].
 */
class ReportsRemoteDataSourceTest {

    // SUT
    private lateinit var reportsRemoteDataSource: ReportsRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        reportsRemoteDataSource = IReportsRemoteDataSource(api)
    }

    @Test
    fun addReport_success() = runBlocking {
        // WHEN - adding report
        val response = reportsRemoteDataSource.addReport("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNotNull()
    }

    @Test
    fun addReport_error() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - adding report
        val response = reportsRemoteDataSource.addReport("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
    }
}