package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_ticket_view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/25/19.
 */
class TicketViewerActivity : AirTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_ticket_view)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_ticket_viewer))

        val stringExtra = intent.getStringExtra("url")
        initViewInitialization(stringExtra)


    }

    private fun initViewInitialization(url: String) {
        pdfByWebView(url)

    }


    private fun pdfByWebView(url: String) {
        webView2.settings.javaScriptEnabled = true
        webView2.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY)
        webView2.setWebViewClient(object : WebViewClient() {
            internal var progressDialog: ProgressDialog? = null

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgressDialog()
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onLoadResource(view: WebView, url: String) {

            }

            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url);
                dismissProgressDialog()
                Logger.e("", "")
            }
        })

        val finalUrl = "http://docs.google.com/viewer?url=$url&embedded=true"
        webView2.loadUrl(finalUrl)
    }


}