package com.cloudwell.paywell.services.activity.notification.allNotificaiton.adapter;


import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewModel> {
    @NonNull
    @Override
    public NotificationAdapter.NotificationViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.NotificationViewModel holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    enum ButtonsState {
        GONE,
        RIGHT_VISIBLE
    }

    public class NotificationViewModel extends RecyclerView.ViewHolder {

        public NotificationViewModel(View itemView) {
            super(itemView);
        }
    }

    public class SwipeController extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return 0;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }
}
