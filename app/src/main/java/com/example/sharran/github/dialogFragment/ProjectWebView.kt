package com.example.sharran.github.dialogFragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import com.example.sharran.github.R
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.dialog_project.*
import kotlinx.android.synthetic.main.progress_layout.*


class ProjectWebView : DialogFragment() {

    lateinit var url : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadWebView(url = url)
    }

    override fun onResume() {
        super.onResume()
        setScreenSize()
    }

    private fun setScreenSize() {
        val metrics = activity!!.resources.displayMetrics
        dialog.window.setLayout(metrics.widthPixels, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebView(url: String) {
        showSpinner(true)
        project_webview.loadUrl(url)
        project_webview.settings.javaScriptEnabled = true
        project_webview.settings.loadsImagesAutomatically = true
        project_webview.settings.allowFileAccess = true
        project_webview.settings.setSupportZoom(true)
        project_webview.settings.allowContentAccess = true
        project_webview.webViewClient = object :WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                showSpinner(false)
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                showSpinner(false)
            }
        }
    }

    private fun showSpinner(show: Boolean) {
        if (show) {
            progress_layout.visibility = View.VISIBLE
            project_webview.visibility = View.GONE
            waveLoadingView.startAnimation()
        } else {
            progress_layout.visibility = View.GONE
            project_webview.visibility = View.VISIBLE
            waveLoadingView.cancelAnimation()
        }
    }

}
