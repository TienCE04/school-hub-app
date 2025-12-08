package com.example.workandstudyapp.ui.todolist.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.workandstudyapp.data.local.room.AppDatabase
import com.example.workandstudyapp.data.local.room.dao.TaskDao
import com.example.workandstudyapp.data.repository.todo.TaskRepository
import com.example.workandstudyapp.databinding.FragmentAddTaskBinding
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskFactory
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskViewModel

class FragmentAddTask: Fragment() {

    private lateinit var _binding: FragmentAddTaskBinding
    private val binding get() = _binding

    private lateinit var taskRepo: TaskRepository
    private lateinit var taskDao: TaskDao
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentAddTaskBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskDao= AppDatabase.getDatabase(requireContext()).taskDao()
        initViewModel()
    }
    private fun initViewModel(){
        taskRepo= TaskRepository(taskDao)
        taskViewModel= ViewModelProvider(requireActivity(), TaskFactory(taskRepo))[TaskViewModel::class.java]
    }

}