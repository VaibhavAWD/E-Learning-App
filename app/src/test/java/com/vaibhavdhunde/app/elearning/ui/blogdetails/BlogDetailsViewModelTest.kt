@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.blogdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.Blog
import com.vaibhavdhunde.app.elearning.util.LiveDataTestUtil
import com.vaibhavdhunde.app.elearning.util.ViewModelScopeMainDispatcherRule
import com.vaibhavdhunde.app.elearning.util.assertLiveDataEventTriggered
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Tests for implementation of [BlogDetailsViewModel].
 */
class BlogDetailsViewModelTest {

    // SUT
    private lateinit var blogDetailsViewModel: BlogDetailsViewModel

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

    // test blog data
    private val testBlog = Blog(
        1,
        1,
        "Test Blog Title",
        "Test Blog Body",
        "https://test.com/image.jpg",
        "created_at"
    )

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        blogDetailsViewModel = BlogDetailsViewModel(repository)
    }

    @Test
    fun loadBlog_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.blog = testBlog

        // WHEN - getting blog
        blogDetailsViewModel.loadBlog(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.blog)).isEqualTo(testBlog)
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadBlog_error_loadingTogglesAndDataNotAvailable() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting blog
        blogDetailsViewModel.loadBlog(1)

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.blog)).isNull()
        assertThat(LiveDataTestUtil.getValue(blogDetailsViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(blogDetailsViewModel.showMessageEvent, "Test exception")
    }
}