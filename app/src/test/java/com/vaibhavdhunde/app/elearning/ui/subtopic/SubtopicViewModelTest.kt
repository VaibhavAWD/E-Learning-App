@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.subtopic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import com.vaibhavdhunde.app.elearning.util.assertLiveDataEventTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for implementation of [SubtopicViewModel].
 */
class SubtopicViewModelTest {

    // SUT
    private lateinit var subtopicViewModel: SubtopicViewModel

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

    // test subtopics data
    private val testSubtopic = Subtopic(
        1,
        1,
        "Test Subtopic",
        "Test Body",
        "https://test.com/url",
        "https://test.com/image.jpg",
        "2:44"
    )

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        subtopicViewModel = SubtopicViewModel(repository)
    }

    @Test
    fun loadSubtopic_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.subtopic = testSubtopic

        // WHEN - getting subtopic
        subtopicViewModel.loadSubtopic(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.subtopic)).isEqualTo(testSubtopic)
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadSubtopic_error_loadingTogglesAndDataNotAvailable() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting subtopic
        subtopicViewModel.loadSubtopic(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.subtopic)).isNull()
        assertThat(LiveDataTestUtil.getValue(subtopicViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(subtopicViewModel.showMessageEvent, "Test exception")
    }
}