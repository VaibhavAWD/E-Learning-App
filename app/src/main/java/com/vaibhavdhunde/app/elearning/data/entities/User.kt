package com.vaibhavdhunde.app.elearning.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_USER_ID = 0

@Entity(tableName = "users")
data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password_hash: String,
    val api_key: String,
    val created_at: String,
    val status: Int
) {
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}