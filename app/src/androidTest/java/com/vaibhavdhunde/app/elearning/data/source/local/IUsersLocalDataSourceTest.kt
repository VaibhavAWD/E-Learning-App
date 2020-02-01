package com.vaibhavdhunde.app.elearning.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.UsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.data.succeeded
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Tests for implementation of [UsersLocalDataSource].
 */
class IUsersLocalDataSourceTest {

    // SUT
    private lateinit var usersLocalDataSource: UsersLocalDataSource

    // Use database to access dao
    private lateinit var database: ElearningDatabase

    // Use context to create database
    private val context = ApplicationProvider.getApplicationContext<Context>()

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
        // create in-memory database that does not persists data after use
        database = Room.inMemoryDatabaseBuilder(context, ElearningDatabase::class.java).build()
        val usersDao = database.usersDao()
        usersLocalDataSource = IUsersLocalDataSource(usersDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getUser_emptyDatabase_errorReturned() = runBlocking {
        // GIVEN - database is empty

        // WHEN - getting user
        val result = usersLocalDataSource.getUser()

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun saveUser_gettingUser_userReturned() = runBlocking {
        // GIVEN - database is empty

        // WHEN - saving user
        usersLocalDataSource.saveUser(testUser)

        // THEN - verify that the user is retrieved and has expected values
        val result = usersLocalDataSource.getUser()
        assertThat(result.succeeded).isTrue()
        val user = (result as Success).data
        assertThat(user).isEqualTo(testUser)
    }

    @Test
    fun deleteUser_gettingUser_errorReturned() = runBlocking {
        // GIVEN - user is saved
        usersLocalDataSource.saveUser(testUser)

        // WHEN - saving user
        usersLocalDataSource.deleteUser()

        // THEN - verify that the result is error
        val result = usersLocalDataSource.getUser()
        assertThat(result).isInstanceOf(Error::class.java)
    }
}