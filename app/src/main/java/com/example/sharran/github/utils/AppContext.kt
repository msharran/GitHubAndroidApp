package com.example.sharran.github.utils

import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

class AppContext {
    companion object {
        val instance by lazy { AppContext() }
    }

    lateinit var searchActivity : SearchActivity

    val apiClient by lazy { APIClient() }
}