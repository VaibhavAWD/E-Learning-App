@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.feedback

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
 * Tests for implementation of [FeedbackViewModel].
 */
class FeedbackViewModelTest {

    // SUT
    private lateinit var feedbackViewModel: FeedbackViewModel

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

    // test message data
    private val testMessage = "Test Message"

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        feedbackViewModel = FeedbackViewModel(repository)
    }

    @Test
    fun sendFeedback_emptyMessage_error() {
        // GIVEN - message is empty
        feedbackViewModel.message.value = ""

        // WHEN - sending feedback
        feedbackViewModel.sendFeedback()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            feedbackViewModel.showMessageEvent,
            R.string.error_empty_message
        )
    }

    @Test
    fun sendFeedback_success_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - message
        feedbackViewModel.message.value = testMessage

        // WHEN - sending feedback
        feedbackViewModel.sendFeedback()

        // THEN - verify that following events occur
        // keyboard is closed
        assertLiveDataEventTriggered(feedbackViewModel.closeSoftKeyboardEvent, Unit)

        // progress is shown
        assertThat(LiveDataTestUtil.getValue(feedbackViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(feedbackViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(feedbackViewModel.showMessageEvent, "Success")

        // sets reset fields event
        assertThat(LiveDataTestUtil.getValue(feedbackViewModel.message)).isNull()
    }

    @Test
    fun sendFeedback_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - sending feedback
        feedbackViewModel.message.value = testMessage
        feedbackViewModel.sendFeedback()

        // THEN - verify that following events occur
        // keyboard is closed
        assertLiveDataEventTriggered(feedbackViewModel.closeSoftKeyboardEvent, Unit)

        // progress is shown
        assertThat(LiveDataTestUtil.getValue(feedbackViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(feedbackViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(feedbackViewModel.showMessageEvent, "Test exception")
    }

}