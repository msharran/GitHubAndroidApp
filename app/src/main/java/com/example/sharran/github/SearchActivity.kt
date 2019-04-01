package com.example.sharran.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.github.utils.Models

class SearchActivity : AppCompatActivity() {
    val appContext = AppContext.instance
    private var searchResult : Models.SearchResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchResult = appContext.apiClient.fetchRepositories("sharran_murali/Sleep-Tracker")
    }
}
