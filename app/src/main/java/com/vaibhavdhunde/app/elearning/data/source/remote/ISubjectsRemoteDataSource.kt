package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.SubjectsResponse
import com.vaibhavdhunde.app.elearning.data.SubjectsRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ISubjectsRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SubjectsRemoteDataSource {

    override suspend fun getSubjects(): SubjectsResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.getSubjects() }
        }
    }
}