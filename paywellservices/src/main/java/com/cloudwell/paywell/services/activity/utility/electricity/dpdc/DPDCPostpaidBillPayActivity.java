package com.cloudwell.paywell.services.activity.utility.electricity.dpdc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.model.DPDCHistory;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.ocr.OCRActivity;
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

public class DPDCPostpaidBillPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_OCR = 1001;


    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AppCompatAutoCompleteTextView etBill, etPhn, etLocation;
    private EditText etPin;
    private ImageView ivInfoBill, ivInfoLocation, ivOCR;
    private Spinner spnr_month, spnr_year;
    private Button btnConfirm;
    private String mBill, mPhn, mLocation, mPin, mMonth, mYear, mTotalAmount, mTrxId;
    private int month = 0, year = 0;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_MESSAGE_TEXT = "msg_text";
    private static final String TAG_TRANSACTION_ID = "trans_id";
    private static final String TAG_TOTAL_AMOUNT = "total_amount";
    private AsyncTask<Void, Void, Void> insertDPDCHistoryAsyncTask;
    private AsyncTask<Void, Void, Void> getAllDPDCHistoryAsyncTask;
    List<String> billNumberList = new ArrayList<>();
    List<String> payeerNumberList = new ArrayList<>();
    List<String> locationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpdc_bill_pay);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_dpdc_bill_title);
        }
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        initializeView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY);

    }


    private void initializeView() {
        mLinearLayout = findViewById(R.id.utilityLinearLayout);

        TextView _mPin = findViewById(R.id.tvDpdcPin);
        TextView _mBill = findViewById(R.id.tvDpdcBill);
        TextView _mPhn = findViewById(R.id.tvDpdcPhn);
        TextView _mLocation = findViewById(R.id.tvDpdcLocation);

        etPin = findViewById(R.id.pin_no);
        etBill = findViewById(R.id.mycash_bill);
        etPhn = findViewById(R.id.mycash_phn);
        etLocation = findViewById(R.id.mycash_location);

        ivInfoBill = findViewById(R.id.imageView_infoBill);
        ivInfoLocation = findViewById(R.id.imageView_infoLocation);
        btnConfirm = findViewById(R.id.mycash_confirm);

        spnr_month = findViewById(R.id.monthSpinner);
        spnr_year = findViewById(R.id.yearSpinner);
        ivOCR = findViewById(R.id.ivOCR);
        ivOCR.setOnClickListener(this);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            etPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            etBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            etPhn.setTypeface(AppController.getInstance().getOxygenLightFont());
            _mLocation.setTypeface(AppController.getInstance().getOxygenLightFont());
            etLocation.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            etBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            etPhn.setTypeface(AppController.getInstance().getAponaLohitFont());
            _mLocation.setTypeface(AppController.getInstance().getAponaLohitFont());
            etLocation.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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

        btnConfirm.setOnClickListener(this);
        ivInfoBill.setOnClickListener(this);
        ivInfoLocation.setOnClickListener(this);


        etBill.setOnTouchListener((v, event) -> {
            etBill.showDropDown();
            return false;
        });


        etPhn.setOnTouchListener((v, event) -> {
            etPhn.showDropDown();
            return false;
        });

        etLocation.setOnTouchListener((v, event) -> {
            etLocation.showDropDown();
            return false;
        });


        getAllDPDCHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                billNumberList.clear();
                payeerNumberList.clear();
                locationList.clear();
                List<DPDCHistory> allDESCOHistory = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllDPDCHistory();
                for (int i = 0; i < allDESCOHistory.size(); i++) {
                    billNumberList.add(allDESCOHistory.get(i).getBilNumber());
                    payeerNumberList.add(allDESCOHistory.get(i).getPayerPhoneNumber());
                    locationList.add(allDESCOHistory.get(i).getLocation());
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DPDCPostpaidBillPayActivity.this, android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(DPDCPostpaidBillPayActivity.this, android.R.layout.select_dialog_item, payeerNumberList);
                etPhn.setThreshold(1);
                etPhn.setAdapter(adapterPhone);
                etPhn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etPhn.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterLocation = new ArrayAdapter<String>(DPDCPostpaidBillPayActivity.this, android.R.layout.select_dialog_item, locationList);
                etLocation.setThreshold(1);
                etLocation.setAdapter(adapterLocation);
                etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etLocation.showDropDown();
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
            mLocation = etLocation.getText().toString().trim();
            mPin = etPin.getText().toString().trim();
            if (mPin.length() == 0) {
                etPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (mBill.length() == 0) {
                etBill.setError(Html.fromHtml("<font color='red'>" + getString(R.string.bill_no_error_msg) + "</font>"));
            } else if (mPhn.length() != 11) {
                etPhn.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else if (mLocation.length() == 0) {
                etLocation.setError(Html.fromHtml("<font color='red'>" + getString(R.string.location_error_msg) + "</font>"));
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
        } else if (v.equals(ivInfoBill)) {
            showBillImage(1);
        } else if (v.equals(ivInfoLocation)) {
            showBillImage(2);
        } else if (v.getId() == R.id.ivOCR) {

            Intent intent = new Intent(getApplicationContext(), OCRActivity.class);
            intent.putExtra(OCRActivity.REQUEST_FROM, OCRActivity.KEY_DPDC);
            startActivityForResult(intent, REQUEST_CODE_OCR);

        }
    }


    private void showBillImage(int number) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_custom_image_layout, null);
        PhotoView photoView = mView.findViewById(R.id.imageView);
        if (number == 1) {
            photoView.setImageResource(R.drawable.ic_help_dpdc_bill_one);
        } else {
            photoView.setImageResource(R.drawable.ic_help_dpdc_bill_two);
        }

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
            new SubmitInquiryAsync().execute(getString(R.string.dpdc_postpaid_bill_inq));
        }
    }

    private class SubmitInquiryAsync extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;

            String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(DPDCPostpaidBillPayActivity.this).getRID());

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(9);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mPin));
                nameValuePairs.add(new BasicNameValuePair("billNo", mBill));
                nameValuePairs.add(new BasicNameValuePair("payerMobileNo", mPhn));
                nameValuePairs.add(new BasicNameValuePair("billMonth", mMonth));
                nameValuePairs.add(new BasicNameValuePair("location", mLocation));
                nameValuePairs.add(new BasicNameValuePair("billYear", mYear));
                nameValuePairs.add(new BasicNameValuePair("service_type", "DPDC_Enquiry"));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                nameValuePairs.add(new BasicNameValuePair(ParameterUtility.KEY_REF_ID, uniqueKey));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        mTotalAmount = jsonObject.getString(TAG_TOTAL_AMOUNT);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);
                        mTrxId = jsonObject.getString(TAG_TRANSACTION_ID);
                        if (!mTotalAmount.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DPDCPostpaidBillPayActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DPDCPostpaidBillPayActivity.this);
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
                    } else {
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DPDCPostpaidBillPayActivity.this);
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
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void submitBillConfirm() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(this.getSupportFragmentManager());
        } else {
            new SubmitBillAsync().execute(getString(R.string.dpdc_postpaid_bill_pay));
        }
    }

    private class SubmitBillAsync extends AsyncTask<String, Void, String> {


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
                List<NameValuePair> nameValuePairs = new ArrayList<>(11);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mPin));
                nameValuePairs.add(new BasicNameValuePair("billNo", mBill));
                nameValuePairs.add(new BasicNameValuePair("payerMobileNo", mPhn));
                nameValuePairs.add(new BasicNameValuePair("billMonth", mMonth));
                nameValuePairs.add(new BasicNameValuePair("location", mLocation));
                nameValuePairs.add(new BasicNameValuePair("billYear", mYear));
                nameValuePairs.add(new BasicNameValuePair("service_type", "DPDC"));
                nameValuePairs.add(new BasicNameValuePair("totalAmount", mTotalAmount));
                nameValuePairs.add(new BasicNameValuePair("transId", mTrxId));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DPDCPostpaidBillPayActivity.this);
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
                        insertDPDCHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {

                                String currentDataAndTIme = DateUtils.INSTANCE.getCurrentDataAndTIme();
                                DPDCHistory dpdcHistory = new DPDCHistory();
                                dpdcHistory.setBilNumber(mBill);
                                dpdcHistory.setPayerPhoneNumber(mPhn);
                                dpdcHistory.setLocation(mLocation);
                                dpdcHistory.setDate(currentDataAndTIme);
                                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().insertDPDCHistory(dpdcHistory);
                                return null;
                            }
                        }.execute();
                    } else {
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DPDCPostpaidBillPayActivity.this);
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
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_OCR){
            String data1 = data.getExtras().getString("data", "");
            etBill.setText(""+data1);

        }
    }

    @Override
    public void onBackPressed() {
        if (getAllDPDCHistoryAsyncTask != null) {
            getAllDPDCHistoryAsyncTask.cancel(true);

        }

        if (insertDPDCHistoryAsyncTask != null) {
            insertDPDCHistoryAsyncTask.cancel(true);
        }


        finish();
    }

}
