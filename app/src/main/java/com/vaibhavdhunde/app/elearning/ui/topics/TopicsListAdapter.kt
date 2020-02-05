package com.vaibhavdhunde.app.elearning.ui.topics

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vaibhavdhunde.app.elearning.data.entities.Topic

class TopicsListAdapter(
    private val topicsViewModel: TopicsViewModel
) : ListAdapter<Topic, TopicViewHolder>(TOPIC_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        return TopicViewHolder(parent, topicsViewModel)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val currentTopic = getItem(position)
        currentTopic?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val TOPIC_COMPARATOR = object : DiffUtil.ItemCallback<Topic>() {
            override fun areItemsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Topic, newItem: Topic): Boolean {
                return oldItem == newItem
            }
        }
    }
}