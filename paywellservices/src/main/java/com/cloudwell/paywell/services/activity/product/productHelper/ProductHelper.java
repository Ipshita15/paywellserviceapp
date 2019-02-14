package com.cloudwell.paywell.services.activity.product.productHelper;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.product.AjkerDealActivity;
import com.cloudwell.paywell.services.activity.product.WholesaleActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 8/1/19.
 */
public class ProductHelper {
    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    private static int TAG_WHOLESALE = 2;
    String URL;
    private int serviceType;
    private MainActivity mAppCompatActivity;


    public void getToken(MainActivity appCompatActivity, int serviceType) {
        mAppCompatActivity = appCompatActivity;
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PRODUCT_MENU, AnalyticsParameters.KEY_PRODUCT_AJKER_DEAL_MENU);
        this.serviceType = serviceType;
        checkPermission(appCompatActivity);
    }

    private void checkPermission(AppCompatActivity appCompatActivity) {
        AppHandler mAppHandler = AppHandler.getmInstance(appCompatActivity);

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(appCompatActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(appCompatActivity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_STORAGE);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            if (serviceType == 1) {
                URL = appCompatActivity.getString(R.string.ajd_token) + "rid=" + mAppHandler.getRID();
            } else if (serviceType == 2) {
                URL = appCompatActivity.getString(R.string.b2b_token) + "rid=" + mAppHandler.getRID();
            }
            new GetToken().execute(URL);
        }
    }

    protected class GetToken extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            mAppCompatActivity.showProgressDialog();
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
                Snackbar snackbar = Snackbar.make(mAppCompatActivity.mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
                    mAppCompatActivity.dismissProgressDialog();
                    if (serviceType == 1) {
                        AjkerDealActivity.token = result;
                        mAppCompatActivity.startActivity(new Intent(mAppCompatActivity.getApplicationContext(), AjkerDealActivity.class));
                    } else {
                        JSONObject jsonObject = new JSONObject(result);
                        WholesaleActivity.token = jsonObject.getString("Data");
                        mAppCompatActivity.startActivity(new Intent(mAppCompatActivity.getApplicationContext(), WholesaleActivity.class));
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mAppCompatActivity.mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mAppCompatActivity.mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
