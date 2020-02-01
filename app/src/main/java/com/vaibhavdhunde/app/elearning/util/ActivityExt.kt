package com.vaibhavdhunde.app.elearning.util

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <T : ViewModel> AppCompatActivity.obtainViewModel(
    modelClass: Class<T>,
    factory: ViewModelFactory
): T {
    return ViewModelProvider(this, factory)[modelClass]
}

fun <T : AppCompatActivity> AppCompatActivity.startFreshActivity(activity: Class<T>) {
    Intent(applicationContext, activity).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
                Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_CLEAR_TOP
    }.also {
        startActivity(it)
    }
}

fun <T : AppCompatActivity> AppCompatActivity.startActivity(activity: Class<T>) {
    startActivity(Intent(applicationContext, activity))
}

fun AppCompatActivity.closeSoftKeyboard() {
    val view = currentFocus
    view?.let {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}