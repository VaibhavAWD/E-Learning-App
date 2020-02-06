package com.vaibhavdhunde.app.elearning.ui.subtopic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.source.MediaSource
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class SubtopicViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _subtopic = MutableLiveData<Subtopic>()
    val subtopic: LiveData<Subtopic> = _subtopic

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    private var playWhenReady: Boolean = true
    private var currentWindow: Int = 0
    private var playbackPosition: Long = 0

    private var mediaSource: MediaSource? = null

    fun loadSubtopic(subtopicId: Long) {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getSubtopic(subtopicId)
            if (result is Success) {
                _subtopic.value = result.data
                _dataAvailable.value = true
            } else {
                _subtopic.value = null
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun getSubtopic(): Subtopic {
        return _subtopic.value!!
    }

    fun setPlayWhenReady(value: Boolean) {
        playWhenReady = value
    }

    fun getPlayWhenReady(): Boolean {
        return playWhenReady
    }

    fun setCurrentWindow(value: Int) {
        currentWindow = value
    }

    fun getCurrentWindow(): Int {
        return currentWindow
    }

    fun setPlaybackPosition(value: Long) {
        playbackPosition = value
    }

    fun getPlaybackPosition(): Long {
        return playbackPosition
    }

    fun setMediaSource(mediaSource: MediaSource) {
        this.mediaSource = mediaSource
    }

    fun getMediaSource(): MediaSource? {
        return mediaSource
    }

}