@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
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
 * Tests for implementation of [ProfileViewModel].
 */
class ProfileViewModelTest {

    // SUT
    private lateinit var profileViewModel: ProfileViewModel

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
        runBlocking { repository.saveUser(testUser) }
        profileViewModel = ProfileViewModel(repository)
    }

    /**
     * load profile _ success _ loading toggles and data loaded
     */
    @Test
    fun loadProfile_success_loadingTogglesAndDataLoaded() = runBlocking {
        // WHEN - loading profile
        profileViewModel.loadProfile()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // and data loaded correctly
        assertThat(LiveDataTestUtil.getValue(profileViewModel.user)).isEqualTo(testUser)
        assertThat(LiveDataTestUtil.getValue(profileViewModel.name)).isEqualTo(testUser.name)
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataAvailable)).isTrue()
    }

    /**
     * load profile _ error
     */
    @Test
    fun loadProfile_error_loadingTogglesAndDataNotLoaded() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - loading profile
        profileViewModel.loadProfile()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // and data not loaded
        assertThat(LiveDataTestUtil.getValue(profileViewModel.user)).isNull()
        assertThat(LiveDataTestUtil.getValue(profileViewModel.name)).isNull()
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataAvailable)).isFalse()
    }

    /**
     * update profile name _ empty name _ error
     */
    @Test
    fun updateProfileName_emptyName_error() = runBlocking {
        // GIVEN - name is empty
        profileViewModel.name.value = ""

        // WHEN - updating profile name
        profileViewModel.updateProfileName()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(profileViewModel.showMessageEvent, R.string.error_empty_name)
    }

    @Test
    fun updateProfileName_validData_setsCloseSoftKeyboardEvent() {
        // GIVEN - name is empty
        profileViewModel.name.value = testUser.name

        // WHEN - updating profile name
        profileViewModel.updateProfileName()

        // THEN - verify that close soft keyboard event is set
        assertLiveDataEventTriggered(profileViewModel.closeSoftKeyboardEvent, Unit)
    }

    /**
     * update profile name _ success _ sets event
     */
    @Test
    fun updateProfileName_success_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - user is loaded
        profileViewModel.loadProfile()

        // WHEN - updating profile name
        profileViewModel.name.value = testUser.name
        profileViewModel.updateProfileName()

        // THEN - verify that following events occured
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // verify that show message event is set
        assertLiveDataEventTriggered(profileViewModel.showMessageEvent, "Success")
    }

    /**
     * update profile name _ error _ sets event
     */
    @Test
    fun updateProfileName_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - updating profile name
        profileViewModel.name.value = testUser.name
        profileViewModel.updateProfileName()

        // THEN - verify that following events occured
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // verify that show message event is set
        assertLiveDataEventTriggered(profileViewModel.showMessageEvent, "Test exception")
    }

    /**
     * change password _ sets event
     */
    @Test
    fun changePassword_setsEvent() {
        // WHEN - change password
        profileViewModel.changePassword()

        // THEN - verify that the change password event is set
        assertLiveDataEventTriggered(profileViewModel.changePasswordEvent, Unit)
    }

    /**
     * logout user _ deletes user and sets event
     */
    @Test
    fun logoutUser_deletesUserAndSetsEvent() = runBlocking {
        // WHEN - logout user
        profileViewModel.logoutUser()

        // execute pending coroutines actions
        testContext.triggerActions()

        // THEN -
        // verify that the user is deleted
        val result = repository.getUser()
        assertThat(result).isInstanceOf(Error::class.java)

        // and login event is set
        assertLiveDataEventTriggered(profileViewModel.loginEvent, Unit)
    }

    /**
     * show deactivate account alert dialog _ sets event
     */
    @Test
    fun showDeactivateAccountAlertDialog_setsEvent() {
        // WHEN - show deactivate account alert dialog
        profileViewModel.showDeactivateAccountAlertDialog()

        // THEN - verify that the deactivate account alert event is set
        assertLiveDataEventTriggered(profileViewModel.deactivateAccountAlertEvent, Unit)
    }

    /**
     * deactivate account _ success _ loading toggles and sets event
     */
    @Test
    fun deactivateAccount_success_loadingTogglesAndSetsEvent() = runBlocking {
        // WHEN - deactivate account
        profileViewModel.deactivateAccount()

        // THEN - verify that following events occured
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // verify that show message event is set
        assertLiveDataEventTriggered(profileViewModel.showMessageEvent, "Success")
    }

    /**
     * deactivate account _ error _ loading toggles and sets event
     */
    @Test
    fun deactivateAccount_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - deactivate account
        profileViewModel.deactivateAccount()

        // THEN - verify that following events occured
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(profileViewModel.dataLoading)).isFalse()

        // verify that show message event is set
        assertLiveDataEventTriggered(profileViewModel.showMessageEvent, "Test exception")
    }

}