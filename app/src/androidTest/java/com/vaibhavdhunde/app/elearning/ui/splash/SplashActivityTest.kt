package com.vaibhavdhunde.app.elearning.ui.splash

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.application.TestElearningApplication
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.util.DataBindingIdlingResource
import com.vaibhavdhunde.app.elearning.util.EspressoIdlingResource
import com.vaibhavdhunde.app.elearning.util.monitorActivity
import com.vaibhavdhunde.app.elearning.util.saveUserBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@RunWith(AndroidJUnit4::class)
class SplashActivityTest : KodeinAware {

    override val kodein by kodein {
        ApplicationProvider.getApplicationContext<TestElearningApplication>()
    }

    private val repository: FakeElearningRepository by instance()

    // An idling resource that waits for the data binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

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
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun launchSplashActivity_activityInView() {
        // WHEN - launch splash activity
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - verify that the splash activity is in view
        onView(withId(R.id.app_logo))
            .check(matches(isDisplayed()))
        onView(withId(R.id.app_name))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.app_name)))
    }

    @Test
    fun loadUser_userNotAvailable_loginActivityInView() {
        // GIVEN - user is not available

        // WHEN - splash activity is launched
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - verify that login activity is in view
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loadUser_userAvailable_mainActivityInView() {
        // GIVEN - user is available
        repository.saveUserBlocking(testUser)

        // WHEN - splash activity is launched
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - verify that main activity is launched
        onView(withId(R.id.drawer_layout))
            .perform(DrawerActions.open())
        onView(withId(R.id.display_profile_name))
            .check(matches(isDisplayed()))
            .check(matches(withText(testUser.name)))
        onView(withId(R.id.display_profile_email))
            .check(matches(isDisplayed()))
            .check(matches(withText(testUser.email)))
    }

}