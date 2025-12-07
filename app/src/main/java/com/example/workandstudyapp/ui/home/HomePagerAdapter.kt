package com.example.workandstudyapp.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.workandstudyapp.ui.message.FragmentMessenger
import com.example.workandstudyapp.ui.notification.FragmentNotification

class HomePagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when(position){
            0-> FragmentMessenger()
            1-> FragmentHome()
            2-> FragmentNotification()
            else-> FragmentHome()
        }
    }

    override fun getItemCount(): Int {
       return 3
    }
}