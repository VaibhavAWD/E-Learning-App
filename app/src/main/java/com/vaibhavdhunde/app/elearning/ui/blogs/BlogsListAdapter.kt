package com.vaibhavdhunde.app.elearning.ui.blogs

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.vaibhavdhunde.app.elearning.data.entities.Blog

class BlogsListAdapter(
    private val blogsViewModel: BlogsViewModel
) : ListAdapter<Blog, BlogViewHolder>(BLOG_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(parent, blogsViewModel)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val currentBlog = getItem(position)
        currentBlog?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val BLOG_COMPARATOR = object : DiffUtil.ItemCallback<Blog>() {
            override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
                return oldItem == newItem
            }
        }
    }
}