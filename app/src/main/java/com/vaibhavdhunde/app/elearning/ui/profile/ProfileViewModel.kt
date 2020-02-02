package com.vaibhavdhunde.app.elearning.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.User
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ElearningRepository) : ViewModel() {

    // Two-way DataBinding
    val name = MutableLiveData<String>()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    private val _closeSoftKeyboardEvent = MutableLiveData<Event<Unit>>()
    val closeSoftKeyboardEvent: LiveData<Event<Unit>> = _closeSoftKeyboardEvent

    private val _changePasswordEvent = MutableLiveData<Event<Unit>>()
    val changePasswordEvent: LiveData<Event<Unit>> = _changePasswordEvent

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent: LiveData<Event<Unit>> = _loginEvent

    private val _deactivateAccountAlertEvent = MutableLiveData<Event<Unit>>()
    val deactivateAccountAlertEvent: LiveData<Event<Unit>> = _deactivateAccountAlertEvent

    fun loadProfile() {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getUser()
            if (result is Success) {
                _user.value = result.data
                name.value = _user.value!!.name
                _dataAvailable.value = true
            } else {
                _user.value = null
                name.value = null
                _dataAvailable.value = false
            }
            _dataLoading.value = false
        }
    }

    fun updateProfileName() {
        if (!hasValidData()) return
        _closeSoftKeyboardEvent.value = Event(Unit)
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.updateProfileName(name.value!!)
            if (result is Success) {
                val message = result.data as String
                _showMessageEvent.value = Event(message)
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
        if (currentName.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_name)
        } else {
            hasValidData = true
        }

        return hasValidData
    }

    fun changePassword() {
        _changePasswordEvent.value = Event(Unit)
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.deleteUser()
            _loginEvent.value = Event(Unit)
        }
    }

    fun showDeactivateAccountAlertDialog() {
        _deactivateAccountAlertEvent.value = Event(Unit)
    }

    fun deactivateAccount() {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.deactivateAccount()
            if (result is Success) {
                val message = result.data as String
                _showMessageEvent.value = Event(message)
            } else {
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

}