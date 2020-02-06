@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.report

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
 * Tests for implementation of [ReportViewModel].
 */
class ReportViewModelTest {

    // SUT
    private lateinit var reportViewModel: ReportViewModel

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
        reportViewModel = ReportViewModel(repository)
    }

    @Test
    fun sendFeedback_emptyMessage_error() {
        // GIVEN - message is empty
        reportViewModel.message.value = ""

        // WHEN - sending report
        reportViewModel.sendReport()

        // THEN - verify that show message event is set
        assertLiveDataEventTriggered(
            reportViewModel.showMessageEvent,
            R.string.error_empty_message
        )
    }

    @Test
    fun sendFeedback_success_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - message
        reportViewModel.message.value = testMessage

        // WHEN - sending report
        reportViewModel.sendReport()

        // THEN - verify that following events occur
        // keyboard is closed
        assertLiveDataEventTriggered(reportViewModel.closeSoftKeyboardEvent, Unit)

        // progress is shown
        assertThat(LiveDataTestUtil.getValue(reportViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(reportViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(reportViewModel.showMessageEvent, "Success")

        // sets reset fields event
        assertThat(LiveDataTestUtil.getValue(reportViewModel.message)).isNull()
    }

    @Test
    fun sendFeedback_error_loadingTogglesAndSetsEvent() = runBlocking {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - sending report
        reportViewModel.message.value = testMessage
        reportViewModel.sendReport()

        // THEN - verify that following events occur
        // keyboard is closed
        assertLiveDataEventTriggered(reportViewModel.closeSoftKeyboardEvent, Unit)

        // progress is shown
        assertThat(LiveDataTestUtil.getValue(reportViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(reportViewModel.dataLoading)).isFalse()

        // sets show message event
        assertLiveDataEventTriggered(reportViewModel.showMessageEvent, "Test exception")
    }

}