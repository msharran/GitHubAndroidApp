package com.example.sharran.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.utils.AppContext
import com.example.sharran.github.utils.EasyToast
import com.example.sharran.github.utils.Repositories
import com.example.sharran.github.utils.RepositoryDetail
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

    private fun initializeRecyclerView() {
        repositoryListRecyclerView.setHasFixedSize(true)
        repositoryListRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryListAdapter = RepositoryListAdapter(this, emptyList())
        repositoryListRecyclerView.adapter = repositoryListAdapter
        showEmptyResults(true)
    }

    private fun initializeSearchView() {
        search_btn.setOnClickListener {
            searchRepoFromServer(searchQuery = search_repository.text.toString())
        }
    }

    private fun searchRepoFromServer(searchQuery: String) {
        showSpinner(true)
        apiClient.fetchRepos(
            searchQuery = searchQuery,
            completionHandler = object : CompletionHandler {
                override fun <T> onSuccess(response: T) {
                    updateRecyclerView(sortByWatchers(response as Repositories))
                    showSpinner(false)
                }

                override fun onFailure(throwable: Throwable) {
                    throwable.printStackTrace()
                    showEmptyResults(true)
                    showSpinner(false)
                    EasyToast.show(this@SearchActivity, getString(R.string.oops_cannot_connect_to_server))
                }
            })
    }

    private fun updateRecyclerView(repositories: List<RepositoryDetail>) {
        if (repositories.isEmpty()){
         showEmptyResults(true)
            return
        }
        showEmptyResults(false)
        repositoryListAdapter.repositoryList = repositories
        repositoryListAdapter.notifyDataSetChanged()
    }

    private fun sortByWatchers(repositories: Repositories): List<RepositoryDetail> {
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

    private fun showSpinner(show: Boolean) {
        if (show){
            progress_layout.visibility = View.VISIBLE
            recycler_layout.visibility = View.GONE
            waveLoadingView.startAnimation()
        }
        else{
            progress_layout.visibility = View.GONE
            recycler_layout.visibility = View.VISIBLE
            waveLoadingView.cancelAnimation()
        }
    }
}
