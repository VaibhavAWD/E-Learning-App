package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse
import com.vaibhavdhunde.app.elearning.data.FeedbacksRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.ReportsRemoteDataSource

class FakeReportsRemoteDataSource : ReportsRemoteDataSource {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun addReport(message: String, apiKey: String): DefaultResponse {
        return if (shouldReturnError) {
            DefaultResponse(true, "Test exception")
        } else {
            DefaultResponse(false, "Success")
        }
    }
}