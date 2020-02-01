package com.vaibhavdhunde.app.elearning.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun loadUser() {
        viewModelScope.launch {
            val result = repository.getUser()
            if (result is Success) {
                _user.value = result.data
            }
        }
    }

}