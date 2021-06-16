package com.cloudwell.paywell.services.activity.reg.missing;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.modelPojo.UserSubBusinessTypeModel;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.activity.reg.model.RequestDistrictList;
import com.cloudwell.paywell.services.activity.reg.model.RespsoeGetDistrictList;
import com.cloudwell.paywell.services.activity.reg.model.postCode.RequestPostCodeList;
import com.cloudwell.paywell.services.activity.reg.model.thana.RequestThanaAPI;
import com.cloudwell.paywell.services.activity.reg.nidOCR.NidInputActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MissingMainActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static String RESPONSE_DETAILS;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ScrollView scrollView;
    private LinearLayout layoutOutletName, layoutOutletAddress, layoutOwnerName, layoutMerchantType, layoutBusinessType,
            layoutPhnNumber, layoutDistrict, layoutThana, layoutPost, layoutLandmark,
            layoutOutletImg, layoutNidFrontImg, layoutNidBackImg, layoutOwnerImg, layoutTradeImg,
            layoutPassportImg, layoutBirthImg, layoutDrivingImg, layoutVisitingImg;
    private EditText editText_outletName, editText_address, editText_ownerName, editText_phnNo, editText_landmark;
    private Spinner spnr_merchatnType, spnr_businessType, spnr_district, spnr_thana, spnr_postcode;

    private String str_which_btn_selected = "";
    private ArrayList<String> layoutNames, business_type_id_array;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String[] district_array_id, thana_array_id, post_array_id, post_array_name;

    private static final int PERMISSION_FOR_GALLERY = 321;


    boolean isNID;

    private Button btPicOutlet, btOwnerImg, btTradeImg, btpassportImg, btBirthImg, btDriveImg, btCardImg, btNid, btSmart;

    ArrayAdapter<CharSequence> arrayAdapter_business_type_spinner;

    boolean isMissingFlowGorble = false;
    private String currentPhotoPath;

    public static RegistrationModel regModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_main);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.paywell_reg_missing);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (regModel == null) {

            regModel = new RegistrationModel();
        }

        isMissingFlowGorble = getIntent().getBooleanExtra("isMissingFlow", false);


        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        mCd = new ConnectionDetector(AppController.getContext());
        scrollView = findViewById(R.id.scrollViewMissing);

        layoutNames = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(RESPONSE_DETAILS);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    String name = jsonArray.getString(i);
                    layoutNames.add(name);
                }
            } else {
                super.onBackPressed();
            }
            initialize();
        } catch (Exception ex) {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }



    }

    private void initialize() {
        layoutOutletName = findViewById(R.id.layoutOutletName);
        layoutOutletAddress = findViewById(R.id.layoutOutletAddress);
        layoutOwnerName = findViewById(R.id.layoutOwnerName);
        layoutMerchantType = findViewById(R.id.layoutMerchantType);
        layoutBusinessType = findViewById(R.id.layoutBusinessType);
        layoutPhnNumber = findViewById(R.id.layoutPhnNumber);
        layoutDistrict = findViewById(R.id.layoutDistrict);
        layoutThana = findViewById(R.id.layoutThana);
        layoutPost = findViewById(R.id.layoutPost);
        layoutLandmark = findViewById(R.id.layoutLandmark);
        layoutOutletImg = findViewById(R.id.layoutOutletImg);
        layoutNidFrontImg = findViewById(R.id.layoutNidFrontImg);
        layoutNidBackImg = findViewById(R.id.layoutNidBackImg);
        layoutOwnerImg = findViewById(R.id.layoutOwnerImg);
        layoutTradeImg = findViewById(R.id.layoutTradeImg);
        layoutPassportImg = findViewById(R.id.layoutPassportImg);
        layoutBirthImg = findViewById(R.id.layoutBirthImg);
        layoutDrivingImg = findViewById(R.id.layoutDrivingImg);
        layoutVisitingImg = findViewById(R.id.layoutVisitingImg);
        editText_outletName = findViewById(R.id.editText_outletName);
        editText_address = findViewById(R.id.editText_address);
        editText_ownerName = findViewById(R.id.editText_ownerName);
        editText_phnNo = findViewById(R.id.editText_mobileNumber);
        editText_landmark = findViewById(R.id.editText_landmark);

        spnr_merchatnType = findViewById(R.id.spinner_merchantType);
        spnr_businessType = findViewById(R.id.spinner_businessType);
        spnr_district = findViewById(R.id.spinner_district);
        spnr_thana = findViewById(R.id.spinner_thana);
        spnr_postcode = findViewById(R.id.spinner_postcode);


        spnr_merchatnType.setOnItemSelectedListener(this);
        spnr_businessType.setOnItemSelectedListener(this);
        spnr_district.setOnItemSelectedListener(this);
        spnr_thana.setOnItemSelectedListener(this);
        spnr_postcode.setOnItemSelectedListener(this);

        hashMap.put("Retail Merchant", "1");

        ArrayList<String> merchant_type_array = new ArrayList<>();
        merchant_type_array.add("Select One");
        merchant_type_array.add("Retail Merchant");

        /****Merchant Type ***/
        ArrayAdapter<CharSequence> arrayAdapter_merchant_type_spinner = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, merchant_type_array);

        spnr_merchatnType.setAdapter(arrayAdapter_merchant_type_spinner);

        ((TextView) scrollView.findViewById(R.id.textView_outletName)).setTypeface(AppController.getInstance().getAponaLohitFont());
        editText_outletName.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_address)).setTypeface(AppController.getInstance().getAponaLohitFont());
        editText_address.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_ownerName)).setTypeface(AppController.getInstance().getAponaLohitFont());
        editText_ownerName.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_merchantType)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_businessType)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_mobileNumber)).setTypeface(AppController.getInstance().getAponaLohitFont());
        editText_phnNo.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_district)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_thana)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_postcode)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) scrollView.findViewById(R.id.textView_landmarke)).setTypeface(AppController.getInstance().getAponaLohitFont());
        editText_landmark.setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_outletImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.btNid)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.btSmart)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_ownerImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_tradeImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_passportImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_birthImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_driveImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_cardImg)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) scrollView.findViewById(R.id.button_submitMissing)).setTypeface(AppController.getInstance().getAponaLohitFont());

        btPicOutlet = findViewById(R.id.button_outletImg);
        btOwnerImg = findViewById(R.id.button_ownerImg);
        btTradeImg = findViewById(R.id.button_tradeImg);
        btpassportImg = findViewById(R.id.button_passportImg);
        btBirthImg = findViewById(R.id.button_birthImg);
        btDriveImg = findViewById(R.id.button_driveImg);
        btCardImg = findViewById(R.id.button_cardImg);

        btNid = findViewById(R.id.btNid);
        btSmart = findViewById(R.id.btSmart);


        visibilityStateDefine();


        if (isMissingFlowGorble) {

            Handler handler = new Handler();
            final Runnable r = new Runnable() {
                public void run() {
                    isMissingFlowGorble = false;

                }
            };
            handler.postDelayed(r, 7000);
        }


    }

    public void visibilityStateDefine() {

        if (layoutNames.contains("outlet_name")) {
            layoutOutletName.setVisibility(View.VISIBLE);

            if (regModel != null) {
                editText_outletName.setText(regModel.getOutletName());
            }

        }
        if (layoutNames.contains("outlet_address")) {
            layoutOutletAddress.setVisibility(View.VISIBLE);

            if (regModel != null) {
                editText_address.setText(regModel.getOutletAddress());
            }
        }
        if (layoutNames.contains("owner_name")) {
            layoutOwnerName.setVisibility(View.VISIBLE);

            if (regModel != null) {
                editText_ownerName.setText(regModel.getOwnerName());
            }
        }
        if (layoutNames.contains("business_type")) {
            layoutBusinessType.setVisibility(View.VISIBLE);
            layoutMerchantType.setVisibility(View.VISIBLE);

            if (regModel != null) {

                if (regModel.getBusinessaTypeAPIRespose() != null) {

                    if (!regModel.getBusinessaTypeAPIRespose().equals("")) {
                        setBusnicesTypeAdapter(regModel.getBusinessaTypeAPIRespose(), true);
                    }
                }
            }
        }
        if (layoutNames.contains("mobile_number")) {
            layoutPhnNumber.setVisibility(View.VISIBLE);

            if (regModel != null) {
                if (regModel != null) {
                    editText_phnNo.setText(regModel.getPhnNumber());
                }
            }
        }
        if (layoutNames.contains("district") || layoutNames.contains("thana") || layoutNames.contains("post_code")) {


            layoutDistrict.setVisibility(View.VISIBLE);
            layoutThana.setVisibility(View.VISIBLE);
            layoutPost.setVisibility(View.VISIBLE);


            if (isMissingFlowGorble) {
                if (regModel != null) {
                    setupDistrictAdapter(regModel.getDistrictAPIRespose(), isMissingFlowGorble);
                    setAdapterThana(regModel.getThanaResponseAPIRespose(), isMissingFlowGorble);
                    setupPostCode(regModel.getPostCodeResponseAPIRespose(), isMissingFlowGorble);

                }

            } else {
                getDistrictList();

            }


        }

        if (layoutNames.contains("landmark")) {
            layoutLandmark.setVisibility(View.VISIBLE);

            if (regModel != null) {
                editText_landmark.setText(regModel.getLandmark());
            }
        }
        if (layoutNames.contains("outlet_img")) {
            layoutOutletImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("nid_img")) {
            layoutNidFrontImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("nid_back_img")) {
            layoutNidBackImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("owner_img")) {
            layoutOwnerImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("trade_license_img")) {
            layoutTradeImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("image_passport")) {
            layoutPassportImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("birth_certificate_img")) {
            layoutBirthImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("driving_license_imege")) {
            layoutDrivingImg.setVisibility(View.VISIBLE);
        }
        if (layoutNames.contains("visiting_card_img")) {
            layoutVisitingImg.setVisibility(View.VISIBLE);
        }


        if (regModel != null) {

            Drawable img = getResources().getDrawable(R.drawable.icon_seleted);
            if (!regModel.getOutletImage().equals("")) {
                btPicOutlet.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOutlet.setCompoundDrawablePadding(100);
            }

            if (!regModel.getNidFront().equals("")) {
                btNid.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btNid.setCompoundDrawablePadding(100);
            }

            if (!regModel.getSmartCardFront().equals("")) {
                btSmart.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btSmart.setCompoundDrawablePadding(100);
            }

            if (!regModel.getOwnerImage().equals("")) {
                btOwnerImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btOwnerImg.setCompoundDrawablePadding(100);
            }

            if (!regModel.getTradeLicense().equals("")) {
                btTradeImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btTradeImg.setCompoundDrawablePadding(100);
            }

            if (!regModel.getPassport().equals("")) {
                btpassportImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btpassportImg.setCompoundDrawablePadding(100);
            }

            if (!regModel.getBirthCertificate().equals("")) {
                btBirthImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btBirthImg.setCompoundDrawablePadding(100);
            }

            if (!regModel.getDrivingLicense().equals("")) {
                btDriveImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btDriveImg.setCompoundDrawablePadding(100);
            }

            if (!regModel.getVisitingCard().equals("")) {
                btCardImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btCardImg.setCompoundDrawablePadding(100);
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (!isMissingFlowGorble) {
            switch (adapterView.getId()) {
                case R.id.spinner_merchantType:
                    String merchantId = spnr_merchatnType.getSelectedItem().toString();
                    if (merchantId.equals("Select One")) {
                        regModel.setMrchantType("");
                    } else {
                        regModel.setMrchantType(hashMap.get(merchantId));
                        initializationBusinessType();
                    }
                    break;
                case R.id.spinner_businessType:

                    if (spnr_businessType.getSelectedItem().toString().trim().equals("Select One")) {
                        regModel.setBusinessId("");
                        regModel.setBusinessType("");
                    } else {
                        regModel.setBusinessId(business_type_id_array.get(i));
                        regModel.setBusinessType(spnr_businessType.getSelectedItem().toString().trim());
                        regModel.setBusinesstypeAdapterPosition(spnr_businessType.getSelectedItemPosition());
                    }
                    break;
                case R.id.spinner_district:

                    if (spnr_district.getSelectedItem().toString().equals("Select One")) {
                        regModel.setDistrict("");
                    } else {
                        regModel.setDistrict(String.valueOf(district_array_id[i]));
                        regModel.setDistrictAdapterPosition(spnr_district.getSelectedItemPosition());


                        if (mCd.isConnectingToInternet()) {
                            getThannaList();

                        } else {
                            Snackbar snackbar = Snackbar.make(scrollView, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }


                    break;
                case R.id.spinner_thana:
                    if (adapterView.getItemAtPosition(i).toString().equals("Select One")) {
                        regModel.setThanaName("");

                    } else {
                        regModel.setThanaName(thana_array_id[i]);
                        regModel.setThanaAdapterPosition(spnr_thana.getSelectedItemPosition());

                        if (mCd.isConnectingToInternet()) {

                            getPostListAPI();

                        } else {
                            Snackbar snackbar = Snackbar.make(scrollView, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                    break;
                case R.id.spinner_postcode:
                    if (adapterView.getItemAtPosition(i).toString().equals("Select One")) {
                        regModel.setPostcodeId("");
                        regModel.setPostcodeName("");
                    } else {
                        regModel.setPostcodeId(post_array_id[i]);
                        regModel.setPostcodeName(post_array_name[i]);

                        regModel.setPostCodeAdapterPosition(spnr_postcode.getSelectedItemPosition());

                    }
                    break;
                default:
                    break;
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        businessTypeModel.setServiceId(regModel.getMMrchantType());
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


                        String result = response.body().string();
                        business_type_id_array = new ArrayList<>();


                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equals("200")){

                            if (regModel == null) {
                                regModel = new RegistrationModel();

                            }
                            regModel.setBusinessaTypeAPIRespose(result);

                            dismissProgressDialog();

                            setBusnicesTypeAdapter(result, false);
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

//    private class BusinessTypeAsync extends AsyncTask<String, String, String> {
//
//        @Override
//        protected void onPreExecute() {
//            showProgressDialog();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            String responseTxt = null;
//            // Create a new HttpClient and Post Header
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(params[0]);
//
//            try {
//                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
//                nameValuePairs.add(new BasicNameValuePair("serviceId", regModel.getMMrchantType()));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                ResponseHandler<String> responseHandler = new BasicResponseHandler();
//                responseTxt = httpclient.execute(httppost, responseHandler);
//            } catch (Exception e) {
//                e.fillInStackTrace();
//                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
//                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                View snackBarView = snackbar.getView();
//                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//            }
//
//            return responseTxt;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//
//            if (regModel == null) {
//                regModel = new RegistrationModel();
//
//            }
//            regModel.setBusinessaTypeAPIRespose(result);
//
//            dismissProgressDialog();
//
//            setBusnicesTypeAdapter(result, false);
//        }
//    }

    private void setBusnicesTypeAdapter(String result, boolean isMissingFlow) {
        if (result != null) {
            try {
                business_type_id_array = new ArrayList<>();
                List business_type_name_array = new ArrayList<>();

                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                if (status.equals("200")) {
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

                    arrayAdapter_business_type_spinner = new ArrayAdapter(MissingMainActivity.this, android.R.layout.simple_spinner_dropdown_item, business_type_name_array);

                    spnr_businessType.setAdapter(arrayAdapter_business_type_spinner);

                    spnr_businessType.setSelection(regModel.getBusinesstypeAdapterPosition());


                    if (isMissingFlow) {
                        spnr_merchatnType.setSelection(1);
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }



    private void setupDistrictAdapter(String result, boolean isMissingFlowGorble) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String result_status = jsonObject.getString("status");
                if (result_status != null && result_status.equals("200")) {
                    JSONArray result_data = jsonObject.getJSONArray("data");
                    initializeDistrict(result_data.toString(), isMissingFlowGorble);
                } else {
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.services_off_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    public void initializeDistrict(String response, boolean isMissingFlowGorble) {
        try {
            JSONArray array = new JSONArray(response);

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

            if (isMissingFlowGorble) {
                spnr_district.setSelection(regModel.getDistrictAdapterPosition());
            }

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }




    private void setAdapterThana(String result, boolean isMissingFlowGorble) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String result_status = jsonObject.getString("status");
                if (result_status != null && result_status.equals("200")) {
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
                    ArrayAdapter<CharSequence> arrayAdapter_spinner_thana = new ArrayAdapter(MissingMainActivity.this, android.R.layout.simple_spinner_dropdown_item, thana_array_name);
                    spnr_thana.setAdapter(arrayAdapter_spinner_thana);

                    if (isMissingFlowGorble) {
                        spnr_thana.setSelection(regModel.getThanaAdapterPosition());
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.services_off_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }


    private void setupPostCode(String result, boolean isMissingFlowGorble) {
        if (result != null) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String result_status = jsonObject.getString("status");
                if (result_status != null && result_status.equals("200")) {
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
                    ArrayAdapter<CharSequence> arrayAdapter_spinner_postcode = new ArrayAdapter(MissingMainActivity.this, android.R.layout.simple_spinner_dropdown_item, post_array_name);
                    spnr_postcode.setAdapter(arrayAdapter_spinner_postcode);

                    if (isMissingFlowGorble) {
                        spnr_postcode.setSelection(regModel.getPostCodeAdapterPosition());
                    }

                } else {
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.services_off_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }


    public void outletImgOnClickMissing(View v) {
        asked("দোকানের ছবি", "1");
    }


    public void nidImgOnClickMissing(View v) {
        str_which_btn_selected = "2";
        isNID = true;

        if (regModel == null) {
            regModel = new RegistrationModel();
        }

        saveSessionData();

        Intent intent = new Intent(getApplicationContext(), NidInputActivity.class);
        intent.putExtra("isNID", isNID);
        intent.putExtra("isMissingPage", true);
        startActivity(intent);

    }

    private void saveSessionData() {
        regModel.setOutletName(editText_outletName.getText().toString());
        regModel.setOutletAddress(editText_address.getText().toString());
        regModel.setOwnerName(editText_ownerName.getText().toString());
        regModel.setPhnNumber(editText_phnNo.getText().toString());
        regModel.setLandmark(editText_landmark.getText().toString());

    }


    public void smartmgOnClickMissing(View v) {
        str_which_btn_selected = "3";
        isNID = false;

        if (regModel == null) {
            regModel = new RegistrationModel();
        }

        saveSessionData();

        Intent intent = new Intent(getApplicationContext(), NidInputActivity.class);
        intent.putExtra("isNID", isNID);
        intent.putExtra("isMissingPage", true);
        startActivity(intent);
    }

    public void ownerImgOnClickMissing(View v) {
        asked("মালিকের ছবি", "4");
    }


    public void tradeLicenseImgOnClickMissing(View v) {
        asked("ট্রেড লাইসেন্সের ছবি", "5");
    }

    public void passportImgOnClickMissing(View v) {
        asked("পাসপোর্টের ছবি", "6");
    }

    public void birthImgOnClickMissing(View v) {
        asked("বার্থ সার্টিফিকেটের ছবি", "7");
    }

    public void drivingImgOnClickMissing(View v) {
        asked("ড্রাইভিং লাইসেন্সের ছবি", "8");
    }

    public void visitingImgOnClickMissing(View v) {
        asked("ভিজিটিং কার্ডের ছবি", "9");
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            } else {

                Bitmap selectedImage = BitmapFactory.decodeFile(currentPhotoPath);
                encodeTobase64(selectedImage);

            }
        }
    }

    public void encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
//        String imageEncoded = Base64.encodeToString(b, Base64.URL_SAFE);
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        String strBuild = ("xxCloud" + imageEncoded + "xxCloud");

        Drawable img = getResources().getDrawable(R.drawable.icon_seleted);

        switch (str_which_btn_selected) {
            case "1":
                btPicOutlet.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOutlet.setCompoundDrawablePadding(100);
                regModel.setOutletImage(strBuild);

                break;
            case "2":
                break;
            case "3":
                break;
            case "4":
                btOwnerImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btOwnerImg.setCompoundDrawablePadding(100);
                regModel.setOwnerImage(strBuild);
                break;
            case "5":
                btTradeImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btTradeImg.setCompoundDrawablePadding(100);
                regModel.setTradeLicense(strBuild);
                break;
            case "6":
                btpassportImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btpassportImg.setCompoundDrawablePadding(100);
                regModel.setPassport(strBuild);
                break;
            case "7":
                btBirthImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btBirthImg.setCompoundDrawablePadding(100);
                regModel.setBirthCertificate(strBuild);
                break;
            case "8":
                btDriveImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btDriveImg.setCompoundDrawablePadding(100);
                regModel.setDrivingLicense(strBuild);
                break;
            case "9":
                btCardImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btCardImg.setCompoundDrawablePadding(100);
                regModel.setVisitingCard(strBuild);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_FOR_GALLERY: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    galleryIntent();
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }

    public void submitMissingOnClick(View view) {
        if (mCd.isConnectingToInternet()) {

            String mDistrict = "", mThanaName = "", mPostcodeId = "";

            try {
                mDistrict = regModel.getDistrict();
            } catch (Exception e) {

            }

            try {
                mThanaName = regModel.getThanaName();
            } catch (Exception e) {

            }


            try {
                mPostcodeId = regModel.getPostcodeId();
            } catch (Exception e) {


            }


            if (layoutNames.contains("outlet_name")) {
                if (editText_outletName.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(scrollView, "দোকানের নাম দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("outlet_address")) {
                if (editText_address.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ঠিকানা দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("owner_name")) {
                if (editText_ownerName.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(scrollView, "মালিকের নাম দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("business_type")) {
                String mMrchantType = regModel.getMMrchantType();
                String mBusinessType = regModel.getBusinessType();
                if (mMrchantType == null || mMrchantType.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "মার্চেন্টের ধরন সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                } else if (mBusinessType == null || mBusinessType.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ব্যবসার ধরন সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("mobile_number")) {
                if (editText_phnNo.getText().toString().trim().isEmpty() || editText_phnNo.getText().toString().trim().length() != 11) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ফোন নম্বর দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("district")) {
                mDistrict = regModel.getDistrict();

                if (mDistrict == null || mDistrict.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "জেলা সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("thana")) {
                if (mDistrict == null || mDistrict.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "জেলা সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                } else if (mThanaName == null || mThanaName.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "থানা সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("post_code")) {
                if (mDistrict == null || mDistrict.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "জেলা সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                } else if (mThanaName == null || mThanaName.equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "থানা সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                } else if (mPostcodeId == null || mPostcodeId.equalsIgnoreCase("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "পোস্টকোড সিলেক্ট করুন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("editText_landmark")) {
                if (editText_landmark.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ল্যান্ডমার্ক নম্বর দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("outlet_img")) {
                if (regModel.getOutletImage().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "দোকানের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("nid_img")) {
                if (regModel.getNidFront().equals("")) {

                    Snackbar snackbar = Snackbar.make(scrollView, "ন্যাশনাল আইডি সামনের পৃষ্ঠার ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("nid_back_img")) {
                if (regModel.getNidBack().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ন্যাশনাল আইডি পেছনের পৃষ্ঠার ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }

            if (layoutNames.contains("smartCardFrontPic")) {
                if (regModel.getSmartCardFront().equals("")) {

                    Snackbar snackbar = Snackbar.make(scrollView, "ন্যাশনাল আইডি সামনের পৃষ্ঠার ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("smartCardFrontPic")) {
                if (regModel.getSmartCardBack().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ন্যাশনাল আইডি পেছনের পৃষ্ঠার ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("owner_img")) {
                if (regModel.getOwnerImage().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "মালিকের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("trade_license_img")) {
                if (regModel.getTradeLicense().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ট্রেড লাইসেন্সের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("image_passport")) {
                if (regModel.getPassport().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "পাসপোর্টের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("birth_certificate_img")) {
                if (regModel.getBirthCertificate().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "বার্থ সার্টিফিকেটের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("driving_license_imege")) {
                if (regModel.getDrivingLicense().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ড্রাইভিং লাইসেন্সের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }
            if (layoutNames.contains("visiting_card_img")) {
                if (regModel.getVisitingCard().equals("")) {
                    Snackbar snackbar = Snackbar.make(scrollView, "ভিজিটিং কার্ডের ছবি দিন", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    return;
                }
            }

            submitRequest();

        } else {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private void submitRequest() {

        try {
            String androidID = AppHandler.getmInstance(getApplicationContext()).getAndroidID();

            JsonObject jsonInformationData = new JsonObject();

            if (layoutNames.contains("outlet_name")) {
                jsonInformationData.addProperty("outlet_name", editText_outletName.getText().toString().trim());
            }
            if (layoutNames.contains("outlet_address")) {
                jsonInformationData.addProperty("outlet_address", editText_address.getText().toString().trim());
            }
            if (layoutNames.contains("owner_name")) {
                jsonInformationData.addProperty("owner_name", editText_ownerName.getText().toString().trim());
            }
            if (layoutNames.contains("business_type")) {
                jsonInformationData.addProperty("business_type_id", regModel.getBusinessId());
                jsonInformationData.addProperty("business_type", regModel.getBusinessType());
            }
            if (layoutNames.contains("mobile_number")) {
                jsonInformationData.addProperty("mobile_number", editText_phnNo.getText().toString().trim());
            }
            if (layoutNames.contains("district")) {
                jsonInformationData.addProperty("district", regModel.getDistrict());
            }
            if (layoutNames.contains("thana")) {
                jsonInformationData.addProperty("district", regModel.getDistrict());
                jsonInformationData.addProperty("thana", regModel.getThanaName());
            }
            if (layoutNames.contains("post_code")) {
                jsonInformationData.addProperty("district", regModel.getDistrict());
                jsonInformationData.addProperty("thana", regModel.getThanaName());
                jsonInformationData.addProperty("post_office_id", regModel.getPostcodeId());
                jsonInformationData.addProperty("post_code", regModel.getPostcodeName());
            }
            if (layoutNames.contains("landmark")) {
                jsonInformationData.addProperty("landmark", editText_landmark.getText().toString().trim());
            }

            if (layoutNames.contains("nid_img")) {
                jsonInformationData.addProperty("nidNumber", regModel.getNidNumber());
                jsonInformationData.addProperty("nidName", regModel.getNidName());
                jsonInformationData.addProperty("nidNameEngish", regModel.getNidNameEngish());
                jsonInformationData.addProperty("nidMotherName", regModel.getNidMotherName());
                jsonInformationData.addProperty("nidBirthday", regModel.getNidBirthday());
                jsonInformationData.addProperty("nidAddress", regModel.getNidAddress());
                jsonInformationData.addProperty("nidFatherName", regModel.getNidFatherName());
            }

            if (layoutNames.contains("smartCardFrontPic")) {
                jsonInformationData.addProperty("smartCardNumber", regModel.getSmartCardNumber());
                jsonInformationData.addProperty("smartCardName", regModel.getSmartCardName());
                jsonInformationData.addProperty("smartCardFatherName", regModel.getSmartCardFatherName());
                jsonInformationData.addProperty("smartCardMotherName", regModel.getSmartCardMotherName());
                jsonInformationData.addProperty("smartCardBirthday", regModel.getSmartCardBirthday());
                jsonInformationData.addProperty("smartCardAddress", regModel.getSmartCardAddress());
            }


            JsonObject imageData = new JsonObject();
            //add data
            JsonObject rootJsonObject = new JsonObject();
            rootJsonObject.addProperty("imei", androidID);
            rootJsonObject.add("informationData", jsonInformationData);
            if (layoutNames.contains("outlet_img")) {
                imageData.addProperty("outlet_img", regModel.getOutletImage());
            }
            if (layoutNames.contains("nid_img")) {
                imageData.addProperty("nid_img", regModel.getNidFront());
            }
            if (layoutNames.contains("nid_back_img")) {
                imageData.addProperty("nid_back_img", regModel.getNidBack());
            }

            if (layoutNames.contains("smartCardFrontPic")) {
                imageData.addProperty("smartCardFrontPic", regModel.getSmartCardFront());
            }

            if (layoutNames.contains("smartCardBackPic")) {
                imageData.addProperty("smartCardBackPic", regModel.getSmartCardBack());
            }

            if (layoutNames.contains("owner_img")) {
                imageData.addProperty("owner_img", regModel.getOwnerImage());
            }
            if (layoutNames.contains("trade_license_img")) {
                imageData.addProperty("trade_license_img", regModel.getTradeLicense());
            }
            if (layoutNames.contains("image_passport")) {
                imageData.addProperty("image_passport", regModel.getPassport());
            }
            if (layoutNames.contains("birth_certificate_img")) {
                imageData.addProperty("birth_certificate_img", regModel.getBirthCertificate());
            }
            if (layoutNames.contains("driving_license_imege")) {
                imageData.addProperty("driving_license_imege", regModel.getDrivingLicense());
            }
            if (layoutNames.contains("visiting_card_img")) {
                imageData.addProperty("visiting_card_img", regModel.getVisitingCard());
            }

            rootJsonObject.add("img", imageData);


            showProgressDialog();


            ApiUtils.getAPIServicePHP7().unverifiedDataCollectAndUpdate(rootJsonObject).enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    dismissProgressDialog();
                    try {
                        String result = null;
                        result = response.body().string();

                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if (status.equals("200")) {
                            String msg = jsonObject.getString("message");
                            AlertDialog.Builder builder = new AlertDialog.Builder(MissingMainActivity.this);
                            builder.setTitle("Status");
                            builder.setMessage(msg);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    Intent intent = getBaseContext().getPackageManager()
                                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            String msg = jsonObject.getString("message");
                            AlertDialog.Builder builder = new AlertDialog.Builder(MissingMainActivity.this);
                            builder.setTitle("Status");
                            builder.setMessage(msg);
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int id) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    dismissProgressDialog();
                    Toast.makeText(MissingMainActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void asked(String title, String number) {

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                askedDialog(title, number);


            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();


    }

    private void askedDialog(String title, String number) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(title)
                .setCancelable(true)
                .setPositiveButton("Camara", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        str_which_btn_selected = number;

                        getCamaraIntent();

                    }
                })
                .setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        str_which_btn_selected = number;

                        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    MissingMainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
                        } else {
                            galleryIntent();
                        }
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
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
                    String data = new Gson().toJson(response.body());



                    if (regModel == null) {

                        regModel = new RegistrationModel();
                    }

                    regModel.setDistrictAPIRespose(data);

                    setupDistrictAdapter(data, isMissingFlowGorble);



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
        m.setDistriID(regModel.getDistrict());

        ApiUtils.getAPIServicePHP7().getThanaInfo(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200) {

                    try {
                        String result = response.body().string();
                        JSONObject jsonObject = new JSONObject(result);
                        String result_status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (result_status.equals("200")) {


                            regModel.setThanaResponseAPIRespose(result);

                            setAdapterThana(result, isMissingFlowGorble);

                        } else {
                            showErrorMessagev1(msg);
                        }
                    } catch (Exception e) {
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

    private void getPostListAPI() {

        showProgressDialog();
        RequestPostCodeList m =  new RequestPostCodeList();
        m.setDeviceId(mAppHandler.getAndroidID());
        m.setUsername(mAppHandler.getAndroidID());
        String currentDataAndTIme = ""+ DateUtils.INSTANCE.getCurrentTimestamp();
        m.setTimestamp(currentDataAndTIme);
        m.setFormat("json");
        m.setChannel("android");
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(mAppHandler.getRID());
        m.setRefId(uniqueKey);
        m.setThanaID(regModel.getThanaName());

        ApiUtils.getAPIServicePHP7().getPostOfficeInfo(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200) {

                    try {

                        String result = response.body().string();

                        JSONObject jsonObject = new JSONObject(result);
                        String result_status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (result_status.equals("200")) {

                            dismissProgressDialog();

                            regModel.setPostCodeResponseAPIRespose(result);

                            setupPostCode(result, isMissingFlowGorble);

                        } else {
                            showErrorMessagev1(msg);
                        }
                    } catch (Exception e) {

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

    private void getCamaraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "" + getApplication().getPackageName(), photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 2);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
