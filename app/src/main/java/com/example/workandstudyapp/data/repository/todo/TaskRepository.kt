package com.example.workandstudyapp.data.repository.todo

import android.database.sqlite.SQLiteException
import android.util.Log
import com.example.workandstudyapp.data.local.room.dao.TaskDao
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {
    suspend fun createTask(task: TaskEntity): Boolean {
        try {
            return taskDao.createTask(task) > 0
        } catch (e: SQLiteException) {
            Log.d("DEBUG_CREATE_TASK", "DB error: ${e.message}", e)
            return false
        } catch (e: Exception) {
            Log.d("DEBUG_CREATE_TASK", "Error: ${e.message}", e)
            return false
        }
    }

    fun getListTaskByDay(createAt: String): Flow<List<TaskEntity>> {
        try {
            return taskDao.getListTaskByDay(createAt)
        } catch (e: Exception) {
            Log.d("DEBUG_GET_TASK", "Error: ${e.message}", e)
            throw e
        }
    }


    suspend fun updateTask(task: TaskEntity): Boolean {
        try {
            return taskDao.updateTask(task) > 0
        } catch (e: Exception) {
            Log.d("DEBUG_UPDATE_TASK", "Error: ${e.message}", e)
            return false
        }
    }

    suspend fun updateContentAndTime(idTask: Int,title:String, content: String, timeStart: String): Boolean {
        try {
            return taskDao.updateContentTask(idTask,title, content, timeStart) > 0
        } catch (e: Exception) {
            Log.d("DEBUG_UPDATE_CONTENT_TASK", "Error: ${e.message}", e)
            return false
        }
    }

    suspend fun deleteTask(idTask: Int): Boolean {
        return taskDao.deleteTaskById(idTask) > 0
    }

    fun getTasksIncompleted(createAt:String): Flow<List<TaskEntity>>{
        try {
            return taskDao.getTasksIncompleted(createAt)
        }
        catch (e: Exception){
            Log.d("DEBUG_GET_TASK_INCOMPLETED", "Error: ${e.message}", e)
            throw e
        }
    }
    fun getTasksCompleted(createAt:String): Flow<List<TaskEntity>>{
        try {
            return taskDao.getTasksCompleted(createAt)
        }
        catch (e: Exception){
            Log.d("DEBUG_GET_TASK_COMPLETED", "Error: ${e.message}", e)
            throw e
        }
    }

    suspend fun checkTasksIn1Week(startDate:String,endDate:String): List<NumberTaskInDay>{
        try {
            return taskDao.getTask1Week(startDate,endDate)
        }
        catch (e: Exception){
            throw e
        }
    }
}