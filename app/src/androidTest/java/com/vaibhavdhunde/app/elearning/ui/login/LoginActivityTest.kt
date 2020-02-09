package com.vaibhavdhunde.app.elearning.ui.login

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.application.TestElearningApplication
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.util.DataBindingIdlingResource
import com.vaibhavdhunde.app.elearning.util.EspressoIdlingResource
import com.vaibhavdhunde.app.elearning.util.monitorActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : KodeinAware {

    override val kodein by kodein {
        ApplicationProvider.getApplicationContext<TestElearningApplication>()
    }

    private val repository: FakeElearningRepository by instance()

    // An idling resource that waits for the data binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    // test login data
    private val testEmail = "test@email.com"
    private val testInvalidEmail = "test@email"
    private val testPassword = "testPassword"
    private val testInvalidPassword = "pass"

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
    fun launchLoginActivity_loginActivityInView() {
        // WHEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - verify that the login activity is in view
        onView(withId(R.id.input_email))
            .check(matches(isDisplayed()))
        onView(withId(R.id.input_password))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_register_here))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_emptyEmail_userNotLoggedIn() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user with empty email
        onView(withId(R.id.input_email))
            .perform(clearText())
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the user is not logged in
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_invalidEmail_userNotLoggedIn() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user with invalid email
        onView(withId(R.id.input_email))
            .perform(replaceText(testInvalidEmail))
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the user is not logged in
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_emptyPassword_userNotLoggedIn() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user with empty password
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(clearText())
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the user is not logged in
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_invalidPassword_userNotLoggedIn() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user with invalid password
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testInvalidPassword))
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the user is not logged in
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_error_userNotLoggedIn() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the user is not logged in
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginUser_success_mainActivityInView() {
        // GIVEN - repository returns success
        repository.setShouldReturnError(false)

        // login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - login user
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.btn_login))
            .perform(click())

        // THEN - verify that the main activity is in view
        onView(withId(R.id.drawer_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickRegisterHere_registerActivityInView() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - click register here
        onView(withId(R.id.btn_register_here))
            .perform(click())

        // THEN - verify that the register activity is in view
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }
}