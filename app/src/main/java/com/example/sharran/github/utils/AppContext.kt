package com.example.sharran.github.utils

import android.webkit.WebView
import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

class AppContext {
    companion object {
        val instance by lazy { AppContext() }
    }
    //singleton
    val apiClient by lazy { APIClient() }

    //activity params
    lateinit var searchActivity : SearchActivity
    lateinit var repositoryDetail : RepositoryDetail
    lateinit var contributor: Contributor

}