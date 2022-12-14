package com.cloudwell.paywell.services.activity.utility.electricity.westzone;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.model.WZPDCLBillInfo;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.model.WZPDCLBillPayModel;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.model.WestZoneHistory;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.ParameterUtility;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WZPDCLBillPayActivity extends BaseActivity implements View.OnClickListener {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn;
    private EditText etPin;
    private ImageView imageView;
    private Spinner spnr_month, spnr_year;
    private Button btnConfirm;
    private String mBill, mPhn, mPin, mMonth, mYear, mTotalAmount, mTrxId;
    private int month = 0, year = 0;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";


    // all Async
    AsyncTask<String, Void, String> mSubmitInquiryAsync;
    AsyncTask<String, Void, String> mSubmitBillAsync;
    private AsyncTask<Void, Void, Void> insertWestZoneHistoryAsyncTask;
    private AsyncTask<Void, Void, Void> getAllWestZoneHistoryAsyncTask;
    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_west_zone);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_west_zone_pay_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_WZPDCL_BILL_PAY);

    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.utilityLinearLayout);

        TextView _mPin = findViewById(R.id.tvWzpdclPin);
        TextView _mBill = findViewById(R.id.tvWzpdclBillNo);
        TextView _mPhn = findViewById(R.id.tvWzpdclPhn);

        etPin = findViewById(R.id.west_zone_pin_no);
        etBill = findViewById(R.id.west_zone_bill);
        etPhn = findViewById(R.id.west_zone_phn);
        imageView = findViewById(R.id.imageView_info);
        btnConfirm = findViewById(R.id.west_zone_confirm);

        spnr_month = findViewById(R.id.monthSpinner);
        spnr_year = findViewById(R.id.yearSpinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> month_adapter = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the mAdapter to the spinner
        spnr_month.setAdapter(month_adapter);
        spnr_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                month = 0;
            }
        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        List<String> dynamicTwoYear = DateUtils.INSTANCE.getDynamicTwoYear();
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter year_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dynamicTwoYear);
        // Specify the layout to use when the list of choices appears
        year_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the mAdapter to the spinner
        spnr_year.setAdapter(year_adapter);
        spnr_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String year_str = String.valueOf(spnr_year.getSelectedItem());
                year = Integer.parseInt(year_str);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                year = 0;
            }
        });

        spnr_year.setSelection(1);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            etPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            etBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            etPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            etBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        btnConfirm.setOnClickListener(this);
        imageView.setOnClickListener(this);

        getAllWestZoneHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                List<WestZoneHistory> allDESCOHistory = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllWestZoneHistory();
                for (int i = 0; i < allDESCOHistory.size(); i++) {
                    billNumberList.add(allDESCOHistory.get(i).getBilNumber());
                    payeerNumberList.add(allDESCOHistory.get(i).getPayerPhoneNumber());
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WZPDCLBillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(WZPDCLBillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
                etPhn.setThreshold(1);
                etPhn.setAdapter(adapterPhone);
                etPhn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etPhn.showDropDown();
                    }
                });

            }

        }.execute();

        etBill.setOnTouchListener((v, event) -> {
            etBill.showDropDown();
            return false;
        });


        etPhn.setOnTouchListener((v, event) -> {
            etPhn.showDropDown();
            return false;
        });


    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            mBill = etBill.getText().toString().trim();
            mPhn = etPhn.getText().toString().trim();
            mPin = etPin.getText().toString().trim();
            if (mPin.length() == 0) {
                etPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (mBill.length() == 0) {
                etBill.setError(Html.fromHtml("<font color='red'>" + getString(R.string.consumer_no_error_msg) + "</font>"));
            } else if (mPhn.length() != 11) {
                etPhn.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else if (month == 0 || year == 0) {
                if (month == 0) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.select_month_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.select_year_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                if (month < 10) {
                    mMonth = "0" + month;
                } else {
                    mMonth = String.valueOf(month);
                }
                mYear = String.valueOf(year);
                submitInquiryConfirm();
            }
        } else if (v == imageView) {
            showBillImage();
        }
    }

    private void showBillImage() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_image_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        photoView.setImageResource(R.drawable.ic_help_west_zone_bill);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).
                        setView(mView);
        builder.create().show();
    }

    private void submitInquiryConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            submitInquiry();
        }
    }

    private void submitInquiry(){
        showProgressDialog();
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(WZPDCLBillPayActivity.this).getRID());

        WZPDCLBillInfo info = new WZPDCLBillInfo();
        info.setUsername(mAppHandler.getUserName());
        info.setBillMonth(mMonth);
        info.setBillNo(mBill);
        info.setBillYear(mYear);
        info.setPassword(mPin);
        info.setPayerMobileNo(mPhn);
        info.setReference_id(uniqueKey);

        ApiUtils.getAPIServiceV2().getWZPDCLBillInfo(info).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dismissProgressDialog();
                if (response.code() == 200){

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int status = jsonObject.getInt(TAG_STATUS);
                        if (status == 200){
                            mTotalAmount = jsonObject.getString(TAG_TOTAL_AMOUNT);
                            mTrxId = jsonObject.getString(TAG_TRANSACTION_ID);
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);
                            if (!mTotalAmount.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WZPDCLBillPayActivity.this);
                                builder.setTitle("Result");
                                builder.setMessage(msg_text + "\n\n" + getString(R.string.phone_no_des) + " " + mPhn + "\n\nPayWell Trx ID: " + trx_id);
                                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        submitBillConfirm();
                                    }
                                });
                                builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(true);
                                AlertDialog alert = builder.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(WZPDCLBillPayActivity.this);
                                builder.setTitle("Result");
                                builder.setMessage(msg_text + "\n\n" + getString(R.string.phone_no_des) + " " + mPhn + "\n\nPayWell Trx ID: " + trx_id);
                                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(true);
                                AlertDialog alert = builder.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();
                            }

                        }else {
                            String msg = jsonObject.getString(TAG_MESSAGE);
                            String msg_text="",trx_id="";
                            if (jsonObject.has(TAG_MESSAGE_TEXT)){
                                msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            }
                            if (jsonObject.has(TAG_MESSAGE_TEXT)){
                                trx_id = jsonObject.getString(TAG_TRANSACTION_ID);
                            }
                                AlertDialog.Builder builder = new AlertDialog.Builder(WZPDCLBillPayActivity.this);
                                builder.setMessage(msg + "\n" + msg_text + "\nPayWell Trx ID: " + trx_id);
                                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(true);
                                AlertDialog alert = builder.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();
                            }

                    }catch (Exception e){
                        e.printStackTrace();
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


    private void submitBillConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            submitBill();
        }
    }

    private void submitBill(){
            showProgressDialog();

        WZPDCLBillPayModel model = new WZPDCLBillPayModel();
        model.setBillMonth(mMonth);
        model.setBillNo(mBill);
        model.setBillYear(mYear);
        model.setPassword(mPin);
        model.setPayerMobileNo(mPhn);
        model.setTotalAmount(mTotalAmount);
        model.setTransId(mTrxId);
        model.setUsername(mAppHandler.getUserName());

        ApiUtils.getAPIServiceV2().submitWZPDCLBillPay(model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString(TAG_STATUS);

                        if (status.equals("200")){
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                            AlertDialog.Builder builder = new AlertDialog.Builder(WZPDCLBillPayActivity.this);
                            builder.setTitle("Result");
                            builder.setMessage(msg_text + "\nPayWell Trx ID: " + trx_id);
                            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    onBackPressed();
                                }
                            });
                            builder.setCancelable(true);
                            AlertDialog alert = builder.create();
                            alert.setCanceledOnTouchOutside(true);
                            alert.show();

                            insertWestZoneHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {

                                    String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                                    WestZoneHistory westZoneHistory = new WestZoneHistory();
                                    westZoneHistory.setBilNumber(mBill);
                                    westZoneHistory.setPayerPhoneNumber(mPhn);
                                    westZoneHistory.setDate(currentDataAndTIme);
                                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insertWestZoneHistory(westZoneHistory);
                                    return null;
                                }
                            }.execute();


                        }else {
                            String msg = jsonObject.getString(TAG_MESSAGE);
                            String msg_text="",trx_id="";
                            if (jsonObject.has(TAG_MESSAGE_TEXT)){
                                msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            }
                            if (jsonObject.has(TAG_MESSAGE_TEXT)){
                                trx_id = jsonObject.getString(TAG_TRANSACTION_ID);
                            }
                                AlertDialog.Builder builder = new AlertDialog.Builder(WZPDCLBillPayActivity.this);
                                builder.setMessage(msg + "\n" + msg_text + "\nPayWell Trx ID: " + trx_id);
                                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(true);
                                AlertDialog alert = builder.create();
                                alert.setCanceledOnTouchOutside(true);
                                alert.show();
                            }
                    }catch (Exception e){
                        e.printStackTrace();
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
        if (getAllWestZoneHistoryAsyncTask != null) {
            getAllWestZoneHistoryAsyncTask.cancel(true);

        }

        if (insertWestZoneHistoryAsyncTask != null) {
            insertWestZoneHistoryAsyncTask.cancel(true);
        }


        finish();
    }

    @Override
    protected void onDestroy() {
        if (mSubmitInquiryAsync != null) {
            mSubmitInquiryAsync.cancel(true);
        }

        if (mSubmitBillAsync != null) {
            mSubmitBillAsync.cancel(true);
        }
        super.onDestroy();
    }
}
