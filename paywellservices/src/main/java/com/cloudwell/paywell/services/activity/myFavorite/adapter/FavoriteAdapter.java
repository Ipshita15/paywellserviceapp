package com.cloudwell.paywell.services.activity.myFavorite.adapter;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.ItemTouchHelperAdapter;
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.ItemTouchHelperViewHolder;
import com.cloudwell.paywell.services.activity.myFavorite.adapter.helper.OnStartDragListener;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventFavDeleted;
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventPositionMove;
import com.cloudwell.paywell.services.app.AppController;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/1/19.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {

    private List<FavoriteMenu> mItems;
    private boolean mIsEnglish;

    private final OnStartDragListener mDragStartListener;

    public FavoriteAdapter(List<FavoriteMenu> data, OnStartDragListener dragStartListener, boolean isEnglish) {
        mDragStartListener = dragStartListener;
        mItems = data;
        mIsEnglish = isEnglish;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_add, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        final FavoriteMenu favoriteMenu = mItems.get(position);
        holder.textView.setText(favoriteMenu.getName());
        holder.ivIcon.setBackgroundResource(favoriteMenu.getIcon());

        if (mIsEnglish) {
            holder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            holder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        // Start a drag whenever the handle view it touched
        holder.rootLinarLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }


        });

        holder.ivDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEventFavDeleted messageEvent = new MessageEventFavDeleted(favoriteMenu);
                EventBus.getDefault().post(messageEvent);


            }
        });


    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        try {
            Log.v("onItemMove ", "fromPosition: " + fromPosition);
            Log.v("onItemMove ", "toPosition: " + toPosition);

            Collections.swap(this.mItems, toPosition, fromPosition);
            notifyItemMoved(fromPosition, toPosition);

            FavoriteMenu fromMenu = this.mItems.get(fromPosition);
            fromMenu.setFavoriteListPosition(fromPosition);

            FavoriteMenu toMenu = this.mItems.get(toPosition);
            toMenu.setFavoriteListPosition(toPosition);

            MessageEventPositionMove messageEvent = new MessageEventPositionMove(fromMenu, toMenu);
            EventBus.getDefault().post(messageEvent);

        } catch (Exception e) {
            Log.v("Exception ", "Exception: " + e.getMessage());


        }

        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView ivIcon;
        public final ImageView ivDeleted;
        public final ConstraintLayout rootLinarLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_content);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivDeleted = itemView.findViewById(R.id.ivDeleted);
            rootLinarLayout = itemView.findViewById(R.id.rootLinarLayout);
        }


        @Override
        public void onItemSelected() {
//            rootLinarLayout.setBackgroundColor(0);
            ivDeleted.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onItemClear() {
//            rootLinarLayout.setBackgroundResource(R.drawable.item_favorite_background);
            ivDeleted.setVisibility(View.VISIBLE);



        }
    }
}

