package com.example.sharran.github.utils

import com.example.sharran.github.SearchActivity
import com.example.sharran.github.services.APIClient

object AppContext {

    lateinit var searchActivity : SearchActivity
    lateinit var repositoryDetail : RepositoryDetail
    lateinit var contributor: Contributor
    val apiClient by lazy { APIClient() }
}