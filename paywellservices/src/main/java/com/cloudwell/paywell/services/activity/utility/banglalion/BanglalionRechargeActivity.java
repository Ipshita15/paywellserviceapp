package com.cloudwell.paywell.services.activity.utility.banglalion;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.model.BanglalionHistory;
import com.cloudwell.paywell.services.activity.utility.banglalion.model.RechargeRequestPojo;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.StringConstant;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanglalionRechargeActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPin;
    private AppCompatAutoCompleteTextView mAccountNo;
    private Button mConfirm;
    private ConnectionDetector cd;
    private EditText mAmount;
    private String accountNo;
    private String amount;
    private AppHandler mAppHandler;
    private LinearLayout mLinearLayout;
    private AsyncTask<String, Integer, String> mSubmitAsync;
    private AsyncTask<Void, Void, Void> insertBanglaliohHistoryAsyncTask;
    private AsyncTask<Void, Void, Void> getAllBanglalionHistoryAsyncTask;

    List<String> customerNumberList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banglalion_recharge);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_utility_banglalion_recharge_title);
        }
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        initView();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE);


        addRecentUsedList();
    }

    private void addRecentUsedList() {
        RecentUsedMenu recentUsedMenu= new RecentUsedMenu(StringConstant.KEY_home_utility_banglalion_recharge, StringConstant.KEY_home_utility, IconConstant.KEY_ic_banglalion_recharge, 0, 28);
        addItemToRecentListInDB(recentUsedMenu);
    }

    private void initView() {
        mLinearLayout = findViewById(R.id.banglalionRechargeLL);
        TextView _pin = findViewById(R.id.tvQbeePin);
        TextView _qubeeAccNo = findViewById(R.id.tvQbeeAccount);
        TextView _amount = findViewById(R.id.tvQbeeAmount);

        mPin = findViewById(R.id.etQubeePin);
        mAccountNo = findViewById(R.id.etQubeeAccount);
        mAmount = findViewById(R.id.etQbeeAmount);
        mConfirm = findViewById(R.id.btnQubeeConfirm);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            _pin.setTypeface(AppController.getInstance().getOxygenLightFont());
            mPin.setTypeface(AppController.getInstance().getOxygenLightFont());
            _qubeeAccNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAccountNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            _amount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mAmount.setTypeface(AppController.getInstance().getOxygenLightFont());
            mConfirm.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            _pin.setTypeface(AppController.getInstance().getAponaLohitFont());
            mPin.setTypeface(AppController.getInstance().getAponaLohitFont());
            _qubeeAccNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAccountNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            _amount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mAmount.setTypeface(AppController.getInstance().getAponaLohitFont());
            mConfirm.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        mConfirm.setOnClickListener(this);

        getAllBanglalionHistoryAsyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                customerNumberList.clear();
                List<BanglalionHistory> banglalionHistories = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mUtilityDab().getAllBanglalionHistoryHistory();
                for (int i = 0; i < banglalionHistories.size(); i++) {
                    customerNumberList.add(banglalionHistories.get(i).getCustomerNumber());
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(BanglalionRechargeActivity.this, android.R.layout.select_dialog_item, customerNumberList);
                mAccountNo.setThreshold(1);
                mAccountNo.setAdapter(adapter);
                mAccountNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        mAccountNo.showDropDown();
                    }
                });




            }

        }.execute();

        mAccountNo.setOnTouchListener((v, event) -> {
            mAccountNo.showDropDown();
            return false;
        });
    }

    public void onClick(View v) {
        if (v == mConfirm) {
            String _pin = mPin.getText().toString().trim();
            accountNo = mAccountNo.getText().toString().trim();
            amount = mAmount.getText().toString().trim();
            if (_pin.length() == 0) {
                mPin.setError(Html.fromHtml("<font color='red'>" + getString(R.string.pin_no_error_msg) + "</font>"));
            } else if (accountNo.length() < 4) {
                mAccountNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.banglalion_acc_error_msg) + "</font></font>"));
            } else if (amount.length() == 0) {
                mAmount.setError(Html.fromHtml("<font color='red'>" + getString(R.string.amount_error_msg) + "</font></font>"));
            } else {
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    submitRecharge(mAppHandler.getUserName(), accountNo, amount, _pin);
                }
            }
        }
    }

    private void submitRecharge(String userName, String accountNo, String amount, String pin) {
        showProgressDialog();
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(BanglalionRechargeActivity.this).getRID());
        RechargeRequestPojo pojo = new RechargeRequestPojo();
        pojo.setAmount(amount);
        pojo.setCustomerID(accountNo);
        pojo.setPassword(pin);
        pojo.setRef_id(uniqueKey);
        pojo.setUserName(userName);


        ApiUtils.getAPIServiceV2().banglalionRecharge(pojo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();

                if (response.code() == 200){
                 try {
                     JSONObject jsonObject = new JSONObject(response.body().string());
                     int status = jsonObject.getInt("status");
                     String message = jsonObject.getString("message");
                     if (status == 200){
                         String trxId = jsonObject.getString("trans_id");
                         JSONObject data = jsonObject.getJSONObject("data");
                         if (data != null) {
                             String blcTrx = data.getString("BLCTrx");
                             String amount = data.getString("amount");
                             String retailCommission = data.getString("retCommission");
                             String accountNum = data.getString("customerID");
                             String hotLine = data.getString("contact");
                             showStatusDialog(message, accountNum, amount, trxId, blcTrx, retailCommission, hotLine);
                         }
                     }else {
                         showErrorMessagev1(message);
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


    private void showStatusDialog(String msg, String accountNo, String amount, String trxId, String banglalinkTrx, String retailCommission, String hotline) {
        StringBuilder reqStrBuilder = new StringBuilder();
        reqStrBuilder.append(getString(R.string.acc_no_des) + " " + accountNo
                + "\n" + getString(R.string.amount_des) + " " + amount + " " + R.string.tk_des
                + "\n" + getString(R.string.trx_id_des) + " " + trxId
                + "\n" + getString(R.string.banglalion_trx_id_des) + " " + banglalinkTrx
                + "\n" + getString(R.string.retail_commission_des) + " " + retailCommission + " " + R.string.tk_des
                + "\n\n" + getString(R.string.using_paywell_des)
                + "\n" + getString(R.string.hotline_des) + " " + hotline);
        AlertDialog.Builder builder = new AlertDialog.Builder(BanglalionRechargeActivity.this);
        builder.setTitle("Result " + msg);
        builder.setMessage(reqStrBuilder.toString());
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                onBackPressed();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubmitAsync != null) {
            mSubmitAsync.cancel(true);
        }
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
        }
        return super.onOptionsItemSelected(item);
    }
}
