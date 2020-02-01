@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
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
 * Tests for implementation of [LoginViewModel].
 */
class LoginViewModelTest {

    // SUT
    private lateinit var loginViewModel: LoginViewModel

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

    // test data
    private val testEmail = "test@email.com"
    private val testInvalidEmail = "test@email..com"
    private val testPassword = "testPassword"
    private val testInvalidPassword = "pass"

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun loginUser_emptyEmail_error() = runBlocking {
        // GIVEN - email is empty
        loginViewModel.email.value = ""

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(loginViewModel.showMessageEvent, R.string.error_empty_email)
    }

    @Test
    fun loginUser_invalidEmail_error() = runBlocking {
        // GIVEN - email is invalid
        loginViewModel.email.value = testInvalidEmail

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(loginViewModel.showMessageEvent, R.string.error_invalid_email)
    }

    @Test
    fun loginUser_emptyPassword_error() = runBlocking {
        // GIVEN - password is empty
        loginViewModel.apply {
            email.value = testEmail
            password.value = ""
        }

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(loginViewModel.showMessageEvent, R.string.error_empty_password)
    }

    @Test
    fun loginUser_invalidPassword_error() = runBlocking {
        // GIVEN - password is invalid
        loginViewModel.apply {
            email.value = testEmail
            password.value = testInvalidPassword
        }

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            loginViewModel.showMessageEvent,
            R.string.error_invalid_password
        )
    }

    @Test
    fun loginUser_validData_setsCloseSoftKeyboardEvent() {
        // GIVEN - email & password
        loginViewModel.apply {
            email.value = testEmail
            password.value = testPassword
        }

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that close soft keyboard event is set
        assertLiveDataEventTriggered(loginViewModel.closeSoftKeyboardEvent, Unit)
    }

    @Test
    fun loginUser_success_loadingTogglesAndSetsMainEvent() = runBlocking {
        // GIVEN - email & password
        loginViewModel.apply {
            email.value = testEmail
            password.value = testPassword
        }

        // WHEN - login user
        loginViewModel.loginUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(loginViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(loginViewModel.dataLoading)).isFalse()

        // sets main event
        assertLiveDataEventTriggered(loginViewModel.mainEvent, Unit)
    }

    @Test
    fun loginUser_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - login user
        loginViewModel.apply {
            email.value = testEmail
            password.value = testPassword
        }
        loginViewModel.loginUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(loginViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(loginViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(loginViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun registerUser_setsRegisterEvent() {
        // WHEN - register user
        loginViewModel.registerUser()

        // THEN - verify that the register event is set
        assertLiveDataEventTriggered(loginViewModel.registerEvent, Unit)
    }

}