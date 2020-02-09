package com.vaibhavdhunde.app.elearning.util

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)

    fun isIdle(value: Boolean) {
        countingIdlingResource.setIsIdle(value)
    }
}