package com.cloudwell.paywell.services.activity.myFavorite.adapter;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventFavDeleted;
import com.cloudwell.paywell.services.activity.myFavorite.model.MessageEventPositionMove;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.utils.ResorceHelper;



import java.util.Collections;
import java.util.List;

public class AdapterForFavList extends RecyclerView.Adapter<AdapterForFavList.MyViewHolder> implements ItemMoveCallback.ItemTouchHelperContract {

    public static List<FavoriteMenu> mItems;
    private final StartDragListener mStartDragListener;
    private boolean mIsEnglish;

    public AdapterForFavList(StartDragListener startDragListener, boolean mIsEnglish) {
        mStartDragListener = startDragListener;
        this.mIsEnglish = mIsEnglish;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_add, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final FavoriteMenu favoriteMenu = mItems.get(position);
        holder.textView.setText(ResorceHelper.getResId(favoriteMenu.getName(), R.string.class));

        holder.ivIcon.setBackgroundResource(ResorceHelper.getResId(favoriteMenu.getIcon(), R.drawable.class));

        if (mIsEnglish) {
            holder.textView.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            holder.textView.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        holder.rootLinarLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });

        holder.ivDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MessageEventFavDeleted messageEvent = new MessageEventFavDeleted(favoriteMenu);
                GlobalApplicationBus.getBus().post(messageEvent);

            }
        });

        holder.ivDeleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageEventFavDeleted messageEvent = new MessageEventFavDeleted(favoriteMenu);
                GlobalApplicationBus.getBus().post(messageEvent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mItems, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mItems, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);


        // change the position status
        FavoriteMenu fromMenu = mItems.get(fromPosition);
        fromMenu.setFavoriteListPosition(fromPosition);

        FavoriteMenu toMenu = mItems.get(toPosition);
        toMenu.setFavoriteListPosition(toPosition);

        MessageEventPositionMove messageEvent = new MessageEventPositionMove(fromMenu, toMenu);
        GlobalApplicationBus.getBus().post(messageEvent);

    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {

        myViewHolder.ivDeleted.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.ivDeleted.setVisibility(View.VISIBLE);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivIcon;
        public ImageView ivDeleted;
        public ConstraintLayout rootLinarLayout;
        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.item_content);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            ivDeleted = itemView.findViewById(R.id.ivDeleted);
            rootLinarLayout = itemView.findViewById(R.id.rootLinarLayout);
        }
    }
}

