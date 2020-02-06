package com.vaibhavdhunde.app.elearning.ui.blogdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vaibhavdhunde.app.elearning.data.ElearningRepository
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Blog
import com.vaibhavdhunde.app.elearning.util.Event
import kotlinx.coroutines.launch

class BlogDetailsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _blog = MutableLiveData<Blog>()
    val blog: LiveData<Blog> = _blog

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    fun loadBlog(blogId: Long) {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getBlog(blogId)
            if (result is Success) {
                _blog.value = result.data
                _dataAvailable.value = true
            } else {
                _blog.value = null
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

}