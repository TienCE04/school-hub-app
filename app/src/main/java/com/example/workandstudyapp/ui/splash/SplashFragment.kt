package com.example.workandstudyapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentSplashBinding
import com.example.workandstudyapp.utils.NavOption
import kotlinx.coroutines.launch

class SplashFragment : Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentSplashBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }
    private fun initListener(){
        binding.start.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.start->{
                lifecycleScope.launch {
                    navigateHome()
                }
            }
        }
    }

    private fun navigateHome(){
        findNavController().navigate(R.id.action_splashFragment_to_loginFragment,null, NavOption.animationFragment)
    }
}