package com.vaibhavdhunde.app.elearning.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vaibhavdhunde.app.elearning.data.entities.CURRENT_USER_ID
import com.vaibhavdhunde.app.elearning.data.entities.User

@Dao
interface UsersDao {

    @Query("SELECT * FROM users WHERE uid = $CURRENT_USER_ID")
    suspend fun getUser(): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("DELETE FROM users WHERE uid = $CURRENT_USER_ID")
    suspend fun deleteUser(): Int

}