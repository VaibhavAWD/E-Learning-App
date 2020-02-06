package com.vaibhavdhunde.app.elearning.ui.blogs

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

class BlogsViewModel(private val repository: ElearningRepository) : ViewModel() {

    private val _blogs = MutableLiveData<List<Blog>>()
    val blogs: LiveData<List<Blog>> = _blogs

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _dataAvailable = MutableLiveData<Boolean>()
    val dataAvailable: LiveData<Boolean> = _dataAvailable

    private val _showMessageEvent = MutableLiveData<Event<String>>()
    val showMessageEvent: LiveData<Event<String>> = _showMessageEvent

    private val _blogEvent = MutableLiveData<Event<Long>>()
    val blogEvent: LiveData<Event<Long>> = _blogEvent

    fun loadBlogs() {
        _dataLoading.value?.let { isLoading ->
            if (isLoading) return
        }
        _dataAvailable.value?.let { isAvailable ->
            if (isAvailable) return
        }
        _dataLoading.value = true
        viewModelScope.launch {
            val result = repository.getBlogs()
            if (result is Success) {
                _blogs.value = result.data
                _dataAvailable.value = true
            } else {
                _blogs.value = emptyList()
                _dataAvailable.value = false
                val error = (result as Error).exception.message as String
                _showMessageEvent.value = Event(error)
            }
            _dataLoading.value = false
        }
    }

    fun openBlog(blogId: Long) {
        _blogEvent.value = Event(blogId)
    }

}