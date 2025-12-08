package com.example.workandstudyapp.data.local.room.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tasks",
    indices = [Index(value = ["createAt"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val idTask:Int,
    val title:String,
    val content:String,
    val timeStart:String,
    val createAt:String,
    val completed: Boolean=false,
    val flag: Boolean=false
) {
}
//index cho createAt