package com.example.sharran.github.utils

import android.content.Context
import android.widget.Toast

object EasyToast {

    fun show(activityContext : Context, message : String){
        val length = if (message.length < 20 )Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        Toast.makeText(activityContext, message, length).show()
    }

}