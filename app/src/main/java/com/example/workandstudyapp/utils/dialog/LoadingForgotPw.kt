package com.example.workandstudyapp.utils.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class LoadingForgotPw: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val progressBar= ProgressBar(requireContext())
        val builder= AlertDialog.Builder(requireContext())
        builder.setView(progressBar)
        builder.setMessage("Đang gửi yêu cầu lấy lại mật khẩu..!")
        builder.setCancelable(false)
        return builder.create()
    }
}