package com.example.sharran.github.utils

import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

class AppContext {
    companion object {
        val instance by lazy { AppContext() }
    }
    val apiClient by lazy { APIClient() } //singleton

    lateinit var searchActivity : SearchActivity
    var repositories : List<APIModels.RepositoryDetail> = emptyList()
    var contributors : List<APIModels.Contributor>? = null


}