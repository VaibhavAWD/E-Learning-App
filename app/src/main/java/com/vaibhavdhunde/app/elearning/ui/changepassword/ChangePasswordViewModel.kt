package com.vaibhavdhunde.app.elearning.ui.changepassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repository: ElearningRepository) : ViewModel() {

    // Two-way DataBinding
    val password = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confPassword = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    private val _closeSoftKeyboardEvent = MutableLiveData<Event<Unit>>()
    val closeSoftKeyboardEvent: LiveData<Event<Unit>> = _closeSoftKeyboardEvent

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    fun updatePassword() {
        if (!hasValidData()) return
        _closeSoftKeyboardEvent.value = Event(Unit)
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.updatePassword(password.value!!, newPassword.value!!)
            if (result is Success) {
                val message = result.data as String
                _showMessageEvent.value = Event(message)
                _loginEvent.value = Event(Unit)
            } else {
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    private fun hasValidData(): Boolean {
        var hasValidData = false

        val currentPassword = password.value
        val currentNewPassword = newPassword.value
        val currentConfPassword = confPassword.value

        if (currentPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_password)
        } else if (currentNewPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_new_password)
        } else if (currentNewPassword.length < 6 || currentNewPassword.length > 16) {
            _showMessageEvent.value = Event(R.string.error_invalid_password)
        } else if (currentConfPassword.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_conf_password)
        } else if (currentConfPassword != currentNewPassword) {
            _showMessageEvent.value = Event(R.string.error_password_mismatch)
        } else {
            hasValidData = true
        }

        return hasValidData
    }

}