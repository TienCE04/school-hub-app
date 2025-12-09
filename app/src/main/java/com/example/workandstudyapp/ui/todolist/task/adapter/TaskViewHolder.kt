package com.example.workandstudyapp.ui.todolist.task.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.databinding.ItemTaskBinding
import androidx.core.graphics.toColorInt

class TaskViewHolder(val binding: ItemTaskBinding): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(task: TaskEntity){
        binding.tvNameTask.text=task.title
        binding.tvTime.text="[${task.timeStart}]"
        binding.checkBox.isChecked= task.completed
        binding.imgMarkTask.imageTintList=if(task.flag) ColorStateList.valueOf("#26A7E6".toColorInt()) else ColorStateList.valueOf(
            "#FF000000".toColorInt())
    }
}