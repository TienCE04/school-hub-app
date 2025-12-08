package com.example.workandstudyapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentHomeBinding
import com.example.workandstudyapp.utils.NavOption

class FragmentHome: Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        setupNavigationView()
    }

    private fun initListener(){
        binding.content.imgSchedule.setOnClickListener(this)
        binding.content.imgAvt.setOnClickListener(this)
        binding.content.imgTodo.setOnClickListener(this)
    }

    private fun setupNavigationView(){
        binding.navHome.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                // xử lý items
            }

            binding.drawer.closeDrawers()
            true
        }
        //header in navigation,
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.img_schedule->{
                findNavController().navigate(R.id.action_homeContainerFragment_to_scheduleFragment,null,
                    NavOption.animationFragment)
            }
            R.id.img_avt->{
                binding.drawer.openDrawer(GravityCompat.START)
            }
            R.id.img_todo->{
                findNavController().navigate(R.id.action_homeContainerFragment_to_fragmentTodo,null,
                    NavOption.animationFragment)
            }
        }
    }
}