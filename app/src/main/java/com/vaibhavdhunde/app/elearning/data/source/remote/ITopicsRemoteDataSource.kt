package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.TopicsResponse
import com.vaibhavdhunde.app.elearning.data.TopicsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ITopicsRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TopicsRemoteDataSource {

    override suspend fun getTopics(subjectId: Long): TopicsResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getTopics(subjectId) }
        }
    }
}