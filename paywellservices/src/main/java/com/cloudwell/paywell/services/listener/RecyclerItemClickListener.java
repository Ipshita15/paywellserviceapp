package com.cloudwell.paywell.services.listener;

import android.view.View;

/**
 * Created by moktar on 10/22/2015.
 */
public class RecyclerItemClickListener implements View.OnClickListener {
    private int position;
    private OnItemClickListener onItemClickCallback;

    public RecyclerItemClickListener(int position, OnItemClickListener onItemClickCallback) {
        this.position = position;
        this.onItemClickCallback = onItemClickCallback;
    }
    @Override
    public void onClick(View view) {
        onItemClickCallback.onItemClicked(view, position);
    }

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }
}
