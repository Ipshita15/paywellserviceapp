package com.cloudwell.paywell.services.activity.myFavorite.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
class ItemViewHolder extends RecyclerView.ViewHolder {
    TextView itemContent;
    ImageView ivIcon;
    ImageView ivAdded;
    ConstraintLayout rootLinarLayout;


    ItemViewHolder(View itemView) {
        super(itemView);
        itemContent = itemView.findViewById(R.id.item_content);
        ivIcon = itemView.findViewById(R.id.ivIcon);
        ivAdded = itemView.findViewById(R.id.ivAdded);
        rootLinarLayout = itemView.findViewById(R.id.rootLinarLayout);
    }
}