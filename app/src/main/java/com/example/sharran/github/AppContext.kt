package com.example.sharran.github

import com.example.sharran.github.services.APIClient

class AppContext {
    companion object {
        val instance by lazy { AppContext() }
    }

    val apiClient by lazy { APIClient() }
}