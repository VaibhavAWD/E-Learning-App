@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.register

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
 * Tests for implementation of [RegisterViewModel].
 */
class RegisterViewModelTest {

    // SUT
    private lateinit var registerViewModel: RegisterViewModel

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
    private val testName = "Test Name"
    private val testEmail = "test@email.com"
    private val testInvalidEmail = "test@email..com"
    private val testPassword = "testPassword"
    private val testInvalidPassword = "pass"
    private val testPasswordMismatch = "testPasswordMismatch"

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun registerUser_emptyName_error() = runBlocking {
        // GIVEN - name is empty
        registerViewModel.name.value = ""

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(registerViewModel.showMessageEvent, R.string.error_empty_name)
    }

    @Test
    fun registerUser_emptyEmail_error() = runBlocking {
        // GIVEN - email is empty
        registerViewModel.apply {
            name.value = testName
            email.value = ""
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(registerViewModel.showMessageEvent, R.string.error_empty_email)
    }

    @Test
    fun registerUser_invalidEmail_error() = runBlocking {
        // GIVEN - email is invalid
        registerViewModel.apply {
            name.value = testName
            email.value = testInvalidEmail
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(registerViewModel.showMessageEvent, R.string.error_invalid_email)
    }

    @Test
    fun registerUser_emptyPassword_error() = runBlocking {
        // GIVEN - password is empty
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = ""
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(registerViewModel.showMessageEvent, R.string.error_empty_password)
    }

    @Test
    fun registerUser_invalidPassword_error() = runBlocking {
        // GIVEN - password is invalid
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testInvalidPassword
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            registerViewModel.showMessageEvent,
            R.string.error_invalid_password
        )
    }

    @Test
    fun registerUser_emptyConfPassword_error() = runBlocking {
        // GIVEN - conf password is empty
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testPassword
            confPassword.value = ""
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            registerViewModel.showMessageEvent,
            R.string.error_empty_conf_password
        )
    }

    @Test
    fun registerUser_passwordMismatch_error() = runBlocking {
        // GIVEN - password mismatch
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testPassword
            confPassword.value = testPasswordMismatch
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            registerViewModel.showMessageEvent,
            R.string.error_password_mismatch
        )
    }

    @Test
    fun registerUser_validData_setsCloseSoftKeyboardEvent() {
        // GIVEN - user data
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testPassword
            confPassword.value = testPassword
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that close soft keyboard event is set
        assertLiveDataEventTriggered(registerViewModel.closeSoftKeyboardEvent, Unit)
    }

    @Test
    fun registerUser_success_loadingTogglesAndSetsMainEvent() = runBlocking {
        // GIVEN - email & password
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testPassword
            confPassword.value = testPassword
        }

        // WHEN - register user
        registerViewModel.registerUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(registerViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(registerViewModel.dataLoading)).isFalse()

        // sets main event
        assertLiveDataEventTriggered(registerViewModel.mainEvent, Unit)
    }

    @Test
    fun registerUser_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - register user
        registerViewModel.apply {
            name.value = testName
            email.value = testEmail
            password.value = testPassword
            confPassword.value = testPassword
        }
        registerViewModel.registerUser()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(registerViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(registerViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(registerViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun loginUser_setsLoginEvent() {
        // WHEN - login user
        registerViewModel.loginUser()

        // THEN - verify that the login event is set
        assertLiveDataEventTriggered(registerViewModel.loginEvent, Unit)
    }

}