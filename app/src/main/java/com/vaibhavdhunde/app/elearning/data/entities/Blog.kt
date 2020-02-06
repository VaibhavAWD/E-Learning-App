package com.vaibhavdhunde.app.elearning.data.entities

data class Blog(
    val id: Long,
    val user_id: Long,
    val title: String,
    val body: String,
    val image_url: String,
    val created_at: String
)