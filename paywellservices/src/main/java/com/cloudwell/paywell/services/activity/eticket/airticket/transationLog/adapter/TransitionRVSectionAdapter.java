package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum;
import com.cloudwell.paywell.services.app.AppController;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
public class TransitionRVSectionAdapter extends StatelessSection {
    private static final String TAG = TransitionRVSectionAdapter.class.getSimpleName();
    private String title;
    private List<Datum> list;
    private boolean mIsEnglish;

    public TransitionRVSectionAdapter(String title, List<Datum> list, boolean isEnglish) {
        super(R.layout.item_header_air_tricket_transtion_log, R.layout.item_child_airtricket_transtion_log);
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
        final Datum object = list.get(position);

        if (mIsEnglish) {
            iHolder.tvBookingId.setTypeface(AppController.getInstance().getOxygenLightFont());
            iHolder.tvTricketPrices.setTypeface(AppController.getInstance().getOxygenLightFont());
            iHolder.tvStatus.setTypeface(AppController.getInstance().getOxygenLightFont());

        } else {
            iHolder.tvBookingId.setTypeface(AppController.getInstance().getAponaLohitFont());
            iHolder.tvTricketPrices.setTypeface(AppController.getInstance().getAponaLohitFont());
            iHolder.tvStatus.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        iHolder.tvBookingId.setText("Booking ID: " + object.getBookingId());
        iHolder.tvTricketPrices.setText("Price: " + object.getCurrency() + " " + object.getTotalFare());
        iHolder.tvStatus.setText("Status: " + object.getMessage());


        iHolder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        TextView tvBookingId, tvTricketPrices, tvStatus;
        LinearLayout mLinearLayout;

        public ItemViewHolder(View view) {
            super(view);
            tvBookingId = view.findViewById(R.id.tvBookingId);
            tvTricketPrices = view.findViewById(R.id.tvTricketPrices);
            tvStatus = view.findViewById(R.id.tvStatus);
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

