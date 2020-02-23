package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.reg.model.thana.RequestThanaAPI;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;

public class EntrySecondActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private EditText et_landmark;
    private Spinner spnr_district, spnr_thana, spnr_postcode;
    private String str_districtId = "", str_thanaId = "", str_postcodeId = "", str_postcode = "";
    private String[] district_array_id, thana_array_id, post_array_id, post_array_name;
    private String dist_name, thana_name;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    private AppHandler mAppHandler;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_two);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("২য় ধাপ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ScrollView mScrollView = findViewById(R.id.scrollView_second);
        TextView tv_landmark = findViewById(R.id.textView_landmark);
        String custom_text = "<font color=#41882b>ল্যান্ডমার্কঃ </font> <font color=#b3b3b3>(ঐচ্ছিক)</font>";
        tv_landmark.setText(Html.fromHtml(custom_text));

        spnr_district = findViewById(R.id.spinner_district);
        et_landmark = findViewById(R.id.editText_landmark);
        spnr_thana = findViewById(R.id.spinner_thana);
        spnr_postcode = findViewById(R.id.spinner_postcode);

        ((TextView) mScrollView.findViewById(R.id.textView_district)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_thana)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_postcode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        tv_landmark.setTypeface(AppController.getInstance().getAponaLohitFont());
        et_landmark.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_preStep)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.btn_nextStep)).setTypeface(AppController.getInstance().getAponaLohitFont());

        spnr_thana.setOnItemSelectedListener(EntrySecondActivity.this);
        spnr_postcode.setOnItemSelectedListener(EntrySecondActivity.this);

        if (mAppHandler.REG_FLAG_TWO) {
            et_landmark.setText(regModel.getLandmark());
        }
        Initializer();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_REGISTRATION_SECOND_PAGE);
    }

    public void Initializer() {
        try {
            JSONArray array = new JSONArray(mAppHandler.getDistrictArray());

            String[] district_array_name = new String[array.length() + 1];
            district_array_id = new String[array.length() + 1];
            district_array_name[0] = "Select One";
            district_array_id[0] = "0";

            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String name = obj.getString("distriName");

                district_array_id[i + 1] = obj.getString("id");
                district_array_name[i + 1] = name;
            }

            ArrayAdapter<CharSequence> arrayAdapter_spinner_district = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, district_array_name);
            spnr_district.setAdapter(arrayAdapter_spinner_district);
            spnr_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    dist_name = spnr_district.getSelectedItem().toString();
                    if (dist_name.equals("Select One")) {
                        str_districtId = "";
                    } else {
                        str_districtId = String.valueOf(district_array_id[i]);
                        cd = new ConnectionDetector(AppController.getContext());
                        isInternetPresent = cd.isConnectingToInternet();
                        if (isInternetPresent) {
                            getThannaList();

                           /// new GetThanaResponseAsync().execute(getResources().getString(R.string.district_info_url));
                        } else {
                            Toast.makeText(EntrySecondActivity.this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private void getThannaList() {
        showProgressDialog();
        RequestThanaAPI m =  new RequestThanaAPI();
        m.setDeviceId(mAppHandler.getAndroidID());
        m.setUsername(mAppHandler.getAndroidID());
        String currentDataAndTIme = ""+ DateUtils.INSTANCE.getCurrentTimestamp();
        m.setTimestamp(currentDataAndTIme);
        m.setFormat("json");
        m.setChannel("android");
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler.getRID());
        m.setRefId(uniqueKey);
        m.setDistriID(str_districtId);

        ApiUtils.getAPIServicePHP7().getThanaInfo(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200) {

                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String result_status = jsonObject.getString("status");
                        if (result_status.equals("200")) {
                            JSONArray result_data = jsonObject.getJSONArray("data");

                            String[] thana_array_name = new String[result_data.length() + 1];
                            thana_array_id = new String[result_data.length() + 1];
                            thana_array_name[0] = "Select One";
                            thana_array_id[0] = "0";

                            for (int i = 0; i < result_data.length(); i++) {
                                JSONObject obj = result_data.getJSONObject(i);
                                String name = obj.getString("thana");

                                thana_array_id[i + 1] = obj.getString("id");
                                thana_array_name[i + 1] = name;
                            }
                            ArrayAdapter<CharSequence> arrayAdapter_spinner_thana = new ArrayAdapter(EntrySecondActivity.this, android.R.layout.simple_spinner_dropdown_item, thana_array_name);
                            spnr_thana.setAdapter(arrayAdapter_spinner_thana);
                        } else {
                            Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }



                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });
    }

    public void previousOnClick(View view) {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_SECOND_PORTION_PREVIOUS_REQUEST);
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
        if (!str_districtId.isEmpty()
                && !str_thanaId.isEmpty()
                && !str_postcode.isEmpty()) {
            regModel.setDistrictName(dist_name);
            regModel.setThanaName(thana_name);
            regModel.setPostcodeId(str_postcodeId);
            regModel.setPostcodeName(str_postcode);
            regModel.setLandmark(et_landmark.getText().toString().trim());

            mAppHandler.REG_FLAG_TWO = true;

            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_SECOND_PORTION_SUBMIT_REQUEST);
            Intent intent = new Intent(EntrySecondActivity.this, EntryThirdActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "সঠিকভাবে পূরণ করুন", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_thana:
                thana_name = adapterView.getItemAtPosition(i).toString();
                if (thana_name.equals("Select One")) {
                    str_thanaId = "";
                } else {
                    str_thanaId = thana_array_id[i];
                    cd = new ConnectionDetector(AppController.getContext());
                    isInternetPresent = cd.isConnectingToInternet();

                    if (isInternetPresent) {
                        new GetPostResponseAsync().execute(getResources().getString(R.string.district_info_url));
                    } else {
                        Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.spinner_postcode:
                String postcode_name = adapterView.getItemAtPosition(i).toString();
                if (postcode_name.equals("Select One")) {
                    str_postcodeId = "";
                    str_postcode = "";
                } else {
                    str_postcodeId = String.valueOf(post_array_id[i]);
                    str_postcode = String.valueOf(post_array_name[i]);
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


        @Override
        protected void onPreExecute() {

            showProgressDialog();
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("mode", "thana"));
                nameValuePairs.add(new BasicNameValuePair("distriID", str_districtId));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT);
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_status = jsonObject.getString("status");
                    if (result_status.equals("200")) {
                        JSONArray result_data = jsonObject.getJSONArray("data");

                        String[] thana_array_name = new String[result_data.length() + 1];
                        thana_array_id = new String[result_data.length() + 1];
                        thana_array_name[0] = "Select One";
                        thana_array_id[0] = "0";

                        for (int i = 0; i < result_data.length(); i++) {
                            JSONObject obj = result_data.getJSONObject(i);
                            String name = obj.getString("thana");

                            thana_array_id[i + 1] = obj.getString("id");
                            thana_array_name[i + 1] = name;
                        }
                        ArrayAdapter<CharSequence> arrayAdapter_spinner_thana = new ArrayAdapter(EntrySecondActivity.this, android.R.layout.simple_spinner_dropdown_item, thana_array_name);
                        spnr_thana.setAdapter(arrayAdapter_spinner_thana);
                    } else {
                        Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EntrySecondActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GetPostResponseAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("mode", "post"));
                nameValuePairs.add(new BasicNameValuePair("distriID", str_districtId));
                nameValuePairs.add(new BasicNameValuePair("thanaID", str_thanaId));
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getUserName()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT);
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String result_status = jsonObject.getString("status");
                    if (result_status.equals("200")) {
                        JSONArray result_data = jsonObject.getJSONArray("data");

                        post_array_name = new String[result_data.length() + 1];
                        post_array_id = new String[result_data.length() + 1];
                        post_array_name[0] = "Select One";
                        post_array_id[0] = "0";

                        for (int i = 0; i < result_data.length(); i++) {
                            JSONObject obj = result_data.getJSONObject(i);
                            String name = obj.getString("post");
                            int id = Integer.parseInt(obj.getString("id"));

                            post_array_id[i + 1] = obj.getString("id");
                            post_array_name[i + 1] = name;
                        }
                        ArrayAdapter<CharSequence> arrayAdapter_spinner_postcode = new ArrayAdapter(EntrySecondActivity.this, android.R.layout.simple_spinner_dropdown_item, post_array_name);
                        spnr_postcode.setAdapter(arrayAdapter_spinner_postcode);
                    } else {
                        Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EntrySecondActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EntrySecondActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }
}

