package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.SubtopicResponse
import com.vaibhavdhunde.app.elearning.api.responses.SubtopicsResponse
import com.vaibhavdhunde.app.elearning.data.SubtopicsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic

class FakeSubtopicsRemoteDataSource : SubtopicsRemoteDataSource {

    var subtopics: List<Subtopic> = emptyList()

    var subtopic: Subtopic? = null

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getSubtopics(topicId: Long): SubtopicsResponse {
        return if (shouldReturnError) {
            SubtopicsResponse(true, "Test exception", null)
        } else {
            SubtopicsResponse(false, null, subtopics)
        }
    }

    override suspend fun getSubtopic(subtopicId: Long): SubtopicResponse {
        return if (shouldReturnError) {
            SubtopicResponse(true, "Test exception", null)
        } else {
            SubtopicResponse(false, null, subtopic)
        }
    }
}