package com.cloudwell.paywell.services.activity.utility.electricity.desco;

import android.content.DialogInterface;
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
import com.cloudwell.paywell.services.activity.utility.electricity.desco.model.DESCOHistory;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.DateUtils;
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

public class DESCOBillPayActivity extends BaseActivity implements View.OnClickListener {

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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, billNumberList);
                etBill.setThreshold(1);
                etBill.setAdapter(adapter);
                etBill.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        etBill.showDropDown();
                    }
                });


                ArrayAdapter<String> adapterPhone = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.select_dialog_item, payeerNumberList);
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
            new SubmitInquiryAsync().execute(getResources().getString(R.string.desco_bill_enq));
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
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(6);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mPin));
                nameValuePairs.add(new BasicNameValuePair("billNo", mBill));
                nameValuePairs.add(new BasicNameValuePair("payerMobileNo", mPhn));
                nameValuePairs.add(new BasicNameValuePair("service_type", "DESCO_Enquiry"));
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

                        mTotalAmount = jsonObject.getString(TAG_TOTAL_AMOUNT);
                        mTrxId = jsonObject.getString(TAG_TRANSACTION_ID);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);
                        if (!mTotalAmount.equals("0")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(DESCOBillPayActivity.this);
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(DESCOBillPayActivity.this);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(DESCOBillPayActivity.this);
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
            new SubmitBillAsync().execute(getString(R.string.desco_bill_pay));
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(8);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mPin));
                nameValuePairs.add(new BasicNameValuePair("billNo", mBill));
                nameValuePairs.add(new BasicNameValuePair("payerMobileNo", mPhn));
                nameValuePairs.add(new BasicNameValuePair("service_type", "DESCO"));
                nameValuePairs.add(new BasicNameValuePair("transId", mTrxId));
                nameValuePairs.add(new BasicNameValuePair("totalAmount", mTotalAmount));
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


                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DESCOBillPayActivity.this);
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


                    } else {


                        String msg = jsonObject.getString(TAG_MESSAGE);
                        String msg_text = jsonObject.getString(TAG_MESSAGE_TEXT);
                        String trx_id = jsonObject.getString(TAG_TRANSACTION_ID);

                        AlertDialog.Builder builder = new AlertDialog.Builder(DESCOBillPayActivity.this);
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
