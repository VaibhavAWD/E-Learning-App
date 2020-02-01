package com.vaibhavdhunde.app.elearning.data.source.remote

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.api.FakeElearningApi
import com.vaibhavdhunde.app.elearning.data.UsersRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test

/**
 * Tests for implementation of [UsersRemoteDataSource].
 */
class UsersRemoteDataSourceTest {

    // SUT
    private lateinit var usersRemoteDataSource: UsersRemoteDataSource

    // Use fake api for testing
    private lateinit var api: FakeElearningApi

    @Before
    fun setUp() {
        api = FakeElearningApi()
        usersRemoteDataSource = IUsersRemoteDataSource(api)
    }

    @Test
    fun loginUser_success_userReturned() = runBlocking {
        // WHEN - login user
        val response = usersRemoteDataSource.loginUser("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.user).isNotNull()
    }

    @Test
    fun loginUser_error_userReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - login user
        val response = usersRemoteDataSource.loginUser("", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.user).isNull()
    }

    @Test
    fun registerUser_success_userReturned() = runBlocking {
        // WHEN - register user
        val response = usersRemoteDataSource.registerUser("", "", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isFalse()
        assertThat(response.message).isNull()
        assertThat(response.user).isNotNull()
    }
    
    @Test
    fun registerUser_error_userReturned() = runBlocking {
        // GIVEN - api returns error
        api.setShouldReturnError(true)

        // WHEN - register user
        val response = usersRemoteDataSource.registerUser("", "", "")

        // THEN - verify that the response has expected values
        assertThat(response.error).isTrue()
        assertThat(response.message).isNotNull()
        assertThat(response.user).isNull()
    }
}