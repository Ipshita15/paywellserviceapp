package com.cloudwell.paywell.services.activity.statements;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.statements.model.RequestWebView;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cloudwell.paywell.services.activity.utility.AllUrl.URL_statementInquiry;

public class ViewStatementActivity extends BaseActivity {

    public static final String DESTINATION_URL ="url";
    public static final String DESTINATION_TITLE ="title";
    private RelativeLayout mRelativeLayout;
    private WebView mWebView;
    public static String url;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_statement);

        AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());

        if (getIntent()!=null){
            url=getIntent().getStringExtra(DESTINATION_URL);
            title=getIntent().getStringExtra(DESTINATION_TITLE);
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
                url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username=" + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
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
            url = URL_statementInquiry;
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
        mWebView.getSettings().setJavaScriptEnabled(true);


        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());


        RequestWebView requestWebView = new RequestWebView();
        requestWebView.setRefId(uniqueKey);
        requestWebView.setUsername(AppHandler.getmInstance(getApplicationContext()).getUserName());
        requestWebView.setLanguage(AppHandler.getmInstance(getApplicationContext()).getAppLanguage());
        String rowJsonData = new Gson().toJson(requestWebView);



        RequestBody body = RequestBody.create(MediaType.parse("application/json"), rowJsonData);

        Request request = new Request.Builder()
                .url(url.trim())
                .post(body)
                .build();
        RequestBody body1 = request.body();


        Call<ResponseBody> call = null;
        if (title.equalsIgnoreCase("mini")) {
            call = ApiUtils.getAPIServiceV2().StatementInquiry(body1);
        } else if (title.equalsIgnoreCase("balance")) {
            call = ApiUtils.getAPIServiceV2().balanceStatement(body1);
        } else if (title.equalsIgnoreCase("sales")) {
            call = ApiUtils.getAPIServiceV2().salesStatementForhttps(body1);
        } else if (title.equalsIgnoreCase("trx")) {
            call = ApiUtils.getAPIServiceV2().getAllTransactionStatementForHttps(body1);
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    String string = null;
                    if (response.body() != null) {
                        string = response.body().string();
                        startWebView(string);
                    }else {
                        showErrorCallBackMessagev1(getString(R.string.try_again_msg));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showErrorCallBackMessagev1(getString(R.string.try_again_msg));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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

            public void onPageFinished(WebView view, String url) {

                dismissProgressDialog();

            }

        }
        );

        mWebView.loadData(data, "", "");



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
