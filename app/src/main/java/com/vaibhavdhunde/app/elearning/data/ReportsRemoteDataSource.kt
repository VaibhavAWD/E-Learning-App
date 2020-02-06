package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse

interface ReportsRemoteDataSource {

    suspend fun addReport(message: String, apiKey: String): DefaultResponse

}