package com.vaibhavdhunde.app.elearning.ui.subtopics

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic

class SubtopicsListAdapter(
    private val subtopicsViewModel: SubtopicsViewModel
) : ListAdapter<Subtopic, SubtopicViewHolder>(SUBTOPIC_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtopicViewHolder {
        return SubtopicViewHolder(parent, subtopicsViewModel)
    }

    override fun onBindViewHolder(holder: SubtopicViewHolder, position: Int) {
        val currentSubtopic = getItem(position)
        currentSubtopic?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val SUBTOPIC_COMPARATOR = object : DiffUtil.ItemCallback<Subtopic>() {
            override fun areItemsTheSame(oldItem: Subtopic, newItem: Subtopic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Subtopic, newItem: Subtopic): Boolean {
                return oldItem == newItem
            }
        }
    }
}