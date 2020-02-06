package com.vaibhavdhunde.app.elearning.ui.blogs

import com.vaibhavdhunde.app.elearning.data.entities.Blog

interface BlogsListUserActionsListener {

    fun onClickBlog(blog: Blog)

}