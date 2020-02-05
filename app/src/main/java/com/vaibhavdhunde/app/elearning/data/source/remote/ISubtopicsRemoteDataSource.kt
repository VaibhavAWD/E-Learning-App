package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.SubtopicResponse
import com.vaibhavdhunde.app.elearning.api.responses.SubtopicsResponse
import com.vaibhavdhunde.app.elearning.data.SubtopicsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ISubtopicsRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SubtopicsRemoteDataSource {

    override suspend fun getSubtopics(topicId: Long): SubtopicsResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getSubtopics(topicId) }
        }
    }

    override suspend fun getSubtopic(subtopicId: Long, apiKey: String): SubtopicResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getSubtopic(subtopicId, apiKey) }
        }
    }
}