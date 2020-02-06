@file:Suppress("DEPRECATION")

package com.vaibhavdhunde.app.elearning.ui.blogs

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
 * Tests for implementation of [BlogsViewModel].
 */
class BlogsViewModelTest {

    // SUT
    private lateinit var blogsViewModel: BlogsViewModel

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

    // test blogs data
    private val testBlog1 = Blog(
        1,
        1,
        "Test Blog 1",
        "Test Blog Body",
        "https://test.com/image.jpg",
        "created_at"
    )
    private val testBlog2 = Blog(
        2,
        1,
        "Test Blog 2",
        "Test Blog Body",
        "https://test.com/image2.jpg",
        "created_at"
    )
    private val remoteBlogs = listOf(testBlog1, testBlog2)

    @Before
    fun setUp() {
        repository = FakeElearningRepository()
        blogsViewModel = BlogsViewModel(repository)
    }

    @Test
    fun loadBlogs_loadingTogglesAndDataLoaded() {
        // GIVEN - repository has data
        repository.blogs = remoteBlogs

        // WHEN - getting blogs
        blogsViewModel.loadBlogs()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataLoading)).isFalse()

        // and data loaded
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.blogs)).isEqualTo(remoteBlogs)
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataAvailable)).isTrue()
    }

    @Test
    fun loadBlogs_error_loadingTogglesAndDataIsEmpty() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // WHEN - getting blogs
        blogsViewModel.loadBlogs()

        // THEN - verify that following events occur
        // progress is shown
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataLoading)).isTrue()

        // execute pending coroutines actions
        testContext.triggerActions()

        // progress is hidden
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataLoading)).isFalse()

        // and data is empty
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.blogs)).isEmpty()
        assertThat(LiveDataTestUtil.getValue(blogsViewModel.dataAvailable)).isFalse()

        // verify that the show message event is set
        assertLiveDataEventTriggered(blogsViewModel.showMessageEvent, "Test exception")
    }

    @Test
    fun openBlog_setsEvent() {
        // WHEN - opening blog
        blogsViewModel.openBlog(1)

        // THEN - verify that blog event is set
        assertLiveDataEventTriggered(blogsViewModel.blogEvent, 1)
    }
}