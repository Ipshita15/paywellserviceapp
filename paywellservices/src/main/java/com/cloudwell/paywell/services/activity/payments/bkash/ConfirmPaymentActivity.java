package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private static final String TAG_RESPONSE_STATUS = "Status";
    private static final String TAG_MESSAGE = "Message";
    private static final String TAG_PAYMENT_REQUESTS = "Payment Requests";
    public static final String JSON_RESPONSE = "jsonResponse";

    private static RelativeLayout mRelativeLayout;
    private static ConnectionDetector cd;
    private static AppHandler mAppHandler;
    ListView listView;
    CustomAdapter adapter;
    int trx_length;
    public static String[] mAmount = null;
    public static String[] mId = null;
    public static String[] mDate = null;
    private static String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_bkash_payment_confirm_title);

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
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_PAYMENT_REQUESTS);

            trx_length = 0;
            mAmount = new String[jsonArray.length()];
            mId = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String id = jo.getString("id");
                String amount = jo.getString("Amount");
                String date = jo.getString("Date Time");

                mId[i] = id;
                mAmount[i] = amount;
                mDate[i] = date;
                trx_length++;
            }

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayConfirm(mId[position]);
                }
            });

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.purpose_declare_list_row, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.datetime = (TextView) convertView.findViewById(R.id.dateTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.amount.setText(getString(R.string.tk_des) + " " + mAmount[position]);
            viewHolder.datetime.setText(getString(R.string.date_and_time_des) + " " + mDate[position]);

            return convertView;
        }


        private class ViewHolder {
            TextView amount;
            TextView datetime;
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
        Intent intent = new Intent(ConfirmPaymentActivity.this, BKashMenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    private void displayConfirm(String id) {
        final String mId= id;
        final AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPaymentActivity.this);
        builder.setTitle(R.string.confirm_title_msg);
        builder.setMessage(R.string.alert_confirm_msg);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                dialogInterface.dismiss();
                if (!cd.isConnectingToInternet()) {
                    AppHandler.showDialog(ConfirmPaymentActivity.this.getSupportFragmentManager());
                } else {
                    new ResponseAsyncNext().execute(getResources().getString(R.string.bkash_pending_req_conf),
                            "imei=" + mAppHandler.getImeiNo(),
                            "&pin=" + mAppHandler.getPin(),
                            "&req_id=" + mId,
                            "&format=" + "json");
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }


    private class ResponseAsyncNext extends AsyncTask<String, String, JSONObject> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ConfirmPaymentActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            String url = params[0] + params[1] + params[2] + params[3] + params[4];
            return jParser.getJSONFromUrl(url);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.cancel();
            try {
                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                mMessage = jsonObject.getString(TAG_MESSAGE);
                if (status.equalsIgnoreCase("602")) {
                    displayResult();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, mMessage, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public void displayResult() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPaymentActivity.this);
        builder.setTitle("Result");
        builder.setMessage(mMessage);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.cancel();
                startActivity(new Intent(ConfirmPaymentActivity.this, BKashMenuActivity.class));
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

}
