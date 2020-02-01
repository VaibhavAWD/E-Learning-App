package com.vaibhavdhunde.app.elearning.data.source.local

import com.vaibhavdhunde.app.elearning.data.Result
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.UsersLocalDataSource
import com.vaibhavdhunde.app.elearning.data.entities.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IUsersLocalDataSource(
    private val usersDao: UsersDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UsersLocalDataSource {

    override suspend fun getUser(): Result<User> = withContext(ioDispatcher) {
        return@withContext try {
            val user = usersDao.getUser()
            if (user != null) {
                Success(user)
            } else {
                Error(Exception("User not found"))
            }
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun saveUser(user: User) {
        withContext(ioDispatcher) {
            usersDao.insertUser(user)
        }
    }

    override suspend fun deleteUser() {
        withContext(ioDispatcher) {
            usersDao.deleteUser()
        }
    }
}