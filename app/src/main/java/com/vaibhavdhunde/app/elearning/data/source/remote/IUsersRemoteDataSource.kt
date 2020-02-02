package com.vaibhavdhunde.app.elearning.data.source.remote

import com.vaibhavdhunde.app.elearning.api.ElearningApi
import com.vaibhavdhunde.app.elearning.api.SafeApiRequest
import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse
import com.vaibhavdhunde.app.elearning.data.UsersRemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IUsersRemoteDataSource(
    private val api: ElearningApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersRemoteDataSource {

    override suspend fun loginUser(email: String, password: String): AuthResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.loginUser(email, password) }
        }
    }

    override suspend fun registerUser(name: String, email: String, password: String): AuthResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.registerUser(name, email, password) }
        }
    }

    override suspend fun updateProfileName(name: String, apiKey: String): DefaultResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.updateProfileName(name, apiKey) }
        }
    }

    override suspend fun updatePassword(
        password: String,
        newPassword: String,
        apiKey: String
    ): DefaultResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest {
                api.updatePassword(
                    password,
                    newPassword,
                    apiKey
                )
            }
        }
    }

    override suspend fun deactivateAccount(apiKey: String): DefaultResponse {
        return withContext(ioDispatcher) {
            return@withContext SafeApiRequest.apiRequest { api.deactivateAccount(apiKey) }
        }
    }
}