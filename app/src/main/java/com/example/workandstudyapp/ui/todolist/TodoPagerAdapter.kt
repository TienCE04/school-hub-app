package com.example.workandstudyapp.ui.todolist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workandstudyapp.ui.todolist.habit.FragmentHabit
import com.example.workandstudyapp.ui.todolist.history.FragmentHistory
import com.example.workandstudyapp.ui.todolist.task.FragmentTask

class TodoPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentTask()
            1-> FragmentHabit()
            2-> FragmentHistory()
            else -> FragmentTask()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}