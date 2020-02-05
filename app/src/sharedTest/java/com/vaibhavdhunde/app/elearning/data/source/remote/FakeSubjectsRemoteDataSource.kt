package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.responses.SubjectsResponse
import com.vaibhavdhunde.app.elearning.data.SubjectsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.entities.Subject

class FakeSubjectsRemoteDataSource: SubjectsRemoteDataSource {

    var subjects: List<Subject> = emptyList()

    private var shouldReturnError = false

    fun setShouldReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getSubjects(): SubjectsResponse {
        return if (shouldReturnError) {
            SubjectsResponse(true, "Test exception", null)
        } else {
            SubjectsResponse(false, null, subjects)
        }
    }
}