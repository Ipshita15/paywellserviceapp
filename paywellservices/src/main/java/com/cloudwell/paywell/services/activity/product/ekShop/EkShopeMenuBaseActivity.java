package com.cloudwell.paywell.services.activity.product.ekShop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity;
import com.cloudwell.paywell.services.activity.product.ekShop.model.ResEkShopToken;
import com.cloudwell.paywell.services.activity.product.ekShop.report.ReportBaseActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
public class EkShopeMenuBaseActivity extends ProductEecommerceBaseActivity {

    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    String URL;
    RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ek_shope_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_title_ek_shope);
        }

        relativeLayout = findViewById(R.id.linearLayout);
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.et_shop_visit:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PRODUCT_MENU, AnalyticsParameters.KEY_PRODUCT_EKSHOPE_DEAL_PAGE);
                startActivity(new Intent(getApplicationContext(), EKShopActivity.class));
                break;
            case R.id.et_ek_shop_report:
                startActivity(new Intent(getApplicationContext(), ReportBaseActivity.class));
                break;

            default:
                break;
        }
    }

    private void checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            AppHandler mAppHandler = AppHandler.getmInstance(getApplicationContext());
            String rid = mAppHandler.getRID();
            URL = getString(R.string.ek_shope_token);
            String utype = "Retailer";

            showProgressDialog();

            Call<ResEkShopToken> ekshopToken = ApiUtils.getAPIService().getEkshopToken(URL, rid, utype);
            ekshopToken.enqueue(new Callback<ResEkShopToken>() {
                @Override
                public void onResponse(Call<ResEkShopToken> call, Response<ResEkShopToken> response) {
                    dismissProgressDialog();

                    if (response.isSuccessful()) {
                        if (response.body().getStatus() == 200) {
                            EKShopActivity.token = response.body().getToken();
                            startActivity(new Intent(getApplicationContext(), EKShopActivity.class));

                        }

                    } else {
                        Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }

                }

                @Override
                public void onFailure(Call<ResEkShopToken> call, Throwable t) {
                    dismissProgressDialog();

                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            });

        }
    }

    protected class GetToken extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
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
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (!result.isEmpty()) {
                    dismissProgressDialog();

                    //startActivity(new Intent(this, AjkerDealActivity.class));

                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
