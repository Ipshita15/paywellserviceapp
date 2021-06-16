package com.cloudwell.paywell.services.activity.mfs.mycash.manage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.ManageMenuActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.cash.model.ReqeustPaymentConfmation;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentConfirmationActivity extends BaseActivity {

    public static final String JSON_RESPONSE = "jsonResponse";
    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    private ListView listView;
    private TrxAdapter mAdapter;
    private String array;
    private int trx_length;
    public static String[] mStatus = null;
    public static String[] mDateTime = null;
    public static String[] mId = null;
    public static String[] mAmount = null;
    private static final String TAG_STATUS = "status";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirmation);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_bkash_payment_confirm_title);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mRelativeLayout = findViewById(R.id.trxRelativeLayout);
        mCd = new ConnectionDetector(AppController.getContext());
        listView = findViewById(R.id.trxListView);
        mAdapter = new TrxAdapter(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array = bundle.getString(JSON_RESPONSE);
        }
        initializeAdapter();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_PAYMENT_CONFIRMATION);

    }

    public void initializeAdapter() {
        try {
            JSONArray jsonArray = new JSONArray(array);
            mStatus = new String[jsonArray.length()];
            mDateTime = new String[jsonArray.length()];
            mId = new String[jsonArray.length()];
            mAmount = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mStatus[i] = object.getString("Status");
                mDateTime[i] = object.getString("Date Time");
                mId[i] = object.getString("id");
                mAmount[i] = object.getString("Amount");
                trx_length++;
            }
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final String trx_id = mId[position];

                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentConfirmationActivity.this);
                    builder.setTitle(R.string.confirm_title_msg);
                    builder.setMessage(R.string.alert_confirm_msg);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            if (!mCd.isConnectingToInternet()) {
                                AppHandler.showDialog(PaymentConfirmationActivity.this.getSupportFragmentManager());
                            } else {
//                                new SubmitAsync().execute(getResources().getString(R.string.mycash_balance_transfer_confirm), trx_id);
                                callMyCashPendingCashRequestConfirmation(trx_id);
                            }
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
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private void callMyCashPendingCashRequestConfirmation(String trx_id) {

        showProgressDialog();
        ReqeustPaymentConfmation m = new ReqeustPaymentConfmation();
        m.setUsername(mAppHandler.getUserName());
        m.setPassword(mAppHandler.getPin());
        m.setPendingId(trx_id);

        ApiUtils.getAPIServiceV2().myCashPendingCashRequestConfirmation(m).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressDialog();
                dismissProgressDialog();
                try {
                    String result = response.body().string();
                    if (result != null) {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString(TAG_STATUS);
                        String msg = jsonObject.getString(TAG_MESSAGE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentConfirmationActivity.this);
                        builder.setTitle("Result");
                        builder.setMessage(msg);
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
                            showTryAgainDialog();
                    }
                } catch (Exception e) {
                    showTryAgainDialog();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PaymentConfirmationActivity.this, ManageMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public class TrxAdapter extends BaseAdapter {

        private final Context mContext;

        private TrxAdapter(Context context) {
            mAppHandler = AppHandler.getmInstance(getApplicationContext());
            mContext = context;
        }

        @Override
        public int getCount() {
            return trx_length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mycash_trx_log, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.status = convertView.findViewById(R.id.serviceType);
                viewHolder.amount = convertView.findViewById(R.id.amount);
                viewHolder.datetime = convertView.findViewById(R.id.trxID);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            if (mStatus[position].equals("600")) {
                viewHolder.status.setText("Pending");
            } else {
                viewHolder.status.setText(mStatus[position]);
            }
            viewHolder.amount.setText(mAmount[position]);
            viewHolder.datetime.setText(mDateTime[position]);
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                viewHolder.status.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.amount.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.datetime.setTypeface(AppController.getInstance().getOxygenLightFont());
            } else {
                viewHolder.status.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.amount.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.datetime.setTypeface(AppController.getInstance().getAponaLohitFont());
            }
            return convertView;
        }


        private class ViewHolder {
            TextView status, amount, datetime;
        }
    }
}
