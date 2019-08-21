package com.cloudwell.paywell.services.activity.myFavorite.adapter;

import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class HeaderViewHolder extends RecyclerView.ViewHolder {
    TextView headerTitle;

    HeaderViewHolder(View itemView) {
        super(itemView);
        headerTitle = (TextView) itemView.findViewById(R.id.header_id);
    }
}
