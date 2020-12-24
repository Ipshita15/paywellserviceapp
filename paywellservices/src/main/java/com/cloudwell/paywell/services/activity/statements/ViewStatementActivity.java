package com.cloudwell.paywell.services.activity.statements;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.statements.model.RequestWebView;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.gson.Gson;

import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cloudwell.paywell.services.activity.utility.AllUrl.URL_statementInquiry;

public class ViewStatementActivity extends BaseActivity {

    public static final String DESTINATION_URL = "url";
    public static final String DESTINATION_TITLE = "title";
    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    public static String url;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        if (getIntent() != null) {
            url = getIntent().getStringExtra(DESTINATION_URL);
            title = getIntent().getStringExtra(DESTINATION_TITLE);
        }

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
                getSupportActionBar().setTitle(R.string.home_statement_mini);
            }
        }


        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            switchToCzLocale(new Locale("en", ""));
        } else {
            switchToCzLocale(new Locale("bn", ""));
        }


        mRelativeLayout = findViewById(R.id.relativeLayout);
        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setSupportMultipleWindows(true);


        if (!title.isEmpty()) {
            if (title.equalsIgnoreCase("mini")) {
                setToolbar(getString(R.string.home_statement_mini));
                callPostWebview();

                RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_statement_mini, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, 0, 39);
                addItemToRecentListInDB(recentUsedMenu);


            } else if (title.equalsIgnoreCase("balance")) {
                setToolbar(getString(R.string.home_statement_balance));
                callPostWebview();


                RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_statement_balance, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, 0, 40);
                addItemToRecentListInDB(recentUsedMenu);

            } else if (title.equalsIgnoreCase("sales")) {
                setToolbar(getString(R.string.home_statement_sales));

                String url = AllUrl.URL_salesStatement + AppHandler.getmInstance(getApplicationContext()).getUserName() + "&language=" + AppHandler.getmInstance(getApplicationContext()).getAppLanguage();

                startWebViewGet(url);


                RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_statement_sales, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, 0, 41);
                addItemToRecentListInDB(recentUsedMenu);

            } else if (title.equalsIgnoreCase("trx")) {
                setToolbar(getString(R.string.home_statement_transaction));

                String url = AllUrl.URL_getAllTransactionStatement + AppHandler.getmInstance(getApplicationContext()).getUserName() + "&language=" + AppHandler.getmInstance(getApplicationContext()).getAppLanguage();

                startWebViewGet(url);


                RecentUsedMenu recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_statement_transaction, StringConstant.KEY_home_statement, IconConstant.KEY_ic_statement, 0, 42);
                addItemToRecentListInDB(recentUsedMenu);
            }

        } else {
            title = "mini";
            url = URL_statementInquiry;
            setToolbar(getString(R.string.home_statement_mini));
        }


    }

    private void callPostWebview() {
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());


        RequestWebView requestWebView = new RequestWebView();
        requestWebView.setRefId(uniqueKey);
        requestWebView.setUsername(AppHandler.getmInstance(getApplicationContext()).getUserName());
        requestWebView.setLanguage(AppHandler.getmInstance(getApplicationContext()).getAppLanguage());
        String rowJsonData = new Gson().toJson(requestWebView);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), rowJsonData);


        showProgressDialog();

        Call<ResponseBody> call = null;
        if (title.equalsIgnoreCase("mini")) {
            call = ApiUtils.getAPIServiceV2().StatementInquiry(body);
        } else if (title.equalsIgnoreCase("balance")) {
            call = ApiUtils.getAPIServiceV2().balanceStatement(body);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();

                try {
                    String string = null;
                    if (response.body() != null) {
                        string = response.body().string();
                        startWebView(string);
                    } else {
                        showErrorCallBackMessagev1(getString(R.string.try_again_msg));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();

                showErrorCallBackMessagev1(getString(R.string.try_again_msg));

            }
        });

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_STATEMENT_VIEW);
    }

    private void startWebView(String data) {

        mWebView.setWebViewClient(new WebViewClient() {


                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);
                                          showProgressDialog();
                                      }


                                      public void onLoadResource(WebView view, String url) {

                                          //showProgressDialog();
                                      }


                                      @Override
                                      public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                                          super.onFormResubmission(view, dontResend, resend);
                                      }

                                      @Override
                                      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                          super.onReceivedSslError(view, handler, error);
                                      }

                                      @Override
                                      public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                          super.onReceivedError(view, request, error);
                                      }


            public void onPageFinished(WebView view, String url) {

                dismissProgressDialog();

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

        }
        );

        String encodedHtml = Base64.encodeToString(data.getBytes(), Base64.NO_PADDING);

        mWebView.loadDataWithBaseURL(null, data, "text/html", "UTF-8", null);


    }


    private void startWebViewGet(String data) {

        mWebView.setWebViewClient(new WebViewClient() {


                                      @Override
                                      public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                          super.onPageStarted(view, url, favicon);
                                          showProgressDialog();
                                      }


                                      public void onLoadResource(WebView view, String url) {

                                          //showProgressDialog();
                                      }


                                      @Override
                                      public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                                          super.onFormResubmission(view, dontResend, resend);
                                      }

                                      @Override
                                      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                                          super.onReceivedSslError(view, handler, error);
                                      }

                                      @Override
                                      public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                          super.onReceivedError(view, request, error);
                                      }


                                      public void onPageFinished(WebView view, String url) {

                                          dismissProgressDialog();

                                      }


                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                          view.loadUrl(url);
                                          return true;
                                      }


                                      @Override
                                      public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                          return super.shouldOverrideUrlLoading(view, request);
                                      }


                                  }
        );

        mWebView.loadUrl(data);

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
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }


}
