package com.cloudwell.paywell.services.activity.topup;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class TransLogActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private ListView listView;
    public static String[] mPhn = null;
    public static String[] mAmount = null;
    public static String[] mDate = null;
    public static String[] mStatus = null;
    public static String[] mTrx = null;
    public static String length;
    public static String TRANSLOG_TAG = "TRANSLOGTXT";
    private String date = "";
    private CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_log);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_topup_trx_log);
        }

        relativeLayout = findViewById(R.id.relativeLayout);
        adapter = new CustomAdapter(this);

        String response = TRANSLOG_TAG;
        if (response != null && response.length() > 0) {

            mPhn = new String[response.length()];
            mAmount = new String[response.length()];
            mDate = new String[response.length()];
            mStatus = new String[response.length()];
            mTrx = new String[response.length()];

            if (response.contains("@@@@")) {
                response = response.split("@@@@")[0];
                String[] splitTripleAt = response.split("@@@");
                int i = 0;
                for (String perTrans : splitTripleAt) {
                    String[] splittedSingleAt = perTrans.split("@");
                    String result;
                    try {
                        if (perTrans.startsWith("200")) {
                            mPhn[i] = splittedSingleAt[2];
                            mAmount[i] = splittedSingleAt[4];
                            mDate[i] = splittedSingleAt[5];
                            mStatus[i] = splittedSingleAt[6];
                            mTrx[i] = "1";
                        } else {
                            mPhn[i] = splittedSingleAt[1];
                            mAmount[i] = splittedSingleAt[3];
                            mDate[i] = splittedSingleAt[4];
                            mStatus[i] = splittedSingleAt[5];
                            mTrx[i] = "2";
                        }

                        String sub_date_comp = mDate[i].substring(0, 10);
                        if (!date.equals(sub_date_comp)) {
                            date = sub_date_comp;
                            adapter.addSectionHeaderItem(date);
                        }
                        String time = mDate[i].substring(11);
                        result = mPhn[i] + "@" + mAmount[i] + "@" + time + "@" + mStatus[i];
                        adapter.addItem(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                    i++;
                }
                length = String.valueOf(i);
                listView = findViewById(R.id.listView);
                listView.setAdapter(adapter);
            }
        }

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_ENQUIRY_TRX_PAGE);
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
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(mData.get(position));
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_topup_trx_log, parent, false);
                        holder.phnNo = convertView.findViewById(R.id.phnNo);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.date = convertView.findViewById(R.id.date);
                        holder.status = convertView.findViewById(R.id.status);
                        convertView.setTag(holder);

                        splitArray_row_first = mData.get(position).split("@");

                        String amountStr = "Tk." + splitArray_row_first[1];
                        holder.phnNo.setText(splitArray_row_first[0]);
                        holder.amount.setText(amountStr);
                        holder.date.setText(splitArray_row_first[2]);
                        holder.status.setText(splitArray_row_first[3]);

                        if (splitArray_row_first[3].equalsIgnoreCase("Successful")) {
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
                        convertView = mInflater.inflate(R.layout.dialog_topup_trx_log, parent, false);
                        holder.phnNo = convertView.findViewById(R.id.phnNo);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.date = convertView.findViewById(R.id.date);
                        holder.status = convertView.findViewById(R.id.status);
                        convertView.setTag(holder);

                        splitArray_row_second = mData.get(position).split("@");

                        String amountStr = "Tk." + splitArray_row_second[1];

                        holder.phnNo.setText(splitArray_row_second[0]);
                        holder.amount.setText(amountStr);
                        holder.date.setText(splitArray_row_second[2]);
                        holder.status.setText(splitArray_row_second[3]);

                        if (splitArray_row_second[3].equalsIgnoreCase("Successful")) {
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
            TextView textView, phnNo, amount, date, status;
        }
    }

}
