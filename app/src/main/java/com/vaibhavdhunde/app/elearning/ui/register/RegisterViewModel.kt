package com.vaibhavdhunde.app.elearning.ui.register

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

class RegisterViewModel(private val repository: ElearningRepository) : ViewModel() {

    // Two-way DataBinding
    val name = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val confPassword = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _mainEvent = MutableLiveData<Event<Unit>>()
    val mainEvent: LiveData<Event<Unit>> = _mainEvent

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _closeSoftKeyboardEvent = MutableLiveData<Event<Unit>>()
    val closeSoftKeyboardEvent: LiveData<Event<Unit>> = _closeSoftKeyboardEvent

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    fun registerUser() {
        if (!hasValidData()) return
        _closeSoftKeyboardEvent.value = Event(Unit)
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.registerUser(name.value!!, email.value!!, password.value!!)
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

        val currentName = name.value
        val currentEmail = email.value
        val currentPassword = password.value
        val currentConfPassword = confPassword.value

        if (currentName.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_name)
        } else if (currentEmail.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_email)
        } else if (!EmailValidator.isValidEmail(currentEmail)) {
            _showMessageEvent.value = Event(R.string.error_invalid_email)
        } else if (currentPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_password)
        } else if (currentPassword.length < 6 || currentPassword.length > 16) {
            _showMessageEvent.value = Event(R.string.error_invalid_password)
        } else if (currentConfPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_conf_password)
        } else if (currentConfPassword != currentPassword) {
            _showMessageEvent.value = Event(R.string.error_password_mismatch)
        } else {
            hasValidData = true
        }

        return hasValidData
    }

    fun loginUser() {
        _loginEvent.value = Event(Unit)
    }

}