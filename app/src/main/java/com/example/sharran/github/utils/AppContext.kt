package com.example.sharran.github.utils

import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

object AppContext {
    val apiClient : APIClient by lazy { APIClient() }

    lateinit var searchActivity : SearchActivity
    lateinit var repositoryDetail : RepositoryDetail
    lateinit var contributor: Contributor
}