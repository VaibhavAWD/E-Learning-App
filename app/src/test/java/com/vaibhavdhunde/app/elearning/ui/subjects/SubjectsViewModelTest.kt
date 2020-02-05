@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.subjects

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import com.vaibhavdhunde.app.elearning.util.assertLiveDataEventTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for implementation of [SubjectsViewModel].
 */
class SubjectsViewModelTest {

    // SUT
    private lateinit var subjectsViewModel: SubjectsViewModel

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

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        subjectsViewModel = SubjectsViewModel(repository)
    }

    @Test
    fun loadSubjects_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.subjects = remoteSubjects

        // WHEN - getting subjects
        subjectsViewModel.loadSubjects()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.subjects)).isEqualTo(remoteSubjects)
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadSubjects_error_loadingTogglesAndDataIsEmpty() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting subjects
        subjectsViewModel.loadSubjects()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.subjects)).isEmpty()
        assertThat(LiveDataTestUtil.getValue(subjectsViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(subjectsViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun openTopics_setsEvent() {
        // WHEN - opening topic
        subjectsViewModel.openTopics(1)

        // THEN - verify that topic event is set
        assertLiveDataEventTriggered(subjectsViewModel.topicsEvent, 1)
    }
}