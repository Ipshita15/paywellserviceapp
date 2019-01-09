package com.cloudwell.paywell.services.activity.myFavorite.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEvent;
import com.cloudwell.paywell.services.app.AppController;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private Context mContext;
    private int mIndex;
    private String title;
    private List<FavoriteMenu> list;
    private boolean mIsEnglish;

    public HeaderRecyclerViewSection(Context context, int index, String title, List<FavoriteMenu> list, boolean isEnglish) {
        super(R.layout.header_favourite, R.layout.item_unfavorite);
        mContext = context;
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
        final FavoriteMenu favoriteMenu = list.get(position);
        iHolder.itemContent.setText(favoriteMenu.getName());
        iHolder.ivIcon.setBackgroundResource(favoriteMenu.getIcon());

        iHolder.rootLinarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEvent messageEvent = new MessageEvent(mIndex, position, title, favoriteMenu);
                EventBus.getDefault().post(messageEvent);

            }
        });

        iHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEvent messageEvent = new MessageEvent(mIndex, position, title, favoriteMenu);
                EventBus.getDefault().post(messageEvent);

            }
        });

        iHolder.ivAdded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEvent messageEvent = new MessageEvent(mIndex, position, title, favoriteMenu);
                EventBus.getDefault().post(messageEvent);


            }
        });

        if (mIsEnglish) {
            iHolder.itemContent.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            iHolder.itemContent.setTypeface(AppController.getInstance().getAponaLohitFont());
        }



    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);

        if (mIsEnglish) {
            hHolder.headerTitle.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            hHolder.headerTitle.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
    }


}