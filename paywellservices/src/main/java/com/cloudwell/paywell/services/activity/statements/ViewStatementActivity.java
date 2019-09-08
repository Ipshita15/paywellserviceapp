package com.cloudwell.paywell.services.activity.statements;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class ViewStatementActivity extends BaseActivity {

    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    public static String url;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            if (!title.isEmpty()) {
                if (title.equalsIgnoreCase("mini")) {
                    getSupportActionBar().setTitle(R.string.home_statement_mini);
                } else if (title.equalsIgnoreCase("balance")) {
                    getSupportActionBar().setTitle(R.string.home_statement_balance);
                } else if (title.equalsIgnoreCase("sales")) {
                    getSupportActionBar().setTitle(R.string.home_statement_sales);
                } else if (title.equalsIgnoreCase("trx")) {
                    getSupportActionBar().setTitle(R.string.home_statement_transaction);
                }
            } else {
                title = "mini";
                url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                getSupportActionBar().setTitle(R.string.home_statement_mini);
            }
        }

        mRelativeLayout = findViewById(R.id.relativeLayout);
        mWebView = findViewById(R.id.webView);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            switchToCzLocale(new Locale("en", ""));
        } else {
            switchToCzLocale(new Locale("bn", ""));
        }

        if (!title.isEmpty()) {
            if (title.equalsIgnoreCase("mini")) {
                setToolbar(getString(R.string.home_statement_mini));
            } else if (title.equalsIgnoreCase("balance")) {
                setToolbar(getString(R.string.home_statement_balance));
            } else if (title.equalsIgnoreCase("sales")) {
                setToolbar(getString(R.string.home_statement_sales));
            } else if (title.equalsIgnoreCase("trx")) {
                setToolbar(getString(R.string.home_statement_transaction));
            }
        } else {
            title = "mini";
            url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                    + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
            setToolbar(getString(R.string.home_statement_mini));
        }

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.no_update_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        startWebView(url);

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_STATEMENT_VIEW);

    }

    private void startWebView(String url) {
        mWebView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(ViewStatementActivity.this);
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
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(url);
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
