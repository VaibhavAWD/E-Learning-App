package com.vaibhavdhunde.app.elearning.ui.subjects

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.util.ScrollChildSwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_subjects.view.*

object SubjectsBindings {

    @JvmStatic
    @BindingAdapter("app:subjects")
    fun setSubjects(rv: RecyclerView, subjects: List<Subject>?) {
        with(rv.adapter as SubjectsListAdapter) {
            subjects?.let { submitList(it) }
        }
    }
}