package com.example.workandstudyapp.ui.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class TodoViewModel: ViewModel() {

    private var _selectedDay= MutableLiveData<String>(LocalDate.now().toString())
    val selectedDay: LiveData<String> =_selectedDay

    fun clickItemDay(day:String){
        _selectedDay.value=day
    }
}