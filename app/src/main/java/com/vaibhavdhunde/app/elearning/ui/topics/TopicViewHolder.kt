package com.vaibhavdhunde.app.elearning.ui.topics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.entities.Topic
import com.vaibhavdhunde.app.elearning.databinding.ItemTopicBinding

class TopicViewHolder(
    private val itemTopicBinding: ItemTopicBinding,
    private val topicsViewModel: TopicsViewModel
) : RecyclerView.ViewHolder(itemTopicBinding.root) {

    private val listener = object : TopicsListUserActionsListener {
        override fun onClickTopic(topic: Topic) {
            topicsViewModel.openSubtopics(topic.id)
        }
    }

    fun bind(topic: Topic) {
        itemTopicBinding.topic = topic
        itemTopicBinding.listener = listener
        itemTopicBinding.executePendingBindings()
    }

    companion object {
        operator fun invoke(
            parent: ViewGroup,
            topicsViewModel: TopicsViewModel
        ): TopicViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_topic, parent, false)
            val binding = ItemTopicBinding.bind(view)
            return TopicViewHolder(binding, topicsViewModel)
        }
    }

}