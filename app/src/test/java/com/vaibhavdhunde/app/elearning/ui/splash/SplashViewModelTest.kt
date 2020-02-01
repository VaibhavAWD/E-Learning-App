@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import com.vaibhavdhunde.app.elearning.util.assertLiveDataEventTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule

import org.junit.Test

/**
 * Tests for implementation of [SplashViewModel].
 */
class SplashViewModelTest {

    // SUT
    private lateinit var splashViewModel: SplashViewModel

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
        splashViewModel = SplashViewModel(repository)
    }

    @Test
    fun loadUser_userAvailable_loadingTogglesAndSetsMainEvent() = runBlocking {
        // GIVEN - user is available
        repository.saveUser(testUser)

        // WHEN - loading user
        splashViewModel.loadUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(splashViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(splashViewModel.dataLoading)).isFalse()

        // sets main event
        assertLiveDataEventTriggered(splashViewModel.mainEvent, Unit)
    }

    @Test
    fun loadUser_userUnavailable_loadingTogglesAndSetsLoginEvent() = runBlocking {
        // GIVEN - user is not available
        // make sure user is not present
        repository.deleteUser()

        // WHEN - loading user
        splashViewModel.loadUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(splashViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(splashViewModel.dataLoading)).isFalse()

        // sets login event
        assertLiveDataEventTriggered(splashViewModel.loginEvent, Unit)
    }
}