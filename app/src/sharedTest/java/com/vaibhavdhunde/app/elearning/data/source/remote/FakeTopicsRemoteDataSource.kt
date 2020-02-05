package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.TopicsResponse
import com.vaibhavdhunde.app.elearning.data.TopicsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.entities.Topic

class FakeTopicsRemoteDataSource : TopicsRemoteDataSource {

    var topics: List<Topic> = emptyList()

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getTopics(subjectId: Long): TopicsResponse {
        return if (shouldReturnError) {
            TopicsResponse(true, "Test exception", null)
        } else {
            TopicsResponse(false, null, topics)
        }
    }
}