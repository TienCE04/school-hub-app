package com.example.workandstudyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentContainerHomeBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class FragmentContainerHome : Fragment() {

    private lateinit var _binding: FragmentContainerHomeBinding
    private val binding get() = _binding
    private lateinit var adapterPager: HomePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContainerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPager()
        setupTabLayout()
//        setupBottomNav()
    }

    private fun setupTabLayout(){
        TabLayoutMediator(binding.tabLayoutBottom,binding.viewPager2){tab,position->
            when(position){
                0->{
                    tab.text="Tin nhắn"
                    tab.setIcon(R.drawable.icon_chat)
                }
                1->{
                    tab.text="Home"
                    tab.setIcon(R.drawable.icon_home)
                }
                2->{
                    tab.text="Thông báo"
                    tab.setIcon(R.drawable.icon_notification)
                }
                else->{
                    tab.text="Home"
                    tab.setIcon(R.drawable.icon_home)
                }
            }
        }.attach()
    }
    private fun setupViewPager() {
        adapterPager = HomePagerAdapter(this)
        binding.viewPager2.adapter = adapterPager

        //lưu để lướt mượt hơn
        binding.viewPager2.offscreenPageLimit = 3
        //tab đầu tiên là tab home
        binding.viewPager2.setCurrentItem(1,false)
        binding.viewPager2.isUserInputEnabled=false

        //khi lướt cập nhật bottom navigation
//        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                binding.bottomNavi.menu[position].isChecked = true
//            }
//        })
        //thay thành tab layout
        binding.tabLayoutBottom.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager2.setCurrentItem(tab.position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun setupBottomNav(){
        binding.bottomNavi.setOnItemSelectedListener { item->
            when(item.itemId){
                R.id.home->binding.viewPager2.setCurrentItem(1,true)
                R.id.message->binding.viewPager2.setCurrentItem(0,true)
                R.id.notification->binding.viewPager2.setCurrentItem(2,true)
            }
            true
        }
    }

}