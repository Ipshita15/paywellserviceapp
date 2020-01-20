package com.cloudwell.paywell.services.activity.utility.ivac;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.model.IvacHistory;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

public class IvacFeePayActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    public static String TAG_CENTER_DETAILS = "centerDetails";
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private ConstraintLayout mConstraintLayout;
    private Spinner spnr_center;
    private ArrayAdapter<String> arrayAdapter_center;
    private ArrayList<String> center_id_array, center_name_array, center_amount_array;
    private String str_centerId = "", str_amount = "", password = "", webFile = "", passportNo = "", phnNum = "";
    private TextView textViewAmount;
    private EditText editTextPassword, editTextWebFile, editTextPassport;
    private AppCompatAutoCompleteTextView   editTextPhnNum;
    Boolean isCenterUserLock = false;

    private static String TAG_RESPONSE_IVAC_CENTER_ID = "center_id";
    private static String TAG_RESPONSE_IVAC_CENTER_NAME = "center_name";
    private static String TAG_RESPONSE_IVAC_CENTER_AMOUNT = "amount";

    private static String TAG_RESPONSE_IVAC_STATUS = "status";
    private static String TAG_RESPONSE_IVAC_MSG = "message";
    private static String TAG_RESPONSE_IVAC_CENTER_DETAILS = "centerDetails";


    private static final int PERMISSION_OPEN_CAMERA = 100;
    private int state = 0;
    private ConnectionDetector cd;

    private AsyncTask<String, Integer, String> mTransactionLogAsync;
    private AsyncTask<String, String, String> mConfirmFeePayAsync;
    private CheckBox checkBoxForCenterLock;
    private AsyncTask<Void, Void, Void> insertIvacHistoryAsyncTask;
    private AsyncTask<Void, Void, Void> getAllIvacHistoryAsyncTask;
    List<String> payeerNumberList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivac_fee_pay);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_ivac_fee_pay_title);
        }

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        initializeData();

        cd = new ConnectionDetector(AppController.getContext());
        if (cd.isConnectingToInternet()) {
            mTransactionLogAsync = new TransactionLogAsync().execute(getResources().getString(R.string.utility_ivac_get_center));
        } else {
            Snackbar snackbar = Snackbar.make(mConstraintLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_IVAC_BILL_PAY);

    }

    private void initializeData() {
        mConstraintLayout = findViewById(R.id.constrainLayout);
        spnr_center = findViewById(R.id.spinner_center);
        spnr_center.setOnTouchListener(this);

        TextView _mPin = findViewById(R.id.tvIvacPin);
        TextView _mCenter = findViewById(R.id.tvIvacCenter);
        TextView _mAmount = findViewById(R.id.tvIvacAmount);
        TextView _mWeb = findViewById(R.id.tvIvacWebFileNo);
        TextView _mPassport = findViewById(R.id.tvIvacPassport);
        TextView _mPhn = findViewById(R.id.tvIvacPhn);
        Button _mBtn = findViewById(R.id.btnIvacSubmit);
        checkBoxForCenterLock = findViewById(R.id.checkBoxForCenterLock);

        editTextPassword = findViewById(R.id.etPassword);
        textViewAmount = findViewById(R.id.tvFeeAmount);
        editTextWebFile = findViewById(R.id.etWebFile);
        editTextPassport = findViewById(R.id.etPassport);
        editTextPhnNum = findViewById(R.id.etPhnNum);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            editTextPassword.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mCenter.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            textViewAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mWeb.setTypeface(AppController.getInstance().getOxygenLightFont());
            editTextWebFile.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mPassport.setTypeface(AppController.getInstance().getOxygenLightFont());
            editTextPassport.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            editTextPhnNum.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mBtn.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            editTextPassword.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mCenter.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            textViewAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mWeb.setTypeface(AppController.getInstance().getAponaLohitFont());
            editTextWebFile.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mPassport.setTypeface(AppController.getInstance().getAponaLohitFont());
            editTextPassport.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            editTextPhnNum.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mBtn.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        editTextWebFile.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editTextWebFile) {
            @Override
            public boolean onDrawableClick() {
                state = 1;
                if (ActivityCompat.checkSelfPermission(IvacFeePayActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission has not been granted.
                    ActivityCompat.requestPermissions(IvacFeePayActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_OPEN_CAMERA);
                } else {
                    //initiate scan with our custom scan activity
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }
                return true;
            }
        });

        editTextPassport.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(editTextPassport) {
            @Override
            public boolean onDrawableClick() {
                state = 2;
                if (ActivityCompat.checkSelfPermission(IvacFeePayActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // permission has not been granted.
                    ActivityCompat.requestPermissions(IvacFeePayActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_OPEN_CAMERA);
                } else {
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                }
                return true;
            }
        });


        handleCenterLockCheckBox();



        getAllIvacHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                payeerNumberList.clear();
                List<IvacHistory> ivacHistories = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllIvacHistoryHistory();
                for (int i = 0; i < ivacHistories.size(); i++) {
                    payeerNumberList.add(ivacHistories.get(i).getPayerPhoneNumber());
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(Void list) {
                super.onPostExecute(list);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(IvacFeePayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
                editTextPhnNum.setThreshold(1);
                editTextPhnNum.setAdapter(adapter);
                editTextPhnNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        editTextPhnNum.showDropDown();
                    }
                });




            }

        }.execute();


        editTextPhnNum.setOnTouchListener((v, event) -> {
            editTextPhnNum.showDropDown();
            return false;
        });

    }

    private void handleCenterLockCheckBox() {
        boolean ivacCenterLock = mAppHandler.isIVACCenterLock();
        if (ivacCenterLock) {
            checkBoxForCenterLock.setChecked(true);
            centerLock();
        } else {
            checkBoxForCenterLock.setChecked(false);
            centerUnlock();
        }


        checkBoxForCenterLock.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                centerLock();
            } else {
                centerUnlock();
            }

        });
    }

    private void centerUnlock() {
        isCenterUserLock = false;
        mAppHandler.setIVACCenterLock(isCenterUserLock);
        checkBoxForCenterLock.setText(R.string.is_center_unlock);
        spnr_center.setBackgroundColor(getResources().getColor(R.color.white));
        spnr_center.setEnabled(true);
        spnr_center.setOnItemSelectedListener(this);
        if (str_centerId!=null && !str_centerId.isEmpty()){
            mAppHandler.setCenterId(str_centerId);
        }
    }

    private void centerLock() {
        checkBoxForCenterLock.setText(R.string.is_center_lock);
        spnr_center.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        isCenterUserLock = true;
        mAppHandler.setIVACCenterLock(isCenterUserLock);
        if (str_centerId!=null && !str_centerId.isEmpty()){
            mAppHandler.setCenterId(str_centerId);
        }
    }

    private void setupCenterDetailsSpiner() {
        try {
            center_id_array = new ArrayList<>();
            center_name_array = new ArrayList<>();
            center_amount_array = new ArrayList<>();

            center_id_array.add("select one");
            center_name_array.add("Select One");
            center_amount_array.add("select one");
            JSONArray jsonArray = new JSONArray(TAG_CENTER_DETAILS);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                String center_id = object.getString(TAG_RESPONSE_IVAC_CENTER_ID);
                String center_name = object.getString(TAG_RESPONSE_IVAC_CENTER_NAME);
                String amount = object.getString(TAG_RESPONSE_IVAC_CENTER_AMOUNT);

                center_id_array.add(center_id);
                center_name_array.add(center_name);
                center_amount_array.add(amount);
            }

            arrayAdapter_center = new ArrayAdapter<>(IvacFeePayActivity.this, android.R.layout.simple_spinner_dropdown_item, center_name_array);
            spnr_center.setAdapter(arrayAdapter_center);
            spnr_center.setOnItemSelectedListener(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private void autoSelectedCenterPosition() {
        int centerDropDownPogistion = mAppHandler.getCenterDropDownPogistion();
        spnr_center.setSelection(centerDropDownPogistion);
        boolean ivacCenterLock = mAppHandler.isIVACCenterLock();
        if (ivacCenterLock){
            str_centerId=mAppHandler.getSavedCenterId();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_OPEN_CAMERA: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    new IntentIntegrator(IvacFeePayActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                } else {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (state == 1) {
                    editTextWebFile.setText("");
                    editTextWebFile.clearFocus();
                    editTextWebFile.setText(result.getContents());
                } else if (state == 2) {
                    editTextPassport.setText("");
                    editTextPassport.clearFocus();
                    editTextPassport.setText(result.getContents());
                }
            }
        }
    }

    public void confirmIvacOnClick(View view) {
        if (view.getId() == R.id.btnIvacSubmit) {
            String pass = editTextPassword.getText().toString();
            String spnr = spnr_center.getSelectedItem().toString();
            String web = editTextWebFile.getText().toString();
            String passport = editTextPassport.getText().toString();
            String phn = editTextPhnNum.getText().toString();
            if (pass.isEmpty()) {
                editTextPassword.setError(getString(R.string.pin_no_error_msg));
            } else if (spnr.equals("Select One")) {
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.center_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            } else if ((web.length() <= 3) || (!web.startsWith("BGD"))) {
                editTextWebFile.setError(getString(R.string.input_error_msg));
            } else if (passport.isEmpty()) {
                editTextPassport.setError(getString(R.string.input_error_msg));
            } else if (phn.length() != 11) {
                editTextPhnNum.setError(getString(R.string.phone_no_error_msg));
            } else {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    password = editTextPassword.getText().toString().trim();
                    webFile = editTextWebFile.getText().toString().trim();
                    passportNo = editTextPassport.getText().toString().trim();
                    phnNum = editTextPhnNum.getText().toString().trim();

//                    Log.d("IVAC_DETAILS","IMEI/"+mAppHandler.getImeiNo()+"PASS/"+password+"WEB_FILE/"+webFile+"AMOUNT/"+str_amount+"PASSPORT/"+passportNo+"CENTER_ID/"+str_centerId+"PHONE_NUMBER/"+phnNum);

                    mConfirmFeePayAsync = new ConfirmFeePayAsync().execute(
                            getResources().getString(R.string.utility_ivac_fee_pay));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mTransactionLogAsync != null) {
            mTransactionLogAsync.cancel(true);
        }

        if (mConfirmFeePayAsync != null) {
            mConfirmFeePayAsync.cancel(true);
        }

        super.onDestroy();

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (!isCenterUserLock) {
            try {
                str_centerId = "";
                str_centerId = center_id_array.get(position);
                str_amount = center_amount_array.get(position);
                String amountText = getString(R.string.tk_des) + " " + str_amount;

                // store selected position
                mAppHandler.setCenterDropDownPogistion(position);

                textViewAmount.setText(amountText);
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return isCenterUserLock;
    }

    private class TransactionLogAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    JSONObject object = new JSONObject(result);

                    String status = object.getString(TAG_RESPONSE_IVAC_STATUS);
                    String msg = object.getString(TAG_RESPONSE_IVAC_MSG);

                    if (status.equalsIgnoreCase("200")) {
                        JSONArray jsonArray = object.getJSONArray(TAG_RESPONSE_IVAC_CENTER_DETAILS);
                        IvacFeePayActivity.TAG_CENTER_DETAILS = jsonArray.toString();

                        setupCenterDetailsSpiner();
                        autoSelectedCenterPosition();

                    } else {
                        Snackbar snackbar = Snackbar.make(mConstraintLayout, msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } catch (Exception ex) {
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private class ConfirmFeePayAsync extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;

            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(8);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("amount", str_amount));
                nameValuePairs.add(new BasicNameValuePair("web_file_no", webFile));
                nameValuePairs.add(new BasicNameValuePair("passport_no", passportNo));
                nameValuePairs.add(new BasicNameValuePair("center_no", str_centerId));
                nameValuePairs.add(new BasicNameValuePair("cust_mobile", phnNum));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                nameValuePairs.add(new BasicNameValuePair(ParameterUtility.KEY_REF_ID, uniqueKey));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    final String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    String trx = jsonObject.getString("trx_id");

                    AlertDialog.Builder builder = new AlertDialog.Builder(IvacFeePayActivity.this);//ERROR ShowDialog cannot be resolved to a type
                    builder.setTitle("Result");
                    builder.setMessage(msg + "\n\n" + trx);

                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            onBackPressed();

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    insertIvacHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                            IvacHistory ivacHistory = new IvacHistory();
                            ivacHistory.setPayerPhoneNumber(phnNum);
                            ivacHistory.setDate(currentDataAndTIme);
                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insertIvacHistory(ivacHistory);
                            return null;
                        }
                    }.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mConstraintLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getAllIvacHistoryAsyncTask != null) {
            getAllIvacHistoryAsyncTask.cancel(true);

        }

        if (insertIvacHistoryAsyncTask != null) {
            insertIvacHistoryAsyncTask.cancel(true);
        }


        finish();
    }
}
