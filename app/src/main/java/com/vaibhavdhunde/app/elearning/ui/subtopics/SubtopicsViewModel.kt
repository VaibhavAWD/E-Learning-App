package com.vaibhavdhunde.app.elearning.ui.subtopics

import androidx.lifecycle.*
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class SubtopicsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _subtopics = MutableLiveData<List<Subtopic>>()
    val subtopics: LiveData<List<Subtopic>> = _subtopics

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    private val _subtopicEvent = MutableLiveData<Event<Long>>()
    val subtopicEvent: LiveData<Event<Long>> = _subtopicEvent

    fun loadSubtopics(topicId: Long) {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getSubtopics(topicId)
            if (result is Success) {
                _subtopics.value = result.data
                _dataAvailable.value = true
            } else {
                _subtopics.value = emptyList()
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun openSubtopic(subtopicId: Long) {
        _subtopicEvent.value = Event(subtopicId)
    }

}