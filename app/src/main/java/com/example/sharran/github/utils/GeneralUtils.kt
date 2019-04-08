package com.example.sharran.github.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.support.v4.content.ContextCompat.getSystemService
import android.view.View
import android.view.inputmethod.InputMethodManager

fun hideKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun String.buildQuery(from : String, to : String, query : (String,String) -> String): String {
    return when {
        to == "" && from == "" -> this
        to == "" -> this + query(from,"*")
        from == "" -> this + query("*",to)
        else -> this + query(from , to)
    }
}


fun String.buildQuery(value: String, query : (String) -> String): String {
    return when {
        value.isEmpty() -> this
        else -> this + query(value)
    }
}

fun isNetworkAvailable(context: Context): Boolean {
    val  conMgr =  context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val  activeNetwork = conMgr.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

fun executeOnline (context: Context, task : () -> Unit){
    if (isNetworkAvailable(context))
        task()
    else
        EasyToast.show(context,"Please Check Your Internet Connection...")
}