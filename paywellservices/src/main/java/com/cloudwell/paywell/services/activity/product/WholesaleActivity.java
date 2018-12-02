package com.cloudwell.paywell.services.activity.product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class WholesaleActivity extends AppCompatActivity {

    public static String token;
    private WebView webView = null;
    private LinearLayout linearLayout;
    final long period = 300000;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;

    private AsyncTask<String, Integer, String> mRefreshTokenAsync;
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wholesale);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(AppController.getContext());

        timer = new Timer();


        timerTask = new TimerTask() {
            @Override
            public void run() {
                // do your task here
                mRefreshTokenAsync = new RefreshTokenAsync().execute(getString(R.string.b2b_refresh_token));
            }
        };

        timer.schedule(timerTask, 0, period);

        //Sharing
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.home_product_pw_wholesale_title);

        linearLayout = findViewById(R.id.linearLayout);

        //Get webview
        webView = findViewById(R.id.webView);

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        try {
            if (token.length() == 36) {
                if (!mCd.isConnectingToInternet()) {
                    connectionError();
                } else {
                    startWebView(getString(R.string.b2b_login) + "token=" + token);
                }
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

    private void startWebView(String url) {
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
                    progressDialog = new ProgressDialog(WholesaleActivity.this);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    if (!isFinishing())
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
                    Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);
        //Load url in webview
        webView.loadUrl(url);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.action_share) {
            String sharerUrl = getString(R.string.b2b_share);
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
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                Configuration config = new Configuration();
                config.locale = Locale.ENGLISH;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            } else {
                Configuration config = new Configuration();
                config.locale = Locale.FRANCE;
                getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            }
            invalidateOptionsMenu();
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
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
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
        mRefreshTokenAsync = new RefreshTokenAsync().execute(getString(R.string.b2b_refresh_token));
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRefreshTokenAsync = new RefreshTokenAsync().execute(getString(R.string.b2b_refresh_token));
    }

    private void connectionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WholesaleActivity.this);
        builder.setTitle(R.string.internet_connection_title_msg);
        builder.setMessage(R.string.connection_error_msg)
                .setPositiveButton(R.string.retry_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCd = new ConnectionDetector(AppController.getContext());
                        if (mCd.isConnectingToInternet()) {
                            dialog.dismiss();
                            startWebView(getString(R.string.b2b_login) + "token=" + token);
                        } else {
                            connectionError();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mRefreshTokenAsync != null) {
            mRefreshTokenAsync.cancel(true);
        }

        if (timer != null) {
            timer.cancel();
        }

        if (timerTask != null) {
            timerTask.cancel();
        }

    }
}
