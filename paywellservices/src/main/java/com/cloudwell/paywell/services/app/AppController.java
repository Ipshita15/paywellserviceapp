package com.cloudwell.paywell.services.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;

import com.cloudwell.paywell.services.utils.MyHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

/**
 * Created by Android on 12/1/2015.
 */
@SuppressWarnings("ALL")
public class AppController extends Application {

    private static final String APONA_LOHIT = "fonts/AponaLohit.ttf";
    private static final String OXYGEN_LIGHT = "fonts/Oxygen-Light.ttf";
    private static AppController mInstance;
    private static SharedPreferences preference;
    public static HttpClient client;
    private static SharedPreferences sPref;
    private static AppController mContext;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext = this;
        client = createTrustedHttpsClient();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static AppController getContext() {
        return mContext;
    }

    public HttpClient getTrustedHttpClient() {
        return client;
    }

    private HttpClient createTrustedHttpsClient() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        int timeoutConnection = 1000000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for
        // data.
        int timeoutSocket = 1000000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Instantiate the custom HttpClient
        HttpClient client = new MyHttpClient(httpParameters, getApplicationContext());
        return client;
    }

    public String getAssestFont() {
        return OXYGEN_LIGHT;
    }

    public Typeface getAponaLohitFont() {
        return Typeface.createFromAsset(getAssets(), APONA_LOHIT);
    }

    public Typeface getOxygenLightFont() {
        return Typeface.createFromAsset(getAssets(), OXYGEN_LIGHT);
    }
}
