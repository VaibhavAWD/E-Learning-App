package com.vaibhavdhunde.app.elearning.util

import com.vaibhavdhunde.app.elearning.data.FakeElearningRepository
import com.vaibhavdhunde.app.elearning.data.entities.User
import kotlinx.coroutines.runBlocking

fun FakeElearningRepository.saveUserBlocking(user: User) = runBlocking {
    this@saveUserBlocking.saveUser(user)
}