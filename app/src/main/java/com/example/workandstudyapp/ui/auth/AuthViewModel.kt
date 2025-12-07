package com.example.workandstudyapp.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.FirebaseError.ERROR_EMAIL_ALREADY_IN_USE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel() : ViewModel() {
    private val auth = Firebase.auth
    private var _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private var _password = MutableLiveData("")
    val password: LiveData<String> = _password

    fun register(email: String, password: String,message: (String) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _email.postValue(email)
                    _password.postValue(password)
                    message("Đăng ký thành công!")
                }
                .addOnFailureListener {e->
                    val code=(e as FirebaseAuthException).errorCode
                    message(getFirebaseErrorMessage(code))
                }
        }
    }

    //các dịch vụ ngoài api, firebase kotlin thường có cơ chế phân luồng hợp lí
    fun login(email:String,password:String,message:(String)-> Unit){
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    message("Đăng nhập thành công!")
                    _password.postValue("")
                }
                .addOnFailureListener {e->
                    val code=(e as FirebaseAuthException).errorCode
                    message(getFirebaseErrorMessage(code))
                }
        }
    }

    fun getFirebaseErrorMessage(code: String?): String {
        return when (code) {
            "ERROR_INVALID_EMAIL" -> "Email không hợp lệ"
            "ERROR_USER_NOT_FOUND" -> "Tài khoản không tồn tại"
            "ERROR_WRONG_PASSWORD" -> "Sai mật khẩu"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Email đã được sử dụng"
            "ERROR_WEAK_PASSWORD" -> "Mật khẩu quá yếu"
            "ERROR_USER_DISABLED" -> "Tài khoản đã bị vô hiệu hóa"
            "ERROR_TOO_MANY_REQUESTS" -> "Bạn thao tác quá nhiều lần, thử lại sau"
            "ERROR_INVALID_CREDENTIAL" -> "Thông tin đăng nhập không hợp lệ"
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> "Tài khoản này đã được liên kết trước đó"
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> "Email này đã đăng nhập bằng phương thức khác"
            "ERROR_OPERATION_NOT_ALLOWED" -> "Provider chưa được bật trong Firebase"
            "ERROR_NETWORK_REQUEST_FAILED" -> "Lỗi mạng, hãy kiểm tra kết nối"
            else -> "Lỗi không xác định: $code"
        }
    }

}