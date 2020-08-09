package com.cloudwell.paywell.services.activity.reg;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.modelPojo.UserSubBusinessTypeModel;
import com.cloudwell.paywell.services.activity.reg.model.RequestDistrictList;
import com.cloudwell.paywell.services.activity.reg.model.RespsoeGetDistrictList;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;

public class EntryFirstActivity extends BaseActivity {

    private ScrollView mScrollView;
    private TextView textView_email;
    private EditText et_outletName, et_address, et_ownerName, et_email;
    private Spinner spnr_merchatnType, spnr_businessType;
    private static String str_businessId = "";
    private static String str_businessType = "";
    private static String str_merchantType = "";
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ArrayList<String> business_type_id_array;
    private ArrayList<String> merchant_type_array;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String merchantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_one);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("১ম ধাপ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        mCd = new ConnectionDetector(AppController.getContext());

        mScrollView = findViewById(R.id.scrollView_first);

        et_outletName = findViewById(R.id.editText_outletName);
        et_address = findViewById(R.id.editText_address);
        et_ownerName = findViewById(R.id.editText_ownerName);
        et_email = findViewById(R.id.editText_emailAddress);
        spnr_merchatnType = findViewById(R.id.spinner_merchantType);
        spnr_businessType = findViewById(R.id.spinner_businessType);
        textView_email = findViewById(R.id.textView_emailAddress);

        if (mAppHandler.REG_FLAG_ONE) {
            et_outletName.setText(regModel.getOutletName());
            et_address.setText(regModel.getOutletAddress());
            et_ownerName.setText(regModel.getOwnerName());
            et_email.setText(regModel.getEmailAddress());
        }

        hashMap.put("Retail Merchant", "1");
        merchant_type_array = new ArrayList<>();
        merchant_type_array.add("Select One");
        merchant_type_array.add("Retail Merchant");

        initializationOther();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_REGISTRATION_FIRST_PAGE);

    }

    public void initializationBusinessType() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {

            getBusinessType();

        }
    }

    private void getBusinessType(){

        showProgressDialog();
        UserSubBusinessTypeModel businessTypeModel =  new UserSubBusinessTypeModel();
        businessTypeModel.setServiceId(str_merchantType);
        businessTypeModel.setDeviceId(mAppHandler.getAndroidID());
        String currentDataAndTIme = ""+ DateUtils.INSTANCE.getCurrentTimestamp();
        businessTypeModel.setTimestamp(currentDataAndTIme);
        businessTypeModel.setFormat("json");
        businessTypeModel.setChannel("android");
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler.getRID());
        businessTypeModel.setRefId(uniqueKey);

        ApiUtils.getAPIServicePHP7().getUserSubBusinessType(businessTypeModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200){

                    try {

                        business_type_id_array = new ArrayList<>();
                        List business_type_name_array = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equals("200")){
                            JSONArray jsonArray = jsonObject.getJSONArray("type_of_business");

                            business_type_id_array.add("ipshita");
                            business_type_name_array.add("Select One");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String name = object.getString("name");

                                business_type_id_array.add(id);
                                business_type_name_array.add(name);
                            }
                            ArrayAdapter<String> arrayAdapter_business_type_spinner = new ArrayAdapter<>(EntryFirstActivity.this, android.R.layout.simple_spinner_dropdown_item, business_type_name_array);

                            spnr_businessType.setAdapter(arrayAdapter_business_type_spinner);
                            spnr_businessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                    try {
                                        str_businessType = "";
                                        str_businessId = business_type_id_array.get(position);
                                        str_businessType = spnr_businessType.getSelectedItem().toString().trim();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        showErrorMessagev1(getString(R.string.try_again_msg));

                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }

                            });
                        }else {
                            showErrorMessagev1(msg);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                        showErrorMessagev1(getString(R.string.try_again_msg));

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });



    }


    private class BusinessTypeAsync extends AsyncTask<String, String, String> {

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
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("serviceId", str_merchantType));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT);
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    business_type_id_array = new ArrayList<>();
                    List business_type_name_array = new ArrayList<>();

                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("type_of_business");

                        business_type_id_array.add("ipshita");
                        business_type_name_array.add("Select One");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id");
                            String name = object.getString("name");

                            business_type_id_array.add(id);
                            business_type_name_array.add(name);
                        }

                        ArrayAdapter<CharSequence> arrayAdapter_business_type_spinner = new ArrayAdapter(EntryFirstActivity.this, android.R.layout.simple_spinner_dropdown_item, business_type_name_array);

                        spnr_businessType.setAdapter(arrayAdapter_business_type_spinner);
                        spnr_businessType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                try {
                                    str_businessType = "";
                                    str_businessId = business_type_id_array.get(position);
                                    str_businessType = spnr_businessType.getSelectedItem().toString().trim();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void initializationOther() {
        String custom_text = "<font color=#41882b> ইমেইল </font> <font color=#b3b3b3> (ঐচ্ছিক)</font>";
        textView_email.setText(Html.fromHtml(custom_text));

        /****Merchant Type ***/
        ArrayAdapter<CharSequence> arrayAdapter_merchant_type_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, merchant_type_array);

        spnr_merchatnType.setAdapter(arrayAdapter_merchant_type_spinner);
        spnr_merchatnType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    merchantId = spnr_merchatnType.getSelectedItem().toString();
                    if (merchantId.equals("Select One")) {
                        str_merchantType = "";
                    } else {
                        str_merchantType = hashMap.get(merchantId);
                        initializationBusinessType();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(EntryFirstActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ((TextView) mScrollView.findViewById(R.id.textView_outletName)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_outletName.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_address)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_address.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_ownerName)).setTypeface(AppController.getInstance().getAponaLohitFont());
        et_ownerName.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_merchantType)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_businessType)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_emailAddress)).setTypeface(AppController.getInstance().getAponaLohitFont());
        textView_email.setTypeface(AppController.getInstance().getAponaLohitFont());
    }

    public void nextOnClick(View view) {
        if (et_outletName.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "দোকানের নাম দিন", Toast.LENGTH_LONG).show();
        } else if (et_address.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "ঠিকানা দিন", Toast.LENGTH_LONG).show();
        } else if (et_ownerName.getText().toString().trim().isEmpty()) {
            Toast.makeText(EntryFirstActivity.this, "মালিকের নাম দিন", Toast.LENGTH_LONG).show();
        } else if (spnr_merchatnType.getSelectedItem().toString().trim().equals("Select One")) {
            Toast.makeText(EntryFirstActivity.this, "মার্চেন্টের ধরন সিলেক্ট করুন", Toast.LENGTH_LONG).show();
        } else if (spnr_businessType.getSelectedItem().toString().trim().equals("Select One")) {
            Toast.makeText(EntryFirstActivity.this, "ব্যবসার ধরন সিলেক্ট করুন", Toast.LENGTH_LONG).show();
        } else {
            if (mCd.isConnectingToInternet()) {
                regModel.setOutletName(et_outletName.getText().toString().trim());
                regModel.setOutletAddress(et_address.getText().toString().trim());
                regModel.setOwnerName(et_ownerName.getText().toString().trim());
                regModel.setBusinessId(str_businessId);
                regModel.setBusinessType(str_businessType);
                regModel.setEmailAddress(et_email.getText().toString().trim());

                mAppHandler.REG_FLAG_ONE = true;
                
                getDistrictList();

            } else {
                Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getDistrictList() {
        showProgressDialog();
        RequestDistrictList businessTypeModel =  new RequestDistrictList();
        businessTypeModel.setDeviceId(mAppHandler.getAndroidID());
        businessTypeModel.setUsername(mAppHandler.getAndroidID());
        String currentDataAndTIme = ""+ DateUtils.INSTANCE.getCurrentTimestamp();
        businessTypeModel.setTimestamp(currentDataAndTIme);
        businessTypeModel.setFormat("json");
        businessTypeModel.setChannel("android");
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler.getRID());
        businessTypeModel.setRefId(uniqueKey);

        ApiUtils.getAPIServicePHP7().getDistrictInfo(businessTypeModel).enqueue(new Callback<RespsoeGetDistrictList>() {
            @Override
            public void onResponse(Call<RespsoeGetDistrictList> call, Response<RespsoeGetDistrictList> response) {
                dismissProgressDialog();
                if (response.code() == 200) {
                    String data = new Gson().toJson(response.body().getData());

                    Bundle bundle = new Bundle();
                    bundle.putString("district_array",data);
                    mAppHandler.setDistrictArray(data);

                    AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_FIRST_PORTION_SUBMIT_REQUEST);
                    Intent intent = new Intent(EntryFirstActivity.this, EntrySecondActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();

                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }

            }

            @Override
            public void onFailure(Call<RespsoeGetDistrictList> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });

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
