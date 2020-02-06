package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse
import com.vaibhavdhunde.app.elearning.data.FeedbacksRemoteDataSource

class FakeFeedbacksRemoteDataSource : FeedbacksRemoteDataSource {

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun addFeedback(message: String, apiKey: String): DefaultResponse {
        return if (shouldReturnError) {
            DefaultResponse(true, "Test exception")
        } else {
            DefaultResponse(false, "Success")
        }
    }
}