package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.BlogResponse
import com.vaibhavdhunde.app.elearning.api.responses.BlogsResponse
import com.vaibhavdhunde.app.elearning.data.BlogsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.entities.Blog

class FakeBlogsRemoteDataSource : BlogsRemoteDataSource {

    var blogs: List<Blog> = emptyList()

    var blog: Blog? = null

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getBlogs(apiKey: String): BlogsResponse {
        return if (shouldReturnError) {
            BlogsResponse(true, "Test exception", null)
        } else {
            BlogsResponse(false, null, blogs)
        }
    }

    override suspend fun getBlog(blogId: Long, apiKey: String): BlogResponse {
        return if (shouldReturnError) {
            BlogResponse(true, "Test exception", null)
        } else {
            BlogResponse(false, null, blog)
        }
    }
}