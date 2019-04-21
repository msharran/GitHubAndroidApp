package com.example.sharran.github

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MotionEvent
import android.view.View
import com.example.sharran.github.adapters.RepositoryListAdapter
import com.example.sharran.github.dialogFragment.FilterData
import com.example.sharran.github.dialogFragment.FilterDialog
import com.example.sharran.github.services.FilterListener
import com.example.sharran.github.utils.*
import com.jakewharton.rxbinding2.widget.afterTextChangeEvents
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.progress_view.*
import java.util.concurrent.TimeUnit

var disposable : Disposable? = null

class SearchActivity : AppCompatActivity() , FilterListener{
    private val apiClient = AppContext.apiClient
    private lateinit var repositoryListAdapter : RepositoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        AppContext.searchActivity = this

        initializeRecyclerView()
        initializeSearchView()
    }

    private fun initializeRecyclerView() {
        repositoryListRecyclerView.setHasFixedSize(true)
        repositoryListRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryListAdapter = RepositoryListAdapter(this, emptyList())
        repositoryListRecyclerView.adapter = repositoryListAdapter
    }

    @SuppressLint("CheckResult")
    private fun initializeSearchView() {

        search_view.afterTextChangeEvents()
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { afterTextChangeEvent ->
                val searchQuery = afterTextChangeEvent.view().text.toString()
                searchRepoFromServer(searchQuery = searchQuery)
            }

        search_view.setOnTouchListener { v, event ->
            val DRAWABLE_RIGHT = 2
            if(event.action == MotionEvent.ACTION_UP) {
                if(event.rawX >= (search_view.right - search_view.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {

                    val bottomSheetFragment =  FilterDialog().apply {
                        filterListener = this@SearchActivity
                    }
                    bottomSheetFragment.show(this.supportFragmentManager, bottomSheetFragment.tag)
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun searchRepoFromServer(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            showEmptyResults(true)
            return
        }
        runOnline(this){
            showEmptyResults(false)
            showSpinner(true)
            apiClient.GET.search(
                query = "$searchQuery+sort:stars",
                onSuccess = { repositories -> updateRecyclerView(fetchFirstTen(repositories)) },
                onFailure = { message ->
                    showSpinner(false)
                    showEmptyResults(true)
                    errorToast(message)
                }
            )
        }
    }

    private fun updateRecyclerView(repositories: List<RepositoryDetail>) {
        if (repositories.isEmpty()){
            infoToast("Oops ! Cannot find repository")
            showEmptyResults(true)
            showSpinner(false)
            return
        }
        showEmptyResults(false)
        repositoryListAdapter.repositoryList = repositories
        repositoryListAdapter.notifyDataSetChanged()
        showSpinner(false)
    }

    private fun fetchFirstTen(repositories: Repositories): List<RepositoryDetail> {
        val items = repositories.items
        return if (items.size > 10)
                 items.subList(0,10)
               else items
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
            shimmer_layout.visibility = View.VISIBLE
            progress_shimmer.startShimmer()
        }
        else{
            progress_shimmer.stopShimmer()
            shimmer_layout.visibility = View.GONE
        }
    }

    override fun onFilterClicked(filterData: FilterData) {
        val searchQuery = constructQuery(filterData)
        println(searchQuery)
        searchRepoFromServer(searchQuery)
    }

    private fun constructQuery(filterData: FilterData)  = "${search_view.text}"
                    .buildQuery(filterData.language){ "+language:$it" }
                    .buildQuery(filterData.createdFrom,filterData.createdTo){ from , to -> "+created:$from..$to" }
                    .buildQuery(filterData.pushedFrom,filterData.pushedTo){ from , to -> "+pushed:$from..$to" }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}