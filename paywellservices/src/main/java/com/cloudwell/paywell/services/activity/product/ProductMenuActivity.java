package com.cloudwell.paywell.services.activity.product;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.product.ekShop.EkShopeMenuActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

public class ProductMenuActivity extends BaseActivity {
    RelativeLayout relativeLayout;
    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    private AppHandler mAppHandler;
    String URL;
    int serviceType;
    private static int TAG_AJKER_DEAL = 1;
    private static int TAG_WHOLESALE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_menu);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.nav_product_catalog);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        relativeLayout = findViewById(R.id.relativeLayout);

        Button btnAjkerDeal = findViewById(R.id.homeBtnAjkerDeal);
        Button btnWholesale = findViewById(R.id.homeBtnWholesale);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnAjkerDeal.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnWholesale.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnAjkerDeal.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnWholesale.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
        Intent intent = new Intent(ProductMenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnAjkerDeal:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PRODUCT_MENU, AnalyticsParameters.KEY_PRODUCT_AJKER_DEAL_MENU);
                serviceType = TAG_AJKER_DEAL;
                checkPermission();
                break;
            case R.id.homeBtnWholesale:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PRODUCT_MENU, AnalyticsParameters.KEY_PRODUCT_WHOLESALE_MENU);
                serviceType = TAG_WHOLESALE;
                checkPermission();
                break;

            case R.id.homeBtnEkShope:
                startActivity(new Intent(this, EkShopeMenuActivity.class));
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
            // Android version is lesser than 6.0 or the permission is already granted.
            if (serviceType == TAG_AJKER_DEAL) {
                URL = getString(R.string.ajd_token) + "rid=" + mAppHandler.getRID();
            } else if (serviceType == TAG_WHOLESALE) {
                URL = getString(R.string.b2b_token) + "rid=" + mAppHandler.getRID();
            }
            new GetToken().execute(URL);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    if (serviceType == TAG_AJKER_DEAL) {
                        String URL = getString(R.string.ajd_token) + "rid=" + mAppHandler.getRID();
                        new GetToken().execute(URL);
                    } else if (serviceType == TAG_WHOLESALE) {
                        String URL = getString(R.string.b2b_token) + "rid=" + mAppHandler.getRID();
                        new GetToken().execute(URL);
                    }
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            }
        }
    }

    @SuppressWarnings("deprecation")
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
                    if (serviceType == TAG_AJKER_DEAL) {
                        AjkerDealActivity.token = result;
                        startActivity(new Intent(ProductMenuActivity.this, AjkerDealActivity.class));
                    } else {
                        JSONObject jsonObject = new JSONObject(result);
                        WholesaleActivity.token = jsonObject.getString("Data");
                        startActivity(new Intent(ProductMenuActivity.this, WholesaleActivity.class));
                    }
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
