package com.cloudwell.paywell.services.activity.refill.banking;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MFSInfoMainActivity extends AppCompatActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private CoordinatorLayout mCoordinateLayout;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_INFORMATION = "information";
    private static final String TAG_MESSAGE = "message";
    String service_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mfsmain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.nav_mfs);
        mAppHandler = new AppHandler(this);
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);
        mCd = new ConnectionDetector(MFSInfoMainActivity.this);
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
        Intent intent = new Intent(MFSInfoMainActivity.this, RefillBalanceMainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBkash:
                service_name = "bKash";
                showInformation(service_name);
                break;
            case R.id.homeBtnRocket:
                service_name = "Rocket";
                showInformation(service_name);
                break;
            case R.id.homeBtnMcash:
                service_name = "Mcash";
                showInformation(service_name);
                break;
            default:
                break;
        }
    }

    public void showInformation(String service_name) {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new MFSInfoMainActivity.InformationAsync().execute(
                    getResources().getString(R.string.refill_mfs_info), service_name);
        }
    }

    private class InformationAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MFSInfoMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("serviceName", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                if (status.equalsIgnoreCase("200")) {
                    String serviceName = jsonObject.getString(TAG_MESSAGE);
                    String serviceInfo = jsonObject.getString(TAG_INFORMATION);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MFSInfoMainActivity.this);
                    builder.setTitle(serviceName);
                    builder.setMessage(serviceInfo);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } else {
                    String message = jsonObject.getString(TAG_MESSAGE);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, message, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
