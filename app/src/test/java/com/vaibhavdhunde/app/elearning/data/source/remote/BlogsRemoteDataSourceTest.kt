package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.BlogsRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementing [BlogsRemoteDataSource].
 */
class BlogsRemoteDataSourceTest {

    // SUT
    private lateinit var blogsRemoteDataSource: BlogsRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        blogsRemoteDataSource = IBlogsRemoteDataSource(api)
    }

    @Test
    fun getBlogs_success_blogsReturned() = runBlocking {
        // WHEN - getting blogs
        val response = blogsRemoteDataSource.getBlogs("")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.blogs).isNotNull()
    }

    @Test
    fun getBlogs_error_blogsNotReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - getting blogs
        val response = blogsRemoteDataSource.getBlogs("")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.blogs).isNull()
    }

    @Test
    fun getBlog_success_blogReturned() = runBlocking {
        // WHEN - getting blog
        val response = blogsRemoteDataSource.getBlog(1, "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.blog).isNotNull()
    }

    @Test
    fun getBlog_error_subtopicNotReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - getting blog
        val response = blogsRemoteDataSource.getBlog(1, "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.blog).isNull()
    }
}