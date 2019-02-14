package com.cloudwell.paywell.services.activity.mfs.mycash.inquiry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.cloudwell.paywell.services.activity.mfs.mycash.InquiryMenuActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class LastTransactionsActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout relativeLayout;
    private ListView listView;
    private TrxAdapter mAdapter;
    private String array;
    private int trx_length;
    public static String[] mServiceType = null;
    public static String[] mTrx = null;
    public static String[] mCustomerMobileNo = null;
    public static String[] mBillNo = null;
    public static String[] mCustomerPhoneNo = null;
    public static String[] mAmount = null;
    public static String[] mMessage = null;
    public static String[] mDate = null;
    private String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_transactions);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_activity_log);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        relativeLayout = findViewById(R.id.trxRelativeLayout);
        listView = findViewById(R.id.trxListView);
        mAdapter = new TrxAdapter(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array = bundle.getString("array");
        }
        initializeAdapter();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_MYCASH_LAST_TRANSACTION);
    }

    public void initializeAdapter() {
        try {
            JSONArray jsonArray = new JSONArray(array);
            mServiceType = new String[jsonArray.length()];
            mTrx = new String[jsonArray.length()];
            mCustomerMobileNo = new String[jsonArray.length()];
            mBillNo = new String[jsonArray.length()];
            mCustomerPhoneNo = new String[jsonArray.length()];
            mAmount = new String[jsonArray.length()];
            mMessage = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mServiceType[i] = object.getString("service_type");
                mTrx[i] = object.getString("trx_id_1");
                mCustomerMobileNo[i] = object.getString("customer_mobile_no");
                mBillNo[i] = object.getString("bill_number");
                mCustomerPhoneNo[i] = object.getString("bill_customer_phone");
                mAmount[i] = object.getString("total_amount");
                mMessage[i] = object.getString("response_msg");
                mDate[i] = object.getString("request_datetime");

                trx_length++;
            }
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String service_type = mServiceType[position];
                    String trx_id = mTrx[position];
                    String customer_mobile_no = mCustomerMobileNo[position];
                    String bill_number = mBillNo[position];
                    String bill_customer_phone = mCustomerPhoneNo[position];
                    String total_amount = mAmount[position];
                    String response_msg = mMessage[position];
                    String request_datetime = mDate[position];

                    data = "";
                    if (service_type.length() != 0 && !service_type.equals("") && !service_type.equals("null")) {
                        data += "Service Type: " + service_type;
                    }
                    if (trx_id.length() != 0 && !trx_id.equals("") && !trx_id.equals("null")) {
                        data += "\nTrx ID: " + trx_id;
                    }
                    if (customer_mobile_no.length() != 0 && !customer_mobile_no.equals("") && !customer_mobile_no.equals("null")) {
                        data += "\nCust. Phone No: " + customer_mobile_no;
                    }
                    if (bill_number.length() != 0 && !bill_number.equals("") && !bill_number.equals("null")) {
                        data += "\nBill Number: " + bill_number;
                    }
                    if (bill_customer_phone.length() != 0 && !bill_customer_phone.equals("") && !bill_customer_phone.equals("null")) {
                        data += "\nProvider Phone No: " + bill_customer_phone;
                    }
                    if (total_amount.length() != 0 && !total_amount.equals("") && !total_amount.equals("null")) {
                        data += "\nAmount: " + total_amount;
                    }
                    if (response_msg.length() != 0 && !response_msg.equals("") && !response_msg.equals("null")) {
                        data += "\nMessage: " + response_msg;
                    }
                    if (request_datetime.length() != 0 && !request_datetime.equals("") && !request_datetime.equals("null")) {
                        data += "\nDate: " + request_datetime;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(LastTransactionsActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(data);
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
            });

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
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
        Intent intent = new Intent(LastTransactionsActivity.this, InquiryMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public class TrxAdapter extends BaseAdapter {

        private final Context mContext;

        TrxAdapter(Context context) {
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
            final TrxAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mycash_trx_log, parent, false);
                viewHolder = new TrxAdapter.ViewHolder();
                viewHolder.serviceType = convertView.findViewById(R.id.serviceType);
                viewHolder.amount = convertView.findViewById(R.id.amount);
                viewHolder.trxID = convertView.findViewById(R.id.trxID);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TrxAdapter.ViewHolder) convertView.getTag();
            }

            viewHolder.serviceType.setText(mServiceType[position]);
            if (mServiceType[position].equals("Cash In")
                    || mServiceType[position].equals("Cash Out")
                    || mServiceType[position].equals("P2M")
                    || mServiceType[position].equals("M2P")
                    || mServiceType[position].equals("DPDC")
                    || mServiceType[position].equals("DPDC Enquiry")
                    || mServiceType[position].equals("DESCO")
                    || mServiceType[position].equals("DESCO Enquiry")) {
                viewHolder.amount.setText("Tk. " + mAmount[position]);
            } else {
                viewHolder.amount.setText("");
            }
            viewHolder.trxID.setText(mTrx[position]);

            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                viewHolder.serviceType.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.amount.setTypeface(AppController.getInstance().getOxygenLightFont());
                viewHolder.trxID.setTypeface(AppController.getInstance().getOxygenLightFont());
            } else {
                viewHolder.serviceType.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.amount.setTypeface(AppController.getInstance().getAponaLohitFont());
                viewHolder.trxID.setTypeface(AppController.getInstance().getAponaLohitFont());
            }
            return convertView;
        }


        private class ViewHolder {
            TextView serviceType, amount, trxID;
        }
    }
}
