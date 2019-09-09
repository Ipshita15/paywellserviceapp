package com.cloudwell.paywell.services.utils;

import android.content.Context;
import android.content.Intent;

import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.service.notificaiton.NotificationCheckerService;
import com.cloudwell.paywell.services.service.notificaiton.NotificationDataSycService;
import com.cloudwell.paywell.services.service.notificaiton.model.StartNotificationService;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 12/2/19.
 */
public class AppHelper {
    public static void notificationCounterCheck(ConnectionDetector mCd, Context context) {
        try {
            if (mCd.isConnectingToInternet()) {
                boolean myServiceRunning = AndroidServiceUtilis.isMyServiceRunning(context, NotificationCheckerService.class);
                if (!myServiceRunning) {
                    Intent intent = new Intent(context, NotificationCheckerService.class);
                    context.startService(intent);
                } else {
                    boolean apiCalledRuing = NotificationCheckerService.Companion.isAPICalledRunning();
                    if (!apiCalledRuing) {
                        GlobalApplicationBus.getBus().post(new StartNotificationService(1));
                    }
                }
            }
        } catch (Exception e) {

        }


    }

    public static void startNotificationSyncService(Context context) {
        context.startService(new Intent(context, NotificationDataSycService.class));
    }
}
