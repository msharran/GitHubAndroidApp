package com.example.sharran.github.utils

import android.app.Activity
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