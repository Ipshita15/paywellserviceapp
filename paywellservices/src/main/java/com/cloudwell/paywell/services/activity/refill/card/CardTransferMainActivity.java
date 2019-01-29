package com.cloudwell.paywell.services.activity.refill.card;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

public class CardTransferMainActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    private AppHandler mAppHandler;
    private ConnectionDetector mCd;
    private WebView mWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_transfer_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_refill_card);
        }

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());
        mRelativeLayout = findViewById(R.id.relativeLayout);
        mWebview = findViewById(R.id.webView);

        mWebview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.no_update_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });

        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            startWebView("https://www.paywellonline.com/payment_paywell/index.php?username=" +
                    mAppHandler.getRID());
        }
        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_BALANCE_REFILL_CARD);

    }

    private void startWebView(String url) {
        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        mWebview.setWebViewClient(new WebViewClient() {
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
                    progressDialog = new ProgressDialog(CardTransferMainActivity.this);
                    progressDialog.setMessage(getString(R.string.loading_msg));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                }
            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        // Javascript inabled on webview
        mWebview.getSettings().setJavaScriptEnabled(true);
        //Load url in webview
        mWebview.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
