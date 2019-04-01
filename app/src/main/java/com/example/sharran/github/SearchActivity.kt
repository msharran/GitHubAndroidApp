package com.example.sharran.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.utils.AppContext
import com.example.sharran.github.utils.APIModels

class SearchActivity : AppCompatActivity() {
    private val appContext = AppContext.instance
    private val apiClient = appContext.apiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initialize()
        populateRepositories()
    }

    private fun initialize() {
        appContext.searchActivity = this
    }

    private fun populateRepositories() {
        apiClient.fetchRepositories(
            query = "Sleep-Tracker",
            completionHandler = object : CompletionHandler {
                override fun <T> onSuccess(response: T) {
                    val repositories = sortByWatchers(response as APIModels.Search)
                }

                override fun onFailure(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            }
        )
    }

    private fun sortByWatchers(repositories: APIModels.Search): List<APIModels.Repository> =
        repositories.items.sortedByDescending { repository ->
            repository.watchers
        }.subList(0,10)
}
