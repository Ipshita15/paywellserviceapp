package com.cloudwell.paywell.services.activity.reg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

/**
 * Created by Naima Gani on 11/2/2016.
 */
public class EntrySecondActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView tv_landmark;
    EditText et_landmark;
    Spinner spnr_district, spnr_thana, spnr_postcode;
    String str_district, str_thana, str_postcode;
    ArrayAdapter<CharSequence> arrayAdapter_spinner_district;
    ArrayAdapter<CharSequence> arrayAdapter_spinner_thana;
    ArrayAdapter<CharSequence> arrayAdapter_spinner_postcode;
    Integer[] district_array_id;
    String[] district_array_name;
    Integer[] thana_array_id;
    String[] thana_array_name;
    Integer[] post_array_id;
    String[] post_array_name;
    String dist_name, thana_name, postcode_name;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private AppHandler mAppHandler;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_two);
        mAppHandler = new AppHandler(this);
        getSupportActionBar().setTitle("২য় ধাপ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tv_landmark = (TextView) findViewById(R.id.textView_landmarke);
        String custom_text = "<font color=#41882b>ল্যান্ডমার্কঃ </font> <font color=#b3b3b3>(ঐচ্ছিক)</font>";
        tv_landmark.setText(Html.fromHtml(custom_text));

        spnr_district = (Spinner) findViewById(R.id.spinner_district);
        et_landmark = (EditText) findViewById(R.id.editText_landmark);
        spnr_thana = (Spinner) findViewById(R.id.spinner_thana);
        spnr_postcode = (Spinner) findViewById(R.id.spinner_postcode);

        spnr_thana.setOnItemSelectedListener(EntrySecondActivity.this);
        spnr_postcode.setOnItemSelectedListener(EntrySecondActivity.this);

        if(mAppHandler.REG_FLAG_TWO) {
            if(!mAppHandler.getLandmark().equals("unknown")){
                et_landmark.setText(mAppHandler.getLandmark());
            }
        }
        Initializer();
    }

    public void Initializer() {
        try {
            JSONArray array = new JSONArray(mAppHandler.getDistrictArray());

            district_array_name = new String[array.length() + 1];
            district_array_id = new Integer[array.length() + 1];
            district_array_name[0] = "Select One";
            district_array_id[0] = 0;

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("distriName");
                int id = Integer.parseInt(obj.getString("id"));

                district_array_id[i + 1] = id;
                district_array_name[i + 1] = name;
            }


            arrayAdapter_spinner_district = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, district_array_name);
            spnr_district.setAdapter(arrayAdapter_spinner_district);
            spnr_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    dist_name = spnr_district.getSelectedItem().toString();
                    if (dist_name.equals("Select One")) {
                        str_district = "";
                    } else {
                        str_district = String.valueOf(district_array_id[i]);
                        cd = new ConnectionDetector(EntrySecondActivity.this);
                        isInternetPresent = cd.isConnectingToInternet();

                        if (isInternetPresent) {
                            new GetThanaResponseAsync().execute(getResources().getString(R.string.district_info_url));
                        } else {
                            Toast.makeText(EntrySecondActivity.this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        et_landmark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    public void previousOnClick(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EntrySecondActivity.this, EntryFirstActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void nextOnClick(View view) {
        if (!str_district.equalsIgnoreCase("") && !str_thana.equalsIgnoreCase("") && !str_postcode.equalsIgnoreCase("")) {
            mAppHandler.setDistrict(dist_name);
            mAppHandler.setThana(thana_name);
            mAppHandler.setPostCode(postcode_name);
            mAppHandler.setLandmark(et_landmark.getText().toString().trim());
            Intent intent = new Intent(EntrySecondActivity.this, EntryThirdActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "সথিকভাবে পূরণ করুন", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_thana:
                thana_name = adapterView.getItemAtPosition(i).toString();
                if (thana_name.equals("Select One")) {
                    str_thana = "";
                } else {
                    str_thana = String.valueOf(thana_array_id[i]);
                    cd = new ConnectionDetector(this);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent) {
                        new GetPostResponseAsync().execute(getResources().getString(R.string.district_info_url));
                    } else {
                        Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.spinner_postcode:
                postcode_name = adapterView.getItemAtPosition(i).toString();
                if (postcode_name.equals("Select One")) {
                    str_postcode = "";
                } else {
                    str_postcode = String.valueOf(thana_array_id[i]);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private class GetThanaResponseAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EntrySecondActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("mode", "thana"));
                nameValuePairs.add(new BasicNameValuePair("distriID", str_district));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //execute http post
                //HttpResponse response = httpclient.execute(httppost);

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

                        thana_array_name = new String[result_data.length() + 1];
                        thana_array_id = new Integer[result_data.length() + 1];
                        thana_array_name[0] = "Select One";
                        thana_array_id[0] = 0;

                        for (int i = 0; i < result_data.length(); i++) {
                            JSONObject obj = result_data.getJSONObject(i);
                            String name = obj.getString("thana");
                            int id = Integer.parseInt(obj.getString("id"));

                            thana_array_id[i + 1] = id;
                            thana_array_name[i + 1] = name;
                        }

                        arrayAdapter_spinner_thana = new ArrayAdapter(EntrySecondActivity.this, android.R.layout.simple_spinner_dropdown_item, thana_array_name);
                        spnr_thana.setAdapter(arrayAdapter_spinner_thana);

                    } else {
                        Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(EntrySecondActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }

    }


    private class GetPostResponseAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EntrySecondActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                nameValuePairs.add(new BasicNameValuePair("mode", "post"));
                nameValuePairs.add(new BasicNameValuePair("distriID", str_district));
                nameValuePairs.add(new BasicNameValuePair("thanaID", str_thana));
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
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

                        post_array_name = new String[result_data.length() + 1];
                        post_array_id = new Integer[result_data.length() + 1];
                        post_array_name[0] = "Select One";
                        post_array_id[0] = 0;

                        for (int i = 0; i < result_data.length(); i++) {
                            JSONObject obj = result_data.getJSONObject(i);
                            String name = obj.getString("post");
                            int id = Integer.parseInt(obj.getString("id"));

                            post_array_id[i + 1] = id;
                            post_array_name[i + 1] = name;
                        }

                        arrayAdapter_spinner_postcode = new ArrayAdapter(EntrySecondActivity.this, android.R.layout.simple_spinner_dropdown_item, post_array_name);
                        spnr_postcode.setAdapter(arrayAdapter_spinner_postcode);

                    } else {
                        Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(EntrySecondActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }

    }
}

