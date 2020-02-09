package com.vaibhavdhunde.app.elearning.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.snackbar(message: Int) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.isIdle(false)
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.isIdle(true)
            }
        })
        show()
    }
}

fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                EspressoIdlingResource.isIdle(false)
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                EspressoIdlingResource.isIdle(true)
            }
        })
        show()
    }
}