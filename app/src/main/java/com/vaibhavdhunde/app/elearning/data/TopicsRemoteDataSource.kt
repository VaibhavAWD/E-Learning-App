package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.TopicsResponse

interface TopicsRemoteDataSource {

    suspend fun getTopics(subjectId: Long): TopicsResponse

}