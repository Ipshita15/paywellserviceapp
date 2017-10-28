package com.cloudwell.paywell.services.activity.payments.rocket;

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
 * Created by Naima Gani on 12/22/2016.
 */

public class InquiryStatementTextListAdapter extends BaseAdapter {

    private AppHandler mAppHandler;
    private final Context mContext;
    private final SparseBooleanArray mCollapsedStatus;

    public InquiryStatementTextListAdapter(Context context) {
        mAppHandler = new AppHandler(context);
        mContext = context;
        mCollapsedStatus = new SparseBooleanArray();
    }

    @Override
    public int getCount() { return RocketInquiryActivity.mPhn.length; }

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
        final InquiryStatementTextListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_list_rocket, parent, false);
            viewHolder = new InquiryStatementTextListAdapter.ViewHolder();
            viewHolder.phnNo = (TextView) convertView.findViewById(R.id.phnNo);
            viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.status = (TextView) convertView.findViewById(R.id.status);
            viewHolder.trx = (TextView) convertView.findViewById(R.id.trx);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (InquiryStatementTextListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.phnNo.setText(RocketInquiryActivity.mPhn[position]);
        viewHolder.amount.setText(RocketInquiryActivity.mAmount[position]);
        viewHolder.date.setText(RocketInquiryActivity.mDate[position]);
        viewHolder.status.setText(RocketInquiryActivity.mStatus[position]);
        viewHolder.trx.setText(RocketInquiryActivity.mTrx[position]);

        return convertView;
    }


    private static class ViewHolder {
        TextView phnNo;
        TextView amount;
        TextView date;
        TextView status;
        TextView trx;

    }
}