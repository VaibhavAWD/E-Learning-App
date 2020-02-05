package com.vaibhavdhunde.app.elearning.ui.subjects

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vaibhavdhunde.app.elearning.data.entities.Subject

class SubjectsListAdapter(
    private val subjectsViewModel: SubjectsViewModel
) : ListAdapter<Subject, SubjectViewHolder>(SUBJECT_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        return SubjectViewHolder(parent, subjectsViewModel)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val currentSubject = getItem(position)
        currentSubject?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val SUBJECT_COMPARATOR = object : DiffUtil.ItemCallback<Subject>() {
            override fun areItemsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem.id == newItem.id
            }

            override fun equals(other: Any?): Boolean {
                return super.equals(other)
            }

            override fun areContentsTheSame(oldItem: Subject, newItem: Subject): Boolean {
                return oldItem == newItem
            }
        }
    }
}