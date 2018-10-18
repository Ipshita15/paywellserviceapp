package com.cloudwell.paywell.services.activity.utility.ivac;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IvacFeeInquiryActivity extends AppCompatActivity {

    public static String TRANSLOG_TAG = "TRANSLOGTXT";
    private RelativeLayout mRelativeLayout;
    private ListView listView;
    private String date = "";
    private CustomAdapter adapter;

    private static String TAG_RESPONSE_IVAC_TRX_ID = "trx_id";
    private static String TAG_RESPONSE_IVAC_TOTAL_AMOUNT = "total_amount";
    private static String TAG_RESPONSE_IVAC_WEB_FILE = "web_file_no";
    private static String TAG_RESPONSE_IVAC_PASSPORT = "passport_no";
    private static String TAG_RESPONSE_IVAC_CENTER_NAME = "center_name";
    private static String TAG_RESPONSE_IVAC_PHN_NUM = "phone_no";
    private static String TAG_RESPONSE_IVAC_STATUS = "status_code";
    private static String TAG_RESPONSE_IVAC_MESSAGE = "response_message";
    private static String TAG_RESPONSE_IVAC_DATETIME = "trx_response_date_time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility_inquiry);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_topup_trx_log);
        }
        mRelativeLayout = findViewById(R.id.relativeLayout);
        adapter = new CustomAdapter(this);
        initializeView();
    }

    private void initializeView() {
        String response = TRANSLOG_TAG;
        if (response != null && response.length() > 0) {
            try {
                JSONArray jsonArray = new JSONArray(response);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String result;

                    String mTrx = jsonObject.getString(TAG_RESPONSE_IVAC_TRX_ID);
                    String mAmount = jsonObject.getString(TAG_RESPONSE_IVAC_TOTAL_AMOUNT);
                    String mWebFile = jsonObject.getString(TAG_RESPONSE_IVAC_WEB_FILE);
                    String mPassport = jsonObject.getString(TAG_RESPONSE_IVAC_PASSPORT);
                    String mCenterName = jsonObject.getString(TAG_RESPONSE_IVAC_CENTER_NAME);
                    String mPhnNum = jsonObject.getString(TAG_RESPONSE_IVAC_PHN_NUM);
                    String mStatus = jsonObject.getString(TAG_RESPONSE_IVAC_STATUS);
                    String mMsg = jsonObject.getString(TAG_RESPONSE_IVAC_MESSAGE);
                    String mDate = jsonObject.getString(TAG_RESPONSE_IVAC_DATETIME);

                    String sub_date_comp = mDate.substring(0, 10);

                    DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
                    Date dateFormatedFirst = inputFormat.parse(sub_date_comp);
                    String outputDateStr = outputFormat.format(dateFormatedFirst);

                    if (!date.equals(outputDateStr)) {
                        date = outputDateStr;
                        adapter.addSectionHeaderItem(date);
                    }

                    result = mWebFile + "@" + mStatus + "@" + mMsg
                            + "@" + mPassport + "@" + mTrx + "@" + mAmount + "@" + mCenterName
                            + "@" + mPhnNum + "@" + mDate;
                    adapter.addItem(result);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.no_data_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 2000);
            }

            listView = findViewById(R.id.listView);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(adapter.mData.get(position).contains("@")) {
                        showFullInfo(position);
                    }
                }
            });
        } else {
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
        Intent intent = new Intent(IvacFeeInquiryActivity.this, IvacMainActivity.class);
        startActivity(intent);
        finish();
    }

    public class CustomAdapter extends BaseAdapter {

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        public ArrayList<String> mData = new ArrayList<>();
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
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
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_polli_reg_inq, parent, false);
                        holder.webFileNo = convertView.findViewById(R.id.accNo);
                        holder.status = convertView.findViewById(R.id.status);
                        holder.passport = convertView.findViewById(R.id.trxId);

                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        holder.webFileNo.setText(splitArray_row_first[0]);
                        holder.status.setText(splitArray_row_first[2]);
                        holder.passport.setText(splitArray_row_first[3]);

                        if (splitArray_row_first[1].equalsIgnoreCase("200")) {
                            holder.status.setTextColor(Color.parseColor("#008000"));
                        } else {
                            holder.status.setTextColor(Color.parseColor("#ff0000"));
                        }
                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_polli_reg_inq, parent, false);
                        holder.webFileNo = convertView.findViewById(R.id.accNo);
                        holder.status = convertView.findViewById(R.id.status);
                        holder.passport = convertView.findViewById(R.id.trxId);

                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        holder.webFileNo.setText(splitArray_row_second[0]);
                        holder.status.setText(splitArray_row_second[2]);
                        holder.passport.setText(splitArray_row_second[3]);

                        if (splitArray_row_second[1].equalsIgnoreCase("200")) {
                            holder.status.setTextColor(Color.parseColor("#008000"));
                        } else {
                            holder.status.setTextColor(Color.parseColor("#ff0000"));
                        }
                        break;
                }
            }
            return convertView;
        }

        public class ViewHolder {
            TextView textView, webFileNo, status, passport;
        }
    }


    private void showFullInfo(int position) {
        String array[] = adapter.mData.get(position).split("@");
        String msg = "Web File: " + array[0] + "\nPassport No: " + array[3]
                + "\nAmount: " + getString(R.string.tk_des) + " " + array[5] + "\nCenter Name: " + array[6]
                +"\nPhone Number: " + array[7] +"\nDate: " + array[8]
                + "\nTrx ID: " + array[4];

        AlertDialog.Builder builder = new AlertDialog.Builder(IvacFeeInquiryActivity.this);
        if (array[1].equalsIgnoreCase("200")) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
            msg = msg + "\n\nStatus: " + array[2];
        }
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
