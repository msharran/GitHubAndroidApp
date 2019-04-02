package com.example.sharran.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.utils.AppContext
import com.example.sharran.github.utils.APIModels
import com.example.sharran.github.utils.EasyToast
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {
    private val appContext = AppContext.instance
    private val apiClient = appContext.apiClient
    private lateinit var repositoryListAdapter : RepositoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        appContext.searchActivity = this
        initializeRecyclerView()
        initializeSearchView()
    }

    private fun initializeSearchView() {
        search_btn.setOnClickListener {
            searchRepoFromServer(searchQuery = search_repository.text.toString())
        }
    }

    private fun initializeRecyclerView() {
        showEmptyResults(true)
        repositoryListRecyclerView.setHasFixedSize(true)
        repositoryListRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryListAdapter = RepositoryListAdapter(this, emptyList())
        repositoryListRecyclerView.adapter = repositoryListAdapter
    }

    private fun searchRepoFromServer(searchQuery: String) {
        if (searchQuery == ""){
            showEmptyResults(true)
            return
        }
        apiClient.fetchRepositories(
            searchQuery = searchQuery,
            completionHandler = object : CompletionHandler {
                override fun <T> onSuccess(response: T) {
                    appContext.repositories = sortByWatchers(response as APIModels.Repositories)
                    updateRecyclerView(appContext.repositories)
                }

                override fun onFailure(throwable: Throwable) {
                    throwable.printStackTrace()
                    showEmptyResults(true)
                    EasyToast.show(this@SearchActivity, getString(R.string.oops_cannot_connect_to_server))
                }
            })
    }

    private fun updateRecyclerView(repositories: List<APIModels.RepositoryDetail>) {
        showEmptyResults(false)
        repositoryListAdapter.repositoryList = repositories
        repositoryListAdapter.notifyDataSetChanged()
    }

    private fun sortByWatchers(repositories: APIModels.Repositories): List<APIModels.RepositoryDetail> {
        val items = repositories.items
        val sortedByWatchers = items.sortedByDescending { it.watchers }
        return if (items.size > 10)
                 sortedByWatchers.subList(0,10)
               else sortedByWatchers
    }

    private fun showEmptyResults(enable: Boolean){
        if (enable){
            emptyResult.visibility = View.VISIBLE
            repositoryListRecyclerView.visibility = View.GONE
        }
        else{
            emptyResult.visibility = View.GONE
            repositoryListRecyclerView.visibility = View.VISIBLE
        }
    }
}
