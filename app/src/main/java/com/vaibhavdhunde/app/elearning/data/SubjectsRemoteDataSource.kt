package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.responses.SubjectsResponse

interface SubjectsRemoteDataSource {

    suspend fun getSubjects(): SubjectsResponse

}