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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.about.AboutActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.chat.ChatActivity;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.MYCashMainActivity;
import com.cloudwell.paywell.services.activity.myFavorite.MyFavoriteMenuActivity;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.notification.NotificationActivity;
import com.cloudwell.paywell.services.activity.notification.NotificationAllActivity;
import com.cloudwell.paywell.services.activity.product.ProductMenuActivity;
import com.cloudwell.paywell.services.activity.product.productHelper.ProductHelper;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity;
import com.cloudwell.paywell.services.activity.scan.DisplayQRCodeActivity;
import com.cloudwell.paywell.services.activity.settings.SettingsActivity;
import com.cloudwell.paywell.services.activity.statements.StatementMainActivity;
import com.cloudwell.paywell.services.activity.statements.ViewStatementActivity;
import com.cloudwell.paywell.services.activity.terms.TermsActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMenuActivity;
import com.cloudwell.paywell.services.activity.topup.brilliant.BrilliantTopupActivity;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionMainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeInquiryActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DESCOMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCPostpaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASABillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeeInquiryMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeePayActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBRegistrationActivity;
import com.cloudwell.paywell.services.adapter.HomeFavoriteAdapter;
import com.cloudwell.paywell.services.adapter.MainSliderAdapter;
import com.cloudwell.paywell.services.adapter.PicassoImageLoadingService;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.database.FavoriteMenuDab;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.LocationUtility;
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
import com.orhanobut.logger.Logger;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    private boolean doubleBackToExitPressedOnce = false;
    public CoordinatorLayout mCoordinateLayout;
    private AppHandler mAppHandler;
    private UpdateChecker mUpdateChecker;
    public final long UPDATE_SOFTWARE_INTERVAL = 24 * 60 * 60;// 1 day
    public final long PHN_NUM_CHECK_INTERVAL = 24 * 60 * 60;// 1 day
    public final long UPDATE_LOCATION_INTERVAL = 1 * 24 * 60 * 60;// 1 day
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
    private int currentPage;

    private Button home_topup, home_utility, home_eticket, home_mfs, home_product_catalog, home_statement, home_refill_balance, home_settings;

    private final int PERMISSIONS_FOR_QR_CODE_SCAN = 100;
    private final int PERMISSIONS_REQUEST_FOR_WRITE_EXTERNAL_STORAGE = 101;
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

    int start = 9;
    int end = 18;
    long startHourMilli, endHourMilli;
    Calendar cal;

    // bottom slider
    private BottomSheetBehavior sheetBehavior;
    private ConstraintLayout layoutBottomSheet;
    private RecyclerView recyclerViewFavoirte;
    private ImageView ivUptown, ivEdit;
    private boolean isSliderUp = false;
    private int lastFavoitePosition = 0;
    private boolean isFirstTime = true;

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

        cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, start);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        startHourMilli = cal.getTimeInMillis() / 1000;

        cal.set(Calendar.HOUR_OF_DAY, end);
        endHourMilli = cal.getTimeInMillis() / 1000;

        Log.e("logTag3", "true " + (System.currentTimeMillis() / 1000) + " " + startHourMilli + " " + endHourMilli);

        InitializeData();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_DASHBOARD);

        setupBottonSheetSlider();
        getAllFavoriteDate();
        hiddenFavoriteRecycview();
    }

    private void hiddenFavoriteRecycview() {

    }

    private void setupBottonSheetSlider() {
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        ivUptown = layoutBottomSheet.findViewById(R.id.ivImageUpAndDown);
        ivEdit = layoutBottomSheet.findViewById(R.id.ivEdit);

        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);


        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });


        ivUptown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSliderUp) {
                    isSliderUp = true;
                    recyclerViewFavoirte.setVisibility(View.VISIBLE);
                    ivEdit.setVisibility(View.VISIBLE);
                    ivUptown.setBackgroundResource(R.drawable.circle_angle_down);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    ivUptown.clearAnimation();


                } else {
                    isSliderUp = false;
                    recyclerViewFavoirte.setVisibility(View.GONE);
                    ivEdit.setVisibility(View.GONE);
                    ivUptown.setBackgroundResource(R.drawable.circle_angle_up);
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item);
                    ivUptown.setAnimation(animation);


                }
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_item);
        ivUptown.setAnimation(animation);


        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyFavoriteMenuActivity();

            }
        });


    }

    private void animateBottomSheetArrows(float slideOffset) {
        ivUptown.setRotation(slideOffset * 180);
    }

    private void getAllFavoriteDate() {

        final FavoriteMenuDab favoriteMenuDab = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mFavoriteMenuDab();
        favoriteMenuDab.getAllFavoriteMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<FavoriteMenu>>() {
                    @Override
                    public void accept(List<FavoriteMenu> favoriteMenus) throws Exception {

                        Collections.sort(favoriteMenus, new Comparator<FavoriteMenu>() {
                            @Override
                            public int compare(FavoriteMenu o1, FavoriteMenu o2) {
                                if (o1.getFavoriteListPosition() > o2.getFavoriteListPosition()) {
                                    return 0;
                                } else if (o1.getFavoriteListPosition() > o2.getFavoriteListPosition()) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            }
                        });

                        generatedFavaroitRecycView(favoriteMenus);

                    }
                });


    }

    private void generatedFavaroitRecycView(List<FavoriteMenu> result) {

        boolean isEnglish = mAppHandler.getAppLanguage().equalsIgnoreCase("en");

        recyclerViewFavoirte = layoutBottomSheet.findViewById(R.id.rvOperatorList);
        recyclerViewFavoirte.setHasFixedSize(true);
        recyclerViewFavoirte.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewFavoirte.setLayoutManager(horizontalLayoutManager);
        recyclerViewFavoirte.getLayoutManager().setMeasurementCacheEnabled(false);


        if (result.size() == 0) {
            result.add(new FavoriteMenu(R.string.add_fav, R.string.home_topup, R.drawable.add_fav_action, "", 0));
        }

        HomeFavoriteAdapter adapter = new HomeFavoriteAdapter(this, result, isEnglish);

        adapter.setClickListener(new HomeFavoriteAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, FavoriteMenu favoriteMenu) {
                onFavoriteItemClick(favoriteMenu);
                lastFavoitePosition = position;
            }
        });
        recyclerViewFavoirte.setAdapter(adapter);

        if (0 != lastFavoitePosition) {
            recyclerViewFavoirte.smoothScrollToPosition(lastFavoitePosition);

        }
        if (isFirstTime) {
            isFirstTime = false;
            recyclerViewFavoirte.setVisibility(View.GONE);
            ivEdit.setVisibility(View.GONE);
            ivUptown.setBackgroundResource(R.drawable.circle_angle_up);
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        // recyclerViewFavoirte.setVisibility(View.GONE);


    }

    private void updateMyFavorityView() {
        getAllFavoriteDate();
    }


    private void InitializeData() {
        /*Buttons Initialization*/
        home_topup = findViewById(R.id.homeBtnTopup);
        home_utility = findViewById(R.id.homeBtnUtility);

        home_eticket = findViewById(R.id.homeBtnEticket);
        home_mfs = findViewById(R.id.homeBtnMFS);
        home_product_catalog = findViewById(R.id.homeBtnProductCategory);
        home_statement = findViewById(R.id.homeBtnMiniStatement);
        home_refill_balance = findViewById(R.id.homeBtnRefillBalance);
        home_settings = findViewById(R.id.homeBtnSettings);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_topup.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_utility.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_eticket.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_mfs.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_product_catalog.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_statement.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_refill_balance.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_settings.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_topup.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_utility.setTypeface(AppController.getInstance().getAponaLohitFont());
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
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getLocationUpdateCheck() + UPDATE_LOCATION_INTERVAL)
                && (System.currentTimeMillis() / 1000) >= startHourMilli
                && (System.currentTimeMillis() / 1000) <= endHourMilli) {
            Log.e("logTag1", "true " + (System.currentTimeMillis() / 1000) + " " + startHourMilli + " " + endHourMilli);
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

        if (mAppHandler.getPhnNumberVerificationStatus().equalsIgnoreCase("false") || mAppHandler.getMerchantTypeVerificationStatus().equalsIgnoreCase("false")) {
            checkPhnNoUpdate();
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
        home_eticket.setText(R.string.home_eticket);
        home_mfs.setText(R.string.home_mfs);
        home_product_catalog.setText(R.string.home_product_catalog);
        home_statement.setText(R.string.home_statement);
        home_refill_balance.setText(R.string.home_refill_balance);
        home_settings.setText(R.string.home_settings);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            home_topup.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_utility.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_eticket.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_mfs.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_product_catalog.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_statement.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_refill_balance.setTypeface(AppController.getInstance().getOxygenLightFont());
            home_settings.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            home_topup.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_utility.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_eticket.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_mfs.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_product_catalog.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_statement.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_refill_balance.setTypeface(AppController.getInstance().getAponaLohitFont());
            home_settings.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        navigationView.getMenu().findItem(R.id.nav_topup).setTitle(R.string.home_topup);
        navigationView.getMenu().findItem(R.id.nav_utility).setTitle(R.string.home_utility);
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

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NOTIFICATION_ICON);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_QRCODE_ICON);
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
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation terms_and_conditions_format item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_topup) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_TOPUP_MENU);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_UTILITY_MENU);
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
        } else if (id == R.id.nav_eticket) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_ETICKET_MENU);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_MFS_MENU);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_PRODUCT_MENU);
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
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_BALANCE_CHECK_MENU);
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                checkPayWellBalance();
            }
        } else if (id == R.id.nav_terms) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_TERMS_MENU);
            startActivity(new Intent(MainActivity.this, TermsActivity.class));
        } else if (id == R.id.nav_policy) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_PRIVACY_MENU);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paywellonline.com/Privacy.php"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_about) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_ABOUT_MENU);
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
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_BALANCE_CHECK);
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
                                            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_CEHCK_NEW_NOTIFICATION);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_UPGRADE_PLAYSTORE_OPTION);
                dialog.dismiss();
                downloadType = TAG_DOWNLOAD_PLAY_STORE;
                checkPermission();
            }
        });
        builder.setNeutralButton(R.string.pw_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_UPGRADE_PAYWELL_OPTION);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_TOPUP_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_UTILITY_MENU);
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
            case R.id.homeBtnEticket:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_ETICKET_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_MFS_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_PRODUCT_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_STATEMENT_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_BALANCE_REFILL_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_SETTINGS_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_CHAT_MENU);
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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_CALL_MENU);
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


    private void startMyFavoriteMenuActivity() {
        Intent intent1 = new Intent(getApplicationContext(), MyFavoriteMenuActivity.class);
        startActivity(intent1);
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

        updateMyFavorityView();

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
            float PLACE_ACCURACY = location.getAccuracy();

            String address = LocationUtility.getAddress(getApplicationContext(), location.getLatitude(), location.getLongitude());
            String country = LocationUtility.getCountry(getApplicationContext(), location.getLatitude(), location.getLongitude());

            if (!mAppHandler.getLatitude().equalsIgnoreCase(String.valueOf(PLACE_LATITUDE))) {
                mAppHandler.setLatitude(String.valueOf(PLACE_LATITUDE));
            }
            if (!mAppHandler.getLongitude().equalsIgnoreCase(String.valueOf(PLACE_LONGITUDE))) {
                mAppHandler.setLongitude(String.valueOf(PLACE_LONGITUDE));
            }
            mAppHandler.setAccuracy(String.valueOf(location.getAccuracy()));
            mAppHandler.setCountry(country);
            mAppHandler.setAddress(address);

            if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getLocationUpdateCheck() + UPDATE_LOCATION_INTERVAL)
                    && (System.currentTimeMillis() / 1000) >= startHourMilli
                    && (System.currentTimeMillis() / 1000) <= endHourMilli) {
                Log.e("logTag2", "true " + (System.currentTimeMillis() / 1000) + " " + startHourMilli + " " + endHourMilli);
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

            builder.setAlwaysShow(true);

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
                nameValuePairs.add(new BasicNameValuePair("accuracy", mAppHandler.getAccuracy()));
                nameValuePairs.add(new BasicNameValuePair("country", mAppHandler.getCountry()));
                nameValuePairs.add(new BasicNameValuePair("address", mAppHandler.getAddress()));

                Logger.v("username " + mAppHandler.getImeiNo() +
                        " latitude " + mAppHandler.getLatitude() +
                        " longitude " + mAppHandler.getLongitude() +
                        " accuracy " + mAppHandler.getAccuracy() +
                        " country" + mAppHandler.getCountry() +
                        " address" + mAppHandler.getAddress()
                );


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
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_SLIDER_IMAGE);
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
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }

    private void onFavoriteItemClick(FavoriteMenu favoriteMenu) {
        Intent intent;

        switch (favoriteMenu.getName()) {
            case R.string.add_fav:
                startMyFavoriteMenuActivity();
                break;

            case R.string.mobileOperator:
                intent = new Intent(getApplicationContext(), TopupMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.brilliant:
                intent = new Intent(getApplicationContext(), BrilliantTopupActivity.class);
                startActivityWithFlag(intent);
                break;
            case R.string.home_utility_desco:
                intent = new Intent(getApplicationContext(), DESCOMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_desco_pay:
                intent = new Intent(getApplicationContext(), DESCOBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_dpdc:
                intent = new Intent(getApplicationContext(), DPDCMainActivity.class);
                startActivityWithFlag(intent);
                break;
            case R.string.home_utility_dpdc_bill_pay:
                intent = new Intent(getApplicationContext(), DPDCPostpaidBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut:
                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_registion:
                intent = new Intent(getApplicationContext(), PBRegistrationActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_reg_inquiry:
                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_RG_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_bill_inquiry:
                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_BILL_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa:
                intent = new Intent(getApplicationContext(), WASAMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa_pay:
                intent = new Intent(getApplicationContext(), WASABillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa_inquiry:
                intent = new Intent(getApplicationContext(), WASAMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_west_zone:
                intent = new Intent(getApplicationContext(), WZPDCLMainActivity.class);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_west_zone_pay:
                intent = new Intent(getApplicationContext(), WZPDCLBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_west_zone_pay_inquiry:
                intent = new Intent(getApplicationContext(), WZPDCLMainActivity.class);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_ivac:
                intent = new Intent(getApplicationContext(), IvacMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_ivac_free_pay_favorite:
                intent = new Intent(getApplicationContext(), IvacFeePayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_ivac_inquiry:
                intent = new Intent(getApplicationContext(), IvacFeeInquiryMainActivity.class);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_banglalion:
                intent = new Intent(getApplicationContext(), BanglalionMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_banglalion_recharge:
                intent = new Intent(getApplicationContext(), BanglalionRechargeActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_banglalion_recharge_inquiry:
                intent = new Intent(getApplicationContext(), BanglalionRechargeInquiryActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_mfs_mycash:
                intent = new Intent(getApplicationContext(), MYCashMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_product_ajker_deal:

                new ProductHelper().getToken(this, 1);

                break;

            case R.string.home_product_pw_wholesale:

                new ProductHelper().getToken(this, 2);

                break;

            case R.string.home_statement_mini:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_MINI_MENU);
                ViewStatementActivity.title = "mini";
                ViewStatementActivity.url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();

                intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
                startActivityWithFlag(intent);

                break;
            case R.string.home_statement_balance:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_BALANCE_MENU);
                ViewStatementActivity.title = "balance";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/balanceStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();

                intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
                startActivityWithFlag(intent);

                break;


            case R.string.home_statement_sales:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_SALES_MENU);
                ViewStatementActivity.title = "sales";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/salesStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();


                intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
                startActivityWithFlag(intent);

                break;
            case R.string.home_statement_transaction:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_TRX_MENU);
                ViewStatementActivity.title = "trx";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/getAllTransactionStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();

                intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
                startActivityWithFlag(intent);

                break;

            case R.string.home_refill_bank:

                intent = new Intent(getApplicationContext(), BankTransferMainActivity.class);
                startActivityWithFlag(intent);

                break;

            case R.string.home_refill_card:

                intent = new Intent(getApplicationContext(), CardTransferMainActivity.class);
                startActivityWithFlag(intent);

                break;

        }


    }

    private void startActivityWithFlag(Intent intent) {
        intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE, true);
        startActivity(intent);
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

    private int getHeightOfView(View contentview) {
        contentview.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //contentview.getMeasuredWidth();
        return contentview.getMeasuredHeight();
    }


}
