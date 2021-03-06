package com.vaibhavdhunde.app.elearning.ui.topics

import androidx.lifecycle.*
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class TopicsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _topics = MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> = _topics

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    private val _subtopicsEvent = MutableLiveData<Event<Long>>()
    val subtopicsEvent: LiveData<Event<Long>> = _subtopicsEvent

    fun loadTopics(subjectId: Long) {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getTopics(subjectId)
            if (result is Success) {
                _topics.value = result.data
                _dataAvailable.value = true
            } else {
                _topics.value = emptyList()
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun openSubtopics(topicId: Long) {
        _subtopicsEvent.value = Event(topicId)
    }

}