package com.cloudwell.paywell.services.activity.myFavorite.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;

import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2/1/19.
 */
public class HeaderRecyclerViewSection extends StatelessSection {
    private static final String TAG = HeaderRecyclerViewSection.class.getSimpleName();
    private String title;
    private List<FavoriteMenu> list;

    public HeaderRecyclerViewSection(String title, List<FavoriteMenu> list) {
        super(R.layout.header_favourite, R.layout.item_unfavorite);
        this.title = title;
        this.list = list;
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
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder iHolder = (ItemViewHolder) holder;
        FavoriteMenu favoriteMenu = list.get(position);
        iHolder.itemContent.setText(favoriteMenu.getName());
        iHolder.ivIcon.setBackgroundResource(favoriteMenu.getIcon());


    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder hHolder = (HeaderViewHolder) holder;
        hHolder.headerTitle.setText(title);
    }


}