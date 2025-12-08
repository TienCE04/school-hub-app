package com.example.workandstudyapp.ui.todolist.task.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.workandstudyapp.databinding.ItemCalendarCellBinding
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay

class DayAdapter(private val listener: OnClickInFragmentTask): ListAdapter<NumberTaskInDay, DayViewHolder>(DayDiffUtil()) {
    private var selectedDate: String? = null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DayViewHolder {
        val view= ItemCalendarCellBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DayViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(
        holder: DayViewHolder,
        position: Int
    ) {
        val day = getItem(position)

        holder.bind(day, day.createAt == selectedDate)

        holder.itemView.setOnClickListener {
            Log.d("DEBUG_CLICK_ITEM_DAY","click: ${day.createAt}")
            selectedDate = day.createAt
            listener.onClickDay(day.createAt)
            notifyDataSetChanged()
        }
    }
}
interface OnClickInFragmentTask{
    fun onClickDay(day:String)
}