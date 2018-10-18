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
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReversePWBalanceActivity extends AppCompatActivity {

    public static String RESPONSE_DATA;

    private AppHandler mAppHandler;
    private ConnectionDetector cd;
    private RelativeLayout mRelativeLayout;
    ListView listView;
    String date = "";
    public static int size = 0;
    CustomAdapter mAdapter;
    private ArrayList<String> myArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_main);

        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_payment_transactions_title);
        }
        mAdapter = new CustomAdapter(this);

        initialize();
    }

    private void initialize() {
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        mRelativeLayout = findViewById(R.id.relativeLayout);
        listView = findViewById(R.id.listView);

        try {
            JSONArray jsonArray = new JSONArray(RESPONSE_DATA);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                String newOne = object.getString("add_datetime");

                String sub_date_comp = newOne.substring(0, 10);
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                Date dateFormatedFirst = inputFormat.parse(sub_date_comp);
                String outputDateStr = outputFormat.format(dateFormatedFirst);

                if (!date.equals(outputDateStr)) {
                    date = outputDateStr;
                    mAdapter.addSectionHeaderItem(date);
                    myArray.add(date);
                    mAdapter.notifyDataSetChanged();

                    String result = object.getString("request_id") + "@" + object.getString("amount") + "@" + object.getString("payment_type") + "@" + object.getString("add_datetime");
                    myArray.add(result);
                    mAdapter.addItem(result);
                    mAdapter.notifyDataSetChanged();

                } else {
                    String result = object.getString("request_id") + "@" + object.getString("amount") + "@" + object.getString("payment_type") + "@" + object.getString("add_datetime");
                    mAdapter.addItem(result);
                    myArray.add(result);
                    mAdapter.notifyDataSetChanged();
                }
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mAdapter.mData.get(position).contains("@")) {
                        showConfirmation(position);
                    }
                }
            });

            if (listView.getAdapter() == null)
                listView.setAdapter(mAdapter);
            else {
                mAdapter.updateArray(); //update your adapter's data
            }
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
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
        Intent intent = new Intent(ReversePWBalanceActivity.this, BKashBalanceRequestMenuActivity.class);
        startActivity(intent);
        finish();
    }

    public class CustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        private ArrayList<String> mData = new ArrayList<>();
        private ArrayList<String> array = new ArrayList<>();

        private LayoutInflater mInflater;

        String splitArray_row_first[];
        String splitArray_row_second[];

        public CustomAdapter(Context context) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void addItem(final String item) {
            mData.add(item);
            array.add("data");
            notifyDataSetChanged();
        }

        private void addSectionHeaderItem(final String item) {
            mData.add(item);
            array.add("header");
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            if (array.get(position).equals("header")) {
                return TYPE_SEPARATOR;
            } else {
                return TYPE_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getCount() {
            return array.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        private void updateArray() {
            mData.clear();
            mData = myArray;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            DecimalFormat decimalFormat = new DecimalFormat("0.00");
            int rowType;
            if (mData.get(position).contains("@")) {
                rowType = 0;
            } else {
                rowType = 1;
            }

            if (convertView == null) {
                holder = new ViewHolder();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        String splitArray[] = mData.get(position).split("@");
                        holder.textView.clearComposingText();
                        holder.textView.setText(splitArray[0]);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_payment_reverse, parent, false);

                        holder.amount = convertView.findViewById(R.id.amount);

                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        Double value_pre = Double.parseDouble(splitArray_row_first[1]);
                        String type_data = "Tk. " + decimalFormat.format(value_pre);

                        holder.amount.setText(type_data);
                        holder.amount.setBackgroundResource(R.drawable.square_right);
                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        String splitArray[] = mData.get(position).split("@");
                        holder.textView.clearComposingText();
                        holder.textView.setText(splitArray[0]);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_payment_reverse, parent, false);

                        holder.amount = convertView.findViewById(R.id.amount);

                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        Double value_pre = Double.parseDouble(splitArray_row_second[1]);
                        String type_data = "Tk. " + decimalFormat.format(value_pre);

                        holder.amount.setText(type_data);
                        holder.amount.setBackgroundResource(R.drawable.square_right);

                        break;
                }
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView textView, amount;
        }
    }

    private void showConfirmation(int position) {
        final String array[] = mAdapter.mData.get(position).split("@");

        String msg = getString(R.string.alert_reverse_msg) + " " + array[1] + " " + getString(R.string.tk_des) + "?";

        AlertDialog.Builder builder = new AlertDialog.Builder(ReversePWBalanceActivity.this);
        builder.setMessage(msg);
        builder.setTitle(getString(R.string.confirm_title_msg));
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                new BkashPWBalanceReverseConfirmAsync().execute(getResources().getString(R.string.bkash_reverse_url), array[1], array[0]);
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
    }

    @SuppressWarnings("deprecation")
    private class BkashPWBalanceReverseConfirmAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ReversePWBalanceActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                //add data
                List<NameValuePair> nameValuePairs = new ArrayList<>(7);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("password", mAppHandler.getPin()));
                nameValuePairs.add(new BasicNameValuePair("amount", params[1]));
                nameValuePairs.add(new BasicNameValuePair("requestId", params[2]));
                nameValuePairs.add(new BasicNameValuePair("paymentType", "1"));
                nameValuePairs.add(new BasicNameValuePair("gateway_id", mAppHandler.getGatewayId()));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String response) {
            progressDialog.cancel();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ReversePWBalanceActivity.this);
                    builder.setMessage(msg);
                    builder.setTitle("Result");
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
