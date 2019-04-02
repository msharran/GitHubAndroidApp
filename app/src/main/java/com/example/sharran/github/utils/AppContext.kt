package com.example.sharran.github.utils

import android.webkit.WebView
import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

class AppContext {
    companion object {
        val instance by lazy { AppContext() }
    }
    val apiClient by lazy { APIClient() } //singleton

    lateinit var searchActivity : SearchActivity

    lateinit var repositoryDetail : RepositoryDetail

    var contributors : List<Contributor> = emptyList()

}