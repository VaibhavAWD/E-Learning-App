package com.vaibhavdhunde.app.elearning.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.ui.login.LoginViewModel
import com.vaibhavdhunde.app.elearning.ui.main.MainViewModel
import com.vaibhavdhunde.app.elearning.ui.register.RegisterViewModel
import com.vaibhavdhunde.app.elearning.ui.splash.SplashViewModel

class ViewModelFactory(private val repository: ElearningRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel(repository)
                isAssignableFrom(LoginViewModel::class.java) ->
                    LoginViewModel(repository)
                isAssignableFrom(RegisterViewModel::class.java) ->
                    RegisterViewModel(repository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)
                else -> throw IllegalArgumentException("Unknown model class: $modelClass")
            }
        } as T
    }
}