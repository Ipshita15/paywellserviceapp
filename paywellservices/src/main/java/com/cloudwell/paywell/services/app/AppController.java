package com.cloudwell.paywell.services.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.activity.RecentUsedStackSet;
import com.cloudwell.paywell.services.activity.home.AppSignatureHelper;
import com.cloudwell.paywell.services.activity.myFavorite.helper.MyFavoriteHelper;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.constant.IconConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.database.FavoriteMenuDab;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.utils.AppVersionUtility;
import com.cloudwell.paywell.services.utils.MyHttpClient;
import com.cloudwell.paywell.services.utils.StringConstant;
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

import java.util.ArrayList;
import java.util.List;

import androidx.multidex.MultiDex;
import io.fabric.sdk.android.Fabric;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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


            Logger.i("SMS HashKey: " + new AppSignatureHelper(getApplicationContext()).getAppSignatures().get(0));
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

    private void addDefaultRecentList() {

        RecentUsedStackSet.getInstance().add(new RecentUsedMenu(StringConstant.KEY_home_utility_desco, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, 4, 4));
        RecentUsedStackSet.getInstance().add(new RecentUsedMenu(StringConstant.KEY_home_utility_ivac_free_pay_favorite, StringConstant.KEY_home_utility, IconConstant.KEY_ic_bill_pay, 3, 25));
        RecentUsedStackSet.getInstance().add(new RecentUsedMenu(StringConstant.KEY_home_utility_pollibiddut_bill_pay_favorite, StringConstant.KEY_home_utility, IconConstant.KEY_ic_polli_biddut, 2, 11));
        RecentUsedStackSet.getInstance().add(new RecentUsedMenu(StringConstant.KEY_mobileOperator, StringConstant.KEY_topup, IconConstant.KEY_all_operator, 1, 1));


        ArrayList<RecentUsedMenu> all = RecentUsedStackSet.getInstance().getAll();

        ArrayList<RecentUsedMenu> update = new ArrayList<>();


        for (int i = 0; i < all.size(); i++) {
            RecentUsedMenu recentUsedMenu1 = all.get(i);
            recentUsedMenu1.setId(+i+1);
            update.add(recentUsedMenu1);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .mFavoriteMenuDab()
                        .deletedALlRecentUsedMenu();

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .mFavoriteMenuDab()
                        .insertRecentUsedMenu(update);

                return null;
            }
        }.execute();


    }


    private void generatedRecentUsedRecycView(List<RecentUsedMenu> favoriteMenus) {


    }

    private void installMenuData() {
        AppVersionUtility.AppStart appStart = AppVersionUtility.checkAppStart(getApplicationContext());
        switch (appStart) {
            case NORMAL:

                getRecentList();

                break;
            case FIRST_TIME:
                MyFavoriteHelper.Companion.insertData(getApplicationContext());
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW, false);

                //setDefaultRecentList();

                break;
            case FIRST_TIME_VERSION:
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.USER_USED_NOTIFICAITON_FLOW, false);
                MyFavoriteHelper.Companion.updateData(getApplicationContext());

               // setDefaultRecentList();

                break;
        }
    }

    private void getRecentList() {


        final FavoriteMenuDab favoriteMenuDab = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mFavoriteMenuDab();
        favoriteMenuDab.getAllRecentUsedMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecentUsedMenu>>() {
                    @Override
                    public void accept(List<RecentUsedMenu> favoriteMenus) throws Exception {

                        if (favoriteMenus.size() == 0) {
                            addDefaultRecentList();
                        }else {
                            List<RecentUsedMenu> favoriteMenus1 = favoriteMenus;

                            RecentUsedStackSet.getInstance().addAll(favoriteMenus);
                        }
                    }
                });

    }


    private void setDefaultRecentList() {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .mFavoriteMenuDab()
                        .deletedALlRecentUsedMenu();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                final FavoriteMenuDab favoriteMenuDab = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mFavoriteMenuDab();
                favoriteMenuDab.getAllRecentUsedMenu().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<RecentUsedMenu>>() {
                            @Override
                            public void accept(List<RecentUsedMenu> favoriteMenus) throws Exception {

                                if (favoriteMenus.size() == 0) {
                                    addDefaultRecentList();
                                }
                            }
                        });

            }
        }.execute();


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
