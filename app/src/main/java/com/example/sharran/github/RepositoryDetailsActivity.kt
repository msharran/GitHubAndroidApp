package com.example.sharran.github

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_repository_details.*
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sharran.github.dialogFragment.ProjectWebView
import com.example.sharran.github.utils.*
import kotlinx.android.synthetic.main.progress_view.*
import org.jetbrains.anko.toast


class RepositoryDetailsActivity : AppCompatActivity() {
    private val repositoryDetail = AppContext.repositoryDetail

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
        setContentView(R.layout.activity_repository_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initialize()
    }

    private fun loadImage() {
        Glide.with(this)
            .load(repositoryDetail.owner.avatar_url)
            .placeholder(R.drawable.profile)
            .fitCenter()
            .listener(object : RequestListener<Drawable>{
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    showSpinner(false)
                    return false
                }
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    return false
                }
            })
            .into(repository_image)
    }

    private fun initializeRepoDetails() {
        repository_name.text = repositoryDetail.name
        repository_description.text = repositoryDetail.description
        repository_link.text = repositoryDetail.html_url
        repository_watchers.text = repositoryDetail.watchers.toString()
        repository_language.text = repositoryDetail.language
        repository_link.setOnClickListener {
           runOnline(this){
               ProjectWebView().apply { url = repositoryDetail.html_url }.show(supportFragmentManager,"")
           }
        }
    }

    private fun initialize() {
        showSpinner(true)
        AppContext.apiClient.GET.contributors(
            fullName = repositoryDetail.full_name ,
            onSuccess = { contributors ->
                initializeContributorsList(contributors)
                initializeRepoDetails()
                loadImage()
            },
            onFailure = { message ->
                showSpinner(false)
                errorToast(message)
            }
        )
    }

    private fun initializeContributorsList(contributors: List<Contributor>) {
        var contributorNames = arrayListOf<String>()
        if (contributors.isEmpty())
            contributorNames.add("There are no contributors for this repository")
        contributorNames = contributors.map { it.login } as ArrayList<String>

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contributorNames)
        contributors_list.adapter = adapter
        contributors_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            runOnline(this){
                saveSelection(contributors, position)
                startActivity(Intent(this@RepositoryDetailsActivity,ContributorDetailsActivity::class.java))
            }
        }
    }

    private fun saveSelection(contributors: List<Contributor>, position: Int) {
        if (contributors.isNotEmpty()) {
            AppContext.contributor = contributors[position]
        } else AppContext.contributor = Contributor()
    }

    private fun showSpinner(show: Boolean) {
        if (show) {
            shimmer_layout.visibility = View.VISIBLE
            progress_shimmer.startShimmer()
        } else {
            progress_shimmer.stopShimmer()
            shimmer_layout.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
}
