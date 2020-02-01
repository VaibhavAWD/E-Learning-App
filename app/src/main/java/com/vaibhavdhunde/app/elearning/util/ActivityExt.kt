package com.vaibhavdhunde.app.elearning.util

import android.content.Intent
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