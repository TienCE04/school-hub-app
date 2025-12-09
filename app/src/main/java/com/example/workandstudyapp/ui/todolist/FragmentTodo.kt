package com.example.workandstudyapp.ui.todolist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentTodoBinding
import com.example.workandstudyapp.ui.todolist.task.adapter.OnClickInFragmentTask
import com.example.workandstudyapp.utils.NavOption
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate

class FragmentTodo: Fragment(), View.OnClickListener{

    private lateinit var _binding: FragmentTodoBinding
    private val binding get() = _binding
    private lateinit var pagerAdapter: TodoPagerAdapter
    private val todoViewModel: TodoViewModel by viewModels()
    var daySelected=""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentTodoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserve()
        setupViewPager()
        setupTabLayout()
        initListener()
        handleBtnBackPhone()
    }


    private fun setupViewPager(){
        pagerAdapter= TodoPagerAdapter(this)
        binding.viewPager2.adapter=pagerAdapter

        binding.viewPager2.offscreenPageLimit=3
    }
    private fun setupTabLayout(){
        TabLayoutMediator(binding.tabLayout,binding.viewPager2){tab,position->
            when(position){
                0->{
                    tab.text= requireContext().getString(com.example.workandstudyapp.R.string.task)
                }
                1->{
                    tab.text=requireContext().getString(com.example.workandstudyapp.R.string.habit)
                }
                2->{
                    tab.text=requireContext().getString(com.example.workandstudyapp.R.string.history)
                }
            }
        }.attach()
    }
    private fun initListener(){
        binding.fab.setOnClickListener(this)
    }

    private fun handleBtnBackPhone(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            Log.d("DEBUG-BACK","Fragment Schedule Back")
            findNavController().popBackStack()
        }
    }

    private fun initObserve(){
        todoViewModel.selectedDay.observe(viewLifecycleOwner){value->
            daySelected=value
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.fab->{
                if(daySelected!=""){
                    val bundle= Bundle()
                    bundle.putString("daySelected",daySelected)
                    findNavController().navigate(R.id.action_fragmentTodo_to_fragmentAddTask,bundle,
                        NavOption.animationFragment)
                }
                else{
                    Toast.makeText(requireContext(),"Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
