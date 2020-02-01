package com.vaibhavdhunde.app.elearning.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.util.EmailValidator
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ElearningRepository) : ViewModel() {

    // Two-way DataBinding
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _mainEvent = MutableLiveData<Event<Unit>>()
    val mainEvent: LiveData<Event<Unit>> = _mainEvent

    private val _registerEvent = MutableLiveData<Event<Unit>>()
    val registerEvent: LiveData<Event<Unit>> = _registerEvent

    private val _closeSoftKeyboardEvent = MutableLiveData<Event<Unit>>()
    val closeSoftKeyboardEvent: LiveData<Event<Unit>> = _closeSoftKeyboardEvent

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    fun loginUser() {
        if (!hasValidData()) return
        _closeSoftKeyboardEvent.value = Event(Unit)
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.loginUser(email.value!!, password.value!!)
            if (result is Success) {
                _mainEvent.value = Event(Unit)
            } else {
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    private fun hasValidData(): Boolean {
        var hasValidData = false

        val currentEmail = email.value
        val currentPassword = password.value

        if (currentEmail.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_email)
        } else if (!EmailValidator.isValidEmail(currentEmail)) {
            _showMessageEvent.value = Event(R.string.error_invalid_email)
        } else if (currentPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_password)
        } else if (currentPassword.length < 6 || currentPassword.length > 16) {
            _showMessageEvent.value = Event(R.string.error_invalid_password)
        } else {
            hasValidData = true
        }

        return hasValidData
    }

    fun registerUser() {
        _registerEvent.value = Event(Unit)
    }

}