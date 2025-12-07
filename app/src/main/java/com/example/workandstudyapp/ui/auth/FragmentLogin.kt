package com.example.workandstudyapp.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.workandstudyapp.R
import com.example.workandstudyapp.databinding.FragmentLoginBinding
import com.example.workandstudyapp.utils.NavOption
import androidx.core.content.edit
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.workandstudyapp.utils.ObjectKeyService
import com.example.workandstudyapp.utils.dialog.LoadingForgotPw
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri

class FragmentLogin : Fragment(), View.OnClickListener {

    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var sharedPref: SharedPreferences
    private val dialogForgotPw= LoadingForgotPw()

    //google
    private val googleIdOption by lazy {
        GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(ObjectKeyService.webClientIdGG)
            .build()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)

        initObserve()
        initListener()
    }

    private fun initListener() {
        binding.signUp.setOnClickListener(this)
        binding.login.setOnClickListener(this)
        binding.forgotPassword.setOnClickListener(this)
        binding.facebookicon.setOnClickListener(this)
        binding.googleicon.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.login -> {
                lifecycleScope.launch {
                    binding.progressBarSplash.visibility= View.VISIBLE
                    login()
                }

            }

            R.id.signUp -> {
                findNavController().navigate(
                    R.id.action_loginFragment_to_fragmentSignUp, null,
                    NavOption.animationFragment
                )
            }

            R.id.forgotPassword -> {
                forgotPassword()
            }

            R.id.googleicon -> {
                lifecycleScope.launch {
                    try {
                        loginGoogle()
                    }
                    catch (e: NoCredentialException){
                        launchGoogleSignInIntent()
                    }
                    catch (e: Exception){
                        Toast.makeText(requireContext(), "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
                    }

                }
            }

            R.id.facebookicon -> {
                loginFacebook()
            }

        }
    }

    private fun saveEmail(email: String) {
        Log.d("DEBUG-saveEmail", email)
        sharedPref.edit { putString("email", email) }
    }

    private fun initObserve() {
        authViewModel.email.observe(viewLifecycleOwner) { e ->
            if (e.isEmpty()) {
                val emailOld = sharedPref.getString("email", "").toString()
                Log.d("DEBUG-getEmailOld", emailOld)
                binding.eMail.setText(emailOld)
            } else {
                binding.eMail.setText(e)
            }
        }
        authViewModel.password.observe(viewLifecycleOwner) { p ->
            binding.passWord.setText(p)
        }
    }

    private fun login() {
        val email = binding.eMail.text.toString()
        val password = binding.passWord.text.toString()
        if (email.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập email!", Toast.LENGTH_SHORT)
                .show()
        } else if (password.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập password", Toast.LENGTH_SHORT)
                .show()
        } else {
            authViewModel.login(email, password) { m ->
                binding.progressBarSplash.visibility= View.GONE
                Toast.makeText(requireContext(), m, Toast.LENGTH_SHORT).show()
                if (m == "Đăng nhập thành công!") {
                    saveEmail(email)
                    findNavController().navigate(
                        R.id.action_loginFragment_to_homeContainerFragment, null,
                        NavOption.animationFragment
                    )
                }
            }
        }
    }

    private fun forgotPassword() {
        dialogForgotPw.show(parentFragmentManager,"Fragment Login")
        val email=binding.eMail.text.toString()
        if(email.isEmpty()){
            Toast.makeText(requireContext(),"Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
            binding.eMail.requestFocus()
            dialogForgotPw.dismiss()
        }
        else{
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    lifecycleScope.launch {
                        delay(1000)
                    }
                    Toast.makeText(requireContext(),"Đã cấp mật khẩu mới, vui lòng kiểm tra email",Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    lifecycleScope.launch {
                        delay(1000)
                    }
                    Toast.makeText(requireContext(),"Email không tồn tại!",Toast.LENGTH_SHORT).show()
                }
            dialogForgotPw.dismiss()
        }
    }

    private suspend fun loginGoogle() {
        //yêu cầu lấy token
        val request = GetCredentialRequest(listOf(googleIdOption))

        //quản lý chứng nhận
        val credentialManager = CredentialManager.create(requireContext())

        //lấy kết quả
        val result = credentialManager.getCredential(
            context = requireContext(),
            request = request
        )
        //chứng nhận có token
        val credential = result.credential

        if (credential is GoogleIdTokenCredential) {
            firebaseAuthWithGoogle(credential.idToken)
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String){
        val firebaseCredential= GoogleAuthProvider.getCredential(idToken,null)

        Firebase.auth
            .signInWithCredential(firebaseCredential)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_homeContainerFragment,null,
                    NavOption.animationFragment)
                Log.d("Auth", "Google login success")
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),"Xác thực không thành công!", Toast.LENGTH_SHORT).show()
                Log.e("Auth", "Firebase failed", it)
            }

    }

    //chưa đăng nhập gg lần đầu tiên
    //vào gg cho user đăng nhập
    private fun launchGoogleSignInIntent(){
        val intent= Intent(Intent.ACTION_VIEW, "https://www.google.com".toUri())
        try {
            startActivity(intent)
        }
        catch (e: Exception){
            intent.setPackage(null)
            startActivity(intent)
        }
    }
    private fun loginFacebook() {

    }
}