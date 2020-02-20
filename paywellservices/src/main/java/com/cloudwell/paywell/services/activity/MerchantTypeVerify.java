package com.cloudwell.paywell.services.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.modelPojo.MerchantRequestPojo;
import com.cloudwell.paywell.services.activity.modelPojo.UserSubBusinessTypeModel;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MerchantTypeVerify extends BaseActivity {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ConstraintLayout mConstraintLayout;
    private String merchantTypeId = "0", str_businessId = "", str_businessType = "";
    private int serviceID=0;
    private Spinner spnr_businessType;
    private ArrayList<String> business_type_id_array, business_type_name_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_type_verify);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.info_collect_title);
        }

        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mCd = new ConnectionDetector(AppController.getContext());

        mAppHandler.setPhnUpdateCheck(System.currentTimeMillis() / 1000);

        mConstraintLayout = findViewById(R.id.constrainLayout);

        RadioGroup radioGroup = findViewById(R.id.radioGroup_merchantType);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                merchantTypeId =  "1";

                initializationBusinessType();
            }
        });

        spnr_businessType = findViewById(R.id.spinner_businessType);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((TextView) mConstraintLayout.findViewById(R.id.bkash_balance_title)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((RadioButton) mConstraintLayout.findViewById(R.id.radioButton_merchant)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mConstraintLayout.findViewById(R.id.business_type_title)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) mConstraintLayout.findViewById(R.id.submit_btn)).setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((TextView) mConstraintLayout.findViewById(R.id.bkash_balance_title)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((RadioButton) mConstraintLayout.findViewById(R.id.radioButton_merchant)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mConstraintLayout.findViewById(R.id.business_type_title)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) mConstraintLayout.findViewById(R.id.submit_btn)).setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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
        businessTypeModel.setUsername(mAppHandler.getUserName());
        businessTypeModel.setServiceId(merchantTypeId);
        businessTypeModel.setDeviceId(mAppHandler.getAndroidID());
        String currentDataAndTIme = ""+DateUtils.INSTANCE.getCurrentTimestamp();
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
                        business_type_name_array = new ArrayList<>();
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
                            ArrayAdapter<String> arrayAdapter_business_type_spinner = new ArrayAdapter<>(MerchantTypeVerify.this, android.R.layout.simple_spinner_dropdown_item, business_type_name_array);

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


    public void confirmOnClick(View view) {
        if (merchantTypeId.trim().equals("0")) {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.merchant_type_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        } else if (spnr_businessType.getSelectedItem().toString().trim().equals("Select One")) {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.business_type_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        } else {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_MERCHANT_TYPE_CONFIRM_REQUEST);
              confirmMerchantType();
            }
        }
    }



    private void confirmMerchantType(){
        showProgressDialog();
        MerchantRequestPojo requestPojo = new MerchantRequestPojo();

        requestPojo.setUsername(mAppHandler.getUserName());
        requestPojo.setMerchantType(merchantTypeId);
        requestPojo.setBusinessType(str_businessId);
        try {
            String busTypeName = URLEncoder.encode(str_businessType, "UTF-8");
            requestPojo.setBusinessTypeName(busTypeName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        ApiUtils.getAPIServiceV2().updateMerchentBusiness(requestPojo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200){
                    try {
                        business_type_id_array = new ArrayList<>();
                        business_type_name_array = new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(response.body().string());
                        final String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        AlertDialog.Builder builder = new AlertDialog.Builder(MerchantTypeVerify.this);//ERROR ShowDialog cannot be resolved to a type
                        builder.setTitle("Result");
                        builder.setMessage(msg);

                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                if (status.equalsIgnoreCase("200") || status.equalsIgnoreCase("335")) {
                                    mAppHandler.setMerchantTypeVerificationStatus("verified");
                                    mAppHandler.setDayCount(0);

                                    startActivity(new Intent(MerchantTypeVerify.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();


                    }catch (Exception e){
                        showErrorMessagev1(getString(R.string.try_again_msg));
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
        final int count = mAppHandler.getDayCount();
        if (count < 3) {
            mAppHandler.setDayCount(count + 1);
            startActivity(new Intent(MerchantTypeVerify.this, MainActivity.class));
            finish();
        } else {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_correct_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }
}
