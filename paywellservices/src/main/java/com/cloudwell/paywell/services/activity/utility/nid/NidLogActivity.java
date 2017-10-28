package com.cloudwell.paywell.services.activity.utility.nid;

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
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("ALL")
public class NidLogActivity extends AppCompatActivity {

    public static final String TRX_DETAILS = "trxDetails";
    private static final String TAG_TRX_ID = "trx_id";
    private static final String TAG_NID_NUMBER = "nid_number";
    private static final String TAG_PHONE_NUMBER = "notfying_phone_number";
    private static final String TAG_BILL_AMOUNT = "bill_amount";
    private static final String TAG_STATUS_NAME = "status_name";
    private static final String TAG_REQEST_DATETIME = "request_datetime";

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    ListView listView;
    TrxAdapter mAdapter;
    String array;
    int trx_length;
    public static String[] mTrx = null;
    public static String[] mNIDNo = null;
    public static String[] mPhoneNo = null;
    public static String[] mAmount = null;
    public static String[] mMessage = null;
    public static String[] mDate = null;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_log_nid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_topup_trx_log);

        mAppHandler = new AppHandler(this);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.trxRelativeLayout);
        mCd = new ConnectionDetector(this);
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new TrxAdapter(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array = bundle.getString(TRX_DETAILS);
        }
        initializeAdapter();
    }

    public void initializeAdapter(){
        try {
            JSONArray jsonArray = new JSONArray(array);
            mTrx = new String[jsonArray.length()];
            mNIDNo = new String[jsonArray.length()];
            mPhoneNo = new String[jsonArray.length()];
            mAmount = new String[jsonArray.length()];
            mMessage = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mTrx[i] = object.getString(TAG_TRX_ID);
                mNIDNo[i] = object.getString(TAG_NID_NUMBER);
                mPhoneNo[i] = object.getString(TAG_PHONE_NUMBER);
                mAmount[i] = object.getString(TAG_BILL_AMOUNT);
                mMessage[i] = object.getString(TAG_STATUS_NAME);
                mDate[i] = object.getString(TAG_REQEST_DATETIME);
                trx_length++;
            }
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String trx_id = mTrx[position];
                    String nid_no = mNIDNo[position];
                    String customer_phone = mPhoneNo[position];
                    String total_amount = mAmount[position];
                    String response_msg = mMessage[position];
                    String request_datetime = mDate[position];

                    data = "";
                    if(trx_id.length() != 0 && !trx_id.equals("") && !trx_id.equals("null")){
                        data += "\nTrx ID: " + trx_id;
                    } if(nid_no.length() != 0 && !nid_no.equals("") && !nid_no.equals("null")){
                        data += "\nNID No: " + nid_no;
                    } if(customer_phone.length() != 0 && !customer_phone.equals("") && !customer_phone.equals("null")){
                        data += "\nPhone No: " + customer_phone;
                    } if(total_amount.length() != 0 && !total_amount.equals("") && !total_amount.equals("null")){
                        data += "\nAmount: " + total_amount;
                    } if(response_msg.length() != 0 && !response_msg.equals("") && !response_msg.equals("null")){
                        data += "\nStatus: " + response_msg;
                    } if(request_datetime.length() != 0 && !request_datetime.equals("") && !request_datetime.equals("null")){
                        data += "\nDate: " + request_datetime;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(NidLogActivity.this);
                    builder.setTitle("Status");
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(NidLogActivity.this, NidMainActivity.class);
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
            final TrxAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_mycash_trx_log, parent, false);
                viewHolder = new TrxAdapter.ViewHolder();
                viewHolder.serviceType = (TextView) convertView.findViewById(R.id.serviceType);
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.trxID = (TextView) convertView.findViewById(R.id.trxID);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (TrxAdapter.ViewHolder) convertView.getTag();
            }

            viewHolder.serviceType.setText(mMessage[position]);
            viewHolder.amount.setText(getString(R.string.tk_des) + " " + mAmount[position]);
            viewHolder.trxID.setText(mTrx[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView serviceType;
            TextView amount;
            TextView trxID;
        }
    }
}
