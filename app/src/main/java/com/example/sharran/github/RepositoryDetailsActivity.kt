package com.example.sharran.github

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.sharran.github.utils.AppContext
import kotlinx.android.synthetic.main.activity_repository_details.*
import android.graphics.BitmapFactory
import android.view.MenuItem
import android.view.View
import com.example.sharran.github.DialogFragment.ProjectDialogFragment
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

        setImage()
        initializeFields()
        initializeContributors()
    }

    private fun setImage() {
        showSpinner(true)
        doAsync {
            val bitmap: Bitmap?
            try {
                val avatarUrl = URL(repositoryDetail.owner.avatar_url)
                bitmap = BitmapFactory.decodeStream(avatarUrl.openConnection().getInputStream())
                uiThread {
                    if (bitmap == null)
                        repository_image.setImageResource(R.drawable.profile)
                    else
                        repository_image.setImageBitmap(bitmap)
                    showSpinner(false)
                }
            } catch (e: Exception) {
                uiThread {
                    e.printStackTrace()
                    repository_image.setImageResource(R.drawable.profile)
                    showSpinner(false)
                }
            }
        }
    }

    private fun initializeFields() {
        repository_name.text = repositoryDetail.name
        repository_description.text = repositoryDetail.description
        repository_link.text = repositoryDetail.html_url
        repository_watchers.text = repositoryDetail.watchers.toString()
        repository_language.text = repositoryDetail.language
    }

    private fun initializeContributors() {
       repository_link.setOnClickListener {
            ProjectDialogFragment().apply { url = repositoryDetail.html_url }.show(supportFragmentManager,"")
       }
    }

    fun showSpinner(show: Boolean) {
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
