package com.vaibhavdhunde.app.elearning.ui.blogs

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.data.entities.Blog

object BlogsBindings {

    @JvmStatic
    @BindingAdapter("app:blogs")
    fun setBlogs(rv: RecyclerView, blogs: List<Blog>?) {
        with(rv.adapter as BlogsListAdapter) {
            blogs?.let { submitList(it) }
        }
    }
}