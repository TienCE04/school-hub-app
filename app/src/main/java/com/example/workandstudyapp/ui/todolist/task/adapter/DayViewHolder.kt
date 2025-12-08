package com.example.workandstudyapp.ui.todolist.task.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.ItemCalendarCellBinding
import com.example.workandstudyapp.ui.todolist.data.NumberTaskInDay
import java.time.LocalDate

class DayViewHolder(val binding: ItemCalendarCellBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(day: NumberTaskInDay,isSelected: Boolean){
        val parts = day.createAt.split("-")
        if(isSelected){
            binding.cellDayText.setTextColor(itemView.context.getColor(R.color.blue))
        }
        else{
            binding.cellDayText.setTextColor(itemView.context.getColor(R.color.black))
        }
        if(day.createAt== LocalDate.now().toString()){
            binding.cellDayText.setTextColor(itemView.context.getColor(R.color.red))
        }
        binding.cellDayText.text= parts[2]
        binding.dotIndicator.visibility=if (day.count>0) View.VISIBLE else View.GONE
    }
}