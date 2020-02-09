package com.vaibhavdhunde.app.elearning.ui.register

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.application.TestElearningApplication
import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.ui.login.LoginActivity
import com.vaibhavdhunde.app.elearning.util.DataBindingIdlingResource
import com.vaibhavdhunde.app.elearning.util.EspressoIdlingResource
import com.vaibhavdhunde.app.elearning.util.monitorActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class RegisterActivityTest : KodeinAware {

    override val kodein by kodein {
        ApplicationProvider.getApplicationContext<TestElearningApplication>()
    }

    private val repository: FakeElearningRepository by instance()

    // An idling resource that waits for the data binding to have no pending bindings
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    // test login data
    private val testName = "Test Name"
    private val testEmail = "test@email.com"
    private val testInvalidEmail = "test@email"
    private val testPassword = "testPassword"
    private val testInvalidPassword = "pass"
    private val testMismatchPassword = "testMismatchPassword"

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
    fun launchRegisterActivity_registerActivityInView() {
        // WHEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // THEN - verify that register activity is in view
        onView(withId(R.id.input_name))
            .check(matches(isDisplayed()))
        onView(withId(R.id.input_email))
            .check(matches(isDisplayed()))
        onView(withId(R.id.input_password))
            .check(matches(isDisplayed()))
        onView(withId(R.id.input_conf_password))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_login_here))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_emptyName_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with empty name
        onView(withId(R.id.input_name))
            .perform(clearText())
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_emptyEmail_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with empty email
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(clearText())
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_invalidEmail_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with invalid email
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testInvalidEmail))
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_emptyPassword_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with empty password
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(clearText())
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_invalidPassword_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with invalid password
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testInvalidPassword))
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_emptyConfPassword_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with empty conf password
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.input_conf_password))
            .perform(clearText())
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_passwordMismatch_userNotRegistered() {
        // GIVEN - register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with password mismatch
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.input_conf_password))
            .perform(replaceText(testMismatchPassword))
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_error_userNotRegistered() {
        // GIVEN - repository returns error
        repository.setShouldReturnError(true)

        // register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with password mismatch
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.input_conf_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the user is not registered
        onView(withId(R.id.btn_register))
            .check(matches(isDisplayed()))
    }

    @Test
    fun registerUser_success_mainActivityInView() {
        // GIVEN - repository returns success
        repository.setShouldReturnError(false)

        // register activity is launched
        val activityScenario = ActivityScenario.launch(RegisterActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN - registering user with password mismatch
        onView(withId(R.id.input_name))
            .perform(replaceText(testName))
        onView(withId(R.id.input_email))
            .perform(replaceText(testEmail))
        onView(withId(R.id.input_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.input_conf_password))
            .perform(replaceText(testPassword))
        onView(withId(R.id.btn_register))
            .perform(click())

        // THEN - verify that the main activity in view
        onView(withId(R.id.drawer_layout))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickLoginHere_loginActivityInView() {
        // GIVEN - login activity is launched
        val activityScenario = ActivityScenario.launch(LoginActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // verify login activity in view
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))

        // go to register user
        onView(withId(R.id.btn_register_here))
            .perform(click())

        // verify that register activity in view
        onView(withId(R.id.btn_login_here))
            .check(matches(isDisplayed()))

        // WHEN - clicking login here
        onView(withId(R.id.btn_login_here))
            .perform(click())

        // THEN - verify that the login activity is in view
        onView(withId(R.id.btn_login))
            .check(matches(isDisplayed()))
    }
}