package com.cloudwell.paywell.services.activity.topup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupData;
import com.cloudwell.paywell.services.activity.topup.model.TopupDatum;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.activity.topup.offer.OfferMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.DrawableClickListener;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TopupMainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static String KEY_TAG = TopupMainActivity.class.getName();
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

    private TranslateAnimation slideInAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1, Animation.RELATIVE_TO_PARENT, 0,
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);
    private TranslateAnimation slideOutAnim = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 1,
            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0);

    private RelativeLayout mRelativeLayout;

    private RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    private String selectedLimit = "";

    String key;
    private int operator_number;
    private static final String TAG_STATUS = "status";
    private static final String TAG_RECHARGE_OFFER = "RechargeOffer";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_main);

//        try {
//            key = getIntent().getStringExtra("key");
//            Log.d("Key", key);
//        }catch (Exception e){
//
//        }


        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
    }

    private void initView() {
        mRelativeLayout = findViewById(R.id.linearLayout);

        topUpLayout = findViewById(R.id.topUpAddLayout);
        Button buttonSubmit = findViewById(R.id.btnSubmit);

        ((Button) mRelativeLayout.findViewById(R.id.btnSubmit)).setTypeface(AppController.getInstance().getOxygenLightFont());

        slideInAnim.setDuration(50);
        slideOutAnim.setDuration(50);
        assert buttonSubmit != null;
        buttonSubmit.setOnClickListener(this);

        ImageView imageViewAdd = findViewById(R.id.imageAdd);
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
        ImageView imageViewTrxLog = findViewById(R.id.transLogBtn);
        imageViewTrxLog.setOnClickListener(this);

        ImageView imageViewInq = findViewById(R.id.enquiryBtn);
        imageViewInq.setOnClickListener(this);

        ImageView imageViewOffer = findViewById(R.id.imageOffer);
        imageViewOffer.setOnClickListener(this);

        inflater = getLayoutInflater();
        cd = new ConnectionDetector(AppController.getContext());
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
            // onClickBundleOffer(key);
        }
    }

    private void addAnotherNo() {
        ++addNoFlag;
        final View topUpView = inflater.inflate(R.layout.activity_topup_layout, null);
        EditText phoneNoET = topUpView.findViewById(R.id.phoneNo);
        Button removeBtn = topUpView.findViewById(R.id.removeLayoutImgBtn);

        topUpView.setTag(addNoFlag);

        if (addNoFlag == 1) {
            removeBtn.setVisibility(View.GONE);
        }

        phoneNoET.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(phoneNoET) {
            @Override
            public boolean onDrawableClick() {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, (Integer) topUpView.getTag());
                return true;
            }
        });

        ((EditText) topUpView.findViewById(R.id.phoneNo)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((EditText) topUpView.findViewById(R.id.amount)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((RadioButton) topUpView.findViewById(R.id.preRadioButton)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((RadioButton) topUpView.findViewById(R.id.postRadioButton)).setTypeface(AppController.getInstance().getOxygenLightFont());

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumOne = null;
                    View singleTopUpViewOne = topUpLayout.getChildAt(0);
                    if (singleTopUpViewOne != null) {
                        EditText phoneNoETOne = singleTopUpViewOne.findViewById(R.id.phoneNo);
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumOne = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumOne.isEmpty() && !phoneNumOne.contains("+") && !phoneNumOne.contains("-")
                                && !phoneNumOne.contains(" ") && !phoneNumOne.startsWith("88")) {
                            phoneNoETOne.setText(phoneNumOne);
                        } else {
                            if (phoneNumOne.contains("+")) {
                                phoneNumOne = phoneNumOne.replace("+", "");
                            }
                            if (phoneNumOne.contains("-")) {
                                phoneNumOne = phoneNumOne.replace("-", "");
                            }
                            if (phoneNumOne.contains(" ")) {
                                phoneNumOne = phoneNumOne.replace(" ", "");
                            }
                            if (phoneNumOne.startsWith("88")) {
                                phoneNumOne = phoneNumOne.replace("88", "");
                            }
                            phoneNoETOne.setText(phoneNumOne);
                        }
                    }
                }
                break;
            case 2:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumTwo = null;
                    View singleTopUpViewTwo = topUpLayout.getChildAt(1);
                    if (singleTopUpViewTwo != null) {
                        EditText phoneNoETTwo = singleTopUpViewTwo.findViewById(R.id.phoneNo);
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumTwo = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumTwo.isEmpty() && !phoneNumTwo.contains("+") && !phoneNumTwo.contains("-")
                                && !phoneNumTwo.contains(" ") && !phoneNumTwo.startsWith("88")) {
                            phoneNoETTwo.setText(phoneNumTwo);
                        } else {
                            if (phoneNumTwo.contains("+")) {
                                phoneNumTwo = phoneNumTwo.replace("+", "");
                            }
                            if (phoneNumTwo.contains("-")) {
                                phoneNumTwo = phoneNumTwo.replace("-", "");
                            }
                            if (phoneNumTwo.contains(" ")) {
                                phoneNumTwo = phoneNumTwo.replace(" ", "");
                            }
                            if (phoneNumTwo.startsWith("88")) {
                                phoneNumTwo = phoneNumTwo.replace("88", "");
                            }
                            phoneNoETTwo.setText(phoneNumTwo);
                        }
                    }
                }
                break;
            case 3:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumThree = null;
                    View singleTopUpViewThree = topUpLayout.getChildAt(2);
                    if (singleTopUpViewThree != null) {
                        EditText phoneNoETThree = singleTopUpViewThree.findViewById(R.id.phoneNo);
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumThree = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumThree.isEmpty() && !phoneNumThree.contains("+") && !phoneNumThree.contains("-")
                                && !phoneNumThree.contains(" ") && !phoneNumThree.startsWith("88")) {
                            phoneNoETThree.setText(phoneNumThree);
                        } else {
                            if (phoneNumThree.contains("+")) {
                                phoneNumThree = phoneNumThree.replace("+", "");
                            }
                            if (phoneNumThree.contains("-")) {
                                phoneNumThree = phoneNumThree.replace("-", "");
                            }
                            if (phoneNumThree.contains(" ")) {
                                phoneNumThree = phoneNumThree.replace(" ", "");
                            }
                            if (phoneNumThree.startsWith("88")) {
                                phoneNumThree = phoneNumThree.replace("88", "");
                            }
                            phoneNoETThree.setText(phoneNumThree);
                        }
                    }
                }
                break;
            case 4:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumFour = null;
                    View singleTopUpViewFour = topUpLayout.getChildAt(3);
                    if (singleTopUpViewFour != null) {
                        EditText phoneNoETFour = singleTopUpViewFour.findViewById(R.id.phoneNo);
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumFour = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumFour.isEmpty() && !phoneNumFour.contains("+") && !phoneNumFour.contains("-")
                                && !phoneNumFour.contains(" ") && !phoneNumFour.startsWith("88")) {
                            phoneNoETFour.setText(phoneNumFour);
                        } else {
                            if (phoneNumFour.contains("+")) {
                                phoneNumFour = phoneNumFour.replace("+", "");
                            }
                            if (phoneNumFour.contains("-")) {
                                phoneNumFour = phoneNumFour.replace("-", "");
                            }
                            if (phoneNumFour.contains(" ")) {
                                phoneNumFour = phoneNumFour.replace(" ", "");
                            }
                            if (phoneNumFour.startsWith("88")) {
                                phoneNumFour = phoneNumFour.replace("88", "");
                            }
                            phoneNoETFour.setText(phoneNumFour);
                        }
                    }
                }
                break;
            case 5:
                if (resultCode == Activity.RESULT_OK) {
                    String phoneNumFive = null;
                    View singleTopUpViewFive = topUpLayout.getChildAt(4);
                    if (singleTopUpViewFive != null) {
                        EditText phoneNoETFive = singleTopUpViewFive.findViewById(R.id.phoneNo);
                        Cursor phones = getContentResolver()
                                .query(data.getData(),
                                        null,
                                        null,
                                        null,
                                        null);
                        while (phones.moveToNext()) {
                            phoneNumFive = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        }
                        phones.close();
                        if (!phoneNumFive.isEmpty() && !phoneNumFive.contains("+") && !phoneNumFive.contains("-")
                                && !phoneNumFive.contains(" ") && !phoneNumFive.startsWith("88")) {
                            phoneNoETFive.setText(phoneNumFive);
                        } else {
                            if (phoneNumFive.contains("+")) {
                                phoneNumFive = phoneNumFive.replace("+", "");
                            }
                            if (phoneNumFive.contains("-")) {
                                phoneNumFive = phoneNumFive.replace("-", "");
                            }
                            if (phoneNumFive.contains(" ")) {
                                phoneNumFive = phoneNumFive.replace(" ", "");
                            }
                            if (phoneNumFive.startsWith("88")) {
                                phoneNumFive = phoneNumFive.replace("88", "");
                            }
                            phoneNoETFive.setText(phoneNumFive);
                        }
                    }
                }
                break;
        }
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
                        new TransEnquiryAsync().execute(getResources().getString(R.string.inq_top), enqNoET.getText().toString());
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
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("iemi_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("msisdn", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                try {
                    String splitSingleAt[] = result.split("@");
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
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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

        private void showEnquiryResult(String phone, String amount, String packageType, String trxId, String status, String datetime, String hotline) {
            if (status.equalsIgnoreCase("Successful")) {
                inquiryReceipt = getString(R.string.phone_no_des) + " " + phone
                        + "\n" + getString(R.string.package_type_des) + " " + packageType
                        + "\n" + getString(R.string.amount_des) + " " + amount + getString(R.string.tk)
                        + "\n" + getString(R.string.trx_id_des) + " " + trxId
                        + "\n" + getString(R.string.date_des) + " " + datetime
                        + "\n\n" + getString(R.string.using_paywell_des)
                        + "\n" + getString(R.string.hotline_des) + " " + hotline;
            } else {
                inquiryReceipt = getString(R.string.phone_no_des) + " " + phone
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

        Button btn_okay = dialog.findViewById(R.id.buttonOk);

        radioButton_five = dialog.findViewById(R.id.radio_five);
        radioButton_ten = dialog.findViewById(R.id.radio_ten);
        radioButton_twenty = dialog.findViewById(R.id.radio_twenty);
        radioButton_fifty = dialog.findViewById(R.id.radio_fifty);
        radioButton_hundred = dialog.findViewById(R.id.radio_hundred);
        radioButton_twoHundred = dialog.findViewById(R.id.radio_twoHundred);

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
                    new TransactionLogAsync().execute(getString(R.string.trx_log), "" + limit);
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
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("iemi_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("pin_code", "1234"));
                nameValuePairs.add(new BasicNameValuePair("limit", params[1]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
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

                    EditText phoneNoET = singleTopUpView.findViewById(R.id.phoneNo);
                    EditText amountET = singleTopUpView.findViewById(R.id.amount);
                    RadioGroup prePostSelector = singleTopUpView.findViewById(R.id.prePostRadioGroup);

                    String phoneStr = phoneNoET.getText().toString();
                    String amountStr = amountET.getText().toString();
                    String planStr = "prepaid";

                    if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton) {
                        planStr = getResources().getString(R.string.pre_paid_str);
                    } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton) {
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
                        // new TopUpAsync().execute();
                        handleTopupAPIValidation(PIN_NO);
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

    private void handleTopupAPIValidation(String pinNo) {
        final ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
        progressDialog.show();

        final RequestTopup requestTopup = new RequestTopup();
        requestTopup.setPassword("" + pinNo);
        requestTopup.setUserName("" + mAppHandler.getImeiNo());

        List<TopupData> topupDatumList = new ArrayList<>();

//        String requestString = null;
        if (topUpLayout.getChildCount() > 1) {

            for (int i = 0; i < topUpLayout.getChildCount(); i++) {
                EditText mPhoneNoET, mAmountET;
                RadioGroup prePostSelector;

                View singleTopUpView = topUpLayout.getChildAt(i);
                if (singleTopUpView != null) {
                    mPhoneNoET = singleTopUpView.findViewById(R.id.phoneNo);
                    mAmountET = singleTopUpView.findViewById(R.id.amount);
                    prePostSelector = singleTopUpView.findViewById(R.id.prePostRadioGroup);

                    String phoneStr = mPhoneNoET.getText().toString();
                    String amountStr = mAmountET.getText().toString();
                    String planStr = null;
                    if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton) {
                        planStr = getResources().getString(R.string.pre_paid_str);
                    } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton) {
                        planStr = getResources().getString(R.string.post_paid_str);
                    }


                    TopupData topupDatum = new TopupData(amountStr, planStr, phoneStr, key);
                    topupDatumList.add(topupDatum);

                }
            }

        } else {
            // Single
            EditText phoneNoET, amountET;
            RadioGroup prePostSelector;

            View singleTopUpView = topUpLayout.getChildAt(0);
            if (singleTopUpView != null) {
                phoneNoET = singleTopUpView.findViewById(R.id.phoneNo);
                amountET = singleTopUpView.findViewById(R.id.amount);
                prePostSelector = singleTopUpView.findViewById(R.id.prePostRadioGroup);

                String phoneStr = phoneNoET.getText().toString();
                String amountStr = amountET.getText().toString();
                String planStr = null;

                if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton) {
                    planStr = getResources().getString(R.string.pre_paid_str);
                } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton) {
                    planStr = getResources().getString(R.string.post_paid_str);
                }

                TopupData topupDatum = new TopupData(amountStr, planStr, phoneStr, key);
                topupDatumList.add(topupDatum);


            }
        }


        requestTopup.setTopupData(topupDatumList);


        Call<TopupReposeData> responseBodyCall = ApiUtils.getAPIService().callTopAPI(requestTopup);


        responseBodyCall.enqueue(new Callback<TopupReposeData>() {
            @Override
            public void onResponse(Call<TopupReposeData> call, Response<TopupReposeData> response) {
                progressDialog.dismiss();
                Log.d(KEY_TAG, "onResponse:" + response.body());

                showReposeUI(response.body());

            }

            @Override
            public void onFailure(Call<TopupReposeData> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();


            }
        });

    }

    private void showReposeUI(TopupReposeData response) {
        StringBuilder receiptBuilder = new StringBuilder();

        AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);

        // check authentication
        TopupData topupData = response.getTopupData().get(0).getTopupData();
        if (topupData == null) {
            showAuthticationError();
        } else {
            showDialog(response, receiptBuilder, builder);
        }


    }

    private void showDialog(TopupReposeData response, StringBuilder receiptBuilder, AlertDialog.Builder builder) {

        boolean isTotalRequestSuccess = false;
        for (int i = 0; i < response.getTopupData().size(); i++) {

            TopupDatum topupData = response.getTopupData().get(i);
            if (topupData != null) {
                int number = i + 1;
                receiptBuilder.append(number + ".");


                receiptBuilder.append(getString(R.string.phone_no_des) + " " + topupData.getTopupData().getMsisdn());
                receiptBuilder.append("\n" + getString(R.string.amount_des) + " " + topupData.getTopupData().getAmount() + " " + getString(R.string.tk_des));

                if (topupData.getStatus().toString().startsWith("3")) {

                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#ff0000'>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());

                    isTotalRequestSuccess = false;
                } else {
                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#008000>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());

                    isTotalRequestSuccess = true;
                }
                receiptBuilder.append("\n" + getString(R.string.trx_id_des) + " " + topupData.getTransId());

                receiptBuilder.append("\n\n");

            }

        }
        mHotLine = response.getHotlineNumber();
        receiptBuilder.append("\n\n" + getString(R.string.using_paywell_des) + "\n" + getString(R.string.hotline_des) + " " + mHotLine);

        if (isTotalRequestSuccess == false) {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        }

        builder.setMessage(receiptBuilder.toString());


        final boolean finalIsTotalRequestSuccess = isTotalRequestSuccess;
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                if (finalIsTotalRequestSuccess) {
                    startActivity(new Intent(TopupMainActivity.this, TopupMainActivity.class));
                    finish();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showAuthticationError() {
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

//    private class TopUpAsync extends AsyncTask<Object, String, String> {
//        private ProgressDialog progressDialog;
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
//            if (!isFinishing())
//                progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(Object... params) {
//            return generateRequestURL();
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            progressDialog.dismiss();
//            if (result != null) {
//                if (result.startsWith("313")) {
//                    showTransactionLog(result, false);
//                } else {
//                    if (result.startsWith("200")) {
//                        statusCode = true;
//                    }
//                    showTransactionLog(result, true);
//                }
//            } else {
//                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
//                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                View snackBarView = snackbar.getView();
//                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                snackbar.show();
//            }
//        }
//
//        private String generateRequestURL() {
//            String requestString = null;
//            if (topUpLayout.getChildCount() > 1) {
//                // Multiple
//                StringBuilder reqStrBuilder = new StringBuilder();
//                reqStrBuilder.append(getResources().getString(R.string.multi_top)
//                        + "iemi_no=" + mAppHandler.getImeiNo()
//                        + "&pin_code=" + PIN_NO
//                        + "&no_of_req=" + topUpLayout.getChildCount());
//
//                for (int i = 0; i < topUpLayout.getChildCount(); i++) {
//                    EditText mPhoneNoET, mAmountET;
//                    RadioGroup prePostSelector;
//
//                    View singleTopUpView = topUpLayout.getChildAt(i);
//                    if (singleTopUpView != null) {
//                        mPhoneNoET = singleTopUpView.findViewById(R.id.phoneNo);
//                        mAmountET = singleTopUpView.findViewById(R.id.amount);
//                        prePostSelector = singleTopUpView.findViewById(R.id.prePostRadioGroup);
//
//                        String phoneStr = mPhoneNoET.getText().toString();
//                        String amountStr = mAmountET.getText().toString();
//                        String planStr = null;
//                        if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton) {
//                            planStr = getResources().getString(R.string.pre_paid_str);
//                        } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton) {
//                            planStr = getResources().getString(R.string.post_paid_str);
//                        }
//                        reqStrBuilder.append("&msisdn" + (i + 1) + "="
//                                + phoneStr + "&amount" + (i + 1) + "="
//                                + amountStr + "&con_type" + (i + 1) + "="
//                                + planStr);
//                    }
//                }
//                requestString = reqStrBuilder.toString();
//            } else {
//                // Single
//                EditText phoneNoET, amountET;
//                RadioGroup prePostSelector;
//
//                View singleTopUpView = topUpLayout.getChildAt(0);
//                if (singleTopUpView != null) {
//                    phoneNoET = singleTopUpView.findViewById(R.id.phoneNo);
//                    amountET = singleTopUpView.findViewById(R.id.amount);
//                    prePostSelector = singleTopUpView.findViewById(R.id.prePostRadioGroup);
//
//                    String phoneStr = phoneNoET.getText().toString();
//                    String amountStr = amountET.getText().toString();
//                    String planStr = null;
//
//                    if (prePostSelector.getCheckedRadioButtonId() == R.id.preRadioButton) {
//                        planStr = getResources().getString(R.string.pre_paid_str);
//                    } else if (prePostSelector.getCheckedRadioButtonId() == R.id.postRadioButton) {
//                        planStr = getResources().getString(R.string.post_paid_str);
//                    }
//                    requestString = getResources().getString(R.string.single_top)
//                            + "iemi_no=" + mAppHandler.getImeiNo()
//                            + "&msisdn=" + phoneStr
//                            + "&amount=" + amountStr
//                            + "&con_type=" + planStr
//                            + "&pin_code=" + PIN_NO;
//                }
//            }
//            return sendingHTTPSTopupRequest(requestString);
//        }
//    }
//
//    @SuppressWarnings("deprecation")
//    private String sendingHTTPSTopupRequest(String requestString) {
//        String responseTxt = null;
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost(requestString);
//        try {
//            ResponseHandler<String> responseHandler = new BasicResponseHandler();
//            responseTxt = httpclient.execute(httppost, responseHandler);
//        } catch (Exception e) {
//            e.fillInStackTrace();
//            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
//            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//            View snackBarView = snackbar.getView();
//            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//        }
//        return responseTxt;
//
//    }
//
//    @SuppressWarnings("deprecation")
//    private void showTransactionLog(String responseTxt, final boolean isAuthorized) {
//        // code for topup receipt dialog
//        if (isAuthorized) {
//            StringBuilder receiptBuilder = new StringBuilder();
//            final StringBuilder topupBuilder = new StringBuilder();
//            EditText multiplePhoneNoET, multipleAmountET, singlePhoneNoET, singleAmountET;
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
//
//            if (topUpLayout.getChildCount() > 1 && responseTxt.contains("@@@")) {
//                // code for multiple top up
//                String[] splitTripleAt = responseTxt.split("@@@");
//                for (int i = 0; i < splitTripleAt.length; i++) {
//                    if (i == splitTripleAt.length - 1) {
//                        mHotLine = splitTripleAt[i].split("@")[2];
//                    } else {
//                        View multipleTopupView = topUpLayout.getChildAt(i);
//                        if (multipleTopupView != null) {
//                            multiplePhoneNoET = multipleTopupView.findViewById(R.id.phoneNo);
//                            multipleAmountET = multipleTopupView.findViewById(R.id.amount);
//                            mPhnNo = multiplePhoneNoET.getText().toString();
//                            mAmount = multipleAmountET.getText().toString();
//                        }
//                        String _status = splitTripleAt[i].split("@")[1];
//                        String _trxId = splitTripleAt[i].split("@")[2];
//
//                        topupBuilder.append((i + 1) + ". " + getString(R.string.phone_no_des) + " " + mPhnNo
//                                + "\n " + getString(R.string.amount_des) + " " + mAmount + getString(R.string.tk)
//                                + "\n " + getString(R.string.trx_id_des) + " " + _trxId
//                                + "\n " + getString(R.string.status_des) + " " + _status
//                                + "\n\n");
//                    }
//                }
//                receiptBuilder.append(topupBuilder
//                        + "\n\n" + getString(R.string.using_paywell_des)
//                        + "\n" + getString(R.string.hotline_des) + mHotLine);
//
//                builder.setTitle("Result");
//            } else {
//                // code for single topup
//                View singleTopupView = topUpLayout.getChildAt(0);
//                if (singleTopupView != null) {
//                    singlePhoneNoET = singleTopupView.findViewById(R.id.phoneNo);
//                    singleAmountET = singleTopupView.findViewById(R.id.amount);
//                    mPhnNo = singlePhoneNoET.getText().toString();
//                    mAmount = singleAmountET.getText().toString();
//
//                }
//                String[] splitSingleAt = responseTxt.split("@");
//                String mStatusMessage = splitSingleAt[1];
//                String mTrxId = splitSingleAt[2];
//                mHotLine = splitSingleAt[5];
//                if (splitSingleAt[0].equals("200")) {
//                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
//                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
//                            + "\n" + getString(R.string.trx_id_des) + " " + mTrxId
//                            + "\n\n" + getString(R.string.using_paywell_des)
//                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
//                } else {
//                    receiptBuilder.append(getString(R.string.phone_no_des) + " " + mPhnNo
//                            + "\n" + getString(R.string.amount_des) + " " + mAmount + " " + getString(R.string.tk_des)
//                            + "\n\n" + getString(R.string.status_des) + " " + mStatusMessage
//                            + "\n\n" + getString(R.string.trx_id_des) + " " + mTrxId
//                            + "\n\n" + getString(R.string.using_paywell_des)
//                            + "\n" + getString(R.string.hotline_des) + " " + mHotLine);
//                }
//                if (splitSingleAt[0].equals("200")) {
//                    builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
//                } else {
//                    builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
//                }
//            }
//            builder.setMessage(receiptBuilder.toString());
//
//            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    dialogInterface.dismiss();
//                    if (statusCode) {
//                        startActivity(new Intent(TopupMainActivity.this, TopupMainActivity.class));
//                        finish();
//                    }
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        } else {
//            // 313@AuthenticationError
//            AlertDialog.Builder builder = new AlertDialog.Builder(TopupMainActivity.this);
//            builder.setMessage(R.string.services_alert_msg);
//            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int id) {
//                    dialogInterface.dismiss();
//                }
//            });
//            AlertDialog alert = builder.create();
//            alert.show();
//        }
//    }

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
        finish();
    }


    public void onClickBundleOffer(String operator) {
        switch (operator) {
            case "GP":
                if (cd.isConnectingToInternet()) {
                    operator_number = 1;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case "GP ST":
                if (cd.isConnectingToInternet()) {
                    operator_number = 1;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;

            case "RB":
                if (cd.isConnectingToInternet()) {
                    operator_number = 3;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case "BL":
                if (cd.isConnectingToInternet()) {
                    operator_number = 2;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case "TT":
                if (cd.isConnectingToInternet()) {
                    operator_number = 6;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case "AT":
                if (cd.isConnectingToInternet()) {
                    operator_number = 4;
                    new OfferInquiryAsync().execute(getResources().getString(R.string.topup_offer), String.valueOf(operator_number));
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
                break;
        }
    }

    private class OfferInquiryAsync extends AsyncTask<String, Void, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TopupMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("subServiceId", data[1]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_STATUS);
                    if (status.equals("200")) {
                        JSONArray array = jsonObject.getJSONArray(TAG_RECHARGE_OFFER);
                        Bundle bundle = new Bundle();
                        bundle.putString("array", array.toString());
                        if (operator_number == 1) {
                            OfferMainActivity.operatorName = getString(R.string.home_gp);
                        } else if (operator_number == 2) {
                            OfferMainActivity.operatorName = getString(R.string.home_bl);
                        } else if (operator_number == 3) {
                            OfferMainActivity.operatorName = getString(R.string.home_rb);
                        } else if (operator_number == 4) {
                            OfferMainActivity.operatorName = getString(R.string.home_at);
                        } else if (operator_number == 6) {
                            OfferMainActivity.operatorName = getString(R.string.home_tt);
                        }
                        Intent intent = new Intent(TopupMainActivity.this, OfferMainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
//                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, jsonObject.getString(TAG_MESSAGE), Snackbar.LENGTH_LONG);
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
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
