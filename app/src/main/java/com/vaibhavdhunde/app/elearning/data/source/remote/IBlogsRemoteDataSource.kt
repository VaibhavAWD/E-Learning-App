package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.BlogResponse
import com.vaibhavdhunde.app.elearning.api.responses.BlogsResponse
import com.vaibhavdhunde.app.elearning.data.BlogsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IBlogsRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): BlogsRemoteDataSource {

    override suspend fun getBlogs(apiKey: String): BlogsResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getBlogs(apiKey) }
        }
    }

    override suspend fun getBlog(blogId: Long, apiKey: String): BlogResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getBlog(blogId, apiKey) }
        }
    }
}