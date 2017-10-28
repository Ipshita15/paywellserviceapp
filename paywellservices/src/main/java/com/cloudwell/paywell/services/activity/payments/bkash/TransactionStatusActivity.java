package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class TransactionStatusActivity extends AppCompatActivity {

    public static String RESPONSE = "response";
    RelativeLayout relativeLayout;
    ListView listView ;
    public static String[] mPhn = null;
    public static String[] mAmount = null;
    public static String[] mDate = null;
    public static String[] mTrx = null;
    public static String length;
    String date = "";
    CustomAdapter adapter;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash_logview);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_bkash_last_trx_title);
        }

        mAppHandler = new AppHandler(this);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new CustomAdapter(this);

        displayResponsePrompt(RESPONSE);

        //BKashListAdapter adapter = new BKashListAdapter(this);
        listView.setAdapter(adapter);
    }

    private void displayResponsePrompt(String response) {

        StringBuilder reqStrBuilder = new StringBuilder();
        if (response != null && response.length() > 0) {
            String[] splitArray = response.split("@");
            length = splitArray[1];
            mPhn = new String[response.length()];
            mAmount = new String[response.length()];
            mDate = new String[response.length()];
            mTrx = new String[response.length()];
            for (int i = 0; i < Integer.parseInt(splitArray[1].toString()); i++) {
                String result;

                //mPhn[i] = "Received From " + splitArray[2 + 6 * i];
                mPhn[i] = splitArray[2 + 6 * i];
                //mAmount[i] = "Tk. " + splitArray[7 + 6 * i];
                mAmount[i] = splitArray[7 + 6 * i];
                mDate[i] = splitArray[4 + 6 * i];
                //mTrx[i] = "Trx. ID: " + splitArray[5 + 6 * i];
                mTrx[i] = splitArray[5 + 6 * i];

                String sub_date_comp = mDate[i].substring(0, 10);
                if (!date.equals(sub_date_comp)) {
                    date = sub_date_comp;
                    adapter.addSectionHeaderItem(date);
                }
                String time = mDate[i].substring(11);
                result = mPhn[i] + "@" + mAmount[i] + "@" + time + "@" + mTrx[i];
                adapter.addItem(result);
            }
        } else {
            Snackbar snackbar = Snackbar.make(relativeLayout, R.string.no_data_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
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
        ;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransactionStatusActivity.this, BKashMenuActivity.class);
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
                        holder.textView = (TextView) convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_bkash_success_trx_log, parent, false);
                        holder.username = (TextView) convertView.findViewById(R.id.username);
                        holder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
                        holder.amount = (TextView) convertView.findViewById(R.id.amount);
                        holder.time = (TextView) convertView.findViewById(R.id.time);
                        holder.trxId = (TextView) convertView.findViewById(R.id.trxId);
                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        String amountStr = "Tk." + splitArray_row_first[1];
                        String trxIdStr = "Trx: " + splitArray_row_first[3];
                        holder.username.setText(mAppHandler.getRID().substring(mAppHandler.getRID().length() - 5, mAppHandler.getRID().length()));
                        holder.phnNo.setText(splitArray_row_first[0]);
                        holder.amount.setText(amountStr);
                        holder.time.setText(splitArray_row_first[2]);
                        holder.trxId.setText(trxIdStr);

                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = (TextView) convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_bkash_success_trx_log, parent, false);
                        holder.username = (TextView) convertView.findViewById(R.id.username);
                        holder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
                        holder.amount = (TextView) convertView.findViewById(R.id.amount);
                        holder.time = (TextView) convertView.findViewById(R.id.time);
                        holder.trxId = (TextView) convertView.findViewById(R.id.trxId);
                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        String amountStr = "Tk." + splitArray_row_second[1];
                        String trxIdStr = "Trx: " + splitArray_row_second[3];
                        holder.username.setText(mAppHandler.getRID().substring(mAppHandler.getRID().length() - 5, mAppHandler.getRID().length()));
                        holder.phnNo.setText(splitArray_row_second[0]);
                        holder.amount.setText(amountStr);
                        holder.time.setText(splitArray_row_second[2]);
                        holder.trxId.setText(trxIdStr);

                        break;
                }
            }
            return convertView;
        }

        public class ViewHolder {
            TextView textView, username, phnNo, amount, time, trxId;
        }
    }
}
