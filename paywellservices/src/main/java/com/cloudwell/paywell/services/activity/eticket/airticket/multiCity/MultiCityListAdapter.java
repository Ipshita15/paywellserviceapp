package com.cloudwell.paywell.services.activity.eticket.airticket.multiCity;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MultiCityListAdapter extends RecyclerView.Adapter<MultiCityListAdapter.MultiCityViewHolder> {

    public MultiCityListAdapter() {
    }

    @NonNull
    @Override
    public MultiCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MultiCityViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MultiCityViewHolder extends RecyclerView.ViewHolder {
        public MultiCityViewHolder(View itemView) {
            super(itemView);
        }
    }
}
