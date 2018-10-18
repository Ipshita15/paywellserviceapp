package com.cloudwell.paywell.services.service;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naima.gani on 12/22/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private static final String TAG_RESPONSE_STATUS = "status";
    private String token;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        this.token = token;
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);
        if (mCd.isConnectingToInternet()) {
            new PushFirebaseIdTask().execute(getString(R.string.notification_token_url), token);
        } else {
            Toast.makeText(MyFirebaseInstanceIDService.this, R.string.connection_error_msg, Toast.LENGTH_LONG).show();
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
                Toast.makeText(MyFirebaseInstanceIDService.this, R.string.try_again_msg, Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MyFirebaseInstanceIDService.this, R.string.try_again_msg, Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(MyFirebaseInstanceIDService.this, R.string.try_again_msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
