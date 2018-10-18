package com.cloudwell.paywell.services.activity.payments.bkash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
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
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

public class ConfirmPaymentActivity extends AppCompatActivity {

    private static final String TAG_RESPONSE_STATUS = "Status";
    private static final String TAG_MESSAGE = "Message";
    private static final String TAG_PAYMENT_REQUESTS = "Payment Requests";
    public static final String JSON_RESPONSE = "jsonResponse";

    private RelativeLayout mRelativeLayout;
    private ConnectionDetector cd;
    private static AppHandler mAppHandler;
    private ListView listView;
    private CustomAdapter adapter;
    private int trx_length;
    public static String[] mAmount = null;
    public static String[] mId = null;
    public static String[] mStatus = null;
    public static String[] mDate = null;
    private static String mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_bkash_payment_confirm_title);

        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        mRelativeLayout = findViewById(R.id.relativeLayout);
        listView = findViewById(R.id.listView);
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
            mStatus = new String[jsonArray.length()];
            mDate = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = jsonArray.getJSONObject(i);

                String id = jo.getString("id");
                String status = jo.getString("Status");
                String amount = jo.getString("Amount");
                String date = jo.getString("Date Time");

                mId[i] = id;
                mStatus[i] = status;
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

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.purpose_declare_list_row, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.amount = convertView.findViewById(R.id.amount);
                viewHolder.datetime = convertView.findViewById(R.id.dateTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String amount = getString(R.string.tk_des) + " " + mAmount[position];
            String date = getString(R.string.date_and_time_des) + " " + mDate[position];
            viewHolder.amount.setText(amount);
            viewHolder.datetime.setText(date);

            return convertView;
        }

        private class ViewHolder {
            TextView amount, datetime;
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
        final String reqId = id;

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
                            "&req_id=" + reqId,
                            "&format=" + "json",
                            "&version=" + AppLoadingActivity.versionName,
                            "&gateway_id=" + mAppHandler.getGatewayId());
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
            String url = params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6];
            //String url = params[0] + params[1] + params[2] + params[3] + params[4];
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
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
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
                dialogInterface.dismiss();
                startActivity(new Intent(ConfirmPaymentActivity.this, BKashMenuActivity.class));
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
