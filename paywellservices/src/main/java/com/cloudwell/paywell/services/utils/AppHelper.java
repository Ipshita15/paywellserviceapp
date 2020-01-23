package com.cloudwell.paywell.services.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Base64;

import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.service.notificaiton.NotificationCheckerService;
import com.cloudwell.paywell.services.service.notificaiton.NotificationDataSycService;
import com.cloudwell.paywell.services.service.notificaiton.model.StartNotificationService;
import com.orhanobut.logger.Logger;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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


    public static String getAndroidID(ContentResolver contentResolver) {
        return Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID);
    }

    public static void startNotificationSyncService(Context context) {
        context.startService(new Intent(context, NotificationDataSycService.class));
    }

    public static ArrayList<String> getRSAKays() {
        ArrayList<String> data = new ArrayList<String>();
        KeyPairGenerator kpg = null;
        KeyPair kp = null;
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.generateKeyPair();


            // private
            String headlinePrivate = "-----BEGIN PRIVATE KEY-----\n";
            String footlinePrivate = "-----END PRIVATE KEY-----\n";

            String privateKey = Base64.encodeToString(kp.getPrivate().getEncoded(), Base64.DEFAULT);
//            privateKey = headlinePrivate+privateKey+footlinePrivate;
//            privateKey = Base64.encodeToString(privateKey.getBytes(), Base64.DEFAULT);;
            data.add(privateKey);


            // public
            String headlinePublic = "-----BEGIN PUBLIC KEY-----\n";
            String footlinePublic = "-----END PUBLIC KEY-----\n";

            String  publicKey = Base64.encodeToString(kp.getPublic().getEncoded(), Base64.DEFAULT);
            publicKey = headlinePublic+publicKey+footlinePublic;
            publicKey = Base64.encodeToString(publicKey.getBytes(), Base64.DEFAULT);
            data.add(publicKey);



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Logger.e(e.getMessage());
        }
        return data;

    }
}
