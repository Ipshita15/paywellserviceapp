package com.cloudwell.paywell.services.activity.product.ekShop;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.product.ekShop.model.ResEkShopToken;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.logger.Logger;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EKShopActivity extends BaseActivity {


    public static String token;
    final long period = 300000;
    private WebView webView = null;
    private LinearLayout linearLayout;
    private ConnectionDetector mCd;
    private AsyncTask<String, Integer, String> mRefreshTokenAsync;
    private Timer timer;
    private TimerTask timerTask;
    private String pendingUrl;
    private boolean flag;

    private boolean isFirstTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ek_shop_webview);

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCd = new ConnectionDetector(AppController.getContext());

        timer = new Timer();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                // do your task here
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateToken();
                    }
                });

            }
        };

        timer.schedule(timerTask, 0, period);

        linearLayout = findViewById(R.id.linearLayout);
        webView = findViewById(R.id.webView);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            switchToCzLocale(new Locale("en", ""));
        } else {
            switchToCzLocale(new Locale("bn", ""));
        }
        setToolbar(getString(R.string.home_title_ek_shope));

        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);


        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_PRODUCT_AJKER_DEAL_PAGE);

    }

    private void updateToken() {

        if (!mCd.isConnectingToInternet()) {
            connectionError();
            return;

        }
        showProgressDialog();

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        String rid = mAppHandler.getRID();
        String URL = AllUrl.URL_ek_shope_token;
        String utype = "Retailer";
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());

        Call<ResEkShopToken> ekshopToken = ApiUtils.getAPIService().getEkshopToken(URL, rid, utype, uniqueKey);
        ekshopToken.enqueue(new Callback<ResEkShopToken>() {
            @Override
            public void onResponse(Call<ResEkShopToken> call, Response<ResEkShopToken> response) {
                dismissProgressDialog();

                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        EKShopActivity.token = response.body().getToken();
                        if (!mCd.isConnectingToInternet()) {
                            connectionError();
                        } else {
                            isFirstTime = true;
                            startWebView(AllUrl.URL_ek_redirect+ "token=" + token);
                        }

                    } else {
                        Snackbar snackbar = Snackbar.make(linearLayout, "" + response.body().getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(linearLayout, "" + R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<ResEkShopToken> call, Throwable t) {
                dismissProgressDialog();

                Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        });
    }


    private void startWebView(String url) {
        Logger.v("startWebView: " + url);
        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }

            public void onLoadResource(WebView view, String url) {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(EKShopActivity.this);
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
                    Snackbar snackbar = Snackbar.make(linearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
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
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl(url);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void connectionError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EKShopActivity.this);
        builder.setTitle(R.string.internet_connection_title_msg);
        builder.setMessage(R.string.connection_error_msg)
                .setPositiveButton(R.string.retry_btn, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mCd = new ConnectionDetector(AppController.getContext());
                        if (mCd.isConnectingToInternet()) {
                            dialog.dismiss();
                            startWebView(AllUrl.URL_ek_redirect+ "token=" + token);
                        } else {
                            connectionError();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
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



    private class MyJavaScriptInterface {
        private Context ctx;

        public MyJavaScriptInterface(EKShopActivity ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            System.out.println(html);
        }

    }
}
