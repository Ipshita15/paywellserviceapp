package com.cloudwell.paywell.services.activity.utility.passport;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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

/**
 * Created by android on 6/29/2016.
 */
public class PassportLogActivity extends AppCompatActivity {

    public static final String TRX_DETAILS = "trxDetails";
    private static final String TAG_TRX_ID = "trx_id";
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_EMAIL_ID = "email_id";
    private static final String TAG_BILL_AMOUNT = "bill_amount";
    private static final String TAG_BILL_STATUS = "status_name";
    private static final String TAG_REQEST_DATETIME = "request_datetime";
    private static final String TAG_PHONE = "notfying_phone_number";

    private AppHandler mAppHandler;
    private RelativeLayout mRelativeLayout;
    private ConnectionDetector mCd;
    ListView listView;
    TrxAdapter mAdapter;
    String array;
    int trx_length;
    public static String[] mTrx = null;
    public static String[] mFullName = null;
    public static String[] mEmail = null;
    public static String[] mAmount = null;
    public static String[] mMessage = null;
    public static String[] mDate = null;
    public static String[] mPhoneNo = null;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_log);
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
            mFullName = new String[jsonArray.length()];
            mEmail = new String[jsonArray.length()];
            mAmount = new String[jsonArray.length()];
            mMessage = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];
            mPhoneNo = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mTrx[i] = object.getString(TAG_TRX_ID);
                mFullName[i] = object.getString(TAG_FULL_NAME);
                mEmail[i] = object.getString(TAG_EMAIL_ID);
                mAmount[i] = object.getString(TAG_BILL_AMOUNT);
                mMessage[i] = object.getString(TAG_BILL_STATUS);
                mDate[i] = object.getString(TAG_REQEST_DATETIME);
                mPhoneNo[i] = object.getString(TAG_PHONE);

                trx_length++;
            }
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String trx_id = mTrx[position];
                    String customer_full_name = mFullName[position];
                    String email = mEmail[position];
                    String total_amount = mAmount[position];
                    String response_msg = mMessage[position];
                    String request_datetime = mDate[position];
                    String customer_phone = mPhoneNo[position];

                    data = "";
                    if(trx_id.length() != 0 && !trx_id.equals("") && !trx_id.equals("null")){
                        data += "\nTrx ID: " + trx_id;
                    } if(customer_full_name.length() != 0 && !customer_full_name.equals("") && !customer_full_name.equals("null")){
                        data += "\nFull Name: " + customer_full_name;
                    } if(email.length() != 0 && !email.equals("") && !email.equals("null")){
                        data += "\nEmail: " + email;
                    } if(total_amount.length() != 0 && !total_amount.equals("") && !total_amount.equals("null")){
                        data += "\nAmount: " + total_amount;
                    } if(response_msg.length() != 0 && !response_msg.equals("") && !response_msg.equals("null")){
                        data += "\nStatus: " + response_msg;
                    } if(request_datetime.length() != 0 && !request_datetime.equals("") && !request_datetime.equals("null")){
                        data += "\nDate: " + request_datetime;
                    } if(customer_phone.length() != 0 && !customer_phone.equals("") && !customer_phone.equals("null")){
                        data += "\nPhone No: " + customer_phone;
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(PassportLogActivity.this);
                    builder.setTitle("Status");
                    builder.setMessage(data + "\n\nAfter successful transaction, please click\"Receipt\" button \nand print passport payment receipt after filling up Reference No. and Mobile Number on the next page\n");
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Receipt", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://ibanking.tblbd.com/CheckoutReports/Passport_Payment_Receipt.aspx"));
                            startActivity(browserIntent);
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
        Intent intent = new Intent(PassportLogActivity.this, PassportMainActivity.class);
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
            viewHolder.amount.setText("Tk. " + mAmount[position]);
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
