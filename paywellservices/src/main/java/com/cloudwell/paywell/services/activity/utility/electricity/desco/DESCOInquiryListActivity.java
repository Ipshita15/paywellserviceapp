package com.cloudwell.paywell.services.activity.utility.electricity.desco;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DESCOInquiryListActivity extends AppCompatActivity {

    private static final String TAG_MSG_TEXT = "msg_text";
    public static final String JSON_RESPONSE = "jsonResponse";
    public static final String TAG_SERVICE_TYPE = "service_type";
    public static final String TAG_TRX_ID = "trx_id_1";
    public static final String TAG_BILL_NUMBER = "bill_number";
    public static final String TAG_PHN_NUMBER = "bill_customer_phone";
    public static final String TAG_TOTAL_AMOUNT = "total_amount";
    public static final String TAG_RESPONSE_MSG = "response_msg";
    public static final String TAG_RESPONSE_MSG_2 = "response_msg2";
    public static final String TAG_RESPONSE_STATUS_INQ = "status_name_enquiry";
    public static final String TAG_RESPONSE_STATUS_BILL = "status_name_bill_pay";
    public static final String TAG_DATE_TIME = "request_datetime";

    private static RelativeLayout mRelativeLayout;
    private static ConnectionDetector cd;
    private static AppHandler mAppHandler;
    ListView listView;
    CustomAdapter adapter;
    int trx_length;
    public static String[] mService = null;
    public static String[] mTrxId = null;
    public static String[] mBill = null;
    public static String[] mPhn = null;
    public static String[] mAmount = null;
    private static String[] mMessageInq;
    private static String[] mMessageBill;
    public static String[] mDate = null;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desco_inquiry_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_inquiry);

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(this);

        String jsonResponse = null;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            jsonResponse = bundle.getString(JSON_RESPONSE);
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_MSG_TEXT);

            trx_length = 0;
            mService = new String[jsonArray.length()];
            mTrxId = new String[jsonArray.length()];
            mBill = new String[jsonArray.length()];
            mPhn = new String[jsonArray.length()];
            mAmount = new String[jsonArray.length()];
            mMessageInq = new String[jsonArray.length()];
            mMessageBill = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String serviceType = jo.getString(TAG_SERVICE_TYPE);
                String trxId = jo.getString(TAG_TRX_ID);
                String billNo = jo.getString(TAG_BILL_NUMBER);
                String phnNo = jo.getString(TAG_PHN_NUMBER);
                String amount = jo.getString(TAG_TOTAL_AMOUNT);
                String msg = jo.getString(TAG_RESPONSE_MSG);
                String msgTwo = jo.getString(TAG_RESPONSE_MSG_2);
                String msgInq = jo.getString(TAG_RESPONSE_STATUS_INQ);
                String msgBill = jo.getString(TAG_RESPONSE_STATUS_BILL);
                String date = jo.getString(TAG_DATE_TIME);

                if (serviceType.startsWith("DESCO")) {
                    mService[trx_length] = serviceType;
                    mTrxId[trx_length] = trxId;
                    mBill[trx_length] = billNo;
                    mPhn[trx_length] = phnNo;
                    mAmount[trx_length] = amount;
                    mMessageInq[trx_length] = msgInq;
                    mMessageBill[trx_length] = msgBill;
                    mDate[trx_length] = date;
                    trx_length++;
                } else {

                }
            }
            if (trx_length > 0) {
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String service_type = mService[position];
                        String trx_id = mTrxId[position];
                        String bill_no = mBill[position];
                        String phn_no = mPhn[position];
                        String total_amount = mAmount[position];
                        String response_msg_inq = mMessageInq[position];
                        String response_msg_bill = mMessageBill[position];
                        String request_datetime = mDate[position];

                        data = "Bill No: " + bill_no + "\nPhone Number: " + phn_no
                                + " \nTotal Amount: " + getString(R.string.tk_des) + " " + total_amount;

                        if(service_type.equalsIgnoreCase("DESCO Enquiry")) {
                            if (!response_msg_inq.isEmpty() && !response_msg_inq.equalsIgnoreCase("null") && !response_msg_inq.equalsIgnoreCase("Successful")) {
                                data += "\n\nInquiry Status: " + response_msg_inq;
                            }
                        } else {
                            if (!response_msg_bill.isEmpty() && !response_msg_bill.equalsIgnoreCase("null")) {
                                data += "\n\nBill Pay Status: " + response_msg_bill;
                            }
                        }
                        data += "\n\nTrx ID:" + trx_id + "\nDate: " + request_datetime;
                        AlertDialog.Builder builder = new AlertDialog.Builder(DESCOInquiryListActivity.this);
                        builder.setTitle(mService[position]);
                        builder.setMessage(data);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.no_data_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        onBackPressed();
                    }

                }, 3000L);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class CustomAdapter extends BaseAdapter {

        private final Context mContext;

        public CustomAdapter(Context context) {
            mAppHandler = new AppHandler(context);
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
                viewHolder.billNo = (TextView) convertView.findViewById(R.id.serviceType);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.service = (TextView) convertView.findViewById(R.id.trxID);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.billNo.setText(mService[position]);
            viewHolder.amount.setText(getString(R.string.tk_des) + " " + mAmount[position]);
            viewHolder.service.setText(mTrxId[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView billNo;
            TextView amount;
            TextView service;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (this != null) {
                this.onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DESCOInquiryListActivity.this, DESCOMainActivity.class);
        startActivity(intent);
        finish();
    }

}
