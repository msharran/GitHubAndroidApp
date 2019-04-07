package com.example.sharran.github

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.example.sharran.github.adapters.RepositoryListAdapter
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.utils.*
import kotlinx.android.synthetic.main.activity_contributor_details.*
import kotlinx.android.synthetic.main.progress_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class ContributorDetailsActivity : AppCompatActivity() {

    private lateinit var contributor: Contributor
    private lateinit var repositoryListAdapter : RepositoryListAdapter

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                this.finish()
                true
            }
            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contributor_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        contributor = AppContext.contributor

        initializeRecyclerView()
        fetchReposAndInitialize()
    }

    private fun initializeRecyclerView() {
        showSpinner(true)
        contributorListRecyclerView.setHasFixedSize(true)
        contributorListRecyclerView.layoutManager = LinearLayoutManager(this)
        repositoryListAdapter = RepositoryListAdapter(this, emptyList())
        contributorListRecyclerView.adapter = repositoryListAdapter
    }

    private fun fetchReposAndInitialize() {
        AppContext.getApiClient().fetchUserRepos(
            userReposUrl = contributor.repos_url,
            completionHandler = object :CompletionHandler{
                override fun <T> onSuccess(response: T?) {
                    val repositories = response ?: emptyList<RepositoryDetail>()
                    updateRecyclerView(repositories as List<RepositoryDetail>)
                    initializeContributorDetails()
                }

                override fun onFailure(throwable: Throwable) {
                    showSpinner(false)
                    EasyToast.show(this@ContributorDetailsActivity, getString(R.string.oops_cannot_connect_to_server))
                }
            }
        )
    }

    private fun initializeContributorDetails() {
        contributor_name.text = contributor.login
        setImageInBackground()
    }

    private fun updateRecyclerView(repositories: List<RepositoryDetail>) {
        repositoryListAdapter.repositoryList = repositories.sortedByDescending { it.watchers }
        repositoryListAdapter.notifyDataSetChanged()
    }

    private fun setImageInBackground() {
        doAsync {
            val bitmap: Bitmap?
            try {
                val avatarUrl = URL(contributor.avatar_url)
                bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream())
                uiThread {
                    showImage(bitmap)
                }
            } catch (e: Exception) {
                uiThread {
                    e.printStackTrace()
                    showImage(null)
                }
            }
        }
    }

    private fun showImage(bitmap: Bitmap?) {
        if (bitmap == null)
            contributor_image.setImageResource(R.drawable.profile)
        else
            contributor_image.setImageBitmap(bitmap)
        showSpinner(false)
    }
    private fun showSpinner(show: Boolean) {
        if (show) {
            progress_layout.visibility = View.VISIBLE
            contributor_details_layout.visibility = View.GONE
            contributor_recycler_layout.visibility = View.GONE
            waveLoadingView.startAnimation()
        } else {
            progress_layout.visibility = View.GONE
            contributor_details_layout.visibility = View.VISIBLE
            contributor_recycler_layout.visibility = View.VISIBLE
            waveLoadingView.cancelAnimation()
        }
    }

}
