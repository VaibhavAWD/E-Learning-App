package com.vaibhavdhunde.app.elearning.ui.subtopics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.entities.Subtopic
import com.vaibhavdhunde.app.elearning.databinding.ItemSubtopicBinding

class SubtopicViewHolder(
    private val itemSubtopicBinding: ItemSubtopicBinding,
    private val subtopicsViewModel: SubtopicsViewModel
) : RecyclerView.ViewHolder(itemSubtopicBinding.root) {

    private val listener = object : SubtopicsListUserActionsListener {
        override fun onClickSubtopic(subtopic: Subtopic) {
            subtopicsViewModel.openSubtopic(subtopic.id)
        }
    }

    fun bind(subtopic: Subtopic) {
        itemSubtopicBinding.subtopic = subtopic
        itemSubtopicBinding.listener = listener
        itemSubtopicBinding.executePendingBindings()
    }

    companion object {
        operator fun invoke(
            parent: ViewGroup,
            subtopicsViewModel: SubtopicsViewModel
        ): SubtopicViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subtopic, parent, false)
            val binding = ItemSubtopicBinding.bind(view)
            return SubtopicViewHolder(binding, subtopicsViewModel)
        }
    }

}