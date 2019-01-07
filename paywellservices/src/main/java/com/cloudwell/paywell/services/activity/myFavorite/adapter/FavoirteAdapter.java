package com.cloudwell.paywell.services.activity.myFavorite.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
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
import com.cloudwell.paywell.services.database.DatabaseClient;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 6/1/19.
 */
public class FavoirteAdapter extends RecyclerView.Adapter<FavoirteAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<FavoriteMenu> mItems = new ArrayList<>();

    private Context mContext;
    private final OnStartDragListener mDragStartListener;

    public FavoirteAdapter(Context context, List<FavoriteMenu> data, OnStartDragListener dragStartListener) {
        mContext = context;
        mDragStartListener = dragStartListener;
        this.mItems = data;
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
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

        FavoriteMenu fromMenu = mItems.get(fromPosition);
        fromMenu.setFavoriteListPosition(fromPosition);


        DatabaseClient.getInstance(mContext).getAppDatabase().mFavoriteMenuDab().update(fromMenu);


        FavoriteMenu toMenu = mItems.get(toPosition);
        toMenu.setFavoriteListPosition(toPosition);
        DatabaseClient.getInstance(mContext).getAppDatabase().mFavoriteMenuDab().update(toMenu);





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

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_content);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivIcon);
            ivDeleted = (ImageView) itemView.findViewById(R.id.ivDeleted);
            rootLinarLayout = (ConstraintLayout) itemView.findViewById(R.id.rootLinarLayout);
        }


        @Override
        public void onItemSelected() {
            // itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            // itemView.setBackgroundColor(0);
        }
    }
}

