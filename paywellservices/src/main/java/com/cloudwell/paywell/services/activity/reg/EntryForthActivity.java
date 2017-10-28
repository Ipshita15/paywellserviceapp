package com.cloudwell.paywell.services.activity.reg;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.TelephonyInfo;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntryForthActivity extends AppCompatActivity {

    RadioGroup radioGroup_one, radioGroup_two, radioGroup_three;
    LinearLayout linearLayoutOne,linearLayoutTwo;
    private ConnectionDetector cd;
    private boolean isInternetPresent = false;
    TelephonyInfo telephonyInfo;
    private AppHandler mAppHandler;
    int radio_one = 0, radio_two = 0, radio_three = 0;
    String email, landmark, sales_code = "", collection_code = "", imeiOne = "", imeiTwo = "", outlet_img = "", nid_img = "",
            nid_back_img = "", owner_img = "", trade_license_img = "", operator = "", downloadSource = "";
    CheckBox checkBox_one, checkBox_two, checkBox_three, checkBox_four, checkBox_five, checkBox_six, checkBox_seven, checkBox_eight, checkBox_nine,
            checkBox_ten;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_forth);
        getSupportActionBar().setTitle("৪র্থ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAppHandler = new AppHandler(this);

        radioGroup_one = (RadioGroup) findViewById(R.id.radioGroup_one);
        linearLayoutOne = (LinearLayout) findViewById(R.id.linearLayoutOne);
        linearLayoutTwo = (LinearLayout) findViewById(R.id.linearLayoutTwo);
        radioGroup_one.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_one = checkedId;
                if (checkedId == R.id.rechargeYes) {
                    checkBox_one = (CheckBox) findViewById(R.id.item_check_gp);
                    checkBox_two = (CheckBox) findViewById(R.id.item_check_bl);
                    checkBox_three = (CheckBox) findViewById(R.id.item_check_rb);
                    checkBox_four = (CheckBox) findViewById(R.id.item_check_at);
                    checkBox_five = (CheckBox) findViewById(R.id.item_check_tt);
                    linearLayoutOne.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutOne.setVisibility(View.GONE);
                }
            }
        });
        radioGroup_two = (RadioGroup) findViewById(R.id.radioGroup_two);
        radioGroup_two.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radio_two = checkedId;
                if (checkedId == R.id.agentYes) {
                    checkBox_six = (CheckBox) findViewById(R.id.item_check_bkash);
                    checkBox_seven = (CheckBox) findViewById(R.id.item_check_rocket);
                    checkBox_eight = (CheckBox) findViewById(R.id.item_check_mcash);
                    checkBox_nine = (CheckBox) findViewById(R.id.item_check_mobile_money);
                    checkBox_ten = (CheckBox) findViewById(R.id.item_check_mycash);
                    linearLayoutTwo.setVisibility(View.VISIBLE);
                } else {
                    linearLayoutTwo.setVisibility(View.GONE);
                }
            }
        });
        radioGroup_three = (RadioGroup) findViewById(R.id.radioGroup_three);
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
        onBackPressed();
    }

    public void submitOnClick(View view) {
        cd = new ConnectionDetector(EntryForthActivity.this);
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            if (radio_one != 0 && radio_two != 0 && radio_three != 0) {
                if (radio_one == R.id.rechargeYes && !(checkBox_one.isChecked() || checkBox_two.isChecked()
                        || checkBox_three.isChecked() || checkBox_four.isChecked() || checkBox_five.isChecked())) {
                        Toast.makeText(this, "অন্তত একটি অপারেটর বাছাই করুন", Toast.LENGTH_SHORT).show();
                        return;
                } if (radio_two == R.id.agentYes && !(checkBox_six.isChecked() || checkBox_seven.isChecked()
                        || checkBox_eight.isChecked() || checkBox_nine.isChecked() || checkBox_ten.isChecked())) {
                        Toast.makeText(this, "অন্তত একটি সেবা বাছাই করুন", Toast.LENGTH_SHORT).show();
                        return;
                } else{
                    new RetailerAsync().execute(getResources().getString(R.string.final_reg_url));
                }
            } else {
                Toast.makeText(this, "সঠিকভাবে ইনপুট দিন", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.connection_error_msg, Toast.LENGTH_SHORT).show();
        }
    }

    private class RetailerAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EntryForthActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... data) {

            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                telephonyInfo = TelephonyInfo.getInstance(EntryForthActivity.this);
                imeiOne = telephonyInfo.getImeiSIM1();
                imeiTwo = telephonyInfo.getImeiSIM2();

                if (mAppHandler.getEmailAddress().equals("unknown")) {
                    email = "0";
                } else {
                    email = mAppHandler.getEmailAddress();
                }
                if (mAppHandler.getLandmark().equals("unknown")) {
                    landmark = "0";
                } else {
                    landmark = mAppHandler.getLandmark();
                }
                if (mAppHandler.getSalesCode().equals("unknown")) {
                    sales_code = "0";
                } else {
                    sales_code = mAppHandler.getSalesCode();
                }
                if (mAppHandler.getCollectionCode().equals("unknown")) {
                    collection_code = "0";
                } else {
                    collection_code = mAppHandler.getCollectionCode();
                }
                if (mAppHandler.getOutletImg().equals("unknown")) {
                    outlet_img = "0";
                } else {
                    outlet_img = mAppHandler.getOutletImg();
                }
                if (mAppHandler.getNIDImg().equals("unknown")) {
                    nid_img = "0";
                } else {
                    nid_img = mAppHandler.getNIDImg();
                }
                if (mAppHandler.getNIDBackImg().equals("unknown")) {
                    nid_back_img = "0";
                } else {
                    nid_back_img = mAppHandler.getNIDBackImg();
                }
                if (mAppHandler.getOwnerImg().equals("unknown")) {
                    owner_img = "0";
                } else {
                    owner_img = mAppHandler.getOwnerImg();
                }
                if (mAppHandler.getTradeLicenseImg().equals("unknown")) {
                    trade_license_img = "0";
                } else {
                    trade_license_img = mAppHandler.getTradeLicenseImg();
                }
                String operators;
                if(!operator.equals("")) {
                    operators = method(operator);
                }else{
                    operators = "";
                }
                Log.e("urlForReg", data[0] + "imei="+imeiOne+"&alternate_imei="+imeiTwo+"&outlet_name="+mAppHandler.getOutletName()
                    +"&outlet_address="+mAppHandler.getOutletAddress()+"&owner_name="+mAppHandler.getOwnerName()+"&mobile_number="
                    +mAppHandler.getMobileNo()+"&post_code="+mAppHandler.getPostCode()+"&thana="+mAppHandler.getThana()+"&district="
                    +mAppHandler.getDistrict()+"&business_type="+mAppHandler.getBusinessType()+"&email="+email+"&landmark="+landmark
                    +"&sales_code="+sales_code+"&collection_code="+collection_code+"&outlet_img="+outlet_img+"&nid_img="+nid_img+"&nid_back_img="
                    +nid_back_img+"&owner_img="+owner_img+"&trade_license_img="+trade_license_img+"&operators="+operators+"&downloadSource="+downloadSource);
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(20);
                nameValuePairs.add(new BasicNameValuePair("imei", imeiOne));
                nameValuePairs.add(new BasicNameValuePair("alternate_imei", imeiTwo));
                nameValuePairs.add(new BasicNameValuePair("outlet_name", mAppHandler.getOutletName()));
                nameValuePairs.add(new BasicNameValuePair("outlet_address", mAppHandler.getOutletAddress()));
                nameValuePairs.add(new BasicNameValuePair("owner_name", mAppHandler.getOwnerName()));
                nameValuePairs.add(new BasicNameValuePair("mobile_number", mAppHandler.getMobileNo()));
                nameValuePairs.add(new BasicNameValuePair("post_code", mAppHandler.getPostCode()));
                nameValuePairs.add(new BasicNameValuePair("thana", mAppHandler.getThana()));
                nameValuePairs.add(new BasicNameValuePair("district", mAppHandler.getDistrict()));
                nameValuePairs.add(new BasicNameValuePair("business_type", mAppHandler.getBusinessType()));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("landmark", landmark));
                nameValuePairs.add(new BasicNameValuePair("sales_code", sales_code));
                nameValuePairs.add(new BasicNameValuePair("collection_code", collection_code));
                nameValuePairs.add(new BasicNameValuePair("outlet_img", outlet_img));
                nameValuePairs.add(new BasicNameValuePair("nid_img", nid_img));
                nameValuePairs.add(new BasicNameValuePair("nid_back_img", nid_back_img));
                nameValuePairs.add(new BasicNameValuePair("owner_img", owner_img));
                nameValuePairs.add(new BasicNameValuePair("trade_license_img", trade_license_img));
                nameValuePairs.add(new BasicNameValuePair("operators", operators));
                nameValuePairs.add(new BasicNameValuePair("downloadSource", downloadSource));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (ClientProtocolException e) {

            } catch (IOException e) {
            }

            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equals("200")) {
                        mAppHandler.deleteRegistrationData();
                        String msg = jsonObject.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntryForthActivity.this);
                        builder.setTitle("Status");
                        builder.setMessage(msg);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                    } else {
                        String msg = jsonObject.getString("message");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EntryForthActivity.this);
                        builder.setTitle("Status");
                        builder.setMessage(msg);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                        TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(EntryForthActivity.this, R.string.services_off_msg, Toast.LENGTH_SHORT).show();
            }
        }

    }

    public String method(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    public void itemClickedOne(View view) { checkBox_one = (CheckBox) view; operator += "1,"; }

    public void itemClickedTwo(View view) { checkBox_two = (CheckBox) view; operator += "2,"; }

    public void itemClickedThree(View view) { checkBox_three = (CheckBox) view; operator += "3,"; }

    public void itemClickedFour(View view) { checkBox_four = (CheckBox) view; operator += "4,"; }

    public void itemClickedFive(View view) { checkBox_five = (CheckBox) view; operator += "6,"; }

    public void itemClickedSix(View view) { checkBox_six = (CheckBox) view; operator += "14,"; }

    public void itemClickedSeven(View view) { checkBox_seven = (CheckBox) view; operator += "19,"; }

    public void itemClickedEight(View view) { checkBox_eight = (CheckBox) view; operator += "21,"; }

    public void itemClickedNine(View view) { checkBox_nine = (CheckBox) view; operator += "22,"; }

    public void itemClickedTen(View view) { checkBox_ten = (CheckBox) view; operator += "23,"; }

}
