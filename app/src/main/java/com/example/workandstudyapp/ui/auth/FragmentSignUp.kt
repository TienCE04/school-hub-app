package com.example.workandstudyapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentSignupBinding

class FragmentSignUp : Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentSignupBinding
    private val binding get() = _binding
    private val authViewModel: AuthViewModel by activityViewModels()
    var email = ""
    var password = ""
    var confirmPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
        handleBtnBackPhone()
    }

    private fun initListener() {
        binding.SignUp.setOnClickListener(this)
        binding.backtologin.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.SignUp -> {
                email = binding.eMail.text.toString()
                password = binding.passWord.text.toString()
                confirmPassword = binding.confirmpass.text.toString()
                when (checkAccountRegister(email, password, confirmPassword)) {
                    -1 -> {
                        Toast.makeText(requireContext(),"Vui lòng nhập email", Toast.LENGTH_SHORT).show()
                        binding.eMail.requestFocus()
                    }
                    -2->{
                        Toast.makeText(requireContext(),"Vui lòng nhập password", Toast.LENGTH_SHORT).show()
                        binding.passWord.requestFocus()
                    }
                    -3->{
                        Toast.makeText(requireContext(),"Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
                        binding.confirmpass.requestFocus()
                    }
                    -4->{
                        Toast.makeText(requireContext(),"Vui lòng nhập đúng định dạng @gmail.com", Toast.LENGTH_SHORT).show()
                        binding.eMail.requestFocus()
                    }
                    -5->{
                        Toast.makeText(requireContext(),"Mật khẩu tối thiểu 6 ký tự, 1 ký tự số, 1 ký tự đặc biệt", Toast.LENGTH_SHORT).show()
                        binding.passWord.requestFocus()
                    }
                    1->{
                        authViewModel.register(email,password){m->
                            if(m=="Đăng ký thành công!"){
                                findNavController().popBackStack()
                            }
                            Toast.makeText(requireContext(),m, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            R.id.backtologin -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun handleBtnBackPhone() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d("BackButton", "Back pressed in Fragment")
            findNavController().popBackStack()
        }
    }

}
