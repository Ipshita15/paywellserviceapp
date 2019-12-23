package com.cloudwell.paywell.services.activity.reg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;

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

import static com.cloudwell.paywell.services.activity.reg.EntryMainActivity.regModel;

public class EntryForthActivity extends BaseActivity {

    private LinearLayout linearLayoutOne, linearLayoutTwo;
    private int radio_one = 0, radio_two = 0, radio_three = 0;
    private String email, landmark, sales_code = "", collection_code = "", imeiOne = "", imeiTwo = "", outlet_img = "", nid_img = "",
            nid_back_img = "", owner_img = "", trade_license_img = "", passport_img = "", birth_certificate_img = "",
            driving_license_img = "", visiting_card_img = "", operator = "", downloadSource = "";
    private CheckBox checkBox_one, checkBox_two, checkBox_three, checkBox_four, checkBox_five, checkBox_six, checkBox_seven, checkBox_eight, checkBox_nine,
            checkBox_ten;
    private SubmitRequestTask mSubmitRequestTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_forth);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("৪র্থ");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ScrollView mScrollView = findViewById(R.id.scrollView_forth);
        RadioGroup radioGroup_one = findViewById(R.id.radioGroup_one);
        linearLayoutOne = findViewById(R.id.linearLayoutOne);
        linearLayoutTwo = findViewById(R.id.linearLayoutTwo);
        radioGroup_one.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_one = checkedId;
                if (checkedId == R.id.rechargeYes) {
                    checkBox_one = findViewById(R.id.item_check_gp);
                    checkBox_two = findViewById(R.id.item_check_bl);
                    checkBox_three = findViewById(R.id.item_check_rb);
                    checkBox_four = findViewById(R.id.item_check_at);
                    checkBox_five = findViewById(R.id.item_check_tt);
                    linearLayoutOne.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutOne.setVisibility(View.GONE);
                }
            }
        });
        RadioGroup radioGroup_two = findViewById(R.id.radioGroup_two);
        radioGroup_two.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_two = checkedId;
                if (checkedId == R.id.agentYes) {
                    checkBox_six = findViewById(R.id.item_check_bkash);
                    checkBox_seven = findViewById(R.id.item_check_rocket);
                    checkBox_eight = findViewById(R.id.item_check_mcash);
                    checkBox_nine = findViewById(R.id.item_check_mobile_money);
                    checkBox_ten = findViewById(R.id.item_check_mycash);
                    linearLayoutTwo.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutTwo.setVisibility(View.GONE);
                }
            }
        });
        RadioGroup radioGroup_three = findViewById(R.id.radioGroup_three);
        radioGroup_three.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_three = checkedId;
                if (checkedId == R.id.downloadYes) {
                    downloadSource = "google";
                } else {
                    downloadSource = "other";
                }
            }
        });

        ((TextView) mScrollView.findViewById(R.id.textView_mobileRecharge)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.rechargeYes)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.rechargeNo)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_opetator)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_gp)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_bl)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_rb)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_at)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_tt)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_bkashagent)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.agentYes)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.agentNo)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_service)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_bkash)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_rocket)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_mcash)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_mobile_money)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((CheckBox) mScrollView.findViewById(R.id.item_check_mycash)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((TextView) mScrollView.findViewById(R.id.textView_downloadOption)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.downloadYes)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((RadioButton) mScrollView.findViewById(R.id.downloadNo)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.button_nextForth)).setTypeface(AppController.getInstance().getAponaLohitFont());
        ((Button) mScrollView.findViewById(R.id.button_preForth)).setTypeface(AppController.getInstance().getAponaLohitFont());

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_REGISTRATION_FORTH_PAGE);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EntryForthActivity.this, EntryThirdActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void previousOnClick(View view) {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_FORTH_PORTION_PREVIOUS_REQUEST);
        onBackPressed();
    }

    public void submitOnClick(View view) {

        ConnectionDetector cd = new ConnectionDetector(AppController.getContext());
        boolean isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (radio_one != 0 && radio_two != 0 && radio_three != 0) {
                if (radio_one == R.id.rechargeYes && !(checkBox_one.isChecked() || checkBox_two.isChecked()
                        || checkBox_three.isChecked() || checkBox_four.isChecked() || checkBox_five.isChecked())) {
                    Toast.makeText(this, "অন্তত একটি অপারেটর বাছাই করুন", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (radio_two == R.id.agentYes && !(checkBox_six.isChecked() || checkBox_seven.isChecked()
                        || checkBox_eight.isChecked() || checkBox_nine.isChecked() || checkBox_ten.isChecked())) {
                    Toast.makeText(this, "অন্তত একটি সেবা বাছাই করুন", Toast.LENGTH_SHORT).show();
                } else {
                    TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(EntryForthActivity.this);
                    imeiOne = telephonyInfo.getImeiSIM1();
                    imeiTwo = telephonyInfo.getImeiSIM2();

                    if (regModel.getEmailAddress().isEmpty()) {
                        email = "0";
                    } else {
                        email = regModel.getEmailAddress();
                    }
                    if (regModel.getLandmark().isEmpty()) {
                        landmark = "0";
                    } else {
                        landmark = regModel.getLandmark();
                    }
                    if (regModel.getSalesCode().isEmpty()) {
                        sales_code = "0";
                    } else {
                        sales_code = regModel.getSalesCode();
                    }
                    if (regModel.getCollectionCode().isEmpty()) {
                        collection_code = "0";
                    } else {
                        collection_code = regModel.getCollectionCode();
                    }
                    if (regModel.getOutletImage() == null) {
                        outlet_img = "0";
                    } else {
                        outlet_img = regModel.getOutletImage();
                    }
                    if (regModel.getNidFront() == null) {
                        nid_img = "0";
                    } else {
                        nid_img = regModel.getNidFront();
                    }
                    if (regModel.getNidBack() == null) {
                        nid_back_img = "0";
                    } else {
                        nid_back_img = regModel.getNidBack();
                    }
                    if (regModel.getOwnerImage() == null) {
                        owner_img = "0";
                    } else {
                        owner_img = regModel.getOwnerImage();
                    }
                    if (regModel.getTradeLicense() == null) {
                        trade_license_img = "0";
                    } else {
                        trade_license_img = regModel.getTradeLicense();
                    }
                    if (regModel.getPassport() == null) {
                        passport_img = "0";
                    } else {
                        passport_img = regModel.getPassport();
                    }
                    if (regModel.getBirthCertificate() == null) {
                        birth_certificate_img = "0";
                    } else {
                        birth_certificate_img = regModel.getBirthCertificate();
                    }
                    if (regModel.getDrivingLicense() == null) {
                        driving_license_img = "0";
                    } else {
                        driving_license_img = regModel.getDrivingLicense();
                    }
                    if (regModel.getVisitingCard() == null) {
                        visiting_card_img = "0";
                    } else {
                        visiting_card_img = regModel.getVisitingCard();
                    }

                    if (!regModel.getOutletName().isEmpty() && !regModel.getOutletAddress().isEmpty() && !regModel.getOwnerName().isEmpty()
                            && !regModel.getBusinessId().isEmpty() && !regModel.getBusinessType().isEmpty() && !regModel.getPhnNumber().isEmpty()
                            && !regModel.getDistrictName().isEmpty() && !regModel.getThanaName().isEmpty() && !regModel.getPostcodeName().isEmpty()
                            && !regModel.getPostcodeId().isEmpty() && !email.isEmpty() && !landmark.isEmpty() && !sales_code.isEmpty()
                            && !collection_code.isEmpty() && !outlet_img.isEmpty() && !nid_img.isEmpty() && !nid_back_img.isEmpty()
                            && !owner_img.isEmpty() && !trade_license_img.isEmpty() && !passport_img.isEmpty() && !birth_certificate_img.isEmpty()
                            && !driving_license_img.isEmpty() && !visiting_card_img.isEmpty()) {

                        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_REGISTRATION_MENU, AnalyticsParameters.KEY_REGISTRATION_FORTH_PORTION_SUBMIT_REQUEST);
                        checkRequest();
                    } else {
                        Toast.makeText(this, "সঠিকভাবে ইনপুট দিন", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, "সঠিকভাবে ইনপুট দিন", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    public String method(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public void itemClickedOne(View view) {
        checkBox_one = (CheckBox) view;
        operator += "1,";
    }

    public void itemClickedTwo(View view) {
        checkBox_two = (CheckBox) view;
        operator += "2,";
    }

    public void itemClickedThree(View view) {
        checkBox_three = (CheckBox) view;
        operator += "3,";
    }

    public void itemClickedFour(View view) {
        checkBox_four = (CheckBox) view;
        operator += "4,";
    }

    public void itemClickedFive(View view) {
        checkBox_five = (CheckBox) view;
        operator += "6,";
    }

    public void itemClickedSix(View view) {
        checkBox_six = (CheckBox) view;
        operator += "14,";
    }

    public void itemClickedSeven(View view) {
        checkBox_seven = (CheckBox) view;
        operator += "19,";
    }

    public void itemClickedEight(View view) {
        checkBox_eight = (CheckBox) view;
        operator += "21,";
    }

    public void itemClickedNine(View view) {
        checkBox_nine = (CheckBox) view;
        operator += "22,";
    }

    public void itemClickedTen(View view) {
        checkBox_ten = (CheckBox) view;
        operator += "23,";
    }

    private void checkRequest() {
        if (mSubmitRequestTask != null) {
            return;
        }
        mSubmitRequestTask = new SubmitRequestTask(getString(R.string.final_reg_url));
        mSubmitRequestTask.execute();
    }

    private class SubmitRequestTask extends AsyncTask<Void, Intent, String> {


        private final String mURL;
        private String operators;

        SubmitRequestTask(String url) {
            mURL = url;

            if (!operator.equals("")) {
                operators = method(operator);
            } else {
                operators = "";
            }
        }

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override
        protected String doInBackground(Void... params) {
            String responseTxt = null;
            try {
                // Create a new HttpClient and Post Header
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(mURL);

                List<NameValuePair> nameValuePairs = new ArrayList<>(28);
                nameValuePairs.add(new BasicNameValuePair("imei", imeiOne));
                nameValuePairs.add(new BasicNameValuePair("alternate_imei", imeiTwo));
                nameValuePairs.add(new BasicNameValuePair("outlet_name", regModel.getOutletName()));
                nameValuePairs.add(new BasicNameValuePair("outlet_address", regModel.getOutletAddress()));
                nameValuePairs.add(new BasicNameValuePair("owner_name", regModel.getOwnerName()));
                nameValuePairs.add(new BasicNameValuePair("mobile_number", regModel.getPhnNumber()));
                nameValuePairs.add(new BasicNameValuePair("post_code", regModel.getPostcodeName()));
                nameValuePairs.add(new BasicNameValuePair("post_office_id", regModel.getPostcodeId()));
                nameValuePairs.add(new BasicNameValuePair("thana", regModel.getThanaName()));
                nameValuePairs.add(new BasicNameValuePair("district", regModel.getDistrictName()));
                nameValuePairs.add(new BasicNameValuePair("business_type_id", regModel.getBusinessId()));
                nameValuePairs.add(new BasicNameValuePair("business_type", regModel.getBusinessType()));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("landmark", landmark));
                nameValuePairs.add(new BasicNameValuePair("sales_code", sales_code));
                nameValuePairs.add(new BasicNameValuePair("collection_code", collection_code));
                nameValuePairs.add(new BasicNameValuePair("outlet_img", outlet_img));
                nameValuePairs.add(new BasicNameValuePair("nid_img", nid_img));
                nameValuePairs.add(new BasicNameValuePair("nid_back_img", nid_back_img));
                nameValuePairs.add(new BasicNameValuePair("owner_img", owner_img));
                nameValuePairs.add(new BasicNameValuePair("trade_license_img", trade_license_img));
                nameValuePairs.add(new BasicNameValuePair("image_passport", passport_img));
                nameValuePairs.add(new BasicNameValuePair("birth_certificate_img", birth_certificate_img));
                nameValuePairs.add(new BasicNameValuePair("driving_license_imege", driving_license_img));
                nameValuePairs.add(new BasicNameValuePair("visiting_card_img", visiting_card_img));
                nameValuePairs.add(new BasicNameValuePair("operators", operators));
                nameValuePairs.add(new BasicNameValuePair("downloadSource", downloadSource));
                nameValuePairs.add(new BasicNameValuePair("dtype", "Tab"));


                nameValuePairs.add(new BasicNameValuePair("nidFrontPic", ""+regModel.getNidFront()));
                nameValuePairs.add(new BasicNameValuePair("nidBackPic", ""+regModel.getNidBack()));
                nameValuePairs.add(new BasicNameValuePair("nidNumber", ""+regModel.getNidNumber()));
                nameValuePairs.add(new BasicNameValuePair("nidName", ""+regModel.getNidName()));
                nameValuePairs.add(new BasicNameValuePair("nidFatherName", ""+regModel.getNidFatherName()));
                nameValuePairs.add(new BasicNameValuePair("nidMotherName", ""+regModel.getNidFatherName()));
                nameValuePairs.add(new BasicNameValuePair("nidBirthday", ""+regModel.getNidBirthday()));
                nameValuePairs.add(new BasicNameValuePair("nidAddress", ""+regModel.getNidAddress()));


                nameValuePairs.add(new BasicNameValuePair("smartCardFrontPic", ""+regModel.getSmartCardFront()));
                nameValuePairs.add(new BasicNameValuePair("smartCardBackPic", ""+regModel.getSmartCardBack()));
                nameValuePairs.add(new BasicNameValuePair("smartCardNumber", ""+regModel.getSmartCardNumber()));
                nameValuePairs.add(new BasicNameValuePair("smartCardName", ""+regModel.getSmartCardName()));
                nameValuePairs.add(new BasicNameValuePair("smartCardFatherName", ""+regModel.getSmartCardFatherName()));
                nameValuePairs.add(new BasicNameValuePair("smartCardMotherName", ""+regModel.getSmartCardMotherName()));
                nameValuePairs.add(new BasicNameValuePair("smartCardBirthday", ""+regModel.getSmartCardBirthday()));
                nameValuePairs.add(new BasicNameValuePair("smartCardAddress", ""+regModel.getSmartCardAddress()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(EntryForthActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT);
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            mSubmitRequestTask = null;
           dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                showTransferMessage(status, message);
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(EntryForthActivity.this, R.string.try_again_msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mSubmitRequestTask = null;
            dismissProgressDialog();
        }
    }

    @SuppressWarnings("deprecation")
    private void showTransferMessage(String status_code, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (status_code.equalsIgnoreCase("200")) {
            builder.setTitle(Html.fromHtml("<font color='#66cc00'>" + getString(R.string.success_msg) + "</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#e62e00'>" + getString(R.string.request_failed_msg) + "</font>"));
        }
        builder.setMessage(message);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                Intent intent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
        alert.setCanceledOnTouchOutside(false);
    }

}
