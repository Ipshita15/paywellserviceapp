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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.cloudwell.paywell.services.ActionReceiver;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.notification.model.NotificationDetailMessage;
import com.cloudwell.paywell.services.activity.notification.notificaitonFullView.NotificationFullViewActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.model.REBNotification;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.AppHelper;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private static final String TAG_RESPONSE_STATUS = "status";
    private String token;
    private AsyncTask<String, Intent, String> mPushFirebaseIdTask;



    private static final String TAG = "FirebaseMessageService";
    private String title;
    private String message;
    private String notifcationDetails;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        mAppHandler.setFirebaseId(token);
        mAppHandler.setFirebaseTokenStatus("true");

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String token) {
        this.token = token;
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        if (mCd.isConnectingToInternet()) {

            mPushFirebaseIdTask = new PushFirebaseIdTask().execute(getString(R.string.notification_token_url), token);
        } else {
            Toast.makeText(getApplicationContext(), R.string.connection_error_msg, Toast.LENGTH_LONG).show();
        }
    }

    private class PushFirebaseIdTask extends AsyncTask<String, Intent, String> {

        @Override
        protected String doInBackground(String... params) {
            String notifications = null;
            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("usertype", "Retailer"));
                nameValuePairs.add(new BasicNameValuePair("token", params[1]));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                notifications = httpClients.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_LONG).show();
            }
            return notifications;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {
                        mAppHandler.setFirebaseId(token);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.try_again_msg, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mPushFirebaseIdTask != null) {
            mPushFirebaseIdTask.cancel(true);
        }

        super.onDestroy();
    }



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

//        String testmessage = "{\"notification_action_type\":\"AirTicketReScheduleConfirmation\",\"id\":14,\"original_message\":\"Dear retailer, Please accept the message. if you accept the message, your request will be processed. otherwise request will be declined soon.\"}";
        String testmessage = message;


//

        if (testmessage.contains("notification_action_type")) {
            // air ticket flow
            try {

                testmessage = StringEscapeUtils.unescapeJava(testmessage);

                testmessage = testmessage.replace("\\", "");
                testmessage = testmessage.replaceAll("\\\\", "");
                testmessage = testmessage.replaceAll("\\\\\\\\", "");
                testmessage = testmessage.replaceAll("\\\\\\\\\\\\", "");

                JSONObject jsonObject = new JSONObject(testmessage);
                String notification_action_type = jsonObject.getString("notification_action_type");
                String original_message = jsonObject.getString("original_message");

                final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                notifcationDetails = remoteMessage.getData().get("Notification_Details");

                if (notification_action_type.equals("AirTicketReScheduleConfirmation")) {
                    handleAirTicketNotification(title, original_message, defaultSoundUri, notifcationDetails, true);
                } else {
                    handleAirTicketNotification(title, original_message, defaultSoundUri, notifcationDetails, false);
                }

            } catch (JSONException e) {
                Logger.e("" + e.getLocalizedMessage());
            }
        } else {

            try {
                // REB
                notifcationDetails = remoteMessage.getData().get("Notification_Details");

                Gson gson = new Gson();
                NotificationDetailMessage notificationDetailMessage = gson.fromJson(notifcationDetails, NotificationDetailMessage.class);

                if (notificationDetailMessage.getBalanceReturnData() != null) {
                    if (!notificationDetailMessage.getBalanceReturnData().equals("")) {
                        String message = StringEscapeUtils.unescapeJava(notificationDetailMessage.getBalanceReturnData());

                        message = message.replace("\\", "");
                        message = message.replace("\\\\", "");
                        message = message.replace("\\\\\\\\", "");
                        message = message.replace("\\\\\\\\\\\\", "");

                        REBNotification REBNotification = gson.fromJson(message, REBNotification.class);
                        checkREBType(REBNotification, notifcationDetails, remoteMessage);

                    } else {
                        handleNormalNotification(remoteMessage);

                    }
                } else {
                    // normal
                    handleNormalNotification(remoteMessage);
                }
            } catch (Exception e) {
                // normal
                handleNormalNotification(remoteMessage);
            }
        }

    }

    private void checkREBType(REBNotification rn, String notifcationDetails, RemoteMessage remoteMessage) {

        String m = StringEscapeUtils.unescapeJava(message);
        final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (rn.getServiceType().equals("REB_BILL")) {

            if (rn.getTrxData().getStatusCode() == 200 || rn.getTrxData().getStatusCode() == 303 || rn.getTrxData().getStatusCode() == 100 || rn.getTrxData().getStatusCode() == 327) {
                handleNormalNotification(remoteMessage);
            } else {
                handleREBBillTextNotification(title, m, defaultSoundUri, new Gson().toJson(rn), notifcationDetails, true);
            }

        } else if (rn.getServiceType().equals("REB_REG")) {
            if (rn.getTrxData().getStatusCode() == 200 || rn.getTrxData().getStatusCode() == 328 || rn.getTrxData().getStatusCode() == 100) {
                handleNormalNotification(remoteMessage);
            } else {
                handleREBRegistrationTextNotification(title, m, defaultSoundUri, new Gson().toJson(rn), notifcationDetails, true);
            }
        }
    }


    private void handleNormalNotification(RemoteMessage remoteMessage) {
        String imageUri;
        imageUri = remoteMessage.getData().get("image");
        notifcationDetails = remoteMessage.getData().get("Notification_Details");

        //To get a Bitmap image from the URL received
        if (imageUri == null) {
            imageUri = "";
        }

        String m = StringEscapeUtils.unescapeJava(message);

        sendNotification(title, m, imageUri);
        ConnectionDetector mCd = new ConnectionDetector(AppController.getContext());
        AppHelper.notificationCounterCheck(mCd, getApplicationContext());
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

    private void handleAirTicketNotification(String title, String messageBody, Uri defaultSoundUri, String notificationDetails, boolean isAction) {
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

        //This is the intent of PendingIntent
        Intent intentActionAccept = new Intent(getApplicationContext(), ActionReceiver.class);
        intentActionAccept.putExtra("action", "Accept");
        PendingIntent pIntentActionAccept = PendingIntent.getBroadcast(getApplicationContext(), 1, intentActionAccept, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent intentActionReject = new Intent(getApplicationContext(), ActionReceiver.class);
        intentActionReject.putExtra("action", "Accept");
        PendingIntent pIntentActionReject = PendingIntent.getBroadcast(getApplicationContext(), 1, intentActionAccept, PendingIntent.FLAG_UPDATE_CURRENT);


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

//        if (isAction) {
//            builder.addAction(R.drawable.pw_notification_bar, "Accept", pIntentActionAccept);
//            builder.addAction(R.drawable.pw_notification_bar, "Reject", pIntentActionReject);
//
//        }

        if (notificationManager != null) {
            notificationManager.notify(requestID, builder.build());
        }
    }

    // REB

    private void handleREBBillTextNotification(String title, String messageBody, Uri defaultSoundUri, String REBNotification, String notifcationDetails, boolean isAction) {

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
        intent.putExtra("Notification_Details", notifcationDetails);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(title);

        //This is the intent of PendingIntent
        Intent intentActionAccept = new Intent(getApplicationContext(), ActionReceiver.class);
        intentActionAccept.putExtra("action", "bill");
        intentActionAccept.putExtra("requestID", requestID);
        intentActionAccept.putExtra("REBNotification", REBNotification);
        PendingIntent pIntentActionAccept = PendingIntent.getBroadcast(getApplicationContext(), 1, intentActionAccept, PendingIntent.FLAG_UPDATE_CURRENT);

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

        if (isAction) {
            builder.addAction(R.drawable.pw_notification_bar, getApplicationContext().getString(R.string.re_submit_reb), pIntentActionAccept);
        }

        if (notificationManager != null) {
            notificationManager.notify(requestID, builder.build());
        }

    }

    private void handleREBRegistrationTextNotification(String title, String messageBody, Uri defaultSoundUri, String REBNotification, String notifcationDetails, boolean isAction) {
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
        intent.putExtra("Notification_Details", notifcationDetails);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, requestID /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText(messageBody);
        bigText.setBigContentTitle(title);

        //This is the intent of PendingIntent
        Intent intentActionAccept = new Intent(getApplicationContext(), ActionReceiver.class);
        intentActionAccept.putExtra("action", "registration");
        intentActionAccept.putExtra("requestID", requestID);
        intentActionAccept.putExtra("REBNotification", REBNotification);
        PendingIntent pIntentActionAccept = PendingIntent.getBroadcast(getApplicationContext(), 1, intentActionAccept, PendingIntent.FLAG_UPDATE_CURRENT);

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

        if (isAction) {
            builder.addAction(R.drawable.pw_notification_bar, getApplicationContext().getString(R.string.re_submit_reb), pIntentActionAccept);
        }

        if (notificationManager != null) {
            notificationManager.notify(requestID, builder.build());
        }
    }
}
