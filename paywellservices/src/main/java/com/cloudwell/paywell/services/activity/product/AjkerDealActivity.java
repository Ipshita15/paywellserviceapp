package com.cloudwell.paywell.services.activity.product;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AjkerDealActivity extends AppCompatActivity {

    public static String token;
    WebView webView = null;
    LinearLayout linearLayout;
    final long period = 300000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajker_deal);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // do your task here
                new RefreshTokenAsync().execute(getString(R.string.ajd_refresh_token));
            }
        }, 0, period);

        //Sharing
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.home_product_ajker_deal);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        //Get webview
        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        try {
            if (token.length() == 36) {
                startWebView(getString(R.string.ajd_login) + "token=" + token);
            } else {
                onBackPressed();
            }
        } catch (Exception ex) {
            onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ajker_deal_action_bar_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @SuppressWarnings("deprecation")
    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;
            Dialog dialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    // in standard case YourActivity.this
                    progressDialog = new ProgressDialog(AjkerDealActivity.this);
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
                }
            }
        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        //Load url in webview
        webView.loadUrl(url);
    }

    // Open previous opened link from history on webview when back button pressed


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_share) {
            String sharerUrl = getString(R.string.ajd_share);
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharerUrl);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
            for (final ResolveInfo app : activityList) {
                if ((app.activityInfo.name).contains("facebook")) {
                    final ActivityInfo activity = app.activityInfo;
                    final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                    shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    shareIntent.setComponent(name);
                    startActivity(shareIntent);
                    break;
                }
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            //super.onBackPressed();
            startActivity(new Intent(AjkerDealActivity.this, ProductMenuActivity.class));
            finish();
        }
    }

    @SuppressWarnings("deprecation")
    private class RefreshTokenAsync extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RefreshTokenAsync().execute(getString(R.string.ajd_refresh_token));
    }

    @Override
    protected void onPause() {
        super.onPause();
        new RefreshTokenAsync().execute(getString(R.string.ajd_refresh_token));
    }
}
