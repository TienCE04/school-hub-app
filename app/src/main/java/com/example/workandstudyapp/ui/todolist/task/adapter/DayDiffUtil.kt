package com.example.workandstudyapp.ui.todolist.task.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay

class DayDiffUtil : DiffUtil.ItemCallback<NumberTaskInDay>() {
    override fun areItemsTheSame(
        oldItem: NumberTaskInDay,
        newItem: NumberTaskInDay
    ): Boolean {
        return oldItem.createAt == newItem.createAt
    }

    override fun areContentsTheSame(
        oldItem: NumberTaskInDay,
        newItem: NumberTaskInDay
    ): Boolean {
        return oldItem == newItem
    }
}