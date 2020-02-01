package com.vaibhavdhunde.app.elearning.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/**
 * Tests for implementation of [EmailValidator].
 */
class EmailValidatorTest {

    private companion object {
        const val TEST_EMAIL = "test@email.com"
        const val TEST_EMAIL_NO_TLD = "test@email"
        const val TEST_EMAIL_DOUBLE_DOT = "test@email..com"
        const val TEST_EMAIL_NO_USERNAME = "@email.com"
    }

    @Test
    fun emailValidator_validEmail_trueReturned() {
        assertThat(EmailValidator.isValidEmail(TEST_EMAIL)).isTrue()
    }

    @Test
    fun emailValidator_invalidEmailWithNoTopLevelDomain_falseReturned() {
        assertThat(EmailValidator.isValidEmail(TEST_EMAIL_NO_TLD)).isFalse()
    }

    @Test
    fun emailValidator_invalidEmailWithDoubleDot_falseReturned() {
        assertThat(EmailValidator.isValidEmail(TEST_EMAIL_DOUBLE_DOT)).isFalse()
    }

    @Test
    fun emailValidator_invalidEmailWithNoUsername_falseReturned() {
        assertThat(EmailValidator.isValidEmail(TEST_EMAIL_NO_USERNAME)).isFalse()
    }

    @Test
    fun emailValidator_emptyEmail_falseReturned() {
        assertThat(EmailValidator.isValidEmail("")).isFalse()
    }

    @Test
    fun emailValidator_null_falseReturned() {
        assertThat(EmailValidator.isValidEmail(null)).isFalse()
    }
}