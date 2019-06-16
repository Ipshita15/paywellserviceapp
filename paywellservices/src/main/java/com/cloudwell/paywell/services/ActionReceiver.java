package com.cloudwell.paywell.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-13.
 */
public class ActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("action");
        if (action.equals("Accept")) {
            performAction1(context);
        } else if (action.equals("Reject")) {
            performAction2(context);

        }
        //This is used to close the notification tray
        Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        context.sendBroadcast(it);
    }

    public void performAction1(Context context) {
        Intent intentActionAccept = new Intent(context, NotificationFullViewActivity.class);
        intentActionAccept.putExtra("action", "Accept");
        context.startActivity(intentActionAccept);
    }

    public void performAction2(Context context) {
        Intent intentActionAccept = new Intent(context, NotificationFullViewActivity.class);
        intentActionAccept.putExtra("action", "Reject");
        context.startActivity(intentActionAccept);

    }
}
