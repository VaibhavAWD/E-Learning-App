package com.vaibhavdhunde.app.elearning.ui.subjects

import androidx.lifecycle.*
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SubjectsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> = _subjects

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    private val _topicsEvent = MutableLiveData<Event<Long>>()
    val topicsEvent: LiveData<Event<Long>> = _topicsEvent

    fun loadSubjects() {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getSubjects()
            if (result is Success) {
                _subjects.value = result.data
                _dataAvailable.value = true
            } else {
                _subjects.value = emptyList()
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun openTopics(subjectId: Long) {
        _topicsEvent.value = Event(subjectId)
    }

}