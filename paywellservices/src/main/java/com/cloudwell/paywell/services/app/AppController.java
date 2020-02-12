package com.cloudwell.paywell.services.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.activity.home.AppSignatureHelper;
import com.cloudwell.paywell.services.activity.myFavorite.helper.MyFavoriteHelper;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.utils.AppVersionUtility;
import com.cloudwell.paywell.services.utils.MyHttpClient;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.RefWatcher;

import org.apache.http.client.HttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import androidx.multidex.MultiDex;
import io.fabric.sdk.android.Fabric;

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

    AppHandler mAppHandler;

    private RefWatcher refWatcher;


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();




        mInstance = this;
        mContext = this;
        client = createTrustedHttpsClient();

        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter());
            FirebaseApp.initializeApp(this);
            String id = FirebaseInstanceId.getInstance().getToken();
            Log.e("device_token", "" + id);



            Logger.i( "SMS HashKey: " + new AppSignatureHelper(getApplicationContext()).getAppSignatures().get(0));
            // Logger.v(DebugDB.getAddressLog());


//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            refWatcher = LeakCanary.install(this);


//            Stetho.initializeWithDefaults(this);
//

        }

        configureCrashReporting();
        setupCrashlyticsUserInfo();

        installMenuData();

        new AppSignatureHelper(getApplicationContext()).getAppSignatures();




    }

    private void installMenuData() {
        AppVersionUtility.AppStart appStart = AppVersionUtility.checkAppStart(getApplicationContext());
        switch (appStart) {
            case NORMAL:

                break;
            case FIRST_TIME:
                MyFavoriteHelper.Companion.insertData(getApplicationContext());
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW,  false);
                break;
            case FIRST_TIME_VERSION:
                 AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW,  false);
                 MyFavoriteHelper.Companion.updateData(getApplicationContext());
                break;
        }
    }


    public static RefWatcher getRefWatcher(Context context) {
        AppController myApplication = (AppController) context.getApplicationContext();
        return myApplication.refWatcher;
    }

    private void configureCrashReporting() {
        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
                .disabled(BuildConfig.DEBUG)
                .build();
        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build(), new Crashlytics());
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

    public static AppController getmContext() {
        return mContext;
    }

    private void setupCrashlyticsUserInfo() {
        try {
            mAppHandler = AppHandler.getmInstance(getApplicationContext());
            String appStatus = mAppHandler.getAppStatus();
            if (!appStatus.equals("unknown")) {
                String rid = mAppHandler.getRID();
                String userName = mAppHandler.getUserName();
                String mobileNumber = mAppHandler.getMobileNumber();
                Crashlytics.setUserIdentifier(rid);
                Crashlytics.setUserName("UserName: " + userName + " Mobile number: " + mobileNumber);
                Logger.v("UserName: " + userName + " Mobile number: " + mobileNumber);


            }
        } catch (Exception e) {

        }
    }
}
