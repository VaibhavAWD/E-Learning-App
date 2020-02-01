@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for implementation of [MainViewModel].
 */
class MainViewModelTest {

    // SUT
    private lateinit var mainViewModel: MainViewModel

    // Use fake repository for testing
    private lateinit var repository: FakeElearningRepository

    // Use test coroutines context that can be controlled from tests
    private val testContext = TestCoroutineContext()

    // Set up coroutines with main dispatcher
    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesMainDispatcherRule = ViewModelScopeMainDispatcherRule(testContext)

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
        repository = FakeElearningRepository()
        mainViewModel = MainViewModel(repository)
    }

    @Test
    fun loadUser_userLoaded() = runBlocking {
        // GIVEN - repository has user
        repository.saveUser(testUser)

        // WHEN - loading user
        mainViewModel.loadUser()

        // execute pending coroutines actions
        testContext.triggerActions()

        // THEN - verify that the user is loaded
        assertThat(LiveDataTestUtil.getValue(mainViewModel.user)).isEqualTo(testUser)
    }

}