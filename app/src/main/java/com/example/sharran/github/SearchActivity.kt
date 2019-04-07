package com.example.sharran.github

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import com.example.sharran.github.adapters.RepositoryListAdapter
import com.example.sharran.github.dialogFragment.FilterData
import com.example.sharran.github.dialogFragment.FilterDialog
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.services.FilterListener
import com.example.sharran.github.utils.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.progress_layout.*
import java.util.concurrent.TimeUnit


class SearchActivity : AppCompatActivity() , FilterListener{
    private val appContext = AppContext
    private val apiClient = AppContext.apiClient
    private lateinit var repositoryListAdapter : RepositoryListAdapter
    private lateinit var searchDebounce : PublishSubject<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        appContext.searchActivity = this

        initializeSearchDebounce()
        initializeRecyclerView()
        initializeSearchView()
    }

    @SuppressLint("CheckResult")
    private fun initializeSearchDebounce() {
        searchDebounce = PublishSubject.create()
        searchDebounce.debounce(400, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { query -> searchRepoFromServer(query) }
    }

    private fun initializeRecyclerView() {
        repositoryListRecyclerView.setHasFixedSize(true)
        repositoryListRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryListAdapter = RepositoryListAdapter(this, emptyList())
        repositoryListRecyclerView.adapter = repositoryListAdapter
        showEmptyResults(true)
    }

    private fun initializeSearchView() {

        search_view.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) { searchDebounce.onNext(s.toString()) }
        })

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
        showSpinner(true)
        apiClient.fetchRepos(
            searchQuery = "$searchQuery+sort:stars",
            completionHandler = object : CompletionHandler {
                override fun <T> onSuccess(response: T?) {
                    val repositories = response ?: Repositories()
                    updateRecyclerView(fetchFirstTen(repositories as Repositories))
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
            progress_layout.visibility = View.VISIBLE
            search_recycler_layout.visibility = View.GONE
            waveLoadingView.startAnimation()
        }
        else{
            progress_layout.visibility = View.GONE
            search_recycler_layout.visibility = View.VISIBLE
            waveLoadingView.cancelAnimation()
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

}
