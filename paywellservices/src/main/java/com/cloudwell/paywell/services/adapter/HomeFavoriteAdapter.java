package com.cloudwell.paywell.services.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.ItemTouchHelperViewHolder;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.app.AppController;

import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 8/1/19.
 */
public class HomeFavoriteAdapter extends RecyclerView.Adapter<HomeFavoriteAdapter.ItemViewHolder> {

    private List<FavoriteMenu> mData;
    private boolean mIsEnglish;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private boolean isSeleted = false;

    // data is passed into the constructor
    public HomeFavoriteAdapter(Context context, List<FavoriteMenu> data, boolean isEnglish) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mIsEnglish = isEnglish;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_favorite_item_home_page, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final FavoriteMenu favoriteMenu = mData.get(position);
        holder.textView.setText(favoriteMenu.getName());
        holder.ivIcon.setBackgroundResource(favoriteMenu.getIcon());

        holder.rootLinarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteMenu favoriteMenu1 = mData.get(position);
                if (mClickListener != null) {
                    mClickListener.onItemClick(position, favoriteMenu1);
                }
            }
        });

        if (mIsEnglish) {
            holder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            holder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

    }


    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // allows clicks events to be caught
    public void setClickListener(HomeFavoriteAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position

    public boolean isSeleted() {
        return isSeleted;
    }

    public void setSeleted(boolean seleted) {
        isSeleted = seleted;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {

        void onItemClick(int position, FavoriteMenu favoriteMenu1);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView ivIcon;
        public final ImageView ivDeleted;
        public final ConstraintLayout rootLinarLayout;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_content);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivDeleted = itemView.findViewById(R.id.ivDeleted);
            rootLinarLayout = itemView.findViewById(R.id.rootLinarLayout);
        }


        @Override
        public void onItemSelected(RecyclerView.ViewHolder viewHolder) {
            // itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear(RecyclerView.ViewHolder viewHolder) {
            // itemView.setBackgroundColor(0);
        }
    }

}