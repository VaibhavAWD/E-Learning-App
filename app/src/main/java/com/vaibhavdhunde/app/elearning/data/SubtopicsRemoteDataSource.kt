package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.SubtopicsResponse

interface SubtopicsRemoteDataSource {

    suspend fun getSubtopics(topicId: Long): SubtopicsResponse

}