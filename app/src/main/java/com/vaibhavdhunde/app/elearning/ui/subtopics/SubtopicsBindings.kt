package com.vaibhavdhunde.app.elearning.ui.subtopics

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic

object SubtopicsBindings {

    @JvmStatic
    @BindingAdapter("app:subtopics")
    fun setTopics(rv: RecyclerView, subtopics: List<Subtopic>?) {
        with(rv.adapter as SubtopicsListAdapter) {
            subtopics?.let { submitList(it) }
        }
    }
}