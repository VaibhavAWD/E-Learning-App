package com.vaibhavdhunde.app.elearning.ui.subjects

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.databinding.ItemSubjectBinding

class SubjectViewHolder(
    private val itemSubjectBinding: ItemSubjectBinding,
    private val subjectsViewModel: SubjectsViewModel
) : RecyclerView.ViewHolder(itemSubjectBinding.root) {

    private val listener = object : SubjectsListUserActionsListener {
        override fun onClickSubject(subject: Subject) {
            subjectsViewModel.openTopics(subject.id)
        }
    }

    fun bind(subject: Subject) {
        itemSubjectBinding.subject = subject
        itemSubjectBinding.listener = listener
        itemSubjectBinding.executePendingBindings()
    }

    companion object {
        operator fun invoke(
            parent: ViewGroup,
            subjectsViewModel: SubjectsViewModel
        ): SubjectViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subject, parent, false)
            val binding = ItemSubjectBinding.bind(view)
            return SubjectViewHolder(binding, subjectsViewModel)
        }
    }

}