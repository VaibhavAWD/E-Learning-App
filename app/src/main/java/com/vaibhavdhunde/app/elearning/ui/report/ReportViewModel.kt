package com.vaibhavdhunde.app.elearning.ui.report

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

class ReportViewModel(private val repository: ElearningRepository) : ViewModel() {

    // Two-way DataBinding
    val message = MutableLiveData<String>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    private val _closeSoftKeyboardEvent = MutableLiveData<Event<Unit>>()
    val closeSoftKeyboardEvent: LiveData<Event<Unit>> = _closeSoftKeyboardEvent

    fun sendReport() {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        if (!hasValidData()) return
        _closeSoftKeyboardEvent.value = Event(Unit)
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.sendReport(message.value!!)
            if (result is Success) {
                val message = result.data
                _showMessageEvent.value = Event(message)
                this@ReportViewModel.message.value = null
            } else {
                val error = (result as Error).exception.message!!
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    private fun hasValidData(): Boolean {
        var hasValidData = false

        val currentMessage = message.value

        if (currentMessage.isNullOrEmpty()) {
            _showMessageEvent.value = Event(R.string.error_empty_message)
        } else {
            hasValidData = true
        }

        return hasValidData
    }

}