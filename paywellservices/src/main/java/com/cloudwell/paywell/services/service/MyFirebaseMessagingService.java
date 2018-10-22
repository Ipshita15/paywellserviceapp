package com.cloudwell.paywell.services.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.notification.NotificationFullViewActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    private AppHandler mAppHandler;

    private String title;
    private String message;
    private String notifcationDetails;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String imageUri = "";
        Log.d(TAG, "From: " + remoteMessage.getFrom());


        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // parsing data from remote message
        title = remoteMessage.getData().get("title");
        message = remoteMessage.getData().get("message");
        imageUri = remoteMessage.getData().get("image");
        notifcationDetails = remoteMessage.getData().get("Notification_Details");

        //To get a Bitmap image from the URL received
        if (imageUri == null) {
            imageUri = "";
        }

        sendNotification(title, message, imageUri);


    }


    /**
     * Create and show a simple notification containing the received FCM message.
     */

    private void sendNotification(final String title, final String messageBody, String image) {

        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!image.equals("")) {
            Fresco.initialize(this);
            ImageRequest imageRequest = ImageRequest.fromUri(image);
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, null);
            dataSource.subscribe(
                    new BaseBitmapDataSubscriber() {

                        @Override
                        protected void onNewResultImpl(Bitmap bitmap) {
                            displayNotification(bitmap, title, messageBody, defaultSoundUri, notifcationDetails);
                        }

                        @Override
                        protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                            // notification. We proceed without the bitmap. when notification image not found in server
                            handleTextNotification(title, messageBody, defaultSoundUri, notifcationDetails);
                        }
                    },
                    UiThreadImmediateExecutorService.getInstance());


        } else {
            handleTextNotification(title, messageBody, defaultSoundUri, notifcationDetails);
        }


    }


    public static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(0);

        public static int getID() {
            return c.incrementAndGet();
        }
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

    private void displayNotification(@Nullable Bitmap bitmap, String title, String messageBody, Uri defaultSoundUri, String notificationDetails) {

        // TODO: 10/22/18

        int requestID = (int) System.currentTimeMillis();

        final Intent intent = new Intent(this, NotificationFullViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isNotificationFlow", true);
        intent.putExtra("Notification_Details", notificationDetails);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.paywell_icon);
        builder.setContentTitle(title);
        builder.setContentText(messageBody);
        builder.setLargeIcon(bitmap);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);


        Notification.BigPictureStyle bigPicutureStyle = new Notification.BigPictureStyle(builder);
        bigPicutureStyle.bigPicture(bitmap);
        bigPicutureStyle.setBigContentTitle(title);
        bigPicutureStyle.setSummaryText("Click on the image for full screen preview");
        builder.setSound(defaultSoundUri);
        builder.setContentIntent(pendingIntent);


        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(requestID, bigPicutureStyle.build());
    }

    private void handleTextNotification(String title, String messageBody, Uri defaultSoundUri, String notificationDetails) {
        int requestID = (int) System.currentTimeMillis();

        final Intent intent = new Intent(this, NotificationFullViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isNotificationFlow", true);
        intent.putExtra("Notification_Details", notificationDetails);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder notificationBuilder;
        notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.paywell_icon)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.InboxStyle())/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify(requestID, notificationBuilder.build());
    }
}
