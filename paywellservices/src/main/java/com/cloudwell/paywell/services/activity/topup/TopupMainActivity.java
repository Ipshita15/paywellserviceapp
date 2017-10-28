package com.cloudwell.paywell.services.activity.topup;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.MyHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

public class TopupMainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout topUpLayout;
    private int addNoFlag = 0;
    private static String mHotLine;
    private String mPhnNo;
    private String mAmount;
    private ConnectionDetector cd;
    public boolean statusCode = false;

    private static String PIN_NO = "unknown";

    private static AppHandler mAppHandler;
    private LayoutInflater inflater;

    TranslateAnimation slideInAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
    TranslateAnimation slideOutAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1,
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);

    private RelativeLayout mRelativeLayout;

    RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    String selectedLimit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topup_main);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);

        topUpLayout = (LinearLayout) findViewById(R.id.topUpAddLayout);
        Button buttonSubmit = (Button) findViewById(R.id.btnSubmit);

        ((Button) mRelativeLayout.findViewById(R.id.btnSubmit)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        slideInAnim.setDuration(50);
        slideOutAnim.setDuration(50);
        assert buttonSubmit != null;
        buttonSubmit.setOnClickListener(this);

        ImageView imageViewAdd = (ImageView) findViewById(R.id.imageAdd);
        imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addNoFlag < AppHandler.MULTIPLE_TOPUP_LIMIT) {
                    addAnotherNo();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.topup_limit_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        ImageView imageViewTrxLog = (ImageView) findViewById(R.id.transLogBtn);
        imageViewTrxLog.setOnClickListener(this);

        ImageView imageViewInq = (ImageView) findViewById(R.id.enquiryBtn);
        imageViewInq.setOnClickListener(this);

        ImageView imageViewOffer = (ImageView) findViewById(R.id.imageOffer);
        imageViewOffer.setOnClickListener(this);

        inflater = getLayoutInflater();
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        addAnotherNo();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            if (cd.isConnectingToInternet()) {
                showCurrentTopupLog();
            } else {
                Snackbar snackbar = Snackbar.make(v, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (v.getId() == R.id.transLogBtn) {
            if (cd.isConnectingToInternet()) {
                showLimitPrompt();
            } else {
                Snackbar snackbar = Snackbar.make(v, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (v.getId() == R.id.enquiryBtn) {
            showEnquiryPrompt();
        } else if (v.getId() == R.id.imageOffer) {
            startActivity(new Intent(TopupMainActivity.this, OperatorMenuActivity.class));
        }
    }

    private void addAnotherNo() {
        ++addNoFlag;
        final View topUpView = inflater.inflate(R.layout.activity_topup_layout, null);
        Button removeBtn = (Button) topUpView.findViewById(R.id.removeLayoutImgBtn);

        ((EditText) topUpView.findViewById(R.id.phoneNo1)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((EditText) topUpView.findViewById(R.id.amount1)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((RadioButton) topUpView.findViewById(R.id.preRadioButton1)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((RadioButton) topUpView.findViewById(R.id.postRadioButton1)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --addNoFlag;
                slideOutAnim
                        .setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                topUpView.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        topUpLayout.removeView(topUpView);
                                    }

                                }, 100);
                            }
                        });
                topUpView.startAnimation(slideOutAnim);

            }
        });
        topUpLayout.addView(topUpView);
        topUpView.startAnimation(slideInAnim);
    }

    // Transaction Inquiry
    private void showEnquiryPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
        builder.setTitle(R.string.phone_no_title_msg);

        final EditText enqNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        enqNoET.setLayoutParams(lp);
        enqNoET.setInputType(InputType.TYPE_CLASS_NUMBER);
        enqNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(11);
        enqNoET.setFilters(FilterArray);
        builder.setView(enqNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (enqNoET.getText().toString().length() < 11) {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.phone_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    showEnquiryPrompt();
                } else {
                    if (!cd.isConnectingToInternet()) {
                        AppHandler.showDialog(getSupportFragmentManager());
                    } else {
                        new TransEnquiryAsync().execute(getResources().getString(R.string.inq_top),
                                "iemi_no=" + mAppHandler.getImeiNo(),
                                "&msisdn=" + enqNoET.getText().toString());
                    }
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @SuppressWarnings("deprecation")
    private class TransEnquiryAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;
        private String inquiryReceipt;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 10000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 10000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient client = new MyHttpClient(httpParameters, getApplicationContext());
            HttpGet request = new HttpGet(params[0] + params[1] + params[2]);

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                try {
                    String splitSingleAt[] = result.split("@");
//                    if (splitSingleAt[0].equalsIgnoreCase("362") ||
//                            splitSingleAt[0].equalsIgnoreCase("313") || splitSingleAt[0].equalsIgnoreCase("360")) {
                    if (splitSingleAt[0].equalsIgnoreCase("362") || splitSingleAt[0].equalsIgnoreCase("360")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
                        builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
                        builder.setMessage("Message: " + splitSingleAt[5]);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        String phnNo = splitSingleAt[1];
                        String amount = splitSingleAt[2];
                        String packageType = splitSingleAt[3];
                        String trxID = splitSingleAt[4];
                        String message = splitSingleAt[5];
                        String dateTime = AppHandler.timeStampFormat(splitSingleAt[6]);
                        String hotline = splitSingleAt[9];
                        showEnquiryResult(phnNo, amount, packageType, trxID, message, dateTime, hotline);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }

        private void showEnquiryResult(String phone, String amount, String packageType, String trxId, String status, String datetime, String hotline) {
            if (status.equalsIgnoreCase("Successful")) {
                inquiryReceipt = getString(R.string.date_and_time_des) + AppHandler.getCurrentDate() + "," + AppHandler.getCurrentTime()
                        + "\n" + getString(R.string.phone_no_des) + " " + phone
                        + "\n" + getString(R.string.package_type_des) + " " + packageType
                        + "\n" + getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                        + "\n" + getString(R.string.trx_id_des) + " " + trxId
                        + "\n" + getString(R.string.date_des) + " " + datetime
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + hotline;
            } else {
                inquiryReceipt = getString(R.string.date_and_time_des) + AppHandler.getCurrentDate() + "," + AppHandler.getCurrentTime()
                        + "\n" + getString(R.string.phone_no_des) + " " + phone
                        + "\n" + getString(R.string.package_type_des) + " " + packageType
                        + "\n" + getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                        + "\n\n" + getString(R.string.status_des) + " " + status
                        + "\n\n" + getString(R.string.trx_id_des) + " " + trxId
                        + "\n" + getString(R.string.date_des) + " " + datetime
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + hotline;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
            if (status.equalsIgnoreCase("Successful")) {
                builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
            } else {
                builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
            }
            builder.setMessage(inquiryReceipt);
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    // Transaction LOG
    private void showLimitPrompt() {
        // custom dialog
        final AppCompatDialog dialog = new AppCompatDialog(this);
        dialog.setTitle(R.string.log_limit_title_msg);
        dialog.setContentView(R.layout.dialog_trx_limit);

        Button btn_okay = (Button) dialog.findViewById(R.id.buttonOk);

        radioButton_five = (RadioButton) dialog.findViewById(R.id.radio_five);
        radioButton_ten = (RadioButton) dialog.findViewById(R.id.radio_ten);
        radioButton_twenty = (RadioButton) dialog.findViewById(R.id.radio_twenty);
        radioButton_fifty = (RadioButton) dialog.findViewById(R.id.radio_fifty);
        radioButton_hundred = (RadioButton) dialog.findViewById(R.id.radio_hundred);
        radioButton_twoHundred = (RadioButton) dialog.findViewById(R.id.radio_twoHundred);

        radioButton_five.setOnCheckedChangeListener(this);
        radioButton_ten.setOnCheckedChangeListener(this);
        radioButton_twenty.setOnCheckedChangeListener(this);
        radioButton_fifty.setOnCheckedChangeListener(this);
        radioButton_hundred.setOnCheckedChangeListener(this);
        radioButton_twoHundred.setOnCheckedChangeListener(this);

        assert btn_okay != null;
        btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (selectedLimit.isEmpty()) {
                        selectedLimit = "5";
                    }
                    int limit = Integer.parseInt(selectedLimit);
                    if (cd.isConnectingToInternet()) {
                        new TransactionLogAsync().execute(getResources().getString(R.string.trx_log),
                                "iemi_no=" + mAppHandler.getImeiNo(),
                                "&pin_code=1234",
                                "&limit=" + limit);
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, getResources().getString(R.string.connection_error_msg), Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                }
            });
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView.getId() == R.id.radio_five) {
                selectedLimit = "5";
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_ten) {
                selectedLimit = "10";
                radioButton_five.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_twenty) {
                selectedLimit = "20";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_fifty) {
                selectedLimit = "50";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_hundred.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_hundred) {
                selectedLimit = "100";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_twoHundred.setChecked(false);
            }
            if (buttonView.getId() == R.id.radio_twoHundred) {
                selectedLimit = "200";
                radioButton_five.setChecked(false);
                radioButton_ten.setChecked(false);
                radioButton_twenty.setChecked(false);
                radioButton_fifty.setChecked(false);
                radioButton_hundred.setChecked(false);
            }
        }
    }


    @SuppressWarnings("deprecation")
    private class TransactionLogAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 1000000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 1000000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new MyHttpClient(httpParameters, getApplicationContext());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = httpClient.execute(request, responseHandler);
            }  catch (IOException e) {
                e.printStackTrace();
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                if (result.startsWith("200")) {
                    TransLogActivity.TRANSLOG_TAG = result;
                    startActivity(new Intent(TopupMainActivity.this, TransLogActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, result, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void showCurrentTopupLog() {
        if (topUpLayout.getChildCount() > 0) {
            StringBuilder reqStrBuilder = new StringBuilder();
            for (int i = 0; i < topUpLayout.getChildCount(); i++) {
                View singleTopUpView = topUpLayout.getChildAt(i);
                if (singleTopUpView != null) {

                    EditText phoneNoET = (EditText) singleTopUpView.findViewById(R.id.phoneNo1);
                    EditText amountET = (EditText) singleTopUpView.findViewById(R.id.amount1);
                    RadioGroup prePostSelector = (RadioGroup) singleTopUpView.findViewById(R.id.prePostRadioGroup1);

                    String phoneStr = phoneNoET.getText().toString();
                    String amountStr = amountET.getText().toString();
                    String planStr = "prepaid";

                    if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton1) {
                        planStr = getResources().getString(R.string.pre_paid_str);
                    } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton1) {
                        planStr = getResources().getString(R.string.post_paid_str);
                    }

                    if (phoneStr.length() < 11) {
                        phoneNoET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                        return;
                    } else if (amountStr.length() < 1) {
                        amountET.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font>"));
                        return;
                    }
                    reqStrBuilder.append((i + 1) + ". " + getString(R.string.phone_no_des) + " " + phoneStr
                            + "\n " + getString(R.string.amount_des) + " " + amountStr + getString(R.string.tk)
                            + "\n " + getString(R.string.package_type_des) + " " + planStr.substring(0, 1).toUpperCase()
                            + planStr.substring(1).toLowerCase() + "\n\n");
                }
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.conf_topup_title_msg);
            builder.setMessage(reqStrBuilder);
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                    askForPin();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();

        } else {
            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.add_number_amount_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }

    }

    private void askForPin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    dialogInterface.dismiss();
                    PIN_NO = pinNoET.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        new TopUpAsync().execute();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class TopUpAsync extends AsyncTask<Object, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {

            return generateRequestURL();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result != null) {
                if (result.startsWith("313")) {
                    showTransactionLog(result, false);
                } else {
                    if (result.startsWith("200")) {
                        statusCode = true;
                    }
                    showTransactionLog(result, true);
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }

        private String generateRequestURL() {
            String requestString = null;
            if (topUpLayout.getChildCount() > 1) {
                // Multiple
                StringBuilder reqStrBuilder = new StringBuilder();
                reqStrBuilder.append(getResources().getString(R.string.multi_top)
                        + "iemi_no=" + mAppHandler.getImeiNo()
                        + "&pin_code=" + PIN_NO
                        + "&no_of_req=" + topUpLayout.getChildCount());

                for (int i = 0; i < topUpLayout.getChildCount(); i++) {
                    EditText mPhoneNoET, mAmountET;
                    RadioGroup prePostSelector;

                    View singleTopUpView = topUpLayout.getChildAt(i);
                    if (singleTopUpView != null) {
                        mPhoneNoET = (EditText) singleTopUpView.findViewById(R.id.phoneNo1);
                        mAmountET = (EditText) singleTopUpView.findViewById(R.id.amount1);
                        prePostSelector = (RadioGroup) singleTopUpView.findViewById(R.id.prePostRadioGroup1);

                        String phoneStr = mPhoneNoET.getText().toString();
                        String amountStr = mAmountET.getText().toString();
                        String planStr = null;
                        if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton1) {
                            planStr = getResources().getString(R.string.pre_paid_str);
                        } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton1) {
                            planStr = getResources().getString(R.string.post_paid_str);
                        }
                        reqStrBuilder.append("&msisdn" + (i + 1) + "="
                                + phoneStr + "&amount" + (i + 1) + "="
                                + amountStr + "&con_type" + (i + 1) + "="
                                + planStr);
                    }
                }
                requestString = reqStrBuilder.toString();

            } else {
                // Single
                EditText phoneNoET, amountET;
                RadioGroup prePostSelector;

                View singleTopUpView = topUpLayout.getChildAt(0);
                if (singleTopUpView != null) {
                    phoneNoET = (EditText) singleTopUpView.findViewById(R.id.phoneNo1);
                    amountET = (EditText) singleTopUpView.findViewById(R.id.amount1);
                    prePostSelector = (RadioGroup) singleTopUpView.findViewById(R.id.prePostRadioGroup1);

                    String phoneStr = phoneNoET.getText().toString();
                    String amountStr = amountET.getText().toString();
                    String planStr = null;

                    if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton1) {
                        planStr = getResources().getString(R.string.pre_paid_str);
                    } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton1) {
                        planStr = getResources().getString(R.string.post_paid_str);
                    }
                    requestString = getResources().getString(R.string.single_top)
                            + "iemi_no=" + mAppHandler.getImeiNo()
                            + "&msisdn=" + phoneStr
                            + "&amount=" + amountStr
                            + "&con_type=" + planStr
                            + "&pin_code=" + PIN_NO;
                }
            }
            return sendingHTTPSTopupRequest(requestString);
        }
    }

    @SuppressWarnings("deprecation")
    private String sendingHTTPSTopupRequest(String requestString) {
        String responseTxt = null;
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        int timeoutConnection = 1000000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 1000000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Instantiate the custom HttpClient
        HttpClient httpClient = new MyHttpClient(httpParameters, getApplicationContext());
        // HttpClient client = AppController.getInstance().getTrustedHttpClient();
        HttpGet request = new HttpGet(requestString);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            responseTxt = httpClient.execute(request, responseHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseTxt;

    }

    @SuppressWarnings("deprecation")
    private void showTransactionLog(String responseTxt, final boolean isAuthorized) {
        // code for topup receipt dialog
        if (isAuthorized) {
            StringBuilder receiptBuilder = new StringBuilder();
            final StringBuilder topupBuilder = new StringBuilder();
            EditText multiplePhoneNoET, multipleAmountET, singlePhoneNoET, singleAmountET;

            AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);

            if (topUpLayout.getChildCount() > 1 && responseTxt.contains("@@@")) {
                // code for multiple top up
                String[] splitTripleAt = responseTxt.split("@@@");
                for (int i = 0; i < splitTripleAt.length; i++) {
                    if (i == splitTripleAt.length - 1) {
                        mHotLine = splitTripleAt[i].split("@")[2];
                    } else {
                        View multipleTopupView = topUpLayout.getChildAt(i);
                        if (multipleTopupView != null) {
                            multiplePhoneNoET = (EditText) multipleTopupView.findViewById(R.id.phoneNo1);
                            multipleAmountET = (EditText) multipleTopupView.findViewById(R.id.amount1);
                            mPhnNo = multiplePhoneNoET.getText().toString();
                            mAmount = multipleAmountET.getText().toString();
                        }
                        String _status = splitTripleAt[i].split("@")[1];
                        String _trxId = splitTripleAt[i].split("@")[2];

                        topupBuilder.append((i + 1) + ". " + getString(R.string.phone_no_des) + " " + mPhnNo
                                + "\n " + getString(R.string.amount_des) + " " + mAmount + getString(R.string.tk)
                                + "\n " + getString(R.string.trx_id_des) + " " + _trxId
                                + "\n " + getString(R.string.status_des) + " " + _status
                                + "\n\n");
                    }
                }

                receiptBuilder.append(topupBuilder
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + mHotLine);

                builder.setTitle("Result");
            } else {
                // code for single topup
                View singleTopupView = topUpLayout.getChildAt(0);
                if (singleTopupView != null) {
                    singlePhoneNoET = (EditText) singleTopupView.findViewById(R.id.phoneNo1);
                    singleAmountET = (EditText) singleTopupView.findViewById(R.id.amount1);
                    mPhnNo = singlePhoneNoET.getText().toString();
                    mAmount = singleAmountET.getText().toString();

                }
                String[] splitSingleAt = responseTxt.split("@");
                String mStatusMessage = splitSingleAt[1];
                String mTrxId = splitSingleAt[2];
                mHotLine = splitSingleAt[5];
                if (splitSingleAt[0].equals("200")) {
                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
                            + "\n" + getString(R.string.trx_id_des) + " " + mTrxId
                            + "\n\n" + getString(R.string.using_paywell_des)
                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
                } else {
                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
                            + "\n\n" + getString(R.string.status_des) + " " + mStatusMessage
                            + "\n\n" + getString(R.string.trx_id_des) + " " + mTrxId
                            + "\n\n" + getString(R.string.using_paywell_des)
                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
                }
                if (splitSingleAt[0].equals("200")) {
                    builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
                } else {
                    builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
                }
            }
            builder.setMessage(receiptBuilder.toString());

            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                    if (statusCode) {
                        startActivity(new Intent(TopupMainActivity.this, TopupMainActivity.class));
                        finish();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            // 313@AuthenticationError
            AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
            builder.setMessage(R.string.services_alert_msg);
            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    dialogInterface.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
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
        Intent intent = new Intent(TopupMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
