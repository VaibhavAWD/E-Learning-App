package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse
import com.vaibhavdhunde.app.elearning.data.FeedbacksRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.ReportsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IReportsRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): ReportsRemoteDataSource {

    override suspend fun addReport(message: String, apiKey: String): DefaultResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.addReport(message, apiKey) }
        }
    }
}