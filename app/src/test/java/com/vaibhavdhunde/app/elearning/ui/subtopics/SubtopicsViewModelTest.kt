@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.subtopics

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
 * Tests for implementation of [SubtopicsViewModel].
 */
class SubtopicsViewModelTest {

    // SUT
    private lateinit var subtopicsViewModel: SubtopicsViewModel

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
    private val testSubtopic1 = Subtopic(
        1,
        1,
        "Test Subtopic 1",
        "Test Body 1",
        "https://test.com/url",
        "https://test.com/image.jpg",
        "2:44"
    )

    private val testSubtopic2 = Subtopic(
        2,
        1,
        "Test Subtopic 2",
        "Test Body 2",
        "https://test.com/url2",
        "https://test.com/image2.jpg",
        "3:17"
    )
    private val remoteSubtopics = listOf(testSubtopic1, testSubtopic2)

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        subtopicsViewModel = SubtopicsViewModel(repository)
    }

    @Test
    fun loadSubtopics_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.subtopics = remoteSubtopics

        // WHEN - getting subtopics
        subtopicsViewModel.loadSubtopics(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.subtopics)).isEqualTo(remoteSubtopics)
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadSubtopics_error_loadingTogglesAndDataIsEmpty() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting subtopics
        subtopicsViewModel.loadSubtopics(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.subtopics)).isEmpty()
        assertThat(LiveDataTestUtil.getValue(subtopicsViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(subtopicsViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun openSubtopic_setsEvent() {
        // WHEN - opening subtopic
        subtopicsViewModel.openSubtopic(1)

        // THEN - verify that subtopic event is set
        assertLiveDataEventTriggered(subtopicsViewModel.subtopicEvent, 1)
    }
}