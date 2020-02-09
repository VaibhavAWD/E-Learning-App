package com.vaibhavdhunde.app.elearning.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.util.EspressoIdlingResource
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: ElearningRepository) : ViewModel() {

    private companion object {
        const val SPLASH_DELAY = 1000L
    }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _mainEvent = MutableLiveData<Event<Unit>>()
    val mainEvent: LiveData<Event<Unit>> = _mainEvent

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    fun loadUser() {
        _dataLoading.value = true

        EspressoIdlingResource.isIdle(false)

        viewModelScope.launch {
            delay(SPLASH_DELAY) // purposely added delay to show splash screen
            val result = repository.getUser()
            if (result is Success) {
                _mainEvent.value = Event(Unit)
            } else {
                _loginEvent.value = Event(Unit)
            }

            EspressoIdlingResource.isIdle(true)

            _dataLoading.value = false
        }
    }

}