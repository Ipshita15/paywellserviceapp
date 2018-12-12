package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.about.AboutActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.chat.ChatActivity;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.activity.notification.NotificationActivity;
import com.cloudwell.paywell.services.activity.notification.NotificationAllActivity;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.activity.product.ProductMenuActivity;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.activity.scan.DisplayQRCodeActivity;
import com.cloudwell.paywell.services.activity.settings.SettingsActivity;
import com.cloudwell.paywell.services.activity.statements.StatementMainActivity;
import com.cloudwell.paywell.services.activity.terms.TermsActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMenuActivity;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.adapter.MainSliderAdapter;
import com.cloudwell.paywell.services.adapter.PicassoImageLoadingService;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.UpdateChecker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private boolean doubleBackToExitPressedOnce = false;
    private CoordinatorLayout mCoordinateLayout;
    private AppHandler mAppHandler;
    private UpdateChecker mUpdateChecker;
    public final long UPDATE_SOFTWARE_INTERVAL = 24 * 60 * 60;// 1 day
    public final long PHN_NUM_CHECK_INTERVAL = 24 * 60 * 60;// 1 day
    public final long UPDATE_LOCATION_INTERVAL = 7 * 24 * 60 * 60;// 7 day
    private TextView mToolbarHeading;
    private ConnectionDetector mCd;
    public int mNumOfNotification;
    private TextView mNotification = null;
    private Toolbar mToolbar;
    private boolean mIsNotificationShown;
    private final String TAG_RESPONSE_STATUS = "status";
    private final String TAG_RESPONSE_TOTAL_UREAD_MSG = "unread_message";
    private final String TAG_RESPONSE_MSG_SUBJECT = "message_sub";
    private final String TAG_RESPONSE_MSG_ID = "message_id";
    private final String TAG_RESPONSE_MSG_ARRAY = "detail_message";
    private final String TAG_RESPONSE_MESSAGE = "message";
    private final String TAG_RESPONSE_DATE = "added_datetime";
    private final String TAG_RESPONSE_IMAGE = "image_url";
    private final String TAG_RESPONSE_TYPE = "type";
    private final String TAG_RESPONSE_NOTIFICATION_BALANCE_RETURN_DATA = "balance_return_data";
    private final String TAG_RESPONSE_OTP = "otp";
    private NavigationView navigationView;
    boolean checkNotificationFlag = false;

    AlertDialog.Builder builderNotification;
    private AlertDialog alertNotification;
    private Slider viewPager;
    private int currentSlideNo;
    private int currentPage;

    private Button home_topup, home_utility, home_payments, home_eticket, home_mfs, home_product_catalog, home_statement, home_refill_balance, home_settings;

    private final int PERMISSIONS_FOR_QR_CODE_SCAN = 100;
    private final int PERMISSIONS_REQUEST_FOR_WRITE_EXTERNAL_STORAGE = 101;
    private final int PERMISSIONS_REQUEST_FOR_READ_OTP = 102;
    private final int PERMISSIONS_REQUEST_ACCESS_LOCATION = 103;
    private final int PERMISSIONS_REQUEST_ACCESS_CALL = 104;
    final int REQUEST_LOCATION = 1000;

    private String phn_num;
    private int phn_num_count;
    private int otp_check = 0;
    private String otp;

    private GoogleApiClient googleApiClient;

    private String selectedPhnNo;

    private int downloadType = 0;
    private int TAG_DOWNLOAD_PLAY_STORE = 1;


    // all async
    private AsyncTask<String, Intent, String> mNotificationAsync;
    private PayWellBalanceAsync pwBalanceCheck;
    private AsyncTask<String, Integer, String> mRequestPhnNumberAddAsync;
    private AsyncTask<String, Integer, String> mConfirmPhnNumberAddAsync;
    private AsyncTask<String, Intent, String> mPushFirebaseIdTask;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        mToolbarHeading = (mToolbar.findViewById(R.id.txtHeading));
        mCoordinateLayout = findViewById(R.id.coordinateLayout);

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = new AppHandler(this);

        builderNotification = new AlertDialog.Builder(this);
        alertNotification = builderNotification.create();

        viewPager = findViewById(R.id.view_pager_auto);

        InitializeData();
    }

    private void InitializeData() {
        /*Buttons Initialization*/
        home_topup = findViewById(R.id.homeBtnTopup);
        home_utility = findViewById(R.id.homeBtnUtility);
        home_payments = findViewById(R.id.homeBtnPayments);
        home_eticket = findViewById(R.id.homeBtnEticket);
        home_mfs = findViewById(R.id.homeBtnMFS);
        home_product_catalog = findViewById(R.id.homeBtnProductCategory);
        home_statement = findViewById(R.id.homeBtnMiniStatement);
        home_refill_balance = findViewById(R.id.homeBtnRefillBalance);
        home_settings = findViewById(R.id.homeBtnSettings);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_topup.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_utility.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_payments.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_eticket.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_mfs.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_product_catalog.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_statement.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_refill_balance.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_settings.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_topup.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_utility.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_payments.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_eticket.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_mfs.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_product_catalog.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_statement.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_refill_balance.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_settings.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle delivered = getIntent().getExtras();
        if (delivered != null && !delivered.isEmpty()) {
            mIsNotificationShown = delivered.getBoolean(NotificationActivity.IS_NOTIFICATION_SHOWN);
        }

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } else {
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }

        mToolbarHeading.setText(getString(R.string.balance) + mAppHandler.getPwBalance() + getString(R.string.tk));
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mToolbarHeading.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mToolbarHeading.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        setAutomaticDatetime();
        checkSoftwareUpdate();

        /*Location Change Log*/
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getLocationUpdateCheck() + UPDATE_LOCATION_INTERVAL)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
            } else {
                try {
                    getMyLocation();
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                } catch (Exception ex) {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
            checkLocationUpdate();
        }

        //////Before
        if (mAppHandler.getPhnNumberVerificationStatus().equalsIgnoreCase("false") || mAppHandler.getMerchantTypeVerificationStatus().equalsIgnoreCase("false")) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
//                // permission has not been granted.
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSIONS_REQUEST_FOR_READ_OTP);
//            } else {
            checkPhnNoUpdate();
            //}
        }
//      Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("Notification") && value.equals("True")) {
                    mNotificationAsync = new NotificationAsync().execute(getResources().getString(R.string.notif_url));
                }
            }
        }
        subscribeToPushService();
        initializePreview();

        //Tutorial
        showTutorial();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
            }
            this.doubleBackToExitPressedOnce = true;
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.exit, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public void RefreshStringsOfButton() {
        home_topup.setText(R.string.home_topup);
        home_utility.setText(R.string.home_utility);
        home_payments.setText(R.string.home_payments);
        home_eticket.setText(R.string.home_eticket);
        home_mfs.setText(R.string.home_mfs);
        home_product_catalog.setText(R.string.home_product_catalog);
        home_statement.setText(R.string.home_statement);
        home_refill_balance.setText(R.string.home_refill_balance);
        home_settings.setText(R.string.home_settings);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_topup.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_utility.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_payments.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_eticket.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_mfs.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_product_catalog.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_statement.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_refill_balance.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_settings.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_topup.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_utility.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_payments.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_eticket.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_mfs.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_product_catalog.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_statement.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_refill_balance.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_settings.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        navigationView.getMenu().findItem(R.id.nav_topup).setTitle(R.string.home_topup);
        navigationView.getMenu().findItem(R.id.nav_utility).setTitle(R.string.home_utility);
        navigationView.getMenu().findItem(R.id.nav_payments).setTitle(R.string.home_payments);
        navigationView.getMenu().findItem(R.id.nav_eticket).setTitle(R.string.home_eticket);
        navigationView.getMenu().findItem(R.id.nav_mfs).setTitle(R.string.nav_mfs);
        navigationView.getMenu().findItem(R.id.nav_product).setTitle(R.string.nav_product_catalog);
        navigationView.getMenu().findItem(R.id.nav_more).setTitle(R.string.nav_more_title);
        navigationView.getMenu().findItem(R.id.nav_check_balance).setTitle(R.string.nav_check_balance);
        navigationView.getMenu().findItem(R.id.nav_terms).setTitle(R.string.nav_terms_and_conditions);
        navigationView.getMenu().findItem(R.id.nav_policy).setTitle(R.string.nav_policy_statement);
        navigationView.getMenu().findItem(R.id.nav_about).setTitle(R.string.nav_about);
    }

    @Override
    @SuppressLint("NewApi")
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.main);
        Menu _menu = mToolbar.getMenu();

        TextView _pwId = findViewById(R.id.pwId);
        assert _pwId != null;

        try {
            _pwId.setText(getString(R.string.rid) + mAppHandler.getRID());
            if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
                _pwId.setTypeface(AppController.getInstance().getOxygenLightFont());
            } else {
                _pwId.setTypeface(AppController.getInstance().getAponaLohitFont());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        }

        MenuItem menuNotification = _menu.findItem(R.id.menu_notification);
        View notificationView = MenuItemCompat.getActionView(menuNotification);

        //Scanner
        MenuItem menuScan = _menu.findItem(R.id.menu_scanner);

        mNotification = notificationView.findViewById(R.id.tvNotification);
        mNotification.setVisibility(View.INVISIBLE);
        if (!mIsNotificationShown) {
            if (!mCd.isConnectingToInternet())
                AppHandler.showDialog(getSupportFragmentManager());
            mNotificationAsync = new NotificationAsync().execute(getResources().getString(R.string.notif_url));
        }
        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mNumOfNotification != 0) {
                    Intent intent = new Intent(MainActivity.this, NotificationAllActivity.class);
                    startActivity(intent);
                    mNumOfNotification = 0;
                    finish();
                } else {
                    if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                        startActivity(new Intent(MainActivity.this, NotificationAllActivity.class));
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                }
            }
        });
        menuScan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return (scanOnClick());
            }
        });
        return true;
    }

    public boolean scanOnClick() {
        if (mAppHandler.getQrCodeImagePath().equalsIgnoreCase("unknown")) {
            generateQRCode();
        }
        startActivity(new Intent(MainActivity.this, DisplayQRCodeActivity.class));
        finish();
        return true;
    }

    public void notificationCount(final int newNotification) {
        mNumOfNotification = newNotification;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newNotification == 0)
                    mNotification.setVisibility(View.INVISIBLE);
                else {
                    String notification = Integer.toString(newNotification);
                    mNotification.setVisibility(View.VISIBLE);
                    mNotification.setText(notification);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            //IS_HOME_AS_UP_ENABLE_PRESS = true;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation terms_and_conditions_format item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_topup) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    // Handle the topup action
                    startActivity(new Intent(this, TopupMainActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_utility) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    // Handle the utility action
                    startActivity(new Intent(this, UtilityMainActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_payments) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    // Handle the bKash action
                    startActivity(new Intent(this, PaymentsMainActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_eticket) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                // Handle the eTicket action
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    // Handle the utility action
                    startActivity(new Intent(this, ETicketMainActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_mfs) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    startActivity(new Intent(this, MFSMainActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, getString(R.string.allow_error_msg), Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_product) {
            if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    startActivity(new Intent(MainActivity.this, ProductMenuActivity.class));
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        } else if (id == R.id.nav_check_balance) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                checkPayWellBalance();
            }
        } else if (id == R.id.nav_terms) {
            startActivity(new Intent(MainActivity.this, TermsActivity.class));
        } else if (id == R.id.nav_policy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paywellonline.com/Privacy.php"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_about) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @SuppressWarnings("deprecation")
    private class NotificationAsync extends AsyncTask<String, Intent, String> {

        @Override
        protected String doInBackground(String... params) {
            String notifications = null;
            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("mes_type", "all_message"));
                nameValuePairs.add(new BasicNameValuePair("message_status", "all"));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                notifications = httpClients.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
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

                        String totalUnreadMsg = jsonObject.getString(TAG_RESPONSE_TOTAL_UREAD_MSG);
                        String totalMsg = jsonObject.getString("total_message");
//                        NotificationActivity.length = Integer.parseInt(totalMsg);
                        mNumOfNotification = Integer.parseInt(totalUnreadMsg);

                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE_MSG_ARRAY);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String msg_id = object.getString(TAG_RESPONSE_MSG_ID);
                            String msg_status = object.getString(TAG_RESPONSE_STATUS);
                            String msg_title = object.getString(TAG_RESPONSE_MSG_SUBJECT);
                            String msg = object.getString(TAG_RESPONSE_MESSAGE);
                            String date = object.getString(TAG_RESPONSE_DATE);
                            String type = object.getString(TAG_RESPONSE_TYPE);
                            String data = object.getString(TAG_RESPONSE_NOTIFICATION_BALANCE_RETURN_DATA);
                            String image;
                            if (!object.getString(TAG_RESPONSE_IMAGE).isEmpty()) {
                                image = object.getString(TAG_RESPONSE_IMAGE);
                            } else {
                                image = "empty";
                            }

                            if (checkNotificationFlag) {
                                checkNotificationFlag = false;
                                mNumOfNotification = 0;
                                startActivity(new Intent(MainActivity.this, NotificationAllActivity.class));
                                finish();
                            }
                        }
                    } else {
                        mNumOfNotification = 0;
                    }
                    notificationCount(mNumOfNotification);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.conn_timeout_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void checkPayWellBalance() {
        pwBalanceCheck = new PayWellBalanceAsync();
        pwBalanceCheck.execute(getResources().getString(R.string.pw_bal));
    }

    @SuppressWarnings("deprecation")
    private class PayWellBalanceAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            mToolbarHeading.setText(R.string.balance_checking);
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(1);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.fillInStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (result != null && result.contains("@")) {
                    String splitArray[] = result.split("@");
                    if (splitArray.length > 0) {
                        if ((splitArray[0].equalsIgnoreCase("\r\n200")) || (splitArray[0].equalsIgnoreCase("200"))) {

                            StringBuilder stringBuilder = new StringBuilder();
                            String[] balance = splitArray[1].split("[.]");
                            String round = balance[0];
                            String fraction = balance[1];
                            fraction = (String) fraction.subSequence(0, 2);
                            stringBuilder.append(round).append(".").append(fraction);

                            mAppHandler.setPWBalance(stringBuilder.toString());

                            String strBalance = getString(R.string.balance) + mAppHandler.getPwBalance() + getString(R.string.tk);
                            mToolbarHeading.setText(strBalance);

                            mAppHandler.setRID(splitArray[3]);

                            if (splitArray.length > 4) {
                                if (Integer.parseInt(splitArray[4]) > 0) {
                                    notificationCount(Integer.parseInt(splitArray[4]));
                                    builderNotification = new AlertDialog.Builder(MainActivity.this);
                                    builderNotification.setTitle(R.string.home_notification);
                                    builderNotification.setMessage(getString(R.string.unread_first_msg) + " "
                                            + splitArray[4] + " " + getString(R.string.unread_second_msg));
                                    builderNotification.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int id) {
                                            dialogInterface.dismiss();
                                            checkNotificationFlag = true;
                                            mNotificationAsync = new NotificationAsync().execute(getResources().getString(R.string.notif_url));
                                        }
                                    });
                                    alertNotification = builderNotification.create();
                                    alertNotification.show();
                                    TextView messageText = alertNotification.findViewById(android.R.id.message);
                                    messageText.setGravity(Gravity.CENTER);
                                }
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(mCoordinateLayout, splitArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                            Intent i = new Intent(MainActivity.this, AppLoadingActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.conn_timeout_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void checkSoftwareUpdate() {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getUpdateCheck() + UPDATE_SOFTWARE_INTERVAL)) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                checkForSoftwareUpdate();
            }
        } else {
            mAppHandler.setUpdateCheck(System.currentTimeMillis() / 1000);
        }
    }

    private void checkForSoftwareUpdate() {
        mUpdateChecker = new UpdateChecker(this);

        new Thread() {
            @Override
            public void run() {
                mUpdateChecker.checkForUpdateByVersionName(getResources().getString(R.string.check_version));
                if (mUpdateChecker.isUpdateAvailable()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            updateConfirmationBuilder();
                        }

                    });
                }
                mAppHandler.setUpdateCheck(System.currentTimeMillis() / 1000);
            }
        }.start();
    }

    private void updateConfirmationBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.version_upgrade);
        builder.setMessage(R.string.upgrade_msg);
        builder.setPositiveButton(R.string.play_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                downloadType = TAG_DOWNLOAD_PLAY_STORE;
                checkPermission();
            }
        });
        builder.setNeutralButton(R.string.pw_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                downloadType = TAG_DOWNLOAD_PLAY_STORE;
                checkPermission();
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_FOR_WRITE_EXTERNAL_STORAGE);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            if (downloadType == TAG_DOWNLOAD_PLAY_STORE) {
                mUpdateChecker.launchMarketDetails();
            } else {
                mUpdateChecker.downloadAndInstall(getResources().getString(R.string.update_check));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FOR_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    if (getMailAddress()) {
                        mUpdateChecker.launchMarketDetails();
                    } else {
                        mUpdateChecker.downloadAndInstall(getResources().getString(R.string.update_check));
                    }
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    checkPermission();
                }
                break;
            }
            case PERMISSIONS_FOR_QR_CODE_SCAN: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                        startActivity(new Intent(MainActivity.this, DisplayQRCodeActivity.class));
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_FOR_QR_CODE_SCAN);
                }
                break;
            }
//            case PERMISSIONS_REQUEST_FOR_READ_OTP: {
//                // Check if the only required permission has been granted
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    // Phone permission has been granted
//                    if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
//                        checkPhnNoUpdate();
//                    } else {
//                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
//                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                        View snackBarView = snackbar.getView();
//                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                        snackbar.show();
//                    }
//                } else {
//                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, PERMISSIONS_REQUEST_FOR_READ_OTP);
//                }
//                break;
//            }
            case PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    try {
                        getMyLocation();
                        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        checkLocationUpdate();
                    } catch (Exception e) {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    // permission denied
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_LOCATION);
                }
                break;
            }
            case PERMISSIONS_REQUEST_ACCESS_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + selectedPhnNo));
                        startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    // permission denied
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                            PERMISSIONS_REQUEST_ACCESS_CALL);
                }
                break;
            }
        }
    }

    private void setAutomaticDatetime() {
        if (!isAutoTimeEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                showSetAutoDatetimeDialog();
            } else {
                Settings.System.putInt(this.getContentResolver(), Settings.System.AUTO_TIME, 1);
            }
        }
    }

    private boolean isAutoTimeEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // For JB+
            return Settings.Global.getInt(getApplicationContext().getContentResolver(), Settings.Global.AUTO_TIME, 0) > 0;
        }
        // For older Android versions
        return Settings.System.getInt(getApplicationContext().getContentResolver(), Settings.System.AUTO_TIME, 0) > 0;
    }

    private void showSetAutoDatetimeDialog() {
        // Create the fragment and show it as a dialog.
        DialogFragment newFragment = AutomaticDatetimeFragment.newInstance();
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public static class AutomaticDatetimeFragment extends DialogFragment {

        static AutomaticDatetimeFragment newInstance() {
            return new AutomaticDatetimeFragment();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.auto_date_time_btn);
            builder.setMessage(R.string.auto_date_time_msg)
                    .setPositiveButton(R.string.okay_btn,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));

                                }
                            });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.setOnKeyListener(new DialogInterface.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            return alert;
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnTopup:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(this, TopupMenuActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnUtility:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(this, UtilityMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnPayments:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(this, PaymentsMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnEticket:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(this, ETicketMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnMFS:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(MainActivity.this, MFSMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnProductCategory:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(MainActivity.this, ProductMenuActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnMiniStatement:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(MainActivity.this, StatementMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnRefillBalance:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                        startActivity(new Intent(this, RefillBalanceMainActivity.class));
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnSettings:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnMessage:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    startActivity(new Intent(MainActivity.this, ChatActivity.class));
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnCall:
                if (pwBalanceCheck.getStatus() == AsyncTask.Status.FINISHED) {
                    callPreview();
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.wait_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
                break;
        }
    }

    public void generateQRCode() {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(mAppHandler.getRID(), BarcodeFormat.QR_CODE, 700, 700);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            String path = saveToInternalStorage(bitmap);
            mAppHandler.setQrCodeImagePath(path);
        } catch (WriterException e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, "Unable to generate image", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File myPath = new File(directory, "qr_code.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, "Unable to store image", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, "Unable to store image", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        // viewPager.stopNestedScroll();
        viewPager.setInterval(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshStringsOfButton();
        if (alertNotification.isShowing()) {
            alertNotification.dismiss();
        }
        // start auto scroll when onResume
        // viewPager.startAutoScroll();
        checkPayWellBalance();

        viewPager.setInterval(2000);
    }

    private boolean getMailAddress() {
        String gmail = "";

        Pattern gmailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(this).getAccounts();
        for (Account account : accounts) {
            if (gmailPattern.matcher(account.name).matches()) {
                gmail = account.name;
            }
        }
        if (gmail.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void checkPhnNoUpdate() {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getPhnUpdateCheck() + PHN_NUM_CHECK_INTERVAL)) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                if (mAppHandler.getPhnNumberVerificationStatus().equalsIgnoreCase("false")) {
                    checkForPhnUpdate();
                } else {
                    mAppHandler.setPhnUpdateCheck(System.currentTimeMillis() / 1000);
                    startActivity(new Intent(MainActivity.this, MerchantTypeVerify.class));
                }
            }
        }
    }

    private void checkForPhnUpdate() {
        phn_num_count = mAppHandler.getDayCount();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.info_collect_title));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 10, 20, 5);

        final EditText etPhn = new EditText(this);
        etPhn.setHint(getString(R.string.phn_num_hint));
        etPhn.setRawInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
        layout.addView(etPhn);

        final TextView tvPhnQuote = new TextView(this);
        String text = "\"" + getString(R.string.verify_quote) + "\"";
        tvPhnQuote.setText(text);
        layout.addView(tvPhnQuote);
        builder.setView(layout);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(etPhn.getWindowToken(), 0);

                if (etPhn.getText().toString().length() == 11) {
                    dialogInterface.dismiss();
                    phn_num = etPhn.getText().toString();
                    if (mCd.isConnectingToInternet()) {
                        mRequestPhnNumberAddAsync = new RequestPhnNumberAddAsync().execute(getResources().getString(R.string.otp_for_phn_num), phn_num);
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_correct_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    checkForPhnUpdate();
                }
            }
        });

        if (phn_num_count < 3) {
            builder.setNegativeButton(R.string.skip_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mAppHandler.setDayCount(phn_num_count + 1);
                }
            });
        }
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        alert.show();

        mAppHandler.setPhnUpdateCheck(System.currentTimeMillis() / 1000);
    }

    private class RequestPhnNumberAddAsync extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("phone", data[1]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            try {
                JSONObject jsonObject = new JSONObject(result);

                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                String msg = jsonObject.getString(TAG_RESPONSE_MESSAGE);
                String foundOtp = jsonObject.getString(TAG_RESPONSE_OTP);

                if (status.equalsIgnoreCase("200")) {
                    otp = foundOtp;
                    checkForOTP();
                } else if (status.equalsIgnoreCase("335")) {
                    mAppHandler.setPhnNumberVerificationStatus("verified");
                    mAppHandler.setPhnNumber(phn_num);
                    mAppHandler.setDayCount(0);
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (Exception ex) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void checkForOTP() {
        if (otp_check < 3) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.info_collect_title));
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(20, 10, 20, 5);

            final EditText etOtp = new EditText(this);
            etOtp.setHint(getString(R.string.otp_error_msg));
            etOtp.setRawInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
            layout.addView(etOtp);

            builder.setView(layout);

//            SmsReceiver.bindListener(new SmsListener() {
//                @Override
//                public void messageReceived(String messageText) {
//                    etOtp.setText(messageText);
//                    etOtp.setSelection(etOtp.getText().length());
//                }
//            });

            builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inMethMan.hideSoftInputFromWindow(etOtp.getWindowToken(), 0);

                    if (etOtp.getText().toString().length() > 0) {
                        dialogInterface.dismiss();
                        String inputedOtp = etOtp.getText().toString();
                        if (inputedOtp.equalsIgnoreCase(otp)) {
                            mConfirmPhnNumberAddAsync = new ConfirmPhnNumberAddAsync().execute(getResources().getString(R.string.conf_phn_num), phn_num, otp);
                        } else {
                            Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_correct_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                            otp_check++;
                            checkForOTP();
                        }
                    } else {
                        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_correct_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                }
            });
            AlertDialog alert = builder.create();
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        } else {
            Snackbar snackbar = Snackbar.make(mCoordinateLayout, getString(R.string.try_again_msg), Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
            otp_check = 0;
        }
    }

    private class ConfirmPhnNumberAddAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... data) {
            String responseTxt = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(data[0]);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(4);
                nameValuePairs.add(new BasicNameValuePair("imei_no", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("phone", data[1]));
                nameValuePairs.add(new BasicNameValuePair("otp", data[2]));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                responseTxt = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();

            try {
                JSONObject jsonObject = new JSONObject(result);

                String status = jsonObject.getString(TAG_RESPONSE_STATUS);
                String msg = jsonObject.getString(TAG_RESPONSE_MESSAGE);

                if (status.equalsIgnoreCase("200")) {
                    mAppHandler.setPhnNumberVerificationStatus("verified");
                    mAppHandler.setPhnNumber(phn_num);
                    mAppHandler.setDayCount(0);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Result");
                builder.setMessage(msg);

                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } catch (Exception ex) {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void subscribeToPushService() {
        if (mAppHandler.getFirebaseTokenStatus().equals("true")) {
            if (mCd.isConnectingToInternet()) {
                mPushFirebaseIdTask = new PushFirebaseIdTask().execute(getString(R.string.notification_token_url), mAppHandler.getFirebaseId());
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }


    private class PushFirebaseIdTask extends AsyncTask<String, Intent, String> {
        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

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
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return notifications;
        }

        @Override
        protected void onPostExecute(String result) {
            dismissProgressDialog();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {
                        mAppHandler.setFirebaseTokenStatus("false");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.conn_timeout_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            double PLACE_LATITUDE = (location.getLatitude());
            double PLACE_LONGITUDE = (location.getLongitude());

            if (!mAppHandler.getLatitude().equalsIgnoreCase(String.valueOf(PLACE_LATITUDE))) {
                mAppHandler.setLatitude(String.valueOf(PLACE_LATITUDE));
            }
            if (!mAppHandler.getLongitude().equalsIgnoreCase(String.valueOf(PLACE_LONGITUDE))) {
                mAppHandler.setLongitude(String.valueOf(PLACE_LONGITUDE));
            }
            if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getLocationUpdateCheck() + UPDATE_LOCATION_INTERVAL)) {
                checkLocationUpdate();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getMyLocation() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
            googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
        }
    }

    private void checkLocationUpdate() {
        if (!mCd.isConnectingToInternet()) {
            AppHandler.showDialog(getSupportFragmentManager());
        } else {
            if (!mAppHandler.getLatitude().equalsIgnoreCase("unknown")
                    && !mAppHandler.getLongitude().equalsIgnoreCase("unknown")) {
                new PushLocationLonLatTask().execute(getResources().getString(R.string.update_location));
            }
        }
    }


    @SuppressWarnings("deprecation")
    private class PushLocationLonLatTask extends AsyncTask<String, Intent, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                HttpClient httpClients = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(params[0]);

                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("latitude", mAppHandler.getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("longitude", mAppHandler.getLongitude()));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                result = httpClients.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {
                        mAppHandler.setLocationUpdateCheck(System.currentTimeMillis() / 1000);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.conn_timeout_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private void callPreview() {
        final String[] numbers = {"09610116566", "09666773333", "09666716566", "09638016566"};
        selectedPhnNo = "09610116566";
        AlertDialog dialog;
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.select_phn_title_msg));

        builder.setSingleChoiceItems(numbers, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                selectedPhnNo = numbers[arg1];
            }

        }).create();
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                call();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    private void call() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSIONS_REQUEST_ACCESS_CALL);
        } else {
            Intent intent = new Intent(Intent.ACTION_CALL);

            intent.setData(Uri.parse("tel:" + selectedPhnNo));
            startActivity(intent);
        }
    }

    private void initializePreview() {

        String[] imageUrl = new String[mAppHandler.getDisplayPictureCount()];
        for (int len = 0; len < mAppHandler.getDisplayPictureCount(); len++) {
            imageUrl[len] = "https://api.paywellonline.com/retailerPromotionImage/retailer_pic_" + len + ".jpg";
        }

        Slider.init(new PicassoImageLoadingService(this));

        viewPager.setAdapter(new MainSliderAdapter(getApplicationContext(), imageUrl));
        viewPager.setInterval(2000);
        viewPager.hideIndicators();
        viewPager.setLoopSlides(true);


        viewPager.setOnSlideClickListener(new OnSlideClickListener() {
            @Override
            public void onSlideClick(int position) {

                try {
                    currentPage = position;

                    String link = mAppHandler.getDisplayPictureArrayList().get(currentPage);


                    if (link.isEmpty()) {

                    } else if (link.contains("facebook.com")) {
                        if (!mCd.isConnectingToInternet()) {
                            AppHandler.showDialog(getSupportFragmentManager());
                        } else {
                            goToFacebook();
                        }
                    } else if (link.contains("youtube.com")) {
                        if (!mCd.isConnectingToInternet()) {
                            AppHandler.showDialog(getSupportFragmentManager());
                        } else {
                            Intent mIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.youtube");
                            if (mIntent != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setPackage("com.google.android.youtube");
                                startActivity(intent);
                            } else {
                                WebViewActivity.TAG_LINK = link;
                                startActivity(new Intent(MainActivity.this, WebViewActivity.class));
                            }
                        }
                    } else {
                        WebViewActivity.TAG_LINK = link;
                        startActivity(new Intent(MainActivity.this, WebViewActivity.class));
                    }
                } catch (Exception e) {

                }

            }
        });
    }


    private void goToFacebook() {
        try {
            String facebookUrl = getFacebookPageURL();
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFacebookPageURL() {
        String FACEBOOK_URL = "https://www.facebook.com/PayWellOnline/";
        String facebookurl = null;

        try {
            PackageManager packageManager = getPackageManager();

            if (packageManager != null) {
                Intent activated = packageManager.getLaunchIntentForPackage("com.facebook.katana");

                if (activated != null) {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

                    if (versionCode >= 3002850) {
                        facebookurl = "fb://page/1889526334650062";
                    }
                } else {
                    facebookurl = FACEBOOK_URL;
                }
            } else {
                facebookurl = FACEBOOK_URL;
            }
        } catch (Exception e) {
            facebookurl = FACEBOOK_URL;
        }
        return facebookurl;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
//                        Toast.makeText(MainActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_LOCATION);
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        } else {
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                        }
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
//                        Toast.makeText(MainActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    private void showTutorial() {
//        String SHOWCASE_ID_TOPUP = "topup";
//        // single example
//        new MaterialShowcaseView.Builder(this)
//                .setTarget(home_topup)
//                .setDismissText("GOT IT")
//                .setContentText("This is some amazing feature you should know about")
//                .setDelay(500) // optional but starting animations immediately in onCreate can make them choppy
//                .singleUse(SHOWCASE_ID_TOPUP) // provide a unique ID used to ensure it is only shown once
//                .show();
    }

    @Override
    protected void onDestroy() {
        if (mNotificationAsync != null) {
            mNotificationAsync.cancel(true);
        }

        if (pwBalanceCheck != null) {
            pwBalanceCheck.cancel(true);
        }

        if (mRequestPhnNumberAddAsync != null) {
            mRequestPhnNumberAddAsync.cancel(true);
        }

        if (mConfirmPhnNumberAddAsync != null) {
            mConfirmPhnNumberAddAsync.cancel(true);
        }

        if (mPushFirebaseIdTask != null) {
            mPushFirebaseIdTask.cancel(true);
        }

        if (viewPager != null) {
            Slider.imageLoadingService = null;
            viewPager = null;
        }
        super.onDestroy();
    }
}
