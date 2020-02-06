package com.vaibhavdhunde.app.elearning.ui.blogs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vaibhavdhunde.app.elearning.R
import com.vaibhavdhunde.app.elearning.data.entities.Blog
import com.vaibhavdhunde.app.elearning.databinding.ItemBlogBinding

class BlogViewHolder(
    private val itemBlogBinding: ItemBlogBinding,
    private val blogsViewModel: BlogsViewModel
) : RecyclerView.ViewHolder(itemBlogBinding.root) {

    private val listener = object : BlogsListUserActionsListener {
        override fun onClickBlog(blog: Blog) {
            blogsViewModel.openBlog(blog.id)
        }
    }

    fun bind(blog: Blog) {
        itemBlogBinding.blog = blog
        itemBlogBinding.listener = listener
        itemBlogBinding.executePendingBindings()
    }

    companion object {
        operator fun invoke(
            parent: ViewGroup,
            blogsViewModel: BlogsViewModel
        ): BlogViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_blog, parent, false)
            val binding = ItemBlogBinding.bind(view)
            return BlogViewHolder(binding, blogsViewModel)
        }
    }

}