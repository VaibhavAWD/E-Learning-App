package com.vaibhavdhunde.app.elearning.util

import androidx.test.espresso.IdlingResource
import androidx.test.espresso.IdlingResource.ResourceCallback
import java.util.concurrent.atomic.AtomicBoolean

class SimpleCountingIdlingResource(private val resourceName: String) : IdlingResource {

    private val isIdle = AtomicBoolean(true)

    // written from main thread, read from any thread
    @Volatile
    private var resourceCallback: ResourceCallback? = null

    override fun getName(): String = resourceName

    override fun isIdleNow(): Boolean = isIdle.get()

    override fun registerIdleTransitionCallback(resourceCallback: ResourceCallback?) {
        this.resourceCallback = resourceCallback
    }

    fun setIsIdle(isIdle: Boolean) {
        this.isIdle.set(isIdle)
        if (isIdle) {
            resourceCallback?.onTransitionToIdle()
        }
    }
}