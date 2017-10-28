package com.cloudwell.paywell.services.activity.mfs.mycash.cash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.cloudwell.paywell.services.activity.mfs.mycash.CashInOutActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.InquiryMenuActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CashOutActivity extends AppCompatActivity {

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    ListView listView;
    TrxAdapter mAdapter;
    String array;
    int trx_length;
    public static String[] mServiceType = null;
    public static String[] mTrx = null;
    public static String[] mCustomerMobileNo = null;
    public static String[] mBillNo = null;
    public static String[] mCustomerPhoneNo = null;
    public static String[] mAmount = null;
    public static String[] mMessage = null;
    public static String[] mDate = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_out);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_cash_out);
        mAppHandler = new AppHandler(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.trxRelativeLayout);
        mCd = new ConnectionDetector(this);
        listView = (ListView) findViewById(R.id.trxListView);
        mAdapter = new TrxAdapter(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array = bundle.getString("array");
        }
        initializeAdapter();
    }

    public void initializeAdapter(){
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
            trx_length = 0;

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

                    AlertDialog.Builder builder = new AlertDialog.Builder(CashOutActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage("Service Type: " + service_type
                            + "\nTrx ID: " + trx_id
                            + "\n Cust. Phone No: " + customer_mobile_no
                            + "\nBill Number: " + bill_number
                            + "\nProvider Phone No: " + bill_customer_phone
                            + "\nAmount: " + total_amount
                            + "\nMessage: " + response_msg
                            + "\nDate: " + request_datetime);
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

        } catch (JSONException e) {
            e.printStackTrace();
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
        Intent intent = new Intent(CashOutActivity.this, CashInOutActivity.class);
        startActivity(intent);
        finish();
    }

    public class TrxAdapter extends BaseAdapter {

        private final Context mContext;

        public TrxAdapter(Context context) {
            mAppHandler = new AppHandler(context);
            mContext = context;
        }

        @Override
        public int getCount() { return trx_length; }

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mycash_cashout, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.serviceType = (TextView) convertView.findViewById(R.id.serviceType);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.trxID = (TextView) convertView.findViewById(R.id.trxID);
                viewHolder.dateTime = (TextView) convertView.findViewById(R.id.dateTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.serviceType.setText(mServiceType[position]);
            viewHolder.amount.setText("Tk. " + mAmount[position]);
            viewHolder.trxID.setText(mCustomerMobileNo[position]);
            viewHolder.dateTime.setText(mDate[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView serviceType;
            TextView amount;
            TextView trxID;
            TextView dateTime;
        }
    }
}
