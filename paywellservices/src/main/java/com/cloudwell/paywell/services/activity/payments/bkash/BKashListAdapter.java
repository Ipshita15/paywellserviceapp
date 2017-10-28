package com.cloudwell.paywell.services.activity.payments.bkash;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;

/**
 * Created by Naima Gani on 12/26/2016.
 */

public class BKashListAdapter extends BaseAdapter {

    private AppHandler mAppHandler;
    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;

    public BKashListAdapter(Context context) {
        mAppHandler = new AppHandler(context);
        mContext = context;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() { return Integer.parseInt(TransactionStatusActivity.length); }

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list_rocket, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.trx = (TextView) convertView.findViewById(R.id.trx);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.phnNo.setText(TransactionStatusActivity.mPhn[position]);
        viewHolder.amount.setText(TransactionStatusActivity.mAmount[position]);
        viewHolder.date.setText(TransactionStatusActivity.mDate[position]);
        viewHolder.trx.setText(TransactionStatusActivity.mTrx[position]);

        return convertView;
    }


    private static class ViewHolder {
        TextView phnNo;
        TextView amount;
        TextView date;
        TextView trx;

    }
}