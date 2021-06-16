package com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model.DESCOPrepaidHistory;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model.DescoBillPaySubmit;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model.DescoBillPaySubmitResponse;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model.DescoInquiryResponse;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.model.DescoRequestInquiryModel;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DescoPrepaidBillPayActivity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_OCR = 1001;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn;
    private EditText etPin, etAmount;
    private ImageView imageView;
    private Button btnConfirm;
    private String mBill;
    private String mPhn;
    private String mPin;
    private String mTrxId;
    private String mAmount;
    private ImageView ivOcrScanner;


    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";

    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();


    AsyncTask<Void, Void, Void> getAllDescoPrepaidHistoryAsyncTask;
    AsyncTask<Void, Void, Void> insertDescoPrepaidHistoryAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desco_prepaid_bill_pay);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_desco_prepaid_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY);

        addRecentUsedList();
    }

    private void initializeView() {
        mLinearLayout = findViewById(R.id.utilityLinearLayout);

        TextView _mPin = findViewById(R.id.tvDescoPin);
        TextView _mBill = findViewById(R.id.tvDescoBillNo);
        TextView _mPhn = findViewById(R.id.tvDescoPhn);
        TextView _mAmount = findViewById(R.id.tvDescoAmount);

        etPin = findViewById(R.id.pin_no);
        etBill = findViewById(R.id.mycash_bill);
        etPhn = findViewById(R.id.mycash_phn);
        etAmount = findViewById(R.id.amountDescoET);
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
            _mAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            etAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            etBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            etAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
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


        getAllDescoPrepaidHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                List<DESCOPrepaidHistory> allDESCOHistory = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllDESCOPrepaidHistory();
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DescoPrepaidBillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(DescoPrepaidBillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
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


    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu= new RecentUsedMenu(StringConstant.KEY_home_utility_desco_prepaid_string, StringConstant.KEY_home_utility, IconConstant.KEY_ic_desco_prepaid, 4, 48);
        addItemToRecentListInDB(recentUsedMenu);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            mBill = etBill.getText().toString().trim();
            mPhn = etPhn.getText().toString().trim();
            mPin = etPin.getText().toString().trim();
            mAmount = etAmount.getText().toString().trim();
            if (mPin.length() == 0) {
                etPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (mBill.length() == 0) {
                etBill.setError(Html.fromHtml("<font color='red'>" + getString(R.string.bill_no_error_msg) + "</font>"));
            } else if (mPhn.length() != 11) {
                etPhn.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else if (mAmount.length() == 0) {
                etAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
            } else {
                submitInquiryConfirm();
            }
        } else if (v == imageView) {
            showBillImage();
        } else if (v.getId() == R.id.ivOcrScanner) {
            Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
            intent.putExtra(OCRActivity.REQUEST_FROM, OCRActivity.KEY_DESCO_PREPAID);
            startActivityForResult(intent, REQUEST_CODE_OCR);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OCR) {
            String data1 = data.getExtras().getString("data", "");
            etBill.setText("" + data1);

        }
    }

    private void showBillImage() {
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_image_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        photoView.setImageResource(R.drawable.ic_desco_prepaid_card_help);

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

            submitInquiryConfirmDesco();
        }
    }


    private void submitInquiryConfirmDesco(){

        showProgressDialog();

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());
        DescoRequestInquiryModel descoRequestInquiryModel = new DescoRequestInquiryModel();
        descoRequestInquiryModel.setPassword(mPin);
        descoRequestInquiryModel.setBillNo(mBill);
        descoRequestInquiryModel.setPayerMobileNo(mPhn);
        descoRequestInquiryModel.setAmount(mAmount);
        descoRequestInquiryModel.setFormat("json");
        descoRequestInquiryModel.setUsername(mAppHandler.getUserName());
        descoRequestInquiryModel.setRefId(uniqueKey);

        ApiUtils.getAPIServiceV2().descoInquiryRequest(descoRequestInquiryModel).enqueue(new Callback<DescoInquiryResponse>() {
            @Override
            public void onResponse(Call<DescoInquiryResponse> call, Response<DescoInquiryResponse> response) {

                dismissProgressDialog();

                DescoInquiryResponse model = response.body();
                if (model.getApiStatus() == 200) {


                   // submitBillConfirm(model);

                    if (model.getDescoInquiryResponseDetails().getStatus() == 200) {

                        String message = model.getDescoInquiryResponseDetails().getMessage();
                        String trx_id = model.getDescoInquiryResponseDetails().getTransId();

                        AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(message + "\n\nPayWell Trx ID: " + trx_id);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                submitBillConfirm(model);
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
                        showErrorMessage(model.getDescoInquiryResponseDetails().getMessage(), model.getDescoInquiryResponseDetails().getTransId());
                    }

                } else {
                    showInquiryErrorMessage(model.getApiStatusName());
                }
            }

            @Override
            public void onFailure(Call<DescoInquiryResponse> call, Throwable t) {

                t.printStackTrace();

                dismissProgressDialog();


            }
        });
    }

    private void showInquiryErrorMessage(String apiStatusName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
        builder.setMessage(apiStatusName);
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

    private void showErrorMessage(String message, String trx_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
        builder.setMessage(message+ "\n\nPayWell Trx ID: " + trx_id);
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



    private void submitBillConfirm(DescoInquiryResponse model) {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {

            descoSubmilBillPay(model);
        }
    }

    private void descoSubmilBillPay(DescoInquiryResponse model) {

        showProgressDialog();
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());
        DescoBillPaySubmit descoBillPaySubmit = new DescoBillPaySubmit();
        descoBillPaySubmit.setUsername( mAppHandler.getUserName());
        descoBillPaySubmit.setPassword(mPin);
        descoBillPaySubmit.setPayerMobileNo(mPhn);
        descoBillPaySubmit.setBillNo(mBill);
        descoBillPaySubmit.setTotalAmount(""+model.getDescoInquiryResponseDetails().getTotalAmount());
        descoBillPaySubmit.setTransId(model.getDescoInquiryResponseDetails().getTransId());
        descoBillPaySubmit.setFormat("json");
        descoBillPaySubmit.setRefId(uniqueKey);


        ApiUtils.getAPIServiceV2().descoBillPayement(descoBillPaySubmit).enqueue(new Callback<DescoBillPaySubmitResponse>() {
            @Override
            public void onResponse(Call<DescoBillPaySubmitResponse> call, Response<DescoBillPaySubmitResponse> response) {

                DescoBillPaySubmitResponse billPaySubmitResponse = response.body();

                if (billPaySubmitResponse.getApiStatus()== 200){

                    if(billPaySubmitResponse.getResponseDetails().getStatus()==200){

                        insertDescoPrepaidHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {

                                String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                                DESCOPrepaidHistory descoprepaidHistory = new DESCOPrepaidHistory();
                                descoprepaidHistory.setBilNumber(mBill);
                                descoprepaidHistory.setPayerPhoneNumber(mPhn);
                                descoprepaidHistory.setDate(currentDataAndTIme);
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insert(descoprepaidHistory);
                                return null;
                            }
                        }.execute();


                        String msg_text = billPaySubmitResponse.getResponseDetails().getMsgText();

                        AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(msg_text);
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

                    }else{

                        String message = billPaySubmitResponse.getResponseDetails().getMessage();
                        String trx_id = billPaySubmitResponse.getResponseDetails().getTransId();

                        AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(message + "\nPayWell Trx ID: " + trx_id);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setCancelable(true);
                        AlertDialog alert = builder.create();
                        alert.setCanceledOnTouchOutside(true);
                        alert.show();


                    }


                }else{


                    String getApiStatusName = billPaySubmitResponse.getApiStatusName();
                    String trx_id = billPaySubmitResponse.getResponseDetails().getTransId();

                    AlertDialog.Builder builder = new AlertDialog.Builder(DescoPrepaidBillPayActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(getApiStatusName + "\nPayWell Trx ID: " + trx_id);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

                            dismissProgressDialog();

                        }
                    });
                    builder.setCancelable(true);
                    AlertDialog alert = builder.create();
                    alert.setCanceledOnTouchOutside(true);
                    alert.show();


                }

       }


            @Override
            public void onFailure(Call<DescoBillPaySubmitResponse> call, Throwable t) {
                t.printStackTrace();
                showInquiryErrorMessage(model.getApiStatusName());
                dismissProgressDialog();
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
        if (getAllDescoPrepaidHistoryAsyncTask != null) {
            getAllDescoPrepaidHistoryAsyncTask.cancel(true);

        }

        if (insertDescoPrepaidHistoryAsyncTask != null) {
            insertDescoPrepaidHistoryAsyncTask.cancel(true);
        }


        finish();
    }



}
