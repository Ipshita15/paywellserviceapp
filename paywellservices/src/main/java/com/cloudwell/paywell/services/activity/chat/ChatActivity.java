package com.cloudwell.paywell.services.activity.chat;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class ChatActivity extends AppCompatActivity {

    private WebView webView = null;
    private LinearLayout linearLayout;
    private static AppHandler mAppHandler;
    private ConnectionDetector mCd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajker_deal);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        linearLayout = findViewById(R.id.linearLayout);

        //Get webview
        webView = findViewById(R.id.webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
            fixNewAndroid(webView);
        }
        if (!mCd.isConnectingToInternet()) {
            connectionError();
        } else {
            startWebView(getString(R.string.chat) + "rid=" + mAppHandler.getRID());
        }
    }

    @TargetApi(16)
    protected void fixNewAndroid(WebView webView) {
        try {
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        } catch(Exception e) {
            Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    @SuppressWarnings("deprecation")
    public void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(ChatActivity.this);
                    progressDialog.setMessage(getString(R.string.loading_msg));
                    progressDialog.setCanceledOnTouchOutside(false);
                    if (!isFinishing()) {
                        progressDialog.show();
                    }
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    // Open previous opened link from history on webview when back button pressed


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            startActivity(new Intent(ChatActivity.this, MainActivity.class));
            finish();
        }
    }

    private void connectionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(R.string.internet_connection_title_msg);
        builder.setMessage(R.string.connection_error_msg)
                .setPositiveButton(R.string.retry_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCd = new ConnectionDetector(AppController.getContext());
                        if (mCd.isConnectingToInternet()) {
                            dialog.dismiss();
                            startWebView(getString(R.string.chat) + "rid=" + mAppHandler.getRID());
                        } else {
                            connectionError();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
