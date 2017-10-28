package com.cloudwell.paywell.services.activity.reg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class EntryFirstActivity extends AppCompatActivity {

    TextView textView_email;
    EditText et_outletName, et_address, et_ownerName, et_phnNo, et_email;
    Spinner spnr_businessType;
    ArrayAdapter<CharSequence> arrayAdapter_spinner;
    static String str_businessType = "";
    private ConnectionDetector mCd;
    private boolean isInternetPresent = false;
    private AppHandler mAppHandler;
    Bundle bundle;
    ArrayList<String> business_type_array;
    private static String PREF_KEY_STRINGS_BUSINESS_TYPE = "business_type";
    private static String PREF_KEY_STRINGS_BUSINESS_TYPE_NAME = "business_type_array";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_one);

        getSupportActionBar().setTitle("১ম ধাপ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppHandler = new AppHandler(this);
        mCd = new ConnectionDetector(EntryFirstActivity.this);
        sharedPreferences = getSharedPreferences(PREF_KEY_STRINGS_BUSINESS_TYPE, Context.MODE_PRIVATE);

        et_outletName = (EditText) findViewById(R.id.editText_outletName);
        et_address = (EditText) findViewById(R.id.editText_address);
        et_ownerName = (EditText) findViewById(R.id.editText_ownerName);
        et_phnNo = (EditText) findViewById(R.id.editText_mobileNumber);
        et_email = (EditText) findViewById(R.id.editText_emailAddress);
        spnr_businessType = (Spinner) findViewById(R.id.spinner_businessType);
        textView_email = (TextView) findViewById(R.id.textView_emailAddress);

        if(mAppHandler.REG_FLAG_ONE) {
            et_outletName.setText(mAppHandler.getOutletName());
            et_address.setText(mAppHandler.getOutletAddress());
            et_ownerName.setText(mAppHandler.getOwnerName());
            et_phnNo.setText(mAppHandler.getMobileNo());
            et_email.setText(mAppHandler.getEmailAddress());
        }
        initializationBusinessType();
    }


    public void initializationBusinessType() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            new EntryFirstActivity.BusinessTypeAsync().execute(
                    getResources().getString(R.string.business_type_url));
        }
    }

    private class BusinessTypeAsync extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EntryFirstActivity.this, "", getString(R.string.loading_msg), true);
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
            if (result != null) {
                try {
                    business_type_array = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("type_of_business");
                    business_type_array.add("Select One");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String object = jsonArray.getString(i);
                        business_type_array.add(object);
                    }
                    editor = sharedPreferences.edit();
                    editor.putString(PREF_KEY_STRINGS_BUSINESS_TYPE_NAME, TextUtils.join(",", business_type_array));
                    editor.apply();
                    editor.commit();
                    initializationOther();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initializationOther() {
        String custom_text = "<font color=#41882b>ইমেইল </font> <font color=#b3b3b3>(ঐচ্ছিক)</font>";
        textView_email.setText(Html.fromHtml(custom_text));

        arrayAdapter_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, business_type_array);
        spnr_businessType.setAdapter(arrayAdapter_spinner);
        spnr_businessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    str_businessType = "";
                    str_businessType = spnr_businessType.getSelectedItem().toString().trim();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }


    public void nextOnClick(View view) {
        if (et_outletName.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "দোকানের নাম দিন", Toast.LENGTH_LONG).show();
        } else if (et_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "ঠিকানা দিন", Toast.LENGTH_LONG).show();
        } else if (et_ownerName.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "মালিকের নাম দিন", Toast.LENGTH_LONG).show();
        } else if (et_phnNo.getText().toString().trim().isEmpty() || et_phnNo.getText().toString().trim().length() != 11) {
            Toast.makeText(EntryFirstActivity.this, "ফোন নম্বর দিন", Toast.LENGTH_LONG).show();
        } else if (spnr_businessType.getSelectedItem().toString().trim().equals("Select One")) {
            Toast.makeText(EntryFirstActivity.this, "ব্যবসার ধরন সিলেক্ট করুন", Toast.LENGTH_LONG).show();
        } else {
            mCd = new ConnectionDetector(this);
            isInternetPresent = mCd.isConnectingToInternet();

            if (isInternetPresent) {
                mAppHandler.setOutletName(et_outletName.getText().toString().trim());
                mAppHandler.setOutletAddress(et_address.getText().toString().trim());
                mAppHandler.setOwnerName(et_ownerName.getText().toString().trim());
                mAppHandler.setBusinessType(str_businessType.trim());
                mAppHandler.setMobileNo(et_phnNo.getText().toString().trim());
                mAppHandler.setEmailAddress(et_email.getText().toString().trim());

                mAppHandler.REG_FLAG_ONE = true;

                new GetDistrictResponseAsync().execute(getResources().getString(R.string.district_info_url));
            } else {
                Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetDistrictResponseAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EntryFirstActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("mode", "district"));
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

            if (progressDialog.isShowing())
                progressDialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_status = jsonObject.getString("status");
                    if (result_status.equals("200")) {
                        JSONArray result_data = jsonObject.getJSONArray("data");

                        bundle = new Bundle();
                        bundle.putString("district_array", result_data.toString());
                        mAppHandler.setDistrictArray(result_data.toString());
                        Intent intent = new Intent(EntryFirstActivity.this, EntrySecondActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(EntryFirstActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mAppHandler.REG_FLAG_ONE = false;
        mAppHandler.REG_FLAG_TWO = false;
        mAppHandler.REG_FLAG_THREE = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
