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

    private static final String ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    private static final String ASSETS_FONT = "Roboto-Regular.ttf";
    private static AppController mInstance;
    //    private static String imei;
    private static SharedPreferences preference;
    public static HttpClient client;
    private static SharedPreferences sPref;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        client = createTrustedHttpsClient();
//        imei = setIMEINumber();
//        sPref = createSPref();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
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
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        // Instantiate the custom HttpClient
        HttpClient client = new MyHttpClient(httpParameters, getApplicationContext());
        return client;
    }
    public Typeface getRobotoRegularFont() {
       return Typeface.createFromAsset(getAssets(), ROBOTO_REGULAR);
    }
    public String getAssestFont() {
        return ASSETS_FONT;
    }
   /* public static String getIMEINumber() {
        return "";
    }*/
   /* private String setIMEINumber() {
        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);
        //anisul.islam//351867061582307//karib_vai:353180066967533/moktar:359254058225400
        String imeiNoSIM1 = telephonyInfo.getImeiSIM1();
//	     String imeiNoSIM2 = telephonyInfo.getImeiSIM2();
        return imeiNoSIM1; // imei no : a10000289bbb5d for Hisense set
    }*/
//    private SharedPreferences createSPref() {
//        return getSharedPreferences("appPref", Context.MODE_PRIVATE);
//    }
//
//    public static SharedPreferences getSharedPreference() {
//        return sPref;
//    }
}
