package com.example.workandstudyapp.ui.todolist.task.adapter

import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.databinding.ItemTaskBinding

class TaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(task: TaskEntity){
        binding.tvNameTask.text=task.title
        binding.tvTime.text="[${task.timeStart}]"
        binding.checkBox.isChecked= task.completed
        binding.imgMarkTask.imageTintList=if(task.flag) ColorStateList.valueOf("#26A7E6".toInt()) else ColorStateList.valueOf("#FF000000".toInt())
    }
}