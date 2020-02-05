package com.vaibhavdhunde.app.elearning.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

@BindingAdapter("app:url")
fun setImage(iv: ImageView, url: String) {
    Glide.with(iv).load(url).into(iv)
}