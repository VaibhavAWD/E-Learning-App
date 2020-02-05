package com.vaibhavdhunde.app.elearning.data

import com.google.common.truth.Truth.assertThat
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.data.source.local.FakeUsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.FakeSubjectsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.FakeSubtopicsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.FakeTopicsRemoteDataSource
import com.vaibhavdhunde.app.elearning.data.source.remote.FakeUsersRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before

import org.junit.Test

/**
 * Tests for implementation of [ElearningRepository].
 */
class ElearningRepositoryTest {

    // SUT
    private lateinit var repository: ElearningRepository

    // Use fake users local data source for testing
    private lateinit var usersLocalDataSource: FakeUsersLocalDataSource

    // Use fake users remote data source for testing
    private lateinit var usersRemoteDataSource: FakeUsersRemoteDataSource

    // Use fake subjects remote data source for testing
    private lateinit var subjectsRemoteDataSource: FakeSubjectsRemoteDataSource

    // Use fake topics remote data source for testing
    private lateinit var topicsRemoteDataSource: FakeTopicsRemoteDataSource

    // Use fake subtopics remote data source for testing
    private lateinit var subtopicsRemoteDataSource: FakeSubtopicsRemoteDataSource

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
    private val testNewName = "Test Name**"

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
        usersLocalDataSource = FakeUsersLocalDataSource()
        usersRemoteDataSource = FakeUsersRemoteDataSource()
        subjectsRemoteDataSource = FakeSubjectsRemoteDataSource()
        topicsRemoteDataSource = FakeTopicsRemoteDataSource()
        subtopicsRemoteDataSource = FakeSubtopicsRemoteDataSource()
        repository = DefaultElearningRepository(
            usersLocalDataSource,
            usersRemoteDataSource,
            subjectsRemoteDataSource,
            topicsRemoteDataSource,
            subtopicsRemoteDataSource
        )
    }

    @Test
    fun loginUser_success_userSavedAndSuccessReturned() = runBlocking {
        // WHEN - login user
        val result = repository.loginUser("", "")

        // THEN - verify that the result is success and user is saved
        assertThat(result.succeeded).isTrue()
        val second = (repository.getUser() as Success).data
        assertThat(second).isNotNull()
    }

    @Test
    fun loginUser_error_errorReturned() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // WHEN - login user
        val result = repository.loginUser("", "")

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun registerUser_success_userSavedAndSuccessReturned() = runBlocking {
        // WHEN - register user
        val result = repository.registerUser("", "", "")

        // THEN - verify that the result is success and user is saved
        assertThat(result.succeeded).isTrue()
        val second = (repository.getUser() as Success).data
        assertThat(second).isNotNull()
    }

    @Test
    fun registerUser_error_errorReturned() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // WHEN - register user
        val result = repository.registerUser("", "", "")

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun updateProfileName_success_nameUpdatedInCacheAndLocal() = runBlocking {
        // GIVEN - initially local data source has user
        usersLocalDataSource.saveUser(testUser)
        val initial = (repository.getUser() as Success).data
        assertThat(initial.name).isEqualTo(testUser.name)

        // WHEN - updating profile name
        val result = repository.updateProfileName(testNewName)

        // THEN - verify that the result is success and name is updated in cache and local
        assertThat(result.succeeded).isTrue()
        val second = (repository.getUser() as Success).data
        assertThat(second.name).isEqualTo(testNewName)
        val localUser = (usersLocalDataSource.getUser() as Success).data
        assertThat(localUser.name).isEqualTo(testNewName)
    }

    @Test
    fun updateProfileName_error() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // initially user is loaded
        usersLocalDataSource.saveUser(testUser)
        repository.getUser()

        // WHEN - updating profile name
        val result = repository.updateProfileName(testNewName)

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun updatePassword_success() = runBlocking {
        // GIVEN - initially user is loaded
        usersLocalDataSource.saveUser(testUser)
        repository.getUser()

        // WHEN - updating password
        val result = repository.updatePassword("", "")

        // THEN - verify that the result is success
        assertThat(result.succeeded).isTrue()
    }

    @Test
    fun updatePassword_error() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // initially user is loaded
        usersLocalDataSource.saveUser(testUser)
        repository.getUser()

        // WHEN - updating password
        val result = repository.updatePassword("", "")

        // THEN - verify that the result is success
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun deactivateAccount_success() = runBlocking {
        // GIVEN - initially user is loaded
        usersLocalDataSource.saveUser(testUser)
        repository.getUser()

        // WHEN - deactivating account
        val result = repository.deactivateAccount()

        // THEN - verify that the result is success
        assertThat(result.succeeded).isTrue()
    }

    @Test
    fun deactivateAccount_error() = runBlocking {
        // GIVEN - remote data source returns error
        usersRemoteDataSource.setShouldReturnError(true)

        // initially user is loaded
        usersLocalDataSource.saveUser(testUser)
        repository.getUser()

        // WHEN - deactivating account
        val result = repository.deactivateAccount()

        // THEN - verify that the result is success
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getSubjects_emptyCache_subjectsLoadedFromRemote() = runBlocking {
        // GIVEN - remote has subjects
        subjectsRemoteDataSource.subjects = remoteSubjects

        // WHEN - getting subjects
        val result = repository.getSubjects()

        // THEN - verify that the subjects are loaded from remote
        assertThat(result.succeeded).isTrue()
        val subjects = (result as Success).data
        assertThat(subjects).isEqualTo(remoteSubjects)
    }

    @Test
    fun getSubjects_repositoryCachesAfterRemote() = runBlocking {
        // remote has subjects
        subjectsRemoteDataSource.subjects = remoteSubjects

        // GIVEN - initial subjects
        val initial = (repository.getSubjects() as Success).data

        // WHEN - getting subjects
        val second = (repository.getSubjects() as Success).data

        // THEN - verify that the initial and second is same as now subjects are cached
        assertThat(initial).isEqualTo(second)
    }

    @Test
    fun getSubjects_forceUpdate_repositoryCachesAfterRemote() = runBlocking {
        // remote has subjects
        subjectsRemoteDataSource.subjects = listOf(testSubject1)

        // GIVEN - initial subjects
        val initial = (repository.getSubjects() as Success).data

        // WHEN - getting subjects with force update
        subjectsRemoteDataSource.subjects = listOf(testSubject1, testSubject2)
        val second = (repository.getSubjects(true) as Success).data

        // THEN - verify that the initial and second are not same as now subjects are forced from remote
        assertThat(initial).isNotEqualTo(second)
    }

    @Test
    fun getSubjects_error() = runBlocking {
        // GIVEN - remote returns error
        subjectsRemoteDataSource.setShouldReturnError(true)

        // WHEN - getting subjects
        val result = repository.getSubjects(true)

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getTopics_emptyCache_topicsLoadedFromRemote() = runBlocking {
        // GIVEN - remote has topics
        topicsRemoteDataSource.topics = remoteTopics

        // WHEN - getting topics
        val result = repository.getTopics(1)

        // THEN - verify that the subjects are loaded from remote
        assertThat(result.succeeded).isTrue()
        val subjects = (result as Success).data
        assertThat(subjects).isEqualTo(remoteTopics)
    }

    @Test
    fun getTopics_repositoryCachesAfterRemote() = runBlocking {
        // remote has topics
        topicsRemoteDataSource.topics = remoteTopics

        // GIVEN - initial topics
        val initial = (repository.getTopics(1) as Success).data

        // WHEN - getting topics
        val second = (repository.getTopics(1) as Success).data

        // THEN - verify that the initial and second is same as now subjects are cached
        assertThat(initial).isEqualTo(second)
    }

    @Test
    fun getTopics_forceUpdate_repositoryCachesAfterRemote() = runBlocking {
        // remote has topics
        topicsRemoteDataSource.topics = listOf(testTopic1)

        // GIVEN - initial topics
        val initial = (repository.getTopics(1) as Success).data

        // WHEN - getting topics with force update
        topicsRemoteDataSource.topics = listOf(testTopic1, testTopic2)
        val second = (repository.getTopics(1, true) as Success).data

        // THEN - verify that the initial and second are not same as now subjects are forced from remote
        assertThat(initial).isNotEqualTo(second)
    }

    @Test
    fun getTopics_error() = runBlocking {
        // GIVEN - remote returns error
        topicsRemoteDataSource.setShouldReturnError(true)

        // WHEN - getting topics
        val result = repository.getTopics(1, true)

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getSubtopics_emptyCache_topicsLoadedFromRemote() = runBlocking {
        // GIVEN - remote has subtopics
        subtopicsRemoteDataSource.subtopics = remoteSubtopics

        // WHEN - getting subtopics
        val result = repository.getSubtopics(1)

        // THEN - verify that the subtopics are loaded from remote
        assertThat(result.succeeded).isTrue()
        val subjects = (result as Success).data
        assertThat(subjects).isEqualTo(remoteSubtopics)
    }

    @Test
    fun getSubtopics_repositoryCachesAfterRemote() = runBlocking {
        // remote has subtopics
        subtopicsRemoteDataSource.subtopics = remoteSubtopics

        // GIVEN - initial subtopics
        val initial = (repository.getSubtopics(1) as Success).data

        // WHEN - getting topics
        val second = (repository.getSubtopics(1) as Success).data

        // THEN - verify that the initial and second is same as now subtopics are cached
        assertThat(initial).isEqualTo(second)
    }

    @Test
    fun getSubtopics_forceUpdate_repositoryCachesAfterRemote() = runBlocking {
        // remote has subtopics
        subtopicsRemoteDataSource.subtopics = listOf(testSubtopic1)

        // GIVEN - initial subtopics
        val initial = (repository.getSubtopics(1) as Success).data

        // WHEN - getting subtopics with force update
        subtopicsRemoteDataSource.subtopics = listOf(testSubtopic1, testSubtopic2)
        val second = (repository.getSubtopics(1, true) as Success).data

        // THEN - verify that the initial and second are not same as now subtopics are forced from remote
        assertThat(initial).isNotEqualTo(second)
    }

    @Test
    fun getSubtopics_error() = runBlocking {
        // GIVEN - remote returns error
        subtopicsRemoteDataSource.setShouldReturnError(true)

        // WHEN - getting subtopics
        val result = repository.getSubtopics(1, true)

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getSubtopic_success_subtopicReturned() = runBlocking {
        // GIVEN - remote has subtopic
        subtopicsRemoteDataSource.subtopic = testSubtopic1

        // WHEN - getting subtopic
        val result = repository.getSubtopic(1)

        // THEN - verify that the result has subtopic
        assertThat(result.succeeded).isTrue()
        assertThat((result as Success).data).isEqualTo(testSubtopic1)
    }

    @Test
    fun getSubtopic_error() = runBlocking {
        // GIVEN - remote returns error
        subtopicsRemoteDataSource.setShouldReturnError(true)

        // WHEN - getting subtopic
        val result = repository.getSubtopic(1)

        // THEN - verify that the result is error
        assertThat(result).isInstanceOf(Error::class.java)
    }

    @Test
    fun getUser_userCachesAfterLocal() = runBlocking {
        // GIVEN - local data source has user
        usersLocalDataSource.saveUser(testUser)

        // initial cached user
        val initial = (repository.getUser() as Success).data

        // WHEN - getting user
        val second = (repository.getUser() as Success).data

        // THEN - verify that the second and initial are same, because second is from cache
        assertThat(second).isEqualTo(initial)
    }

    @Test
    fun saveUser_savesInCacheAndLocal() = runBlocking {
        // WHEN - saving user
        repository.saveUser(testUser)

        // THEN - verify that the user is saved in local and cache
        val localUser = (usersLocalDataSource.getUser() as Success).data
        assertThat(localUser).isEqualTo(testUser)
        val cachedUser = (repository.getUser() as Success).data
        assertThat(cachedUser).isEqualTo(testUser)
    }

    @Test
    fun deleteUser_deletesFromCacheAndLocal() = runBlocking {
        // GIVEN - user is saved
        repository.saveUser(testUser)

        // WHEN - deleting user
        repository.deleteUser()

        // THEN - verify that the user is deleted from local and cache
        val localUser = usersLocalDataSource.getUser()
        assertThat(localUser).isInstanceOf(Error::class.java)
        val cachedUser = repository.getUser()
        assertThat(cachedUser).isInstanceOf(Error::class.java)
    }

}