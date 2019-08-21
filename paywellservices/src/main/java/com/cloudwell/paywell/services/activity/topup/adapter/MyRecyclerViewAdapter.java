package com.cloudwell.paywell.services.activity.topup.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.model.MobileOperator;
import com.cloudwell.paywell.services.app.AppController;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 11/7/18.
 */
public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<MobileOperator> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean isSeleted = false;

    // data is passed into the constructor
    public MyRecyclerViewAdapter(Context context, ArrayList<MobileOperator> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvOperatorName.setText(mData.get(position).getName());
        if (!mData.get(position).isSeleted()) {
            holder.tvOperatorIcon.setBackgroundResource(mData.get(position).getIcon());
            isSeleted = false;
        } else {
            holder.tvOperatorIcon.setBackgroundResource(mData.get(position).getIsSeletedIcon());
            isSeleted = true;
        }

        holder.tvOperatorName.setTypeface(AppController.getInstance().getOxygenLightFont());

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOperatorName;
        ImageView tvOperatorIcon;

        ViewHolder(View itemView) {
            super(itemView);
            tvOperatorName = itemView.findViewById(R.id.tvOperatorName);
            tvOperatorIcon = itemView.findViewById(R.id.ivLogo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    MobileOperator getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }
}
