package com.cloudwell.paywell.services.activity.utility.electricity.wasa;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.SubmitBill;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.WASABillInfoModel;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.model.WasaHistory;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.ocr.OCRActivity;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.github.chrisbanes.photoview.PhotoView;

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

public class WASABillPayActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_OCR = 1001;

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn;
    private EditText etPin;
    private ImageView imageView, ivOcrScan;
    private Button btnConfirm;
    private String mBill, mPhn, mPin, mTrxId, mTotalAmount;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";
    private AsyncTask<Void, Void, Void> insertWasaHistoryAsyncTask;
    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();
    private AsyncTask<Void, Void, Void> getAllWasaHistoryAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasa_bill_pay);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_wasa_billpay_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_WASA_BILL_PAY);

        addRecentUsedList();
    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu= new RecentUsedMenu(StringConstant.KEY_home_utility_wasa_pay, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, 0, 19);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.wasaLinearLayout);

        TextView _mPin = findViewById(R.id.tvWasaPin);
        TextView _mBill = findViewById(R.id.tvWasaBill);
        TextView _mPhn = findViewById(R.id.tvWasaPhn);

        etPin = findViewById(R.id.wasa_pin_no);
        etBill = findViewById(R.id.wasa_bill);
        etPhn = findViewById(R.id.wasa_phn);

        imageView = findViewById(R.id.imageView_info);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivOcrScan = findViewById(R.id.ivOcrScan);
        ivOcrScan.setOnClickListener(this);

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



        getAllWasaHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                List<WasaHistory> allDESCOHistory = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllWasaHistory();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(WASABillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(WASABillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
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
        } else if (v.getId() == R.id.ivOcrScan) {
            Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
            intent.putExtra(OCRActivity.REQUEST_FROM, OCRActivity.KEY_WASA);
            startActivityForResult(intent, REQUEST_CODE_OCR);
        }
    }

    private void showBillImage() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_image_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        photoView.setImageResource(R.drawable.ic_help_wasa_bill);

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

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(WASABillPayActivity.this).getRID());

        WASABillInfoModel model = new WASABillInfoModel();
        model.setBillNo(mBill);
        model.setPassword(mPin);
        model.setPayerMobileNo(mPhn);
        model.setUsername(mAppHandler.getUserName());
        model.setReference_id(uniqueKey);


        ApiUtils.getAPIServiceV2().getWASABillInfo(model).enqueue(new Callback<ResponseBody>() {
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(WASABillPayActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(WASABillPayActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(WASABillPayActivity.this);
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

        SubmitBill submitBill = new SubmitBill();
        submitBill.setBillNo(mBill);
        submitBill.setPassword(mPin);
        submitBill.setPayerMobileNo(mPhn);
        submitBill.setTotalAmount(mTotalAmount);
        submitBill.setTransId(mTrxId);
        submitBill.setUsername(mAppHandler.getUserName());

        ApiUtils.getAPIServiceV2().submitWASABillPay(submitBill).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200){

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString(TAG_STATUS);

                        if (status.equals("200")){
                            String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                            String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                            AlertDialog.Builder builder = new AlertDialog.Builder(WASABillPayActivity.this);
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

                            insertWasaHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {

                                    String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                                    WasaHistory wasaHistory = new WasaHistory();
                                    wasaHistory.setBilNumber(mBill);
                                    wasaHistory.setPayerPhoneNumber(mPhn);
                                    wasaHistory.setDate(currentDataAndTIme);
                                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insertWasaHistory(wasaHistory);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(WASABillPayActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OCR){
            String data1 = data.getExtras().getString("data", "");
            etBill.setText(""+data1);

        }
    }

    @Override
    public void onBackPressed() {
        if (getAllWasaHistoryAsyncTask != null) {
            getAllWasaHistoryAsyncTask.cancel(true);

        }

        if (insertWasaHistoryAsyncTask != null) {
            insertWasaHistoryAsyncTask.cancel(true);
        }


        finish();
    }
}
