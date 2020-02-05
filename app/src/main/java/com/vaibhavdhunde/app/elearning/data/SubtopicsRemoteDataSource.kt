package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.SubtopicResponse
import com.vaibhavdhunde.app.elearning.api.responses.SubtopicsResponse

interface SubtopicsRemoteDataSource {

    suspend fun getSubtopics(topicId: Long): SubtopicsResponse

    suspend fun getSubtopic(subtopicId: Long, apiKey: String): SubtopicResponse
}