package com.vaibhavdhunde.app.elearning.data

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.data.source.local.FakeUsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.FakeUsersRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test

/**
 * Tests for implementation of [ElearningRepository].
 */
class ElearningRepositoryTest {

    // SUT
    private lateinit var repository: ElearningRepository

    // Use fake users local data source for testing
    private lateinit var usersLocalDataSource: FakeUsersLocalDataSource

    // Use fake users remote data source for testing
    private lateinit var usersRemoteDataSource: FakeUsersRemoteDataSource

    // test user data
    private val testUser = User(
        1,
        "Test Name",
        "test@email.com",
        "password_hash",
        "api_key",
        "created_at",
        1
    )

    @Before
    fun setUp() {
        usersLocalDataSource = FakeUsersLocalDataSource()
        usersRemoteDataSource = FakeUsersRemoteDataSource()
        repository = DefaultElearningRepository(usersLocalDataSource, usersRemoteDataSource)
    }

    @Test
    fun loginUser_success_userSavedAndSuccessReturned() = runBlocking {
        // WHEN - login user
        val result = repository.loginUser("", "")

        // THEN - verify that the result is success and user is saved
        assertThat(result.succeeded).isTrue()
        val second = (repository.getUser() as Success).data
        assertThat(second).isNotNull()
    }

    @Test
    fun loginUser_error_errorReturned() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // WHEN - login user
        val result = repository.loginUser("", "")

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun registerUser_success_userSavedAndSuccessReturned() = runBlocking {
        // WHEN - register user
        val result = repository.registerUser("", "", "")

        // THEN - verify that the result is success and user is saved
        assertThat(result.succeeded).isTrue()
        val second = (repository.getUser() as Success).data
        assertThat(second).isNotNull()
    }

    @Test
    fun registerUser_error_errorReturned() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // WHEN - register user
        val result = repository.registerUser("", "", "")

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getUser_userCachesAfterLocal() = runBlocking {
        // GIVEN - local data source has user
        usersLocalDataSource.saveUser(testUser)

        // initial cached user
        val initial = (repository.getUser() as Success).data

        // WHEN - getting user
        val second = (repository.getUser() as Success).data

        // THEN - verify that the second and initial are same, because second is from cache
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun saveUser_savesInCacheAndLocal() = runBlocking {
        // WHEN - saving user
        repository.saveUser(testUser)

        // THEN - verify that the user is saved in local and cache
        val localUser = (usersLocalDataSource.getUser() as Success).data
        assertThat(localUser).isEqualTo(testUser)
        val cachedUser = (repository.getUser() as Success).data
        assertThat(cachedUser).isEqualTo(testUser)
    }

    @Test
    fun deleteUser_deletesFromCacheAndLocal() = runBlocking {
        // GIVEN - user is saved
        repository.saveUser(testUser)

        // WHEN - deleting user
        repository.deleteUser()

        // THEN - verify that the user is deleted from local and cache
        val localUser = usersLocalDataSource.getUser()
        assertThat(localUser).isInstanceOf(Error::class.java)
        val cachedUser = repository.getUser()
        assertThat(cachedUser).isInstanceOf(Error::class.java)
    }

}