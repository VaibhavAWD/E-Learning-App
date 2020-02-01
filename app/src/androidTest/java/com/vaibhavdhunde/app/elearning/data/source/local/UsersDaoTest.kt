package com.vaibhavdhunde.app.elearning.data.source.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.entities.User
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for implementation of [UsersDao].
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class UsersDaoTest {

    // SUT
    private lateinit var usersDao: UsersDao

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
        usersDao = database.usersDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getUser_emptyDatabase_userNotRetrieved() = runBlocking {
        // GIVEN - database is empty

        // WHEN - getting user
        val user = usersDao.getUser()

        // THEN - verify that the user is not retrieved
        assertThat(user).isNull()
    }

    @Test
    fun insertUser_gettingUser_userReturned() = runBlocking {
        // GIVEN - database is empty

        // WHEN - inserting user
        usersDao.insertUser(testUser)

        // THEN - verify that the user was inserted
        val user = usersDao.getUser()
        assertThat(user).isNotNull()
        assertThat(user).isEqualTo(testUser)
    }

    @Test
    fun deleteUser_gettingUser_userNotRetrieved() = runBlocking {
        // GIVEN - user is inserted
        usersDao.insertUser(testUser)

        // WHEN - deleting user
        val affectedRows = usersDao.deleteUser()
        assertThat(affectedRows).isGreaterThan(0)
        val user = usersDao.getUser()
        assertThat(user).isNull()
    }
}