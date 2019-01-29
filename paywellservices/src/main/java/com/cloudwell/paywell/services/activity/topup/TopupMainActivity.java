package com.cloudwell.paywell.services.activity.topup;

import android.app.Activity;
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
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.topup.adapter.MyRecyclerViewAdapter;
import com.cloudwell.paywell.services.activity.topup.model.MobileOperator;
import com.cloudwell.paywell.services.activity.topup.model.RequestTopup;
import com.cloudwell.paywell.services.activity.topup.model.TopupData;
import com.cloudwell.paywell.services.activity.topup.model.TopupDatum;
import com.cloudwell.paywell.services.activity.topup.model.TopupReposeData;
import com.cloudwell.paywell.services.activity.topup.offer.OfferMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.DrawableClickListener;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
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

public class TopupMainActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public final static String KEY_GP = "GP";
    public final static String KEY_ROBI = "Robi";
    public final static String KEY_BL = "Banglalink";
    public final static String KEY_AIRTEL = "Airtel";
    public final static String KEY_TELETALK = "Teletalk";
    public final static String KEY_SKITTO = "Skitto";

    public static String KEY_TAG = TopupMainActivity.class.getName();
    private LinearLayout topUpLayout;
    private int addNoFlag = 0;
    private static String mHotLine;
    private ConnectionDetector cd;
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

    private int operator_number;
    private static final String TAG_STATUS = "status";
    private static final String TAG_RECHARGE_OFFER = "RechargeOffer";
    private static final String TAG_MESSAGE = "message";
    private TextView tvError;
    private TextView tvResult;

    private ImageView imageViewOffer;
    private ImageView imageViewAdd;
    private ImageView imageViewInq;
    private ImageView imageViewTrxLog;
    Button buttonSubmit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_main);
        mAppHandler = new AppHandler(this);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_topup);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initView();
        refreshLanguage();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_PAGE);

    }


    private void initView() {
        mRelativeLayout = findViewById(R.id.linearLayout);

        topUpLayout = findViewById(R.id.topUpAddLayout);
        buttonSubmit = findViewById(R.id.btnSubmit);

        slideInAnim.setDuration(50);
        slideOutAnim.setDuration(50);
        assert buttonSubmit != null;
        buttonSubmit.setOnClickListener(this);

        imageViewAdd = findViewById(R.id.imageAdd);
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
        imageViewTrxLog = findViewById(R.id.transLogBtn);
        imageViewTrxLog.setOnClickListener(this);

        imageViewInq = findViewById(R.id.enquiryBtn);
        imageViewInq.setOnClickListener(this);

        imageViewOffer = findViewById(R.id.imageOffer);
        imageViewOffer.setOnClickListener(this);

        inflater = getLayoutInflater();
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);

        ScrollView myScrollView = findViewById(R.id.topupScrollView);

        myScrollView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event != null && event.getAction() == MotionEvent.ACTION_MOVE)
                {
                    InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                    boolean isKeyboardUp = imm.isAcceptingText();

                    if (isKeyboardUp)
                    {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
                return false;
            }
        });

        addAnotherNo();
    }

    private void refreshLanguage() {

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            ((Button) findViewById(R.id.btnSubmit)).setTypeface(AppController.getInstance().getOxygenLightFont());
            imageViewOffer.setBackgroundResource(R.drawable.bundle_en);
            imageViewAdd.setBackgroundResource(R.drawable.add_en);
            imageViewInq.setBackgroundResource(R.drawable.topup_in_en);
            imageViewTrxLog.setBackgroundResource(R.drawable.transaction_log_en);
            buttonSubmit.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            ((Button) findViewById(R.id.btnSubmit)).setTypeface(AppController.getInstance().getAponaLohitFont());
            imageViewOffer.setBackgroundResource(R.drawable.bundle_bn);
            imageViewAdd.setBackgroundResource(R.drawable.add_bn);
            imageViewInq.setBackgroundResource(R.drawable.topup_in_bn);
            imageViewTrxLog.setBackgroundResource(R.drawable.transaction_log_bn);
            buttonSubmit.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_SUBMIT_RECHARGE_REQUEST);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_TRX_LOG_MENU);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_ENQUIRY_TRX_MENU);
            showEnquiryPrompt();
        } else if (v.getId() == R.id.imageOffer) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_BUNDLE_OFFER_MENU);
            startActivity(new Intent(TopupMainActivity.this, OperatorMenuActivity.class));
        }
    }

    private void addAnotherNo() {
        ++addNoFlag;
        final View topUpView = inflater.inflate(R.layout.activity_topup_layout, null);
        EditText phoneNoET = topUpView.findViewById(R.id.phoneNo);
        Button removeBtn = topUpView.findViewById(R.id.removeLayoutImgBtn);

        tvError = topUpView.findViewById(R.id.tvError);
        tvResult = topUpView.findViewById(R.id.tvResult);

        topUpView.setTag(addNoFlag);

        if (addNoFlag == 1) {
            removeBtn.setVisibility(View.GONE);
        } else {
            LinearLayout layout = (LinearLayout) topUpView.findViewById(R.id.linerLayoutRoot);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4, 18, 4, 4);
            layout.setLayoutParams(params);
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


        refrashLanguageDynamicView(topUpView);

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

        tvError.setVisibility(View.VISIBLE);
        tvError.setText(Html.fromHtml(getString(R.string.error_correct_operator_msg)));
        tvResult.setText("");


        final ArrayList<MobileOperator> mobileOperatorArrayList = new ArrayList<>();
        mobileOperatorArrayList.add(new MobileOperator(KEY_GP, R.drawable.gp_logo, R.drawable.gp_selected, false));
        mobileOperatorArrayList.add(new MobileOperator(KEY_BL, R.drawable.banglalink_logo, R.drawable.banglalink_selected, false));
        mobileOperatorArrayList.add(new MobileOperator(KEY_ROBI, R.drawable.robi_logo, R.drawable.robi_selected, false));
        mobileOperatorArrayList.add(new MobileOperator(KEY_AIRTEL, R.drawable.airtel_logo, R.drawable.airtel_selected, false));
        mobileOperatorArrayList.add(new MobileOperator(KEY_TELETALK, R.drawable.teletalk_logo, R.drawable.teletalk_selected, false));
        mobileOperatorArrayList.add(new MobileOperator(KEY_SKITTO, R.drawable.skitto_logo, R.drawable.skitto_selected, false));

        // new recycle view
        final RecyclerView recyclerView = topUpView.findViewById(R.id.rvOperatorList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, mobileOperatorArrayList);

        adapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                updateRecycleviewData(topUpView, position, mobileOperatorArrayList, recyclerView);

            }
        });
        recyclerView.setAdapter(adapter);


        topUpLayout.addView(topUpView);
        topUpView.startAnimation(slideInAnim);
    }

    private void updateRecycleviewData(View topUpView, int position, ArrayList<MobileOperator> mobileOperatorArrayList, RecyclerView recyclerView) {
        MobileOperator mobileOperator = mobileOperatorArrayList.get(position);

        boolean isCheck = false;
        String seletedresult = "";

        for (MobileOperator data : mobileOperatorArrayList) {

            // compared selected data
            if (mobileOperator.getName().equals(data.getName())) {

                boolean seleted = data.isSeleted();
                if (seleted) {

                    data.setSeleted(false);
                    mobileOperatorArrayList.set(position, data);

                    isCheck = false;
                    seletedresult = "";

                } else {

                    // desleeted all data
                    for (MobileOperator data1 : mobileOperatorArrayList) {
                        data1.setSeleted(false);
                        mobileOperatorArrayList.set(position, data1);
                    }


                    seletedresult = data.getName();
                    // set selected data
                    data.setSeleted(true);
                    mobileOperatorArrayList.set(position, data);

                    isCheck = true;

                }
            }
        }


        if (isCheck) {
            tvError = topUpView.findViewById(R.id.tvError);
            tvError.setVisibility(View.GONE);
            tvResult = topUpView.findViewById(R.id.tvResult);
            tvResult.setText("" + seletedresult);


            tvError.setText(getString(R.string.error_correct_operator_msg));
            tvError.setTextColor(Color.BLACK);
            LinearLayout linearLayout = topUpView.findViewById(R.id.llHeader);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setVisibility(View.GONE);


        } else {
            tvError = topUpView.findViewById(R.id.tvError);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(Html.fromHtml(getString(R.string.error_correct_operator_msg)));
            tvResult.setText("" + seletedresult);


            tvError.setText(getString(R.string.error_correct_operator_msg));
            tvError.setTextColor(Color.BLACK);

            LinearLayout linearLayout = topUpView.findViewById(R.id.llHeader);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.setBackgroundColor(Color.WHITE);


        }


        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private void refrashLanguageDynamicView(View topUpView) {
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {

            ((EditText) topUpView.findViewById(R.id.phoneNo)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((EditText) topUpView.findViewById(R.id.amount)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((RadioButton) topUpView.findViewById(R.id.preRadioButton)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((RadioButton) topUpView.findViewById(R.id.postRadioButton)).setTypeface(AppController.getInstance().getOxygenLightFont());
            ((TextView) topUpView.findViewById(R.id.tvError)).setTypeface(AppController.getInstance().getOxygenLightFont());


        } else {
            ((EditText) topUpView.findViewById(R.id.phoneNo)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((EditText) topUpView.findViewById(R.id.amount)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((RadioButton) topUpView.findViewById(R.id.preRadioButton)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((RadioButton) topUpView.findViewById(R.id.postRadioButton)).setTypeface(AppController.getInstance().getAponaLohitFont());
            ((TextView) topUpView.findViewById(R.id.tvError)).setTypeface(AppController.getInstance().getAponaLohitFont());
        }
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

        private String inquiryReceipt;

        @Override
        protected void onPreExecute() {
            showProgressDialog();
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
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
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
        Button btn_cancel = dialog.findViewById(R.id.cancelBtn);

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
        assert btn_cancel != null;
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
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


        @Override
        protected void onPreExecute() {
            showProgressDialog();
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
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
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


                    TextView textView = singleTopUpView.findViewById(R.id.tvResult);
                    String result = textView.getText().toString();


                    if (result.equals("")) {
                        // No item selected
                        TextView tvError = singleTopUpView.findViewById(R.id.tvError);
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText(getString(R.string.error_correct_operator_msg));
                        tvError.setTextColor(Color.WHITE);
                        LinearLayout linearLayout = singleTopUpView.findViewById(R.id.llHeader);
                        linearLayout.setBackgroundColor(Color.RED);
                        return;

                    }

                    reqStrBuilder.append((i + 1) + ". " + getString(R.string.phone_no_des) + " " + phoneStr
                            + "\n " + getString(R.string.amount_des) + " " + amountStr + getString(R.string.tk)
                            + "\n " + getString(R.string.package_type_des) + " : " + planStr.substring(0, 1).toUpperCase()
                            + planStr.substring(1).toLowerCase() + "\n"
                            + "" + getString(R.string.operator) + " : " + result.toString() + "\n\n");

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
        showProgressDialog();

        final RequestTopup requestTopup = new RequestTopup();
        requestTopup.setPassword("" + pinNo);
        requestTopup.setUserName("" + mAppHandler.getImeiNo());

        List<TopupData> topupDatumList = new ArrayList<>();

        if (topUpLayout.getChildCount() > 1) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_RECHARGE, AnalyticsParameters.KEY_BULK_TOPUP, (topUpLayout.getChildCount() + 1));

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


                    TextView textView = singleTopUpView.findViewById(R.id.tvResult);
                    String result = textView.getText().toString();


                    if (result.equals("")) {
                        // No item selected
                        TextView tvError = singleTopUpView.findViewById(R.id.tvError);
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText(Html.fromHtml("<font color='white'>" + getString(R.string.error_correct_operator_msg)));

                        return;

                    }

                    String operatorTextForServer = getOperatorTextForServer(result);

                    TopupData topupDatum = new TopupData(amountStr, planStr, phoneStr, operatorTextForServer);
                    topupDatumList.add(topupDatum);

                }
            }

        } else {
            // Single
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_RECHARGE, AnalyticsParameters.KEY_SINGLE_TOPUP);
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

                TextView textView = singleTopUpView.findViewById(R.id.tvResult);
                String result = textView.getText().toString();


                if (result.equals("")) {
                    // No item selected
                    TextView tvError = singleTopUpView.findViewById(R.id.tvError);
                    tvError.setVisibility(View.VISIBLE);
                    tvError.setText(Html.fromHtml("<font color='white'>" + getString(R.string.error_correct_operator_msg)));

                    return;

                }

                String operatorTextForServer = getOperatorTextForServer(result);

                TopupData topupDatum = new TopupData(amountStr, planStr, phoneStr, operatorTextForServer);
                topupDatumList.add(topupDatum);

            }
        }


        requestTopup.setTopupData(topupDatumList);


        Call<TopupReposeData> responseBodyCall = ApiUtils.getAPIService().callTopAPI(requestTopup);


        responseBodyCall.enqueue(new Callback<TopupReposeData>() {
            @Override
            public void onResponse(Call<TopupReposeData> call, Response<TopupReposeData> response) {
                dismissProgressDialog();
                Log.d(KEY_TAG, "onResponse:" + response.body());

                showReposeUI(response.body());

            }

            @Override
            public void onFailure(Call<TopupReposeData> call, Throwable t) {
                dismissProgressDialog();

                Log.d(KEY_TAG, "onFailure:");
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();


            }
        });

    }


    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }

    private String getOperatorTextForServer(CharSequence checkedRadioButtonText) {
        String operator = "";

        switch (checkedRadioButtonText.toString()) {

            case KEY_GP:
                operator = OperatorType.GP.getText();
                break;

            case KEY_SKITTO:
                operator = OperatorType.Skitto.getText();
                break;
            case KEY_BL:
                operator = OperatorType.BANGLALINK.getText();
                break;
            case KEY_ROBI:
                operator = OperatorType.ROBI.getText();
                break;
            case KEY_AIRTEL:
                operator = OperatorType.AIRTEL.getText();
                break;

            case KEY_TELETALK:
                operator = OperatorType.TELETALK.getText();
                break;

        }
        return operator;
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

        boolean isTotalRequestSuccess;
        TopupDatum topupData = null;

        for (int i = 0; i < response.getTopupData().size(); i++) {

            topupData = response.getTopupData().get(i);
            if (topupData != null) {
                int number = i + 1;
                receiptBuilder.append(number + ".");
                receiptBuilder.append(getString(R.string.phone_no_des) + " " + topupData.getTopupData().getMsisdn());
                receiptBuilder.append("\n" + getString(R.string.amount_des) + " " + topupData.getTopupData().getAmount() + " " + getString(R.string.tk_des));

                if (topupData.getStatus().toString().equals("200")) {
                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#ff0000'>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());
                } else {
                    receiptBuilder.append("\n" + Html.fromHtml("<font color='#008000>" + getString(R.string.status_des) + "</font>") + " " + topupData.getMessage());
                }

                receiptBuilder.append("\n" + getString(R.string.trx_id_des) + " " + topupData.getTransId());
                receiptBuilder.append("\n\n");
            }
        }

        isTotalRequestSuccess = checkTotalRequestSuccess(response);

        mHotLine = response.getHotlineNumber();
        receiptBuilder.append("\n\n" + getString(R.string.using_paywell_des) + "\n" + getString(R.string.hotline_des) + " " + mHotLine);

        if (isTotalRequestSuccess) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
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

    public boolean checkTotalRequestSuccess(TopupReposeData response) {
        boolean isTotalSuccess = false;

        for (int i = 0; i < response.getTopupData().size(); i++) {
            TopupDatum topupData = response.getTopupData().get(i);
            if (topupData != null) {

                if (topupData.getStatus().toString().equals("200")) {
                    isTotalSuccess = true;
                } else {
                    isTotalSuccess = false;
                    return isTotalSuccess;
                }
            }
        }
        return isTotalSuccess;
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
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("subServiceId", data[1]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
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
