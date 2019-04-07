package com.example.sharran.github.utils

import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

object AppContext {

    lateinit var searchActivity : SearchActivity
    lateinit var repositoryDetail : RepositoryDetail
    lateinit var contributor: Contributor

    private var apiClient : APIClient? = null
    fun getApiClient() : APIClient {
        when (apiClient) {
            null -> this.apiClient = APIClient()
        }
        return this.apiClient!!
    }
}