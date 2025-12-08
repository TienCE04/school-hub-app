package com.example.workandstudyapp.ui.schedule

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentScheduleBinding

class FragmentSchedule: Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentScheduleBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentScheduleBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        handleBtnBackPhone()
    }

    private fun initListener(){
        binding.iconBack.setOnClickListener(this)
    }
    private fun handleBtnBackPhone(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            Log.d("DEBUG-BACK","Fragment Schedule Back")
            findNavController().popBackStack()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.icon_back->{
                findNavController().popBackStack()
            }
        }
    }
}