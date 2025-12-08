package com.example.workandstudyapp.ui.todolist.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.data.repository.todo.TaskRepository
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class TaskViewModel(private val taskRepository: TaskRepository): ViewModel() {


    private var _listTaskInDay = MutableStateFlow<List<TaskEntity>>(listOf())
    val listTaskInDay = _listTaskInDay

    private var _checkTaskIn1Week = MutableLiveData<List<NumberTaskInDay>>(listOf())
    val checkTaskIn1Week: LiveData<List<NumberTaskInDay>> =_checkTaskIn1Week

    private var _isState= MutableStateFlow<String>("")
    val isState =_isState

    fun createTask(task: TaskEntity){
        viewModelScope.launch {
            val response=taskRepository.createTask(task)
            if(response){
                _isState.value="Thêm nhiệm vụ thành công!"
            }
            else{
                _isState.value="Insert Fail.."
            }
        }
    }

    fun deleteTask(task: TaskEntity){
        viewModelScope.launch {
            val response=taskRepository.deleteTask(task)
            if(response){
                _isState.value="Xóa thành công!"
            }
            else{
                _isState.value="Delete Fail.."
            }
        }
    }

    fun updateTask(task: TaskEntity){
        viewModelScope.launch {
            val response=taskRepository.updateTask(task)
            if(response){
                _isState.value="Cập nhật thành công!"
            }
            else{
                _isState.value="Update Fail.."
            }
        }
    }

    fun getListTaskInDay(createAt:String){
        viewModelScope.launch {
            taskRepository.getListTaskByDay(createAt)
                .distinctUntilChanged()
                .catch { e-> Log.e("FLOW_ERROR", e.toString()) }
                .collect { taskList->
                    _listTaskInDay.value=taskList
                }
        }
    }

    fun checkTasksIn1Week(startDate:String,endDate:String){
        viewModelScope.launch {
            val response=taskRepository.checkTasksIn1Week(startDate,endDate)
            Log.d("DEBUG_checkTasksIn1Week",response.toString())
            _checkTaskIn1Week.postValue(response)
        }
    }
}