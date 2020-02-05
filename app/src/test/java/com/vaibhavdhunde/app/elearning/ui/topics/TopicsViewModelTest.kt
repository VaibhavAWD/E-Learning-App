@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.topics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import com.vaibhavdhunde.app.elearning.util.assertLiveDataEventTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for implementation of [TopicsViewModel].
 */
class TopicsViewModelTest {

    // SUT
    private lateinit var topicsViewModel: TopicsViewModel

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

    // test subject data
    private val testSubject1 = Subject(
        1,
        "Test Subject1 ",
        "Test subtitle 1",
        "https://testapi.com/image1.jpg"
    )
    private val testSubject2 = Subject(
        2,
        "Test Subject 2",
        "Test subtitle 2",
        "https://testapi.com/image2.jpg"
    )
    private val remoteSubjects = listOf(testSubject1, testSubject2)

    // test topics data
    private val testTopic1 = Topic(
        1,
        1,
        "Test Subject 1",
        "Test Subtitle 1"
    )
    private val testTopic2 = Topic(
        2,
        1,
        "Test Subject 2",
        "Test Subtitle 2"
    )
    private val remoteTopics = listOf(testTopic1, testTopic2)

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        topicsViewModel = TopicsViewModel(repository)
    }

    @Test
    fun loadTopics_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.topics = remoteTopics

        // WHEN - getting topics
        topicsViewModel.loadTopics(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.topics)).isEqualTo(remoteTopics)
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadTopics_error_loadingTogglesAndDataIsEmpty() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting subjects
        topicsViewModel.loadTopics(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.topics)).isEmpty()
        assertThat(LiveDataTestUtil.getValue(topicsViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(topicsViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun openSubtopics_setsEvent() {
        // WHEN - opening subtopics
        topicsViewModel.openSubtopics(1)

        // THEN - verify that subtopic event is set
        assertLiveDataEventTriggered(topicsViewModel.subtopicsEvent, 1)
    }
}