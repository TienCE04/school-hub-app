package com.example.workandstudyapp.ui.todolist.task

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.data.local.room.AppDatabase
import com.example.workandstudyapp.data.local.room.dao.TaskDao
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.data.repository.todo.TaskRepository
import com.example.workandstudyapp.databinding.FragmentAddTaskBinding
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskFactory
import com.example.workandstudyapp.ui.todolist.viewmodel.TaskViewModel
import java.util.Calendar
import java.util.Locale

class FragmentAddTask: Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentAddTaskBinding
    private val binding get() = _binding

    private lateinit var taskRepo: TaskRepository
    private lateinit var taskDao: TaskDao
    private lateinit var taskViewModel: TaskViewModel
    private var selectedDay=""
    private lateinit var timePicker: TimePickerDialog

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

        selectedDay= arguments?.getString("daySelected","").toString()
        taskDao = AppDatabase.getDatabase(requireContext()).taskDao()

        setUpTime()
        initViewModel()
        initListener()
    }
    private fun initViewModel(){
        taskRepo= TaskRepository(taskDao)
        taskViewModel= ViewModelProvider(requireActivity(), TaskFactory(taskRepo))[TaskViewModel::class.java]
    }

    private fun initListener(){
        binding.tvCancel.setOnClickListener(this)
        binding.tvAdd.setOnClickListener(this)
        binding.imgClock.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.tv_add->{
                val title=binding.tieudeWork.text.toString()
                val content=binding.ghichuWork.text.toString()
                var time=binding.timeWork.text.toString()
                if(checkInputAddTask(title,content,time)){
                    if(checkTimeTask(time)){
                        time=convertTime(time)
                        val task= TaskEntity(0,title,content,time,selectedDay)
                        taskViewModel.createTask(task)
                        findNavController().popBackStack()
                    }else{
                        Toast.makeText(requireContext(),"Vui lòng nhập đúng định dạng thời gian HH:MM",
                            Toast.LENGTH_SHORT).show()
                    }

                }
                else{
                    Toast.makeText(requireContext(),"Vui lòng nhập đầy đủ thông tin task!", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.tv_cancel->{
                findNavController().popBackStack()
            }
            R.id.img_clock->{
                timePicker.show()
            }
        }
    }


    private fun setUpTime(){
        val now = Calendar.getInstance()
        timePicker= TimePickerDialog(requireContext(),{_,hourOfDay,minute->
            val formatted = String.format(Locale.getDefault(),"%02d:%02d", hourOfDay, minute)
            binding.timeWork.setText(formatted)
        },now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),true)
    }

}