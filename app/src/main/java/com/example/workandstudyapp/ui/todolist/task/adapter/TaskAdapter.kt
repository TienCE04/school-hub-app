package com.example.workandstudyapp.ui.todolist.task.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.workandstudyapp.data.local.room.entity.TaskEntity
import com.example.workandstudyapp.databinding.ItemTaskBinding

class TaskAdapter(private val listener: OnClickItemTask): ListAdapter<TaskEntity, TaskViewHolder>(TaskDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {
        val view= ItemTaskBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {
        val task=getItem(position)
        holder.bind(task)
        holder.binding.checkBox.setOnClickListener {
            listener.clickCheckBox(task)
        }
        holder.binding.imgMarkTask.setOnClickListener {
            listener.clickMarkTask(task)
        }
        holder.binding.cvDelete.setOnClickListener {
            listener.clickDeleteTask(task.idTask)
        }
        holder.binding.cvDetail.setOnClickListener {
            listener.clickDetailTask(task)
        }
    }
}
interface OnClickItemTask{
    fun clickCheckBox(task: TaskEntity)
    fun clickMarkTask(task: TaskEntity)
    fun clickDeleteTask(idTask:Int)
    fun clickDetailTask(taskEntity: TaskEntity)

}