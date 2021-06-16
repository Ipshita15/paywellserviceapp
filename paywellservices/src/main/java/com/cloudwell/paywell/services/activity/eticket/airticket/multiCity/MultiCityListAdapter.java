package com.cloudwell.paywell.services.activity.eticket.airticket.multiCity;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
