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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
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

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.reg.EntryMainActivity;
import com.cloudwell.paywell.services.activity.reg.model.RegistrationModel;
import com.cloudwell.paywell.services.activity.reg.nidOCR.NidInputActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;
import com.google.android.material.snackbar.Snackbar;
import com.imagepicker.FilePickUtils;
import com.imagepicker.LifeCycleCallBackManager;

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;
import static com.imagepicker.FilePickUtils.CAMERA_PERMISSION;

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
//    private String str_businessId, str_businessType, str_merchantType, str_district, str_thana, str_postcodeId, str_postcode,
//            str_which_btn_selected, outlet_img = "", nid_img = "", nid_back_img = "",
//            owner_img = "", trade_license_img = "", passport_img = "", birth_certificate_img = "", driving_license_img = "",
//            visiting_card_img = "";
    private String str_which_btn_selected ="";
    private ArrayList<String> layoutNames, business_type_id_array;
    private HashMap<String, String> hashMap = new HashMap<>();
    private String[] district_array_id, thana_array_id, post_array_id, post_array_name;

    private static final int PERMISSION_FOR_GALLERY = 321;


    FilePickUtils filePickUtils;
    LifeCycleCallBackManager lifeCycleCallBackManager;
    boolean isNID;

    private Button btPicOutlet, btOwnerImg, btTradeImg, btpassportImg, btBirthImg, btDriveImg, btCardImg, btNid, btSmart;

    ArrayAdapter<CharSequence> arrayAdapter_business_type_spinner;

    boolean isMissingFlowGorble = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_main);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.paywell_reg_missing);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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


        filePickUtils = new FilePickUtils(this, onFileChoose);
        lifeCycleCallBackManager = filePickUtils.getCallBackManager();


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


        if (isMissingFlowGorble){

            Handler handler =new Handler();
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

            if (EntryMainActivity.regModel != null) {
                if (!regModel.getBusinessaTypeAPIRespose().equals("")) {
                    setBusnicesTypeAdapter(regModel.getBusinessaTypeAPIRespose(), true);
                }
            }
        }
        if (layoutNames.contains("mobile_number")) {
            layoutPhnNumber.setVisibility(View.VISIBLE);

            if (EntryMainActivity.regModel != null) {
                if (regModel != null) {
                    editText_phnNo.setText(regModel.getPhnNumber());
                }
            }
        }
        if (layoutNames.contains("district")) {


            layoutDistrict.setVisibility(View.VISIBLE);
            layoutThana.setVisibility(View.VISIBLE);
            layoutPost.setVisibility(View.VISIBLE);


            if (isMissingFlowGorble) {
                if (regModel != null) {
                    setupDistrictAdapter(regModel.getDistrictAPIRespose(), isMissingFlowGorble);
                }

            } else {

                new GetDistrictResponseAsync().execute(getResources().getString(R.string.district_info_url));
            }


        }
        if (layoutNames.contains("thana")) {


            layoutDistrict.setVisibility(View.VISIBLE);
            layoutThana.setVisibility(View.VISIBLE);
            layoutPost.setVisibility(View.VISIBLE);



            if (isMissingFlowGorble) {
                if (regModel != null) {
                    setAdapterThana(regModel.getThanaResponseAPIRespose(), isMissingFlowGorble);
                }

            } else {

                new GetDistrictResponseAsync().execute(getResources().getString(R.string.district_info_url));
            }


        }
        if (layoutNames.contains("post_code")) {

            if (isMissingFlowGorble) {

                if (regModel != null) {
                    setupPostCode(regModel.getPostCodeResponseAPIRespose(), isMissingFlowGorble);
                }
            } else {

                new GetDistrictResponseAsync().execute(getResources().getString(R.string.district_info_url));

            }

            layoutDistrict.setVisibility(View.VISIBLE);
            layoutThana.setVisibility(View.VISIBLE);
            layoutPost.setVisibility(View.VISIBLE);
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
            if(!regModel.getOutletName().equals("")){
                btPicOutlet.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btPicOutlet.setCompoundDrawablePadding(100);
            }

            if(!regModel.getNidFront().equals("")){
                btNid.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btNid.setCompoundDrawablePadding(100);
            }

            if(!regModel.getSmartCardFront().equals("")){
                btSmart.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btSmart.setCompoundDrawablePadding(100);
            }

            if(!regModel.getOwnerImage().equals("")){
                btOwnerImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btOwnerImg.setCompoundDrawablePadding(100);
            }

            if(!regModel.getTradeLicense().equals("")){
                btTradeImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btTradeImg.setCompoundDrawablePadding(100);
            }

            if(!regModel.getPassport().equals("")){
                btpassportImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btpassportImg.setCompoundDrawablePadding(100);
            }

            if(!regModel.getBirthCertificate().equals("")){
                btBirthImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btBirthImg.setCompoundDrawablePadding(100);
            }

            if(!regModel.getDrivingLicense().equals("")){
                btDriveImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btDriveImg.setCompoundDrawablePadding(100);
            }

            if(!regModel.getVisitingCard().equals("")){
                btCardImg.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                btCardImg.setCompoundDrawablePadding(100);
            }

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      if (!isMissingFlowGorble){
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
                          new GetThanaResponseAsync().execute(getResources().getString(R.string.district_info_url));
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
                          new GetPostResponseAsync().execute(getResources().getString(R.string.district_info_url));
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
            new BusinessTypeAsync().execute(
                    getResources().getString(R.string.business_type_url));
        }
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("serviceId", regModel.getMMrchantType()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            if (regModel == null) {
                regModel = new RegistrationModel();

            }
            regModel.setBusinessaTypeAPIRespose(result);

            dismissProgressDialog();

            setBusnicesTypeAdapter(result, false);
        }
    }

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

    private class GetDistrictResponseAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
//            showProgressDialog();
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
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            // dismissProgressDialog();

            if (regModel == null) {

                regModel = new RegistrationModel();
            }

            regModel.setDistrictAPIRespose(result);

            setupDistrictAdapter(result, isMissingFlowGorble);
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
                nameValuePairs.add(new BasicNameValuePair("distriID", regModel.getDistrict()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();


            regModel.setThanaResponseAPIRespose(result);


            setAdapterThana(result, isMissingFlowGorble);
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
                nameValuePairs.add(new BasicNameValuePair("distriID", regModel.getDistrict()));
                nameValuePairs.add(new BasicNameValuePair("thanaID", regModel.getThanaName()));
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {

            dismissProgressDialog();

            regModel.setPostCodeResponseAPIRespose(result);

            setupPostCode(result,isMissingFlowGorble);
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
//        str_which_btn_selected = "1";
//        int permissionCheckGallery = ContextCompat.checkSelfPermission(MissingMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (permissionCheckGallery != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
//        } else {
//            galleryIntent();
//        }

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
        str_which_btn_selected = "4";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }


    public void tradeLicenseImgOnClickMissing(View v) {
        str_which_btn_selected = "5";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }

    public void passportImgOnClickMissing(View v) {
        str_which_btn_selected = "6";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }

    public void birthImgOnClickMissing(View v) {
        str_which_btn_selected = "7";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }

    public void drivingImgOnClickMissing(View v) {
        str_which_btn_selected = "8";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }

    public void visitingImgOnClickMissing(View v) {
        str_which_btn_selected = "9";
        int permissionCheck = ContextCompat.checkSelfPermission(MissingMainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MissingMainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_FOR_GALLERY);
        } else {
            galleryIntent();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), 1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                InputStream imageStream;
                try {
                    imageStream = this.getContentResolver().openInputStream(data.getData());
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    encodeTobase64(selectedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MissingMainActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
                }
            } else {

                if (lifeCycleCallBackManager != null) {
                    lifeCycleCallBackManager.onActivityResult(requestCode, resultCode, data);
                }

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
                if (regModel != null) {
                    regModel.setOutletImage(strBuild);
                }
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

            String mMrchantType = regModel.getMMrchantType();
            String mBusinessType = regModel.getBusinessType();
            String mDistrict = regModel.getDistrict();
            String mThanaName = regModel.getThanaName();
            String mPostcodeId = regModel.getPostcodeId();


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

//            checkRequest();
            new RetailerMissingAsync().execute(getResources().getString(R.string.missing_reg_url));
        } else {
            Snackbar snackbar = Snackbar.make(scrollView, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

//    private void checkRequest() {
//        showProgressDialog();
//
//        try {
//                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(MissingMainActivity.this);
//                String imeiOne = telephonyInfo.getImeiSIM1();
//                String imeiTwo = telephonyInfo.getImeiSIM2();
//
//                JSONObject jsonInformationData = new JSONObject();
//                try {
//                    if (layoutNames.contains("outlet_name")) {
//                        jsonInformationData.put("outlet_name", editText_outletName.getText().toString().trim());
//                    }
//                    if (layoutNames.contains("outlet_address")) {
//                        jsonInformationData.put("outlet_address", editText_address.getText().toString().trim());
//                    }
//                    if (layoutNames.contains("owner_name")) {
//                        jsonInformationData.put("owner_name", editText_ownerName.getText().toString().trim());
//                    }
//                    if (layoutNames.contains("business_type")) {
//                        jsonInformationData.put("business_type_id", regModel.getBusinessId());
//                        jsonInformationData.put("business_type", regModel.getBusinessType());
//                    }
//                    if (layoutNames.contains("mobile_number")) {
//                        jsonInformationData.put("mobile_number", editText_phnNo.getText().toString().trim());
//                    }
//                    if (layoutNames.contains("district")) {
//                        jsonInformationData.put("district", regModel.getDistrict());
//                    }
//                    if (layoutNames.contains("thana")) {
//                        jsonInformationData.put("district", regModel.getDistrict());
//                        jsonInformationData.put("thana", regModel.getThanaName());
//                    }
//                    if (layoutNames.contains("post_code")) {
//                        jsonInformationData.put("district", regModel.getDistrict());
//                        jsonInformationData.put("thana", regModel.getThanaName());
//                        jsonInformationData.put("post_office_id", regModel.getPostcodeId());
//                        jsonInformationData.put("post_code", regModel.getPostcodeName());
//                    }
//                    if (layoutNames.contains("landmark")) {
//                        jsonInformationData.put("landmark", editText_landmark.getText().toString().trim());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
//                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                    View snackBarView = snackbar.getView();
//                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                }
//
//                //add data
//                List<NameValuePair> nameValuePairs = new ArrayList<>(12);
//                nameValuePairs.add(new BasicNameValuePair("imei", imeiOne));
//                nameValuePairs.add(new BasicNameValuePair("alternate_imei", imeiTwo));
//                nameValuePairs.add(new BasicNameValuePair("informationData", jsonInformationData.toString()));
//                if (layoutNames.contains("outlet_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("outlet_img", regModel.getOutletImage()));
//                }
//                if (layoutNames.contains("nid_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("nid_img", nid_img));
//                }
//                if (layoutNames.contains("nid_back_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("nid_back_img", nid_back_img));
//                }
//                if (layoutNames.contains("owner_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("owner_img", regModel.getOwnerImage()));
//                }
//                if (layoutNames.contains("trade_license_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("trade_license_img", regModel.getTradeLicense()));
//                }
//                if (layoutNames.contains("image_passport")) {
//                    nameValuePairs.add(new BasicNameValuePair("image_passport", regModel.getPassport()));
//                }
//                if (layoutNames.contains("birth_certificate_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("birth_certificate_img", regModel.getBirthCertificate()));
//                }
//                if (layoutNames.contains("driving_license_imege")) {
//                    nameValuePairs.add(new BasicNameValuePair("driving_license_imege", regModel.getDrivingLicense()));
//                }
//                if (layoutNames.contains("visiting_card_img")) {
//                    nameValuePairs.add(new BasicNameValuePair("visiting_card_img", regModel.getVisitingCard()));
//                }
//
//
//        ApiUtils.getAPIServicePHP7().userInformationForRegistration(regModel).enqueue(new Callback<ResponseBody>() {
//
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                dismissProgressDialog();
//                try {
//                    JSONObject jsonObject = new JSONObject(response.body().string());
//                    String status = jsonObject.getString("status");
//                    String message = jsonObject.getString("message");
//                    showTransferMessage(status, message);
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                    Toast.makeText(EntryForthActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
//                }
//            }
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                dismissProgressDialog();
//                Toast.makeText(EntryForthActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



    private class RetailerMissingAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... data) {

            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            try {
                TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(MissingMainActivity.this);
                String imeiOne = telephonyInfo.getImeiSIM1();
                String imeiTwo = telephonyInfo.getImeiSIM2();

                JSONObject jsonInformationData = new JSONObject();
                try {
                    if (layoutNames.contains("outlet_name")) {
                        jsonInformationData.put("outlet_name", editText_outletName.getText().toString().trim());
                    }
                    if (layoutNames.contains("outlet_address")) {
                        jsonInformationData.put("outlet_address", editText_address.getText().toString().trim());
                    }
                    if (layoutNames.contains("owner_name")) {
                        jsonInformationData.put("owner_name", editText_ownerName.getText().toString().trim());
                    }
                    if (layoutNames.contains("business_type")) {
                        jsonInformationData.put("business_type_id", regModel.getBusinessId());
                        jsonInformationData.put("business_type", regModel.getBusinessType());
                    }
                    if (layoutNames.contains("mobile_number")) {
                        jsonInformationData.put("mobile_number", editText_phnNo.getText().toString().trim());
                    }
                    if (layoutNames.contains("district")) {
                        jsonInformationData.put("district", regModel.getDistrict());
                    }
                    if (layoutNames.contains("thana")) {
                        jsonInformationData.put("district", regModel.getDistrict());
                        jsonInformationData.put("thana", regModel.getThanaName());
                    }
                    if (layoutNames.contains("post_code")) {
                        jsonInformationData.put("district", regModel.getDistrict());
                        jsonInformationData.put("thana", regModel.getThanaName());
                        jsonInformationData.put("post_office_id", regModel.getPostcodeId());
                        jsonInformationData.put("post_code", regModel.getPostcodeName());
                    }
                    if (layoutNames.contains("landmark")) {
                        jsonInformationData.put("landmark", editText_landmark.getText().toString().trim());
                    }

                    if (layoutNames.contains("nid_img")) {
                        jsonInformationData.put("nidNumber", regModel.getNidNumber());
                        jsonInformationData.put("nidName", regModel.getNidName());
                        jsonInformationData.put("nidMotherName", regModel.getNidMotherName());
                        jsonInformationData.put("nidBirthday", regModel.getNidBirthday());
                        jsonInformationData.put("nidAddress", regModel.getNidAddress());
                        jsonInformationData.put("nidFatherName", regModel.getNidFatherName());
                    }

                    if (layoutNames.contains("smartCardFrontPic")) {
                        jsonInformationData.put("smartCardNumber", regModel.getSmartCardNumber());
                        jsonInformationData.put("smartCardName", regModel.getSmartCardName());
                        jsonInformationData.put("smartCardFatherName", regModel.getSmartCardFatherName());
                        jsonInformationData.put("smartCardMotherName", regModel.getSmartCardMotherName());
                        jsonInformationData.put("smartCardBirthday", regModel.getSmartCardBirthday());
                        jsonInformationData.put("smartCardAddress", regModel.getSmartCardAddress());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                }

                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("imei", imeiOne));
                nameValuePairs.add(new BasicNameValuePair("alternate_imei", imeiTwo));
                nameValuePairs.add(new BasicNameValuePair("informationData", jsonInformationData.toString()));
                if (layoutNames.contains("outlet_img")) {
                    nameValuePairs.add(new BasicNameValuePair("outlet_img", regModel.getOutletImage()));
                }
                if (layoutNames.contains("nid_img")) {
                    nameValuePairs.add(new BasicNameValuePair("nid_img", regModel.getNidFront()));
                }
                if (layoutNames.contains("nid_back_img")) {
                    nameValuePairs.add(new BasicNameValuePair("nid_back_img", regModel.getNidBack()));
                }

                if (layoutNames.contains("smartCardFrontPic")) {
                    nameValuePairs.add(new BasicNameValuePair("smartCardFrontPic", regModel.getSmartCardFront()));
                }

                if (layoutNames.contains("smartCardBackPic")) {
                    nameValuePairs.add(new BasicNameValuePair("smartCardBackPic", regModel.getSmartCardBack()));
                }

                if (layoutNames.contains("informationData")) {
                    nameValuePairs.add(new BasicNameValuePair("owner_img", regModel.getOwnerImage()));
                }
                if (layoutNames.contains("trade_license_img")) {
                    nameValuePairs.add(new BasicNameValuePair("trade_license_img", regModel.getTradeLicense()));
                }
                if (layoutNames.contains("image_passport")) {
                    nameValuePairs.add(new BasicNameValuePair("image_passport", regModel.getPassport()));
                }
                if (layoutNames.contains("birth_certificate_img")) {
                    nameValuePairs.add(new BasicNameValuePair("birth_certificate_img", regModel.getBirthCertificate()));
                }
                if (layoutNames.contains("driving_license_imege")) {
                    nameValuePairs.add(new BasicNameValuePair("driving_license_imege", regModel.getDrivingLicense()));
                }
                if (layoutNames.contains("visiting_card_img")) {
                    nameValuePairs.add(new BasicNameValuePair("visiting_card_img", regModel.getVisitingCard()));
                }
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(scrollView, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();

            if (result != null) {
                try {
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
                                Intent intent = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
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

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage(title)
                .setCancelable(true)
                .setPositiveButton("Camara", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        str_which_btn_selected = number;

                        filePickUtils.requestImageCamera(CAMERA_PERMISSION, false, true); // pass false if you dont want to allow image crope


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

    private FilePickUtils.OnFileChoose onFileChoose = new FilePickUtils.OnFileChoose() {
        @Override
        public void onFileChoose(String fileUri, int requestCode, int size) {
            // here you will get captured or selected image<br>

            Bitmap selectedImage = BitmapFactory.decodeFile(fileUri);
            encodeTobase64(selectedImage);


            Log.e("", "");
        }
    };
}
