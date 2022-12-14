package com.cloudwell.paywell.services.activity.utility.pallibidyut;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.WebViewActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBBillPayNewActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBInquiryBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusInquiryActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.MobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.PBInquiryMobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.ReqInquiryModel;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBInquiryRegActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBRegistrationActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PBMainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RelativeLayout mRelativeLayout;
    private RadioButton radioButton_five, radioButton_ten, radioButton_twenty, radioButton_fifty, radioButton_hundred, radioButton_twoHundred;
    private String selectedLimit = "";
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private static String serviceName;
    private String packageNameYoutube = AllUrl.packageNameYoutube;
    private String linkPolliBillPay =AllUrl.linkPolliBillPay;
    private static String TAG_SERVICE_REGISTRATION_INQUIRY = "POLLI_REG";
    private static String TAG_SERVICE_BILL_INQUIRY = "POLLI_BILL";
    private static String TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY = "POLLI_RREG";
    private static String TAG_SERVICE_PHONE_NUMBER_BILL_STATUS = "POLLI_STATUS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbmain);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_pollibiddut);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Button btnReg = findViewById(R.id.homeBtnRegistration);
        Button btnBill = findViewById(R.id.homeBtnBillPay);
        Button btnRegInq = findViewById(R.id.homeBtnInquiryReg);
        Button btnBillInq = findViewById(R.id.homeBtnInquiryBillPay);

        Button btRequestInquiry = findViewById(R.id.btRequestInquiry);
        Button btBillStatusInquiry = findViewById(R.id.btBillStatusInquiry);

        Button btChangeMobileNumber = findViewById(R.id.btChangeMobileNumber);
        Button btChangeMobileNumbEnquiry = findViewById(R.id.btChangeMobileNumbeEnquiry);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnReg.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBill.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnRegInq.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBillInq.setTypeface(AppController.getInstance().getOxygenLightFont());
            btRequestInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btBillStatusInquiry.setTypeface(AppController.getInstance().getOxygenLightFont());
            btChangeMobileNumber.setTypeface(AppController.getInstance().getOxygenLightFont());
            btChangeMobileNumbEnquiry.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            btnReg.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBill.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnRegInq.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBillInq.setTypeface(AppController.getInstance().getAponaLohitFont());
            btRequestInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btBillStatusInquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
            btChangeMobileNumber.setTypeface(AppController.getInstance().getAponaLohitFont());
            btChangeMobileNumbEnquiry.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        checkIsComeFromFav(getIntent());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU);


    }

    private void checkIsComeFromFav(Intent intent) {

        RecentUsedMenu recentUsedMenu;
        boolean isFav = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE, false);
        if (isFav) {
            boolean booleanExtra = intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_RG_INQUERY, false);
            if (booleanExtra) {
                shwTheLimitedPrompt(TAG_SERVICE_REGISTRATION_INQUIRY);
                recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_reg_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 12);
                addItemToRecentListInDB(recentUsedMenu);

            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_BILL_INQUERY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_BILL_INQUIRY);
                recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_bill_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 13);
                addItemToRecentListInDB(recentUsedMenu);

            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_REQUEST_BILL_INQUIRY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS);

                recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pb_bill_statu_inquery, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 15);
                addItemToRecentListInDB(recentUsedMenu);

            } else if (intent.getBooleanExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_MOBILE_NUMBER_CHANGE_INQUIRY, false)) {
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY);


                recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pb_phone_number_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry ,0, 17);
                addItemToRecentListInDB(recentUsedMenu);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_video) {
            if (isAppInstalled(packageNameYoutube)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkPolliBillPay));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage(packageNameYoutube);
                startActivity(intent);
            } else {
                WebViewActivity.TAG_LINK = linkPolliBillPay;
                startActivity(new Intent(this, WebViewActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onButtonClicker(View v) {

        RecentUsedMenu recentUsedMenu;

        switch (v.getId()) {
            case R.id.homeBtnRegistration:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION);
                startActivity(new Intent(this, PBRegistrationActivity.class));
                break;
            case R.id.homeBtnBillPay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_PAY);
                startActivity(new Intent(this, PBBillPayNewActivity.class));
                break;
            case R.id.homeBtnInquiryReg:
                shwTheLimitedPrompt(TAG_SERVICE_REGISTRATION_INQUIRY);
                 recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_reg_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 12);
                addItemToRecentListInDB(recentUsedMenu);

                break;
            case R.id.homeBtnInquiryBillPay:
                shwTheLimitedPrompt(TAG_SERVICE_BILL_INQUIRY);
                 recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_bill_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 13);
                addItemToRecentListInDB(recentUsedMenu);
                break;

            case R.id.btRequestInquiry:
                startActivity(new Intent(this, PBBillStatusActivity.class));

                break;
            case R.id.btBillStatusInquiry:
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS);

                recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pb_bill_statu_inquery, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry, 0, 15);
                addItemToRecentListInDB(recentUsedMenu);
                break;

            case R.id.btChangeMobileNumber:
                startActivity(new Intent(this, MobileNumberChangeActivity.class));
                break;

            case R.id.btChangeMobileNumbeEnquiry:
                shwTheLimitedPrompt(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY);

                addRecentUsedList();
                break;
            default:
                break;
        }
    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu;
        recentUsedMenu = new RecentUsedMenu(StringConstant.KEY_home_utility_pb_phone_number_inquiry, StringConstant.KEY_home_utility, IconConstant.KEY_ic_enquiry ,0, 17);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void shwTheLimitedPrompt(String tagServiceBillInquiry) {
        serviceName = tagServiceBillInquiry;
        showLimitPrompt();
    }

    private void showLimitPrompt() {
        final AppCompatDialog dialog = new AppCompatDialog(this);
        if (serviceName.equalsIgnoreCase(TAG_SERVICE_REGISTRATION_INQUIRY)) {
            dialog.setTitle(R.string.reg_log_limit_title_msg);
        } else {
            dialog.setTitle(R.string.log_limit_title_msg);
        }
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
                if (cd.isConnectingToInternet()) {
                    callAPI(serviceName,selectedLimit);
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

    private void callAPI(String serviceName, String selectedLimit) {

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(this).getRID());
        ReqInquiryModel m = new ReqInquiryModel();
        m.setUsername(AppHandler.getmInstance(getApplicationContext()).getUserName());
        m.setLimit(selectedLimit);
        m.setService(serviceName);
        m.setRefId(uniqueKey);

        showProgressDialog();

        ApiUtils.getAPIServiceV2().PBInquiry(m).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                if (response.code() == 200){
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String responseMessage = jsonObject.getString("Message");
                        int status = jsonObject.getInt("Status");

                        if (status == 200){

                            JSONArray responseDetails = jsonObject.getJSONArray("ResponseDetails");
                            String result = responseDetails.toString();

                            if (PBMainActivity.serviceName.equalsIgnoreCase(TAG_SERVICE_REGISTRATION_INQUIRY)) {
                                PBInquiryRegActivity.TRANSLOG_TAG = result;
                                startActivity(new Intent(PBMainActivity.this, PBInquiryRegActivity.class));
                            } else if (PBMainActivity.serviceName.equalsIgnoreCase(TAG_SERVICE_BILL_INQUIRY)) {
                                PBInquiryBillPayActivity.TRANSLOG_TAG = result;
                                startActivity(new Intent(PBMainActivity.this, PBInquiryBillPayActivity.class));

                            } else if (PBMainActivity.serviceName.equalsIgnoreCase(TAG_SERVICE_PHONE_NUMBER_BILL_STATUS)) {
                                PBBillStatusInquiryActivity.TRANSLOG_TAG = result;
                                startActivity(new Intent(PBMainActivity.this, PBBillStatusInquiryActivity.class));

                            } else if (PBMainActivity.serviceName.equalsIgnoreCase(TAG_SERVICE_PHONE_NUMBER_CHANGE_INQUIRY)) {
                                PBInquiryMobileNumberChangeActivity.Companion.setTRANSLOG_TAG(result);
                                startActivity(new Intent(PBMainActivity.this, PBInquiryMobileNumberChangeActivity.class));
                            }

                        } else {
                            showErrorMessagev1(responseMessage);
                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
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



    protected boolean isAppInstalled(String packageName) {
        Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            return true;
        } else {
            return false;
        }
    }
}
