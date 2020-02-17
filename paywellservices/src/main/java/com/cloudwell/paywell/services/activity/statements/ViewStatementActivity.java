package com.cloudwell.paywell.services.activity.statements;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
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

import org.apache.http.util.EncodingUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
        startWebView(url);

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_STATEMENT_VIEW);

    }

    private void startWebView(String url) {



        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());


        RequestWebView requestWebView = new RequestWebView();
        requestWebView.setRefId(uniqueKey);
        requestWebView.setUsername(AppHandler.getmInstance(getApplicationContext()).getUserName());
        requestWebView.setLanguage(AppHandler.getmInstance(getApplicationContext()).getAppLanguage());
        String rowJsonData = new Gson().toJson(requestWebView);


        mWebView.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressDialog();
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                byte[] base64s = EncodingUtils.getBytes(rowJsonData, "BASE64");
                view.postUrl(url, base64s);
                return true;
            }

            public void onLoadResource(WebView view, String url) {

                showProgressDialog();
            }

            public void onPageFinished(WebView view, String url) {

                dismissProgressDialog();

            }



            // Handle API until level 21
            @SuppressWarnings("deprecation")
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                return getNewResponse(url);
            }

            // Handle API 21+
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

                String url = request.getUrl().toString();

                return getNewResponse(url);
            }

            private WebResourceResponse getNewResponse(String url) {

                try {

                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), rowJsonData);

                    Request request = new Request.Builder()
                            .url(url.trim())
                            .post(body)
                            .build();
                    RequestBody body1 = request.body();


                    Response<ResponseBody> response = null;
                    if (title.equalsIgnoreCase("mini")) {
                        response = ApiUtils.getAPIServiceV2().StatementInquiry(body1).execute();
                    } else if (title.equalsIgnoreCase("balance")) {
                        response = ApiUtils.getAPIServiceV2().balanceStatement(body1).execute();
                    } else if (title.equalsIgnoreCase("sales")) {
                        response = ApiUtils.getAPIServiceV2().salesStatementForhttps(body1).execute();
                    } else if (title.equalsIgnoreCase("trx")) {
                        response = ApiUtils.getAPIServiceV2().getAllTransactionStatementForHttps(body1).execute();
                    }

                    return new WebResourceResponse(
                            "text/html", "utf-8",
                            response.body().byteStream()
                    );

                } catch (Exception e) {
                    return null;
                }

            }

        });
        mWebView.getSettings().setJavaScriptEnabled(true);

        Map<String, String> extraHeaders = new HashMap<String, String>();
        extraHeaders.put("Authorization","Bearer");


        byte[] base64s = EncodingUtils.getBytes(rowJsonData, "BASE64");

        mWebView.postUrl(url, base64s);

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
