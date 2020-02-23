package com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.BillPayMOdel;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.BillPayResponseModel;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOBillInfo;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOBillInfoResponse;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.ResponseDetails;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.model.DESCOHistory;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.model.BillDatum;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.ocr.OCRActivity;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DESCOPostpaidBillPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_OCR = 1001;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn;
    private EditText etPin;
    private ImageView imageView;
    private Button btnConfirm;
    private String mBill;
    private String mPhn;
    private String mPin;
    private String mTrxId;
    private String mTotalAmount;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";
    private ImageView ivOcrScanner;

    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();


    AsyncTask<Void, Void, Void> getAllDescoHistoryAsyncTask;
    AsyncTask<Void, Void, Void> insertDescoHistoryAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desco_bill_pay);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_desco_pay_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY);

    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.utilityLinearLayout);

        TextView _mPin = findViewById(R.id.tvDescoPin);
        TextView _mBill = findViewById(R.id.tvDescoBillNo);
        TextView _mPhn = findViewById(R.id.tvDescoPhn);

        etPin = findViewById(R.id.pin_no);
        etBill = findViewById(R.id.mycash_bill);
        etPhn = findViewById(R.id.mycash_phn);
        imageView = findViewById(R.id.imageView_info);
        btnConfirm = findViewById(R.id.mycash_confirm);

        ivOcrScanner = findViewById(R.id.ivOcrScanner);
        ivOcrScanner.setOnClickListener(this);

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


        etBill.setOnTouchListener((v, event) -> {
            etBill.showDropDown();
            return false;
        });


        etPhn.setOnTouchListener((v, event) -> {
            etPhn.showDropDown();
            return false;
        });


        getAllDescoHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                List<DESCOHistory> allDESCOHistory = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllDESCOHistory();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DESCOPostpaidBillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(DESCOPostpaidBillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
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
        } else if (v == imageView) {
            showBillImage();
        }else if (v.getId() == R.id.ivOcrScanner){
            Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
            intent.putExtra(OCRActivity.REQUEST_FROM, OCRActivity.KEY_DESCO);
            startActivityForResult(intent, REQUEST_CODE_OCR);
        }
    }

    private void showBillImage() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_image_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        photoView.setImageResource(R.drawable.ic_help_desco_bill);

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

        DESCOBillInfo billInfo = new DESCOBillInfo();
        billInfo.setUsername(mAppHandler.getUserName());
        billInfo.setBillNo(mBill);
        billInfo.setPassword(mPin);
        billInfo.setPayerMobileNo(mPhn);

        ApiUtils.getAPIServiceV2().getDESCOBillInfo(billInfo).enqueue(new Callback<DESCOBillInfoResponse>() {
            @Override
            public void onResponse(Call<DESCOBillInfoResponse> call, Response<DESCOBillInfoResponse> response) {
                dismissProgressDialog();

                if (response.code() == 200){
                    DESCOBillInfoResponse descoBillInfoResponse = response.body();
                    if (descoBillInfoResponse.getApiStatus()==200){

                        ResponseDetails details = descoBillInfoResponse.getResponseDetails();
                        if (details.getStatus() == 200){

                            mTotalAmount = details.getTotalAmount();
                            mTrxId = details.getTransId();
                            String msg_text = details.getMsgText();
                            String trx_id = details.getTransId();

                            if (!mTotalAmount.equals("0")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DESCOPostpaidBillPayActivity.this);
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
                            }else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(DESCOPostpaidBillPayActivity.this);
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

                            String msg = details.getMessage();
                            String msg_text =details.getMsgText();
                            String trx_id = details.getTransId();

                            AlertDialog.Builder builder = new AlertDialog.Builder(DESCOPostpaidBillPayActivity.this);
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
                        showErrorMessagev1(descoBillInfoResponse.getApiStatusName());
                    }

                }else {
                    showErrorMessagev1(getString(R.string.try_again_msg));
                }
            }

            @Override
            public void onFailure(Call<DESCOBillInfoResponse> call, Throwable t) {
                dismissProgressDialog();
            }
        });



    }


    private void submitBillConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            billSubmission();
        }
    }


    private void billSubmission(){
        showProgressDialog();

        BillPayMOdel billPayMOdel = new BillPayMOdel();
        billPayMOdel.setUsername(mAppHandler.getUserName());
        billPayMOdel.setBillNo(mBill);
        billPayMOdel.setPassword(mPin);
        billPayMOdel.setPayerMobileNo(mPhn);
       // billPayMOdel.setTotalAmount(mTotalAmount);
        billPayMOdel.setTotalAmount("133");
        //billPayMOdel.setTransId(mTrxId);
        billPayMOdel.setTransId("yb8by4rhfuir");

        ApiUtils.getAPIServiceV2().confirmBillPay(billPayMOdel).enqueue(new Callback<BillPayResponseModel>() {
            @Override
            public void onResponse(Call<BillPayResponseModel> call, Response<BillPayResponseModel> response) {
             dismissProgressDialog();
             if (response.code() == 200){

                 if (response.body().getApiStatus()==200){

                     if (response.body().getResponseDetails().getStatus() ==200){


                         ResponseDetails responseDetails = response.body().getResponseDetails();

                         insertDescoHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                             @Override
                             protected Void doInBackground(Void... voids) {

                                 String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                                 DESCOHistory descoHistory = new DESCOHistory();
                                 descoHistory.setBilNumber(mBill);
                                 descoHistory.setPayerPhoneNumber(mPhn);
                                 descoHistory.setDate(currentDataAndTIme);
                                 DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insert(descoHistory);
                                 return null;
                             }
                         }.execute();


                         String msg_text =responseDetails.getMsgText();
                         String trx_id = responseDetails.getTransId();

                         AlertDialog.Builder builder = new AlertDialog.Builder(DESCOPostpaidBillPayActivity.this);
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

                         String msg = response.body().getResponseDetails().getMessage();
                         String msg_text = response.body().getResponseDetails().getMsgText();
                         String trx_id = response.body().getResponseDetails().getTransId();

                         AlertDialog.Builder builder = new AlertDialog.Builder(DESCOPostpaidBillPayActivity.this);
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

             }else {
                 dismissProgressDialog();
                 showErrorMessagev1(getString(R.string.try_again_msg));
             }


            }

            @Override
            public void onFailure(Call<BillPayResponseModel> call, Throwable t) {
                dismissProgressDialog();
                showErrorMessagev1(getString(R.string.try_again_msg));

            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OCR){
            String data1 = data.getExtras().getString("data", "");
            etBill.setText(""+data1);

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

    @Override
    public void onBackPressed() {
        if (getAllDescoHistoryAsyncTask != null) {
            getAllDescoHistoryAsyncTask.cancel(true);

        }

        if (insertDescoHistoryAsyncTask != null) {
            insertDescoHistoryAsyncTask.cancel(true);
        }


        finish();
    }
}
