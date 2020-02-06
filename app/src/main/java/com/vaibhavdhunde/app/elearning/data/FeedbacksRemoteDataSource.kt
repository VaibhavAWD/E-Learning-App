package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse

interface FeedbacksRemoteDataSource {

    suspend fun addFeedback(message: String, apiKey: String): DefaultResponse

}