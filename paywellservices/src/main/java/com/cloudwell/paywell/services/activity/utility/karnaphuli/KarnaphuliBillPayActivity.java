package com.cloudwell.paywell.services.activity.utility.karnaphuli;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.KarnaphuliHistory;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.requestPojo.KgdlcBillInfoRequest;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.requestPojo.SubmitBillRequestPojo;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.responsePojo.ResponseDetails;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.responsePojo.SubmitBillResponse;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.model.responsePojo.SubmitInquiry;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KarnaphuliBillPayActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn;
    private EditText etPin;
    private Button btnConfirm;
    private String mBill, mPhn, mPin, mTrxId, mTotalAmount;
    private AsyncTask<Void, Void, Void> insertKarnaphuliHistoryAsyncTask;
    private AsyncTask<Void, Void, Void> getAllKarnaphuliHistoryAsyncTask;
    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karnaphuli_bill_pay);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_karnaphuli_bill_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU_BILL_PAY);


        addRecentUsedList();

    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu= new RecentUsedMenu(StringConstant.KEY_home_utility_karnaphuli_bill_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, 0, 31);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.utilityLinearLayout);

        TextView _mPin = findViewById(R.id.tvKarnaphuliPin);
        TextView _mBill = findViewById(R.id.tvKarnaphuliBillNo);
        TextView _mPhn = findViewById(R.id.tvKarnaphuliPhn);

        etPin = findViewById(R.id.pin_no);
        etBill = findViewById(R.id.mycash_bill);
        etPhn = findViewById(R.id.mycash_phn);
        btnConfirm = findViewById(R.id.mycash_confirm);

//        etBill.setKeyListener(DigitsKeyListener.getInstance("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-"));

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

        etBill.setOnTouchListener((v, event) -> {
            etBill.showDropDown();
            return false;
        });
        etPhn.setOnTouchListener((v, event) -> {
            etPhn.showDropDown();
            return false;
        });

        getAllKarnaphuliHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                List<KarnaphuliHistory> karnaphuliHistories = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllKarnaphuliHistoryHistory();
                for (int i = 0; i < karnaphuliHistories.size(); i++) {
                    payeerNumberList.add(karnaphuliHistories.get(i).getPayerPhoneNumber());
                    billNumberList.add(karnaphuliHistories.get(i).getBilNumber());
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(KarnaphuliBillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });

                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(KarnaphuliBillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
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
                etBill.setError(Html.fromHtml("<font color='red'>" + getString(R.string.bill_no_error_msg) + "</font>"));
            } else if (mPhn.length() != 11) {
                etPhn.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else {
                submitInquiryConfirm();
            }
        }
    }

    private void submitInquiryConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            submitInquiry();
        }
    }

    private void submitBillConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {

            submitBill();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void submitInquiry(){
        showProgressDialog();

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());

        KgdlcBillInfoRequest request = new KgdlcBillInfoRequest();
        request.setUsername(mAppHandler.getUserName());
        request.setBillNo(mBill);
        request.setPassword(mPin);
        request.setPayerMobileNo(mPhn);
        request.setReference_id(uniqueKey);


        ApiUtils.getAPIServiceV2().kgdlcSubmitInquiry(request).enqueue(new Callback<SubmitInquiry>() {
            @Override
            public void onResponse(Call<SubmitInquiry> call, Response<SubmitInquiry> response) {
                dismissProgressDialog();

                if (response.code() == 200){
                    if(response.body().getApiStatus() == 200){

                        ResponseDetails responseDetails = response.body().getResponseDetails();
                        if (responseDetails.getStatus() == 200){

                            mTotalAmount = responseDetails.getTotal_amount();
                            mTrxId = responseDetails.getTransId();
                            String msg_text = responseDetails.getMsgText();
                            String trx_id = responseDetails.getTransId();
                            if (!mTotalAmount.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(KarnaphuliBillPayActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(KarnaphuliBillPayActivity.this);
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
                            showErrorMessagev1(responseDetails.getMessage());
                        }

                    }else {
                        showErrorMessagev1(response.body().getApiStatusName());
                    }

                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onFailure(Call<SubmitInquiry> call, Throwable t) {
                dismissProgressDialog();

            }
        });

    }



    private void submitBill(){

        showProgressDialog();

        SubmitBillRequestPojo billRequestPojo = new SubmitBillRequestPojo();
        billRequestPojo.setUsername(mAppHandler.getUserName());
        billRequestPojo.setBillNo(mPin);
        billRequestPojo.setBillNo(mBill);
        billRequestPojo.setPayerMobileNo(mPhn);
        billRequestPojo.setTotalAmount(mTotalAmount);
        billRequestPojo.setTransId(mTrxId);

        //billRequestPojo.setTotalAmount("300");
        //billRequestPojo.setTransId("202002241355189750");


        ApiUtils.getAPIServiceV2().kgdlcSubmitBill(billRequestPojo).enqueue(new Callback<SubmitBillResponse>() {
            @Override
            public void onResponse(Call<SubmitBillResponse> call, Response<SubmitBillResponse> response) {
                dismissProgressDialog();
                if (response.code() == 200){
                    //TODO get response from hema and then implementation
                    if (response.code() == 200){
                        if(response.body().getApiStatus() == 200){

                            ResponseDetails responseDetails = response.body().getResponseDetails();
                            if (responseDetails.getStatus() == 200){

                                String msg_text = responseDetails.getMsgText();
                                String trx_id = responseDetails.getTransId();

                                AlertDialog.Builder builder = new AlertDialog.Builder(KarnaphuliBillPayActivity.this);
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


                            }else {

                                String msg = responseDetails.getMessage();
                                String msg_text = responseDetails.getMsgText();
                                String trx_id = responseDetails.getTransId();

                                AlertDialog.Builder builder = new AlertDialog.Builder(KarnaphuliBillPayActivity.this);
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

                        }else {
                            showErrorMessagev1(response.body().getApiStatusName());
                        }
                 }

            }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onFailure(Call<SubmitBillResponse> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (getAllKarnaphuliHistoryAsyncTask != null) {
            getAllKarnaphuliHistoryAsyncTask.cancel(true);

        }

        if (insertKarnaphuliHistoryAsyncTask != null) {
            insertKarnaphuliHistoryAsyncTask.cancel(true);
        }


        finish();
    }
}
