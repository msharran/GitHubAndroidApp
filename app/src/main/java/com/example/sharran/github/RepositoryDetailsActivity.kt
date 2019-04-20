package com.example.sharran.github

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_repository_details.*
import android.graphics.BitmapFactory
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.sharran.github.dialogFragment.ProjectWebView
import com.example.sharran.github.utils.*
import kotlinx.android.synthetic.main.progress_view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


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

    private fun setImageInBackground() {
        doAsync {
            val bitmap: Bitmap?
            try {
                val avatarUrl = URL(repositoryDetail.owner.avatar_url)
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
            repository_image.setImageResource(R.drawable.profile)
        else
            repository_image.setImageBitmap(bitmap)
        showSpinner(false)
    }

    private fun initializeRepoDetails() {
        repository_name.text = repositoryDetail.name
        repository_description.text = repositoryDetail.description
        repository_link.text = repositoryDetail.html_url
        repository_watchers.text = repositoryDetail.watchers.toString()
        repository_language.text = repositoryDetail.language
        repository_link.setOnClickListener {
           executeOnline(this){
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
                setImageInBackground()
            },
            onFailure = { error ->
                showSpinner(false)
                EasyToast.show(this@RepositoryDetailsActivity,error)
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
            executeOnline(this){
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
