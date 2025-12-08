package com.example.workandstudyapp.data.local.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.workandstudyapp.data.local.room.dao.TaskDao
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import android.content.Context

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}