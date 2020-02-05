package com.vaibhavdhunde.app.elearning.ui.subjects

import androidx.lifecycle.*
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class SubjectsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> = _subjects

    val empty: LiveData<Boolean> = Transformations.map(_subjects) {
        it.isEmpty()
    }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _showMessageEvent = MutableLiveData<Event<Any>>()
    val showMessageEvent: LiveData<Event<Any>> = _showMessageEvent

    private val _topicEvent = MutableLiveData<Event<Long>>()
    val topicEvent: LiveData<Event<Long>> = _topicEvent

    fun loadSubjects(forceUpdate: Boolean = false) {
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getSubjects(forceUpdate)
            if (result is Success) {
                _subjects.value = result.data
            } else {
                _subjects.value = emptyList()
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun openTopic(topicId: Long) {
        _topicEvent.value = Event(topicId)
    }

}