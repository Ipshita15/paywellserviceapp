package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.MessageEventMobileNumberChange;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.ResMobileChangeLog;
import com.cloudwell.paywell.services.app.AppController;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
public class HeaderRVSectionForLog extends StatelessSection {
    private static final String TAG = HeaderRVSectionForLog.class.getSimpleName();
    private int mIndex;
    private String title;
    private List<ResMobileChangeLog> list;
    private boolean mIsEnglish;

    public HeaderRVSectionForLog(int index, String title, List<ResMobileChangeLog> list, boolean isEnglish) {
        super(R.layout.dialog_both_header, R.layout.dialog_polli_reg_inq);
        mIndex = index;
        this.title = title;
        this.list = list;
        mIsEnglish = isEnglish;
    }


    @Override
    public int getContentItemsTotal() {
        return list.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ItemViewHolder iHolder = (ItemViewHolder) holder;
        final ResMobileChangeLog object = list.get(position);

        if (mIsEnglish) {
            iHolder.accNo.setTypeface(AppController.getInstance().getOxygenLightFont());
            iHolder.status.setTypeface(AppController.getInstance().getOxygenLightFont());
            iHolder.tvSMS.setTypeface(AppController.getInstance().getOxygenLightFont());
            iHolder.tvMobile.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            iHolder.accNo.setTypeface(AppController.getInstance().getAponaLohitFont());
            iHolder.status.setTypeface(AppController.getInstance().getAponaLohitFont());
            iHolder.tvSMS.setTypeface(AppController.getInstance().getAponaLohitFont());
            iHolder.tvMobile.setTypeface(AppController.getInstance().getAponaLohitFont());

        }


        iHolder.status.setText(object.getStatusName());
        iHolder.accNo.setText(object.getCustomerAccNo());
        iHolder.trxId.setText(String.format("%s %s", AppController.getContext().getString(R.string.trx_id_des), object.getTrxId()));
        iHolder.tvMobile.setText(String.format("%s %s", AppController.getContext().getString(R.string.phone_no_des), object.getCustomerPhn()));
        iHolder.tvSMS.setText(String.format("%s %s", AppController.getContext().getString(R.string.sms), object.getResponseDetails()));

        if (object.getResponseDetails() != null) {
            iHolder.tvSMS.setText(object.getResponseDetails());
            iHolder.tvSMS.setVisibility(View.VISIBLE);
        } else {
            iHolder.tvSMS.setVisibility(View.GONE);
        }

        if (object.getStatusCode().equalsIgnoreCase("200")) {
            iHolder.status.setTextColor(Color.parseColor("#008000"));
        } else if (object.getStatusCode().equalsIgnoreCase("100")) {
            iHolder.status.setTextColor(Color.parseColor("#0099cc"));
        } else {
            iHolder.status.setTextColor(Color.parseColor("#ff0000"));
        }

        iHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEventMobileNumberChange messageEvent = new MessageEventMobileNumberChange(position, object);
                EventBus.getDefault().post(messageEvent);

            }
        });


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.tvHeader.setText(title);
        if (mIsEnglish) {
            hHolder.tvHeader.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            hHolder.tvHeader.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView accNo, status, trxId, tvMobile, tvSMS;
        LinearLayout mLinearLayout;

        public ItemViewHolder(View view) {
            super(view);
            accNo = view.findViewById(R.id.accNo);
            status = view.findViewById(R.id.status);
            trxId = view.findViewById(R.id.trxId);
            tvMobile = view.findViewById(R.id.tvMobileNumber);
            tvSMS = view.findViewById(R.id.tvSMS);
            mLinearLayout = view.findViewById(R.id.ivRootLayout);

        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;

        public HeaderViewHolder(View view) {
            super(view);
            tvHeader = view.findViewById(R.id.header);
        }
    }
}

