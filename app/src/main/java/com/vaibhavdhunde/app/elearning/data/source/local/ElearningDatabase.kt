package com.vaibhavdhunde.app.elearning.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vaibhavdhunde.app.elearning.data.entities.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class ElearningDatabase : RoomDatabase() {

    abstract fun usersDao(): UsersDao

    companion object {
        @Volatile
        var INSTANCE: ElearningDatabase? = null

        fun getInstance(context: Context): ElearningDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = buildDatabase(context)
                INSTANCE = instance
                return instance
            }
        }

        private fun buildDatabase(context: Context): ElearningDatabase {
            return Room.databaseBuilder(
                context,
                ElearningDatabase::class.java,
                "elearning.db"
            ).build()
        }
    }
}