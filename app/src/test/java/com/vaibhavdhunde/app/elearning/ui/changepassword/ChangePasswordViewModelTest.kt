@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.changepassword

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
 * Tests for implementation of [ChangePasswordViewModel].
 */
class ChangePasswordViewModelTest {

    // SUT
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

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
    private val testPassword = "testPassword"
    private val testNewPassword = "testNewPassword"
    private val testInvalidPassword = "pass"
    private val testMismatchPassword = "testMismatchPassword"

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        changePasswordViewModel = ChangePasswordViewModel(repository)
    }

    @Test
    fun updatePassword_emptyPassword_error() {
        // GIVEN - password is empty
        changePasswordViewModel.password.value = ""

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            changePasswordViewModel.showMessageEvent,
            R.string.error_empty_password
        )
    }

    @Test
    fun updatePassword_emptyNewPassword_error() {
        // GIVEN - new password is empty
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = ""
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            changePasswordViewModel.showMessageEvent,
            R.string.error_empty_new_password
        )
    }

    @Test
    fun updatePassword_invalidNewPassword_error() {
        // GIVEN - new password is invalid
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testInvalidPassword
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            changePasswordViewModel.showMessageEvent,
            R.string.error_invalid_password
        )
    }

    @Test
    fun updatePassword_emptyConfPassword_error() {
        // GIVEN - conf password is empty
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testNewPassword
            confPassword.value = ""
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            changePasswordViewModel.showMessageEvent,
            R.string.error_empty_conf_password
        )
    }

    @Test
    fun updatePassword_passwordMismatch_error() {
        // GIVEN - password mismatch
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testNewPassword
            confPassword.value = testMismatchPassword
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            changePasswordViewModel.showMessageEvent,
            R.string.error_password_mismatch
        )
    }

    @Test
    fun updatePassword_validData_setsCloseSoftKeyboardEvent() {
        // GIVEN - valid data
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testNewPassword
            confPassword.value = testNewPassword
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that close soft keyboard event is set
        assertLiveDataEventTriggered(changePasswordViewModel.closeSoftKeyboardEvent, Unit)
    }

    @Test
    fun updatePassword_success_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN -
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testNewPassword
            confPassword.value = testNewPassword
        }

        // WHEN - updating password
        changePasswordViewModel.updatePassword()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(changePasswordViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(changePasswordViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(changePasswordViewModel.showMessageEvent, "Success")

        // and sets login event
        assertLiveDataEventTriggered(changePasswordViewModel.loginEvent, Unit)
    }

    @Test
    fun updatePassword_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - updating password
        changePasswordViewModel.apply {
            password.value = testPassword
            newPassword.value = testNewPassword
            confPassword.value = testNewPassword
        }
        changePasswordViewModel.updatePassword()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(changePasswordViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(changePasswordViewModel.dataLoading)).isFalse()

        // and sets show message event
        assertLiveDataEventTriggered(changePasswordViewModel.showMessageEvent, "Test exception")
    }
}