package com.vaibhavdhunde.app.elearning.util

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.vaibhavdhunde.app.elearning.application.TestElearningApplication

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, (TestElearningApplication::class.java).name, context)
    }
}