package com.example.workandstudyapp.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(taskEntity: TaskEntity):Long

    @Query("SELECT * FROM tasks")
    fun getAllTask(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE createAt = :createAt")
    fun getListTaskByDay(createAt:String): Flow<List<TaskEntity>>

    @Delete
    suspend fun deleteTask(task: TaskEntity): Int

    @Update
    suspend fun updateTask(task: TaskEntity): Int

    @Query("UPDATE tasks SET content = :newContent, timeStart = :timeStart WHERE idTask = :taskId")
    suspend fun updateContentTask(taskId:Int,newContent:String,timeStart:String):Int

    //truy vấn nhiệm vụ quá hạn, đã hoàn thành trước ngày hiện tại
    @Query("SELECT * FROM tasks WHERE createAt = :createAt AND completed ='false'")
    fun getTasksIncompleted(createAt: String): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE createAt = :createAt AND completed ='true'")
    fun getTasksCompleted(createAt: String): Flow<List<TaskEntity>>

    @Query("SELECT createAt, COUNT(*) as count FROM tasks WHERE createAt BETWEEN :start AND :end GROUP BY createAt")
    suspend fun getTask1Week(start: String, end: String): List<NumberTaskInDay>

}