package com.vaibhavdhunde.app.elearning.ui.topics

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.data.entities.Topic

object TopicsBindings {

    @JvmStatic
    @BindingAdapter("app:topics")
    fun setTopics(rv: RecyclerView, topics: List<Topic>?) {
        with(rv.adapter as TopicsListAdapter) {
            topics?.let { submitList(it) }
        }
    }
}