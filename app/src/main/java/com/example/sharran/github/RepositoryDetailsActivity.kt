package com.example.sharran.github

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.github.utils.AppContext
import kotlinx.android.synthetic.main.activity_repository_details.*
import android.graphics.BitmapFactory
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.sharran.github.dialogFragment.ProjectWebView
import com.example.sharran.github.services.CompletionHandler
import com.example.sharran.github.utils.Contributor
import com.example.sharran.github.utils.EasyToast
import kotlinx.android.synthetic.main.progress_layout.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class RepositoryDetailsActivity : AppCompatActivity() {
    private val appContext = AppContext.instance
    private val repositoryDetail = appContext.repositoryDetail

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

        fetchContributorsAndInitialize()
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
            ProjectWebView().apply { url = repositoryDetail.html_url }.show(supportFragmentManager,"")
        }
    }

    private fun fetchContributorsAndInitialize() {
        showSpinner(true)
        appContext.apiClient.fetchContributors(
            fullName = repositoryDetail.full_name ,
            completionHandler = object : CompletionHandler{
                override fun <T> onSuccess(response: T?) {
                    val contributors = if (response == null) emptyList() else response  as List<Contributor>
                    initializeContributorsList(contributors)
                    initializeRepoDetails()
                    setImageInBackground()
                }

                override fun onFailure(throwable: Throwable) {
                    showSpinner(false)
                    EasyToast.show(this@RepositoryDetailsActivity,getString(R.string.oops_cannot_connect_to_server))
                }
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
        contributors_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            storeSelectedConributor(contributors, position)
            startActivity(Intent(this@RepositoryDetailsActivity,ContributorDetailsActivity::class.java))
        }
    }

    private fun storeSelectedConributor(
        contributors: List<Contributor>,
        position: Int
    ) {
        if (contributors.isNotEmpty()) {
            appContext.contributor = contributors[position]
        } else appContext.contributor = Contributor()
    }

    private fun showSpinner(show: Boolean) {
        if (show) {
            progress_layout.visibility = View.VISIBLE
            repository_details_layout.visibility = View.GONE
            waveLoadingView.startAnimation()
        } else {
            progress_layout.visibility = View.GONE
            repository_details_layout.visibility = View.VISIBLE
            waveLoadingView.cancelAnimation()
        }
    }
}
