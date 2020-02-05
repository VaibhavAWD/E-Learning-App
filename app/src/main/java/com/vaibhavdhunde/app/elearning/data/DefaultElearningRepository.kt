package com.vaibhavdhunde.app.elearning.data

import com.vaibhavdhunde.app.elearning.api.ApiException
import com.vaibhavdhunde.app.elearning.api.NetworkException
import com.vaibhavdhunde.app.elearning.data.Result.Error
import com.vaibhavdhunde.app.elearning.data.Result.Success
import com.vaibhavdhunde.app.elearning.data.entities.Subject
import com.vaibhavdhunde.app.elearning.data.entities.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultElearningRepository(
    private val usersLocalDataSource: UsersLocalDataSource,
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val subjectsRemoteDataSource: SubjectsRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElearningRepository {

    private var cachedUser: User? = null

    private var cachedSubjects: List<Subject>? = null

    override suspend fun loginUser(email: String, password: String): Result<*> {
        return withContext(ioDispatcher) {
            try {
                val response = usersRemoteDataSource.loginUser(email, password)
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    saveUser(response.user!!)
                    return@withContext Success(Unit)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun registerUser(name: String, email: String, password: String): Result<*> {
        return withContext(ioDispatcher) {
            try {
                val response = usersRemoteDataSource.registerUser(name, email, password)
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    saveUser(response.user!!)
                    return@withContext Success(Unit)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun updateProfileName(name: String): Result<*> {
        return withContext(ioDispatcher) {
            try {
                val response = usersRemoteDataSource.updateProfileName(name, cachedUser!!.api_key)
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    cachedUser!!.name = name
                    saveUser(cachedUser!!)
                    return@withContext Success(response.message)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun updatePassword(password: String, newPassword: String): Result<*> {
        return withContext(ioDispatcher) {
            try {
                val response = usersRemoteDataSource.updatePassword(
                    password,
                    newPassword,
                    cachedUser!!.api_key
                )
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    return@withContext Success(response.message)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun deactivateAccount(): Result<*> {
        return withContext(ioDispatcher) {
            try {
                val response = usersRemoteDataSource.deactivateAccount(cachedUser!!.api_key)
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    return@withContext Success(response.message)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun getSubjects(forceUpdate: Boolean): Result<List<Subject>> {
        return withContext(ioDispatcher) {
            if (!forceUpdate) {
                if (cachedSubjects != null) {
                    return@withContext Success(cachedSubjects!!)
                }
            }
            try {
                val response = subjectsRemoteDataSource.getSubjects()
                if (response.error) {
                    return@withContext Error(Exception(response.message))
                } else {
                    val subjects = response.subjects
                    cachedSubjects?.toMutableList()?.clear()
                    cachedSubjects = subjects
                    return@withContext Success(cachedSubjects!!)
                }
            } catch (e: NetworkException) {
                return@withContext Error(e)
            } catch (e: ApiException) {
                return@withContext Error(e)
            }
        }
    }

    override suspend fun getUser(): Result<User> = withContext(ioDispatcher) {
        if (cachedUser != null) {
            return@withContext Success(cachedUser!!)
        }

        val user = usersLocalDataSource.getUser()

        (user as? Success)?.let { cachedUser = it.data }

        return@withContext user
    }

    override suspend fun saveUser(user: User) {
        cachedUser = user
        withContext(ioDispatcher) {
            usersLocalDataSource.saveUser(user)
        }
    }

    override suspend fun deleteUser() {
        cachedUser = null
        withContext(ioDispatcher) {
            usersLocalDataSource.deleteUser()
        }
    }
}