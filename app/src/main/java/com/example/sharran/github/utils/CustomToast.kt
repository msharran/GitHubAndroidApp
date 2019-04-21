package com.example.sharran.github.utils

import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.example.sharran.github.R
import kotlinx.android.synthetic.main.toast_view.view.*

class CustomToast(private val context: Context) : Toast(context) {

    private val layout = LayoutInflater.from(context).inflate(R.layout.toast_view, null)

    fun alertToast(message : String){
        layout.custom_toast_message.text = message
        layout.custom_toast_message.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        layout.custom_toast_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
        showToast(message)
    }

    fun infoToast(message : String){
        layout.custom_toast_message.text = message
        layout.custom_toast_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        showToast(message)
    }

    fun errorToast(message : String){
        layout.custom_toast_message.text = message
        layout.custom_toast_layout.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
        showToast(message)
    }

    private fun showToast(message: String) {
        view = this.layout
        setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        duration = if (message.length >= 25) LENGTH_LONG else LENGTH_SHORT
        show()
    }


}