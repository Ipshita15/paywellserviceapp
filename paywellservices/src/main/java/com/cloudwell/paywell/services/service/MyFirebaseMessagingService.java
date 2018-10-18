package com.cloudwell.paywell.services.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    private Bitmap bitmap;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private static final String TAG_RESPONSE_STATUS = "status";
    private String token;
    private String title, message, trueOrFlase;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        if (remoteMessage.getData().size() > 0) {
            //title will contain the Push Message
            title = remoteMessage.getData().get("title");
            //message will contain the Push Message
            message = remoteMessage.getData().get("message");
            //imageUri will contain URL of the image to be displayed with Notification
            String imageUri = remoteMessage.getData().get("image");
            //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
            //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
            trueOrFlase = remoteMessage.getData().get("Notification");

            //To get a Bitmap image from the URL received
            bitmap = getBitmapfromUrl(imageUri);
        } else {
            //title will contain the Push Message
            title = remoteMessage.getNotification().getTitle();
            //message will contain the Push Message
            message = remoteMessage.getNotification().getBody();
            //imageUri will contain URL of the image to be displayed with Notification
//            String imageUri = remoteMessage.getNotification().
            //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
            //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.
            trueOrFlase = remoteMessage.getData().get("Notification");

            //To get a Bitmap image from the URL received
//            bitmap = getBitmapfromUrl(imageUri);
        }
        sendNotification(title, message, bitmap, trueOrFlase);
    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(String title, String messageBody, Bitmap image, String TrueOrFalse) {
        NotificationCompat.Builder notificationBuilder;

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Notification", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if(image != null) {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setLargeIcon(image)/*Notification icon image*/
                    .setSmallIcon(R.mipmap.paywell_icon)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(image))/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        } else {
            notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.paywell_icon)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.InboxStyle())/*Notification with Image*/
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /*
     * To get a Bitmap image from the URL received
     */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        mAppHandler = new AppHandler(this);
        mAppHandler.setFirebaseId(token);
        mAppHandler.setFirebaseTokenStatus("true");
    }
}
