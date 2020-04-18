package com.cloudwell.paywell.services.activity.refill.nagad.nagad_v2.webView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.UtilityBaseActivity;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.orhanobut.logger.Logger;

public class WebViewActivity extends UtilityBaseActivity {

    public static String token;
    private WebView webView = null;
    private LinearLayout linearLayout;
    private String pendingUrl;

    private boolean isFirstTime = true;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setToolbar(getString(R.string.home_title_nagad_balance_claim));
        linearLayout = findViewById(R.id.linearLayout);
        webView = findViewById(R.id.nagad_webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptEnabled(true); // Done above
        webSettings.setDomStorageEnabled(true); // Try
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        startWebView(url);
    }

    private void startWebView(String url) {
        Logger.v("startWebView: " + url);
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // The webView is about to navigate to the specified url.
                return super.shouldOverrideUrlLoading(view, url);
            }

//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(WebViewActivity.this);
                    progressDialog.setMessage(getString(R.string.loading_msg));
                    progressDialog.setCanceledOnTouchOutside(false);
                    if (!isFinishing()) {
                        progressDialog.show();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (pendingUrl == null) {
                    pendingUrl = url;
                }

                super.onPageStarted(view, url, favicon);
            }

            public void onPageFinished(WebView view, String url) {
                if (url.equals(AllUrl.ekshop_paywell_auth_check)) {
                    if (isFirstTime) {
                        isFirstTime = false;
                    } else {
                        // finish();
                    }
                }
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                /* process JSON */
                String message = cmsg.message();
                Log.e("", "");
                return true;

            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
