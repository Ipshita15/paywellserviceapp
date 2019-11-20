package com.cloudwell.paywell.services;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBBillPayOldActivity;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-13.
 */
public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        String REBNotification = intent.getStringExtra("REBNotification");
        int requestID = intent.getIntExtra("requestID", 0);
        if (action.equals("Re payment")) {
            performAction1(context, REBNotification);
        } else if (action.equals("Reject")) {


        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(requestID);

        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void performAction1(Context context, String REBNotification) {
        Intent intentActionAccept = new Intent(context, PBBillPayOldActivity.class);
        intentActionAccept.putExtra("REBNotification", REBNotification);
        context.startActivity(intentActionAccept);
    }

    public void performAction2(Context context) {
        Intent intentActionAccept = new Intent(context, NotificationFullViewActivity.class);
        intentActionAccept.putExtra("action", "Reject");
        context.startActivity(intentActionAccept);

    }
}
