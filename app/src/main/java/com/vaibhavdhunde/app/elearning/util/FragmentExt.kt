package com.vaibhavdhunde.app.elearning.util

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T : ViewModel> Fragment.obtainViewModel(modelClass: Class<T>, factory: ViewModelFactory): T {
    return ViewModelProvider(this, factory)[modelClass]
}

fun <T : AppCompatActivity> Fragment.startFreshActivity(activity: Class<T>) {
    Intent(context, activity).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
    }.also {
        startActivity(it)
    }
}

fun Fragment.closeSoftKeyboard() {
    val view = activity?.currentFocus
    view?.let {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}