package com.example.workandstudyapp.ui.todolist.task.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.workandstudyapp.data.local.room.entity.TaskEntity

class TaskDiffUtil: DiffUtil.ItemCallback<TaskEntity>(){
    override fun areItemsTheSame(
        oldItem: TaskEntity,
        newItem: TaskEntity
    ): Boolean {
        return oldItem.idTask==newItem.idTask
    }

    override fun areContentsTheSame(
        oldItem: TaskEntity,
        newItem: TaskEntity
    ): Boolean {
        return oldItem==newItem
    }
}