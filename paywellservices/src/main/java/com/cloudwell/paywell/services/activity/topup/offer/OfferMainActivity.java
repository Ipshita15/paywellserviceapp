package com.cloudwell.paywell.services.activity.topup.offer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Vector;


public class OfferMainActivity extends AppCompatActivity {

    private RelativeLayout mRelativeLayout;
    public static String length;
    private ListView listView;
    private TrxAdapter mAdapter;
    private String array;
    public static String[] mAmount = null;
    public static String[] mRetCom = null;
    public static String[] mDetails = null;
    public static String[] mType = null;
    public static String operatorName;
    private String type = "";
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(operatorName);
        }
        mRelativeLayout = findViewById(R.id.relativeLayoutOfferMain);
        listView = findViewById(R.id.trxListView);
        mAdapter = new TrxAdapter(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            array = bundle.getString("array");
        }

        try {
            key = getIntent().getStringExtra("key");
            Log.d("Key", key);
        } catch (Exception e) {

        }

        initializeAdapter();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_TOFU_ALL_OPERATOR_BUNDLE_OFFER_CONFIRM);
    }

    public void initializeAdapter() {
        try {
            JSONArray jsonArray = new JSONArray(array);
            mAmount = new String[jsonArray.length()];
            mRetCom = new String[jsonArray.length()];
            mDetails = new String[jsonArray.length()];
            mType = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                mAmount[i] = object.getString("amount");
                mRetCom[i] = object.getString("retailer_comm");
                mDetails[i] = object.getString("remarks");
                mType[i] = object.getString("type");

                if (!type.equals(mType[i])) {
                    type = mType[i];
                    mAdapter.addSectionHeaderItem(mType[i]);
                    mAdapter.notifyDataSetChanged();

                    mAdapter.addItem(mDetails[i], mAmount[i], mRetCom[i]);
                    mAdapter.notifyDataSetChanged();

                } else {
                    mAdapter.addItem(mDetails[i], mAmount[i], mRetCom[i]);
                    mAdapter.notifyDataSetChanged();
                }
            }
            listView.setAdapter(mAdapter);
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
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(OfferMainActivity.this, OperatorMenuActivity.class);
//        startActivity(new Intent(OfferMainActivity.this, TopupMainActivity.class));
        finish();
    }

    private class TrxAdapter extends BaseAdapter {
        int selectedPosition = 0;
        RadioButton selected = null;

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;

        private LayoutInflater mInflater;
        private ArrayList<String> array = new ArrayList<>();
        private ArrayList<String> mData = new ArrayList<>();

        String splitArray_row_first[];
        String splitArray_row_second[];

        private TrxAdapter(Context context) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void addItem(final String details, final String amount, final String com) {
            mData.add(details + "," + amount + "," + com);
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
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        ViewHolder holder = null;

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            int rowType = getItemViewType(position);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_offer_header, parent, false);
                        holder.textViewHeader = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);

                        holder.textViewHeader.clearComposingText();
                        if (mData.get(position).equals("1")) {
                            holder.textViewHeader.setText(getString(R.string.offer_combo_msg));
                        } else if (mData.get(position).equals("2")) {
                            holder.textViewHeader.setText(getString(R.string.offer_talk_time_msg));
                        } else if (mData.get(position).equals("3")) {
                            holder.textViewHeader.setText(getString(R.string.offer_internet_msg));
                        } else if (mData.get(position).equals("4")) {
                            holder.textViewHeader.setText(getString(R.string.offer_other_msg));
                        }
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_offer_gp, parent, false);

                        holder.textViewDescription = convertView.findViewById(R.id.description);
                        holder.radioButton = convertView.findViewById(R.id.radioButton);

                        splitArray_row_first = mData.get(position).split(",");

                        holder.textViewDescription.setText(splitArray_row_first[0] + " (Tk." + splitArray_row_first[1] + ")");

                        holder.radioButton.setTag(position);
                        holder.radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (selected != null) {
                                    selected.setChecked(false);
                                }
                                selected = holder.radioButton;
                                selectedPosition = (Integer) view.getTag();

                                String[] splitArray_trx = mData.get(position).split(",");
                                ConfirmOfferActivity.amount = splitArray_trx[1];
                                ConfirmOfferActivity.retCom = splitArray_trx[2];
                                ConfirmOfferActivity.details = splitArray_trx[0];
                                ConfirmOfferActivity.operatorName = operatorName;
                                startActivity(new Intent(OfferMainActivity.this, ConfirmOfferActivity.class));
                            }
                        });
                        convertView.setTag(holder);
                        break;
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.dialog_offer_header, parent, false);
                        holder.textViewHeader = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);

                        holder.textViewHeader.clearComposingText();
                        if (mData.get(position).equals("1")) {
                            holder.textViewHeader.setText(getString(R.string.offer_combo_msg));
                        } else if (mData.get(position).equals("2")) {
                            holder.textViewHeader.setText(getString(R.string.offer_talk_time_msg));
                        } else if (mData.get(position).equals("3")) {
                            holder.textViewHeader.setText(getString(R.string.offer_internet_msg));
                        } else if (mData.get(position).equals("4")) {
                            holder.textViewHeader.setText(getString(R.string.offer_other_msg));
                        }
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_offer_gp, parent, false);

                        holder.textViewDescription = convertView.findViewById(R.id.description);
                        holder.radioButton = convertView.findViewById(R.id.radioButton);

                        splitArray_row_second = mData.get(position).split(",");

                        holder.textViewDescription.setText(splitArray_row_second[0] + " (Tk." + splitArray_row_second[1] + ")");

                        holder.radioButton.setTag(position);
                        holder.radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (selected != null) {
                                    selected.setChecked(false);
                                }

                                selected = holder.radioButton;
                                selectedPosition = (Integer) view.getTag();

                                String[] splitArray_trx = mData.get(position).split(",");
                                ConfirmOfferActivity.amount = splitArray_trx[1];
                                ConfirmOfferActivity.retCom = splitArray_trx[2];
                                ConfirmOfferActivity.details = splitArray_trx[0];
                                ConfirmOfferActivity.operatorName = operatorName;


                                Intent intent = new Intent(OfferMainActivity.this, ConfirmOfferActivity.class);
                                intent.putExtra("key", key);
                                startActivity(intent);
                            }
                        });
                        convertView.setTag(holder);
                        break;
                }
            }
            return convertView;
        }

        private class ViewHolder {
            TextView textViewHeader, textViewDescription;
            RadioButton radioButton;

            Vector va = new Vector();


        }
    }
}
