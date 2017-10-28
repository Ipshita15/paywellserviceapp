package com.cloudwell.paywell.services.activity.product;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppHandler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class ProductMenuActivity extends AppCompatActivity {
    RelativeLayout relativeLayout;
    private static final int PERMISSIONS_REQUEST_WRITE_STORAGE = 100;
    private AppHandler mAppHandler;
    String token;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_product_catalog);

        mAppHandler = new AppHandler(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
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
                checkPermission();
                break;
            case R.id.homeBtnB2B:
                Snackbar snackbar = Snackbar.make(relativeLayout, R.string.coming_soon_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
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
//            dialog = new Dialog(ProductMenuActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setCancelable(false);
//            dialog.setContentView(R.layout.custom_progress_dialog_ajd);
//
//            relativeLayout = (RelativeLayout) dialog.findViewById(R.id.loadingPanel);
//            dialog.show();
//            final Handler handler  = new Handler();
//            final Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
//            };

//            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    handler.removeCallbacks(runnable);
//                }
//            });
//            handler.postDelayed(runnable, 5000);
            String URL = getString(R.string.ajd_token) + "rid=" + mAppHandler.getRID();
            new GetToken().execute(URL);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
//                    dialog = new Dialog(ProductMenuActivity.this);
//                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialog.setCancelable(false);
//                    dialog.setContentView(R.layout.custom_progress_dialog_ajd);
//
//                    dialog.show();
//                    // Hide after some seconds
//                    final Handler handler  = new Handler();
//                    final Runnable runnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            if (dialog.isShowing()) {
//                                dialog.dismiss();
//                            }
//                        }
//                    };
//
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            handler.removeCallbacks(runnable);
//                        }
//                    });
//                    handler.postDelayed(runnable, 5000);
                    String URL = getString(R.string.ajd_token) + "rid=" + mAppHandler.getRID();
                    new GetToken().execute(URL);
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
    private class GetToken extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ProductMenuActivity.this, "", getString(R.string.loading_msg), true);
            progressDialog.show();
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
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                if (!result.isEmpty()) {
                    progressDialog.dismiss();
                    AjkerDealActivity.token = result;
                    startActivity(new Intent(ProductMenuActivity.this, AjkerDealActivity.class));
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
