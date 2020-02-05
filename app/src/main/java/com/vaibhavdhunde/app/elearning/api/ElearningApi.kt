package com.vaibhavdhunde.app.elearning.api

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import com.vaibhavdhunde.app.elearning.api.responses.DefaultResponse
import com.vaibhavdhunde.app.elearning.api.responses.SubjectsResponse
import com.vaibhavdhunde.app.elearning.api.responses.TopicsResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ElearningApi {

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AuthResponse>

    @FormUrlEncoded
    @PUT("profilename")
    suspend fun updateProfileName(
        @Field("name") name: String,
        @Header(AUTHORIZATION) apiKey: String
    ): Response<DefaultResponse>

    @FormUrlEncoded
    @PUT("password")
    suspend fun updatePassword(
        @Field("password") password: String,
        @Field("new_password") newPassword: String,
        @Header(AUTHORIZATION) apiKey: String
    ): Response<DefaultResponse>

    @PUT("deactivate")
    suspend fun deactivateAccount(
        @Header(AUTHORIZATION) apiKey: String
    ): Response<DefaultResponse>

    /**
     * Subjects Api
     */
    @GET("subjects")
    suspend fun getSubjects(): Response<SubjectsResponse>

    /**
     * Topics Api
     */
    @GET("topics")
    suspend fun getTopics(
        @Query("subject_id") subjectId: Long
    ): Response<TopicsResponse>

    companion object {

        private const val AUTHORIZATION = "Authorization"

        private const val BASE_URL = "https://simplifycoders.com/api/elearning/v1/"

        operator fun invoke(networkInterceptor: NetworkInterceptor): ElearningApi {

            val client = OkHttpClient.Builder()
                .addInterceptor(networkInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ElearningApi::class.java)
        }

    }

}