package com.vaibhavdhunde.app.elearning.api

import com.vaibhavdhunde.app.elearning.api.responses.AuthResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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

    companion object {

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