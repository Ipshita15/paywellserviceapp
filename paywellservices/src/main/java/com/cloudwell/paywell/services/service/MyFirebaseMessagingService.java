package com.cloudwell.paywell.services.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
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

import org.apache.commons.lang3.StringEscapeUtils;

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

        String m = StringEscapeUtils.unescapeJava(message);

        sendNotification(title, m, imageUri);


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


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        mAppHandler = new AppHandler(this);
        mAppHandler.setFirebaseId(token);
        mAppHandler.setFirebaseTokenStatus("true");
    }

    private void displayNotification(@Nullable Bitmap bitmap, String title, String messageBody, Uri defaultSoundUri, String notificationDetails) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "PayWell-01";
        String channelName = "PayWell";

        int requestID = (int) System.currentTimeMillis();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        final Intent intent = new Intent(this, NotificationFullViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isNotificationFlow", true);
        intent.putExtra("Notification_Details", notificationDetails);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(intent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                requestID,
                PendingIntent.FLAG_UPDATE_CURRENT
        );


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId);
        builder.setSmallIcon(R.drawable.pw_notification_bar);
        builder.setContentTitle(title);
        builder.setContentText(messageBody);
        builder.setLargeIcon(bitmap);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);


        NotificationCompat.BigPictureStyle bigPicutureStyle = new NotificationCompat.BigPictureStyle(builder);
        bigPicutureStyle.bigPicture(bitmap);
        bigPicutureStyle.setBigContentTitle(title);
        bigPicutureStyle.setSummaryText(messageBody);
        builder.setSound(defaultSoundUri);
        builder.setContentIntent(resultPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(requestID, bigPicutureStyle.build());
        }

    }

    private void handleTextNotification(String title, String messageBody, Uri defaultSoundUri, String notificationDetails) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "PayWell-01";
        String channelName = "PayWell";

        int requestID = (int) System.currentTimeMillis();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }

        final Intent intent = new Intent(this, NotificationFullViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("isNotificationFlow", true);
        intent.putExtra("Notification_Details", notificationDetails);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(title);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.pw_notification_bar)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.InboxStyle())/*Notification with Image*/
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(bigText)
                .setPriority(Notification.PRIORITY_MAX);

        if (notificationManager != null) {
            notificationManager.notify(requestID, builder.build());
        }
    }
}
