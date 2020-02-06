package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.BlogResponse
import com.vaibhavdhunde.app.elearning.api.responses.BlogsResponse

interface BlogsRemoteDataSource {

    suspend fun getBlogs(apiKey: String): BlogsResponse

    suspend fun getBlog(blogId: Long, apiKey: String): BlogResponse

}