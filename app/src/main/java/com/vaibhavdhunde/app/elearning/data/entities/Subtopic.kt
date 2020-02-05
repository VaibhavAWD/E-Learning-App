package com.vaibhavdhunde.app.elearning.data.entities

data class Subtopic(
    val id: Long,
    val topic_id: Long,
    val title: String,
    val body: String,
    val url: String,
    val thumbnail: String,
    val time: String
)