package com.example.workandstudyapp.utils


import androidx.navigation.NavOptions
import com.example.workandstudyapp.R

object NavOption{
    val animationFragment = NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_in_left)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
}
