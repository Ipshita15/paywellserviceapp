package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
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
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudwell.paywell.services.BuildConfig;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.about.AboutActivity;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.chat.ChatActivity;
import com.cloudwell.paywell.services.activity.education.EducationMainActivity;
import com.cloudwell.paywell.services.activity.education.bbc.BBC_Main_Activity;
import com.cloudwell.paywell.services.activity.entertainment.EntertainmentMainActivity;
import com.cloudwell.paywell.services.activity.entertainment.bongo.BongoMainActivity;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity;
import com.cloudwell.paywell.services.activity.faq.PostLoginFAQActivity;
import com.cloudwell.paywell.services.activity.fee.FeeMainActivity;
import com.cloudwell.paywell.services.activity.healthInsurance.HealthInsuranceMainActivity;
import com.cloudwell.paywell.services.activity.home.HomeActivity;
import com.cloudwell.paywell.services.activity.location.model.CurrentLocationModel;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.activity.mfs.mycash.MYCashMainActivity;
import com.cloudwell.paywell.services.activity.myFavorite.MyFavoriteMenuActivity;
import com.cloudwell.paywell.services.activity.myFavorite.model.FavoriteMenu;
import com.cloudwell.paywell.services.activity.navigationdrawer.Constant;
import com.cloudwell.paywell.services.activity.navigationdrawer.NavMenuAdapter;
import com.cloudwell.paywell.services.activity.navigationdrawer.NavMenuModel;
import com.cloudwell.paywell.services.activity.navigationdrawer.SubTitle;
import com.cloudwell.paywell.services.activity.navigationdrawer.TitleMenu;
import com.cloudwell.paywell.services.activity.notification.allNotificaiton.NotificationAllActivity;
import com.cloudwell.paywell.services.activity.product.ProductMenuActivity;
import com.cloudwell.paywell.services.activity.product.ekShop.EKShopActivity;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.activity.refill.banktransfer.BankTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.card.CardTransferMainActivity;
import com.cloudwell.paywell.services.activity.refill.nagad.NagadMainActivity;
import com.cloudwell.paywell.services.activity.scan.DisplayQRCodeActivity;
import com.cloudwell.paywell.services.activity.settings.ChangePinActivity;
import com.cloudwell.paywell.services.activity.settings.HelpMainActivity;
import com.cloudwell.paywell.services.activity.settings.SettingsActivity;
import com.cloudwell.paywell.services.activity.statements.StatementMainActivity;
import com.cloudwell.paywell.services.activity.statements.ViewStatementActivity;
import com.cloudwell.paywell.services.activity.terms.TermsActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMenuActivity;
import com.cloudwell.paywell.services.activity.topup.brilliant.BrilliantTopupActivity;
import com.cloudwell.paywell.services.activity.topup.model.MobileOperator;
import com.cloudwell.paywell.services.activity.utility.AllUrl;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionMainActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeActivity;
import com.cloudwell.paywell.services.activity.utility.banglalion.BanglalionRechargeInquiryActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.DescoMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.DescoPrepaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.DescoPrepaidMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.dpdc.DPDCPostpaidBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASABillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.wasa.WASAMainActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.electricity.westzone.WZPDCLMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeeInquiryMainActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacFeePayActivity;
import com.cloudwell.paywell.services.activity.utility.ivac.IvacMainActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.KarnaphuliBillPayActivity;
import com.cloudwell.paywell.services.activity.utility.karnaphuli.KarnaphuliMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.PBMainActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.PBBillPayNewActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.BannerDetailsDialog;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.billStatus.PBBillStatusActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.MobileNumberChangeActivity;
import com.cloudwell.paywell.services.activity.utility.pallibidyut.registion.PBRegistrationActivity;
import com.cloudwell.paywell.services.adapter.HomeFavoriteAdapter;
import com.cloudwell.paywell.services.adapter.MainSliderAdapter;
import com.cloudwell.paywell.services.adapter.PicassoImageLoadingService;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.model.APIResBalanceCheck;
import com.cloudwell.paywell.services.app.model.RequestBalanceCheck;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.constant.AllConstant;
import com.cloudwell.paywell.services.database.DatabaseClient;
import com.cloudwell.paywell.services.database.FavoriteMenuDab;
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.recentList.MyRecyclerViewAdapterRecentUsed;
import com.cloudwell.paywell.services.recentList.model.RecentUsedMenu;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.service.notificaiton.model.EventNewNotificaiton;
import com.cloudwell.paywell.services.utils.AppHelper;
import com.cloudwell.paywell.services.utils.AppTestVersionUtility;
import com.cloudwell.paywell.services.utils.AppsStatusConstant;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.CountryUtility;
import com.cloudwell.paywell.services.utils.LocationUtility;
import com.cloudwell.paywell.services.utils.ResorceHelper;
import com.cloudwell.paywell.services.utils.UpdateChecker;
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.orhanobut.logger.Logger;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.Slider;
import ss.com.bannerslider.event.OnSlideClickListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_BANGLA;
import static com.cloudwell.paywell.services.utils.LanuageConstant.KEY_ENGLISH;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener, View.OnClickListener, NavMenuAdapter.MenuItemClickListener {

    public static String KEY_COMMING_NEW_NOTIFICATION = "COMMING_NEW_NOTIFICATION";
    private static final long KEY_BALANCE_CHECK_INTERVAL = 5000;
    private boolean doubleBackToExitPressedOnce = false;
    public CoordinatorLayout mCoordinateLayout;
    private LinearLayout linearLayoutRecentUsedList;
    private AppHandler mAppHandler;
    private UpdateChecker mUpdateChecker;
    public final long UPDATE_SOFTWARE_INTERVAL = 24 * 60 * 60;// 1 day
    public final long PHN_NUM_CHECK_INTERVAL = 24 * 60 * 60;// 1 day
    public final long UPDATE_LOCATION_INTERVAL = 24 * 60 * 60;// 1 day
    private TextView mToolbarHeading;
    DotProgressBar pb_dot;
    private ConnectionDetector mCd;
    public int mNumOfNotification;
    private Toolbar mToolbar;
    private boolean mIsNotificationShown;
    private final String TAG_RESPONSE_STATUS = "status";
    private final String TAG_RESPONSE_MESSAGE = "message";
    private NavigationView navigationView;
    boolean checkNotificationFlag = false;

    AlertDialog.Builder builderNotification;
    private AlertDialog alertNotification;
    private Slider viewPager;
    private int currentPage;

    private Button home_topup, home_utility, home_eticket, home_mfs, home_product_catalog, home_statement, home_refill_balance, home_settings, home_Eduction;

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


    private int downloadType = 0;
    private int TAG_DOWNLOAD_PLAY_STORE = 1;

    // all async
    private AsyncTask<String, Intent, String> mNotificationAsync;
    private AsyncTask<String, Integer, String> mRequestPhnNumberAddAsync;
    private AsyncTask<String, Integer, String> mConfirmPhnNumberAddAsync;
    private AsyncTask<String, Intent, String> mPushFirebaseIdTask;

    // hidden balance menu
    boolean isBalaceCheckProcessRunning;
    boolean isBalaceBoxOpen = true;
    ImageView ivBalanceBorder;
    ScreenStateReceiver mReceiver;
    //

    int start = 9;
    int end = 18;
    long startHourMilli, endHourMilli;
    Calendar cal;

    private int lastFavoitePosition = 0;
    DrawerLayout drawer;
    ImageView ivRightSliderUpDown;
    ObjectAnimator animation;
    private BottomSheetBehavior sheetBehavior;
    LinearLayout layoutBottomSheet;

    private ImageView notif_bell;


    ArrayList<NavMenuModel> menu;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatusBarColor();

        changeAppThemeForNoActionBar();

        setContentView(R.layout.activity_main);

        PayWellShortcutManager.Companion.enableShortcutList(this);

        String bd = CountryUtility.getCountryCode("BD");

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        mToolbarHeading = findViewById(R.id.txtHeading);
        mCoordinateLayout = findViewById(R.id.coordinateLayout);
        pb_dot = findViewById(R.id.dot_progress_bar);
        linearLayoutRecentUsedList  = findViewById(R.id.linearLayoutRecentUsedList);

        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

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

        notif_bell = findViewById(R.id.notif_bell);


        InitializeData();

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_DASHBOARD);

        checkBalance();
        getAllFavoriteDate();
        setupRightNagivationView();
        setScreenStateReciver();
        setUpBottomSheet();
        setNavigationDrawerMenu();
        setUserRID();

        // newVersionShowcase();


    }

    private void newVersionShowcase() {
//        new MaterialShowcaseView.Builder(this)
//                .setTarget(home_Eduction)
//                .setDismissText("GOT IT")
//                .setContentText("This is some amazing feature you should know about")
//                .show();

        // sequence example
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500); // half second between each showcase view

        MaterialShowcaseSequence sequence = new MaterialShowcaseSequence(this, "" + home_Eduction.getId());

        sequence.setConfig(config);

        sequence.addSequenceItem(home_Eduction,
                "This is button one", "GOT IT");

        sequence.addSequenceItem(home_settings,
                "This is button two", "GOT IT");


        sequence.start();


    }

    private void setUserRID() {
        TextView _pwId = findViewById(R.id.pwId);
        assert _pwId != null;

        try {
            String rid = getString(R.string.rid) + mAppHandler.getRID();
            if (BuildConfig.DEBUG) {
                rid = rid + "(" + AppTestVersionUtility.testVersion + ")";
            }
            _pwId.setText(rid);
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
    }

    private void setRecentList() {


        final FavoriteMenuDab favoriteMenuDab = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().mFavoriteMenuDab();
        favoriteMenuDab.getAllRecentUsedMenu().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<RecentUsedMenu>>() {
                    @Override
                    public void accept(List<RecentUsedMenu> favoriteMenus) throws Exception {
                        generatedRecentUsedRecycView(favoriteMenus);
                    }
                });




    }

    private void generatedRecentUsedRecycView(List<RecentUsedMenu> favoriteMenus) {


        if(favoriteMenus.size() == 0){
            linearLayoutRecentUsedList.setVisibility(View.GONE);
        }else {
            linearLayoutRecentUsedList.setVisibility(View.VISIBLE);
        }


        boolean isEnglish = mAppHandler.getAppLanguage().equalsIgnoreCase("en");


        List<Object> objects = Arrays.asList(RecentUsedStackSet.getInstance().getAll());
        List<MobileOperator> TEMP  = (List<MobileOperator>)(Object) objects;
        ArrayList<MobileOperator> mobileOperatorArrayList = new ArrayList<>(TEMP);


        // new recycle view
        final RecyclerView recyclerView = findViewById(R.id.rvRecent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        recyclerView.getLayoutManager().setMeasurementCacheEnabled(false);
        MyRecyclerViewAdapterRecentUsed adapter = new MyRecyclerViewAdapterRecentUsed(this, favoriteMenus,isEnglish);


        adapter.setClickListener(new MyRecyclerViewAdapterRecentUsed.ItemClickListener() {
            @Override
            public void onItemClick(int position, RecentUsedMenu favoriteMenu) {
                onRecentUsedItemClick(favoriteMenu);
               // lastFavoitePosition = position;
            }
        });



        recyclerView.setAdapter(adapter);
    }



    private void setUpBottomSheet() {

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        /**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * */
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
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
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }

    private void setScreenStateReciver() {
        mReceiver = new ScreenStateReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalApplicationBus.getBus().register(this);

        refreshStringsOfButton();
        if (alertNotification.isShowing()) {
            alertNotification.dismiss();
        }

        viewPager.setInterval(2000);
        updateMyFavorityView();

        mNumOfNotification = 0;

        AppHelper.notificationCounterCheck(mCd, getApplicationContext());

        startRightLeftAnimation();

        isBalaceBoxOpen = true;


        mToolbarHeading.setText(getString(R.string.balance_pre_text));

        setRecentList();

    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager.setInterval(0);
        GlobalApplicationBus.getBus().unregister(this);

    }


    @Override
    public void onStop() {
        super.onStop();
        stopRightLefAnimation();
        isBalaceBoxOpen = false;
    }

    @Override
    protected void onDestroy() {
        Logger.v("onDestroy");
        if (mNotificationAsync != null) {
            mNotificationAsync.cancel(true);
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

        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        super.onDestroy();
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = getResources().getColor(R.color.colorPrimaryDark);
            window.setStatusBarColor(color);
        }
    }

    private void setupRightNagivationView() {
        NavigationView navigationView = findViewById(R.id.nav_view_right);
        navigationView.setNavigationItemSelectedListener(this);


        ivRightSliderUpDown = findViewById(R.id.ivRightSliderUpDown);
        ivRightSliderUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);

            }
        });

        ImageView ivEdit = findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyFavoriteMenuActivity();

            }
        });

    }

    private void hiddenFavoriteRecycview() {

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

        RecyclerView recyclerViewFavoirte = findViewById(R.id.rvOperatorList);
        recyclerViewFavoirte.setHasFixedSize(true);
        recyclerViewFavoirte.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewFavoirte.setLayoutManager(horizontalLayoutManager);
        recyclerViewFavoirte.getLayoutManager().setMeasurementCacheEnabled(false);


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


    }

    private void updateMyFavorityView() {
        getAllFavoriteDate();
        PayWellShortcutManager.Companion.enableShortcutList(this);

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
        home_Eduction = findViewById(R.id.homeBtnEduction);



        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.setDrawerListener(toggle);
        mToolbar.setNavigationIcon(R.drawable.ic_nav_back);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);


        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            switchToCzLocale(new Locale(KEY_BANGLA, ""));
        } else {
            switchToCzLocale(new Locale(KEY_ENGLISH, ""));
        }

         mToolbarHeading.setText(mAppHandler.getPwBalance() + getString(R.string.tk));
        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            mToolbarHeading.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            mToolbarHeading.setTypeface(AppController.getInstance().getAponaLohitFont());
        }
        mToolbarHeading.setText(getString(R.string.balance_pre_text));
        mToolbarHeading.setOnClickListener(this);

        setAutomaticDatetime();
        checkSoftwareUpdate();

        /*Location Change Log*/
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getLocationUpdateCheck() + UPDATE_LOCATION_INTERVAL)
                && (System.currentTimeMillis() / 1000) >= startHourMilli
                && (System.currentTimeMillis() / 1000) <= endHourMilli) {
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

        if (mAppHandler.getMerchantTypeVerificationStatus().equalsIgnoreCase("false")) {

            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, MerchantTypeVerify.class));
            }

        }

//        else  if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("false")){
//            Intent intent = new Intent(MainActivity.this, ChangePinActivity.class);
//            intent.putExtra("isFirstTime", true);
//            startActivity(intent);
//            finish();
//        }


//      Handle possible data accompanying notification message.
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);
                if (key.equals("Notification") && value.equals("True")) {
                    // mNotificationAsync = new NotificationAsync().execute(getResources().getString(R.string.notif_url));
                }
            }
        }
       // subscribeToPushService();
        initializePreview();

        //Tutorial
        showTutorial();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
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

    public void refreshStringsOfButton() {
        home_topup.setText(R.string.home_topup);
        home_utility.setText(R.string.home_utility);
        home_eticket.setText(R.string.home_eticket);
        home_mfs.setText(R.string.home_mfs);
        home_product_catalog.setText(R.string.home_product_catalog);
        home_statement.setText(R.string.home_statement);
        home_refill_balance.setText(R.string.home_refill_balance);
        home_settings.setText(R.string.home_settings);
        home_Eduction.setText(R.string.home_education);


//        navigationView.getMenu().findItem(R.id.nav_topup).setTitle(R.string.home_topup);
//        navigationView.getMenu().findItem(R.id.nav_utility).setTitle(R.string.home_utility);
//        navigationView.getMenu().findItem(R.id.nav_eticket).setTitle(R.string.home_eticket);
//        navigationView.getMenu().findItem(R.id.nav_mfs).setTitle(R.string.nav_mfs);
//        navigationView.getMenu().findItem(R.id.nav_product).setTitle(R.string.nav_product_catalog);
//        navigationView.getMenu().findItem(R.id.nav_more).setTitle(R.string.nav_more_title);
//        navigationView.getMenu().findItem(R.id.nav_check_balance).setTitle(R.string.nav_check_balance);
//        navigationView.getMenu().findItem(R.id.nav_terms).setTitle(R.string.nav_terms_and_conditions);
//        navigationView.getMenu().findItem(R.id.nav_policy).setTitle(R.string.nav_policy_statement);
//        navigationView.getMenu().findItem(R.id.nav_about).setTitle(R.string.nav_about);
    }

    @Override
    @SuppressLint("NewApi")
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbar.inflateMenu(R.menu.main);
        Menu _menu = mToolbar.getMenu();



        MenuItem menuNotification = _menu.findItem(R.id.menu_notification);
        View notificationView = MenuItemCompat.getActionView(menuNotification);
        notif_bell = notificationView.findViewById(R.id.notif_bell);

        //Scanner
        MenuItem menuScan = _menu.findItem(R.id.menu_scanner);

        if (!mIsNotificationShown) {
            if (!mCd.isConnectingToInternet())
                AppHandler.showDialog(getSupportFragmentManager());
//            mNotificationAsync = new NotificationAsync().execute(getResources().getString(R.string.notif_url));
        }
        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NOTIFICATION_ICON);
                if (mNumOfNotification != 0) {
                    mNumOfNotification = 0;
                    Intent intent = new Intent(MainActivity.this, NotificationAllActivity.class);
                    intent.putExtra(KEY_COMMING_NEW_NOTIFICATION, true);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(MainActivity.this, NotificationAllActivity.class));
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

        return true;
    }

    public void notificationCount(final int newNotification) {
        mNumOfNotification = newNotification;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (newNotification == 0) {
                    notif_bell.setImageDrawable(getResources().getDrawable(R.drawable.ic_bell));
                } else {
                    notif_bell.setImageDrawable(getResources().getDrawable(R.drawable.group_180));
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
                    startActivity(new Intent(this, TopupMenuActivity.class));
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
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AllUrl.privacy));
            startActivity(browserIntent);
        } else if (id == R.id.nav_about) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_ABOUT_MENU);
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        } else if (id == R.id.nav_logout) {
            logout();


        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_LOGOUT);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        AppHandler.getmInstance(getApplicationContext()).setAppStatus(AppsStatusConstant.KEY_logout);

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.logout_message)).setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtHeading:
                Log.e("check balance", "check balance");
                if (!isBalaceCheckProcessRunning) {
                    checkPayWellBalance();
                }

                break;
        }
    }

    private void initializePreview() {

        String[] imageUrl = new Gson().fromJson(mAppHandler.getImageAddress(), String[].class);
//        for (int len = 0; len < mAppHandler.getDisplayPictureCount(); len++) {
//            imageUrl[len] = AllUrl.len + len + ".jpg";
////                        imageUrl[len] = "https://api.paywellonline.com/retailerPromotionImage/Banner_Bus.9.png";
//        }

//        Log.e("ipshi", "" + imageUrl.length);
        String imageUpdateVersionString = mAppHandler.getDisplayPictureCount() + mAppHandler.getImageAddress();

        Slider.init(new PicassoImageLoadingService(imageUpdateVersionString));

        viewPager.setAdapter(new MainSliderAdapter(getApplicationContext(), imageUrl));
        viewPager.setInterval(2000);
        viewPager.hideIndicators();
        viewPager.setLoopSlides(true);


        String[] bannerDetails = new Gson().fromJson(mAppHandler.getBannerDetails(), String[].class);

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

                    String bannerDetail = bannerDetails[position];
                    String image = imageUrl[position];

                    if (!bannerDetail.equals("")){
                        showBannerDetails(image, bannerDetail);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showBannerDetails(String image, String bannerDetail) {

        BannerDetailsDialog bannerDetailsDialog = new BannerDetailsDialog(image, bannerDetail);
        bannerDetailsDialog.show(getSupportFragmentManager(), "bannerDetailsDialog");

    }

    private void checkPayWellBalance() {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_BALANCE_CHECK);
        checkBalance();
    }

    private void checkBalance() {

        isBalaceCheckProcessRunning = true;
        pb_dot.setVisibility(View.VISIBLE);
        mToolbarHeading.setVisibility(View.INVISIBLE);

        String userName = mAppHandler.getUserName();

        RequestBalanceCheck m = new RequestBalanceCheck();
        m.setUsername(userName);
        m.setDeviceId(mAppHandler.getAndroidID());


        Call<APIResBalanceCheck> responseBodyCall = ApiUtils.getAPIServiceV2().callCheckBalance(m);
        responseBodyCall.enqueue(new Callback<APIResBalanceCheck>() {
            @Override
            public void onResponse(Call<APIResBalanceCheck> call, Response<APIResBalanceCheck> response) {
                try {
                    pb_dot.setVisibility(View.INVISIBLE);
                    mToolbarHeading.setVisibility(View.VISIBLE);
                    isBalaceCheckProcessRunning = false;

                    if (response.isSuccessful()) {
                        if (((response.body() != null) ? response.body().getStatus() : null) != 200) {

                            showSnackMessageWithTextMessage(getString(R.string.try_again_msg));

                        } else {
                            String balance = Objects.requireNonNull(response.body()).getBalanceData().getBalance();

                            BigDecimal a = new BigDecimal(balance);
                            BigDecimal roundOff = a.setScale(2, BigDecimal.ROUND_HALF_EVEN);


                            mAppHandler.setPWBalance("" + roundOff);

                            String strBalance = mAppHandler.getPwBalance();
                            mToolbarHeading.setText(strBalance);

                        }

                    } else {

                        showSnackMessageWithTextMessage(getString(R.string.try_again_msg));

                    }
                } catch (Exception ignored) {
                    showSnackMessageWithTextMessage(getString(R.string.try_again_msg));
                }

            }

            @Override
            public void onFailure(Call<APIResBalanceCheck> call, Throwable t) {
                pb_dot.setVisibility(View.INVISIBLE);
                mToolbarHeading.setVisibility(View.VISIBLE);
                isBalaceCheckProcessRunning = false;


                showSnackMessageWithTextMessage(getString(R.string.try_again_msg));


            }
        });
    }

    private void startHiddenBalance() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Logger.v("Hidden Balance box");
                mToolbarHeading.setText(getString(R.string.balance_pre_text));

            }
        }, KEY_BALANCE_CHECK_INTERVAL);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_FOR_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    if (getMailAddress()) {
                        mUpdateChecker.launchMarketDetails();
                    } else {
                        mUpdateChecker.downloadAndInstall(AllUrl.URL_update_check);
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

                    startActivity(new Intent(MainActivity.this, DisplayQRCodeActivity.class));
                    finish();

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
                mUpdateChecker.checkForUpdateByVersionName(AllUrl.URL_check_version);
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
                mUpdateChecker.downloadAndInstall(AllUrl.URL_update_check);
            }
        }
    }

    public void onButtonClicker(View v) {
        switch (v.getId()) {
            case R.id.homeBtnTopup:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_TOPUP_MENU);
                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(this, TopupMenuActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnUtility:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_UTILITY_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(this, UtilityMainActivity.class));
                } else {
                    showNotAllowMessage();
                }


                break;
            case R.id.homeBtnEticket:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_ETICKET_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(this, ETicketMainActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnMFS:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_MFS_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(MainActivity.this, MFSMainActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnProductCategory:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_PRODUCT_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(MainActivity.this, ProductMenuActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnMiniStatement:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_STATEMENT_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(MainActivity.this, StatementMainActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnRefillBalance:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_BALANCE_REFILL_MENU);

                if (mAppHandler.getInitialChangePinStatus().equalsIgnoreCase("true")) {
                    startActivity(new Intent(this, RefillBalanceMainActivity.class));
                } else {
                    showNotAllowMessage();
                }

                break;
            case R.id.homeBtnSettings:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_SETTINGS_MENU);
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                break;
            case R.id.homeBtnMessage:
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                break;

            case R.id.llChat:
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                break;

            case R.id.homeBtnCall:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_CALL_MENU);
                callPreview(false, "");
                break;

            case R.id.LLCall:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_CALL_MENU);
                callPreview(false, "");
                break;


            case R.id.homeBtnEduction:

                startActivity(new Intent(this, EducationMainActivity.class));

                break;


            case R.id.homeBtnFee:

                startActivity(new Intent(this, FeeMainActivity.class));

                break;


            case R.id.homeBtnInsurance:

//                CommingSoonDialog commingSoonDialog = new CommingSoonDialog();
//                commingSoonDialog.show(getSupportFragmentManager(), "commingSoonDialog");
                startActivity(new Intent(getApplicationContext(), HealthInsuranceMainActivity.class));


                break;

            case R.id.homeBtnEntertainment:

                startActivity(new Intent(this, EntertainmentMainActivity.class));

                break;

            case R.id.homeBtnFaq:

                startFAQActiivty();

                break;

            default:
                break;
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

    private void showNotAllowMessage() {
        Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.allow_error_msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
        snackbar.show();
    }


    private void startRightLeftAnimation() {
        if (animation == null) {
            animation = ObjectAnimator.ofFloat(ivRightSliderUpDown, "translationX", -16f);
            animation.setDuration(800);
            animation.setRepeatCount(ObjectAnimator.INFINITE);
            animation.setRepeatMode(ObjectAnimator.REVERSE);
        }
        animation.start();
    }

    private void stopRightLefAnimation() {
        animation.cancel();
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


    @Subscribe
    public void onNewnotificationcomming(EventNewNotificaiton eventNewNotificaiton) {
        int counter = eventNewNotificaiton.getCounter();
        mNumOfNotification = counter;
        if (mNumOfNotification > 0) {
            notificationCount(counter);
        } else {
            notificationCount(counter);
        }

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
        return gmail.length() > 0;
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
                pushLocationLonLat();
            }
        }
    }

    private void pushLocationLonLat(){
        CurrentLocationModel currentLocationModel = new CurrentLocationModel();
        currentLocationModel.setUsername(mAppHandler.getUserName());
        currentLocationModel.setLatitude(mAppHandler.getLatitude());
        currentLocationModel.setLongitude(mAppHandler.getLongitude());
        currentLocationModel.setAccuracy(mAppHandler.getAccuracy());
        currentLocationModel.setCountry(mAppHandler.getCountry());
        currentLocationModel.setAddress(mAppHandler.getAddress());


        ApiUtils.getAPIServiceV2().updateCurrentLocation(currentLocationModel).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200){

                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        int status = object.getInt(TAG_RESPONSE_STATUS);
                        if (status == 200){
                            mAppHandler.setLocationUpdateCheck(System.currentTimeMillis() / 1000);
                        }else {
                            showErrorMessagev1(getString(R.string.try_again_msg));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        showErrorMessagev1(getString(R.string.try_again_msg));
                    }


                }else {
                    showErrorMessagev1(getString(R.string.conn_timeout_msg));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showErrorMessagev1(getString(R.string.conn_timeout_msg));
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
        String FACEBOOK_URL = AllUrl.FACEBOOK_URL;
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
        super.onActivityResult(requestCode, resultCode, data);
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

        drawer.closeDrawer(GravityCompat.END);



        int resId = ResorceHelper.getResId(favoriteMenu.getName(), R.string.class);

        handleFavoriteAndRecientListOnclicl(resId);


    }


    private void onRecentUsedItemClick(RecentUsedMenu favoriteMenu) {
        drawer.closeDrawer(GravityCompat.END);


        int resId = ResorceHelper.getResId(favoriteMenu.getName(), R.string.class);

        handleFavoriteAndRecientListOnclicl(resId);

    }

    private void handleFavoriteAndRecientListOnclicl(int resId) {
        Intent intent;
        switch (resId) {

            case R.string.mobileOperator:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_TOPUP_ALL_OPERATOR_MENU);

                intent = new Intent(getApplicationContext(), TopupMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.brilliant:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_TOPUP_BRILLIANT_MENU);

                intent = new Intent(getApplicationContext(), BrilliantTopupActivity.class);
                startActivityWithFlag(intent);
                break;
            case R.string.home_utility_desco:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_MENU);

                intent = new Intent(getApplicationContext(), DescoMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.desco_postpaid_string:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY);

                intent = new Intent(getApplicationContext(), DESCOPostpaidBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.desco_postpaid_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_BILL_PAY_INQUIRY);

                intent = new Intent(getApplicationContext(), DESCOPostpaidMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DESCO_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_desco_prepaid_title:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DESCO_PRE_PAID_BILL_PAY);

                intent = new Intent(getApplicationContext(), DescoPrepaidBillPayActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DESCO_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_desco_prepaid_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.UtilityDescoPrepaidBillInquiry);

                intent = new Intent(getApplicationContext(), DescoPrepaidMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DESCO_INQUERY, true);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_dpdc:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_MENU);

                intent = new Intent(getApplicationContext(), DPDCMainActivity.class);
                startActivityWithFlag(intent);
                break;
            case R.string.home_utility_dpdc_bill_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY);

                intent = new Intent(getApplicationContext(), DPDCPostpaidBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_dpdc_bill_pay_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_DPDC_BILL_PAY_INQUIRY);

                intent = new Intent(getApplicationContext(), DPDCMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_DPDC_INQUERY, true);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_pollibiddut:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MENU);

                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_registion:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION);

                intent = new Intent(getApplicationContext(), PBRegistrationActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_bill_pay_favorite:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_PAY);

                intent = new Intent(getApplicationContext(), PBBillPayNewActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_reg_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_REGISTION_INQUIRY);

                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_RG_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pollibiddut_bill_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_INQUIRY);


                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_BILL_INQUERY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pb_request_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_STATUS);


                intent = new Intent(getApplicationContext(), PBBillStatusActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pb_bill_statu_inquery:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_BILL_STATUS_INQUIRY);

                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_REQUEST_BILL_INQUIRY, true);
                startActivityWithFlag(intent);
                break;
            case R.string.home_utility_pb_bill_change_number:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE);

                intent = new Intent(getApplicationContext(), MobileNumberChangeActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_pb_phone_number_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE_INQUIRY);

                intent = new Intent(getApplicationContext(), PBMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_PB_MOBILE_NUMBER_CHANGE_INQUIRY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_MENU);

                intent = new Intent(getApplicationContext(), WASAMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_BILL_PAY);

                intent = new Intent(getApplicationContext(), WASABillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_wasa_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WASA_BILL_PAY_INQUIRY);

                intent = new Intent(getApplicationContext(), WASAMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_WASA_BILL_INQUIRY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_west_zone:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_MENU);

                intent = new Intent(getApplicationContext(), WZPDCLMainActivity.class);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_west_zone_pay:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_BILL_PAY);

                intent = new Intent(getApplicationContext(), WZPDCLBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_west_zone_pay_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_WZPDCL_BILL_INQUIRY);

                intent = new Intent(getApplicationContext(), WZPDCLMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AND_WEST_ZONE_BILL_INQUIRY, true);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_ivac:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_MENU);

                intent = new Intent(getApplicationContext(), IvacMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_ivac_free_pay_favorite:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_BILL_PAY);

                intent = new Intent(getApplicationContext(), IvacFeePayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_ivac_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_IVAC_BILL_INQUIRY);

                intent = new Intent(getApplicationContext(), IvacFeeInquiryMainActivity.class);
                startActivityWithFlag(intent);
                break;


            case R.string.home_utility_banglalion:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_MENU);

                intent = new Intent(getApplicationContext(), BanglalionMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_banglalion_recharge:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE);

                intent = new Intent(getApplicationContext(), BanglalionRechargeActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_banglalion_recharge_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_BANGLALION_RECHARGE_INQUIRY);


                intent = new Intent(getApplicationContext(), BanglalionRechargeInquiryActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_karnaphuli:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU);

                intent = new Intent(getApplicationContext(), KarnaphuliMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_karnaphuli_bill_pay:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU_BILL_PAY);

                intent = new Intent(getApplicationContext(), KarnaphuliBillPayActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_utility_karnaphuli_inquiry:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_UTILITY_KARNAPHULI_MENU_BILL_PAY_INQUIRY);

                intent = new Intent(getApplicationContext(), KarnaphuliMainActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_KARACHI_INQUIRY, true);
                startActivityWithFlag(intent);
                break;

            case R.string.home_eticket_bus:

                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW, true);

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_ETICKET_BUS);


                intent = new Intent(getApplicationContext(), BusTicketMenuActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_BUS, true);
                startActivityWithFlag(intent);
                break;

            case R.string.launch:
                AppStorageBox.put(getApplicationContext(), AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW, false);

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_ETICKET_LUUNCH);

                intent = new Intent(getApplicationContext(), BusTicketMenuActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_BUS, true);
                startActivityWithFlag(intent);
                break;


            case R.string.home_eticket_air:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_ETICKET_AIR);

                intent = new Intent(getApplicationContext(), AirTicketMenuActivity.class);
                intent.putExtra(AllConstant.IS_FLOW_FROM_FAVORITE_AIR, true);
                startActivityWithFlag(intent);
                break;


            case R.string.home_mfs_mycash:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_MFS_MYCASH_MENU);

                intent = new Intent(getApplicationContext(), MYCashMainActivity.class);
                startActivityWithFlag(intent);
                break;

            case R.string.home_product_ekshop:

                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_PRODUCT_EK_SHOP);
                startActivity(new Intent(this, EKShopActivity.class));
                break;


            case R.string.home_statement_mini:


                openMiniStatment();

                break;
            case R.string.home_statement_balance:

                openBalanceStatment();

                break;


            case R.string.home_statement_sales:

                openSalesStatement();

                break;
            case R.string.home_statement_transaction:

                openTransactionStatement();

                break;

            case R.string.home_refill_bank:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_BANK);


                intent = new Intent(getApplicationContext(), BankTransferMainActivity.class);
                startActivityWithFlag(intent);

                break;

            case R.string.home_refill_card:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_CARD);

                intent = new Intent(getApplicationContext(), CardTransferMainActivity.class);
                startActivityWithFlag(intent);

            case R.string.nagad_refill_msg:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BALANCE_REFILL_NAGAD);

                intent = new Intent(getApplicationContext(), NagadMainActivity.class);
                startActivityWithFlag(intent);

                break;

            case R.string.bbc_janala:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_BBC);

                intent = new Intent(getApplicationContext(), BBC_Main_Activity.class);
                startActivityWithFlag(intent);

                break;


            case R.string.bongo_bd:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_Bongo);

                intent = new Intent(getApplicationContext(), BongoMainActivity.class);
                startActivityWithFlag(intent);

                break;


        }
    }

    private void openTransactionStatement() {
        Intent intent;
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_TRX_MENU);

        intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
        intent.putExtra(ViewStatementActivity.DESTINATION_TITLE, "trx");

        startActivityWithFlag(intent);
    }

    private void openSalesStatement() {
        Intent intent;
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_SALES_MENU);

        intent = new Intent(getApplicationContext(), ViewStatementActivity.class);

        intent.putExtra(ViewStatementActivity.DESTINATION_TITLE, "sales");


        startActivityWithFlag(intent);
    }

    private void openBalanceStatment() {
        Intent intent;
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_BALANCE_MENU);

        intent = new Intent(getApplicationContext(), ViewStatementActivity.class);

        intent.putExtra(ViewStatementActivity.DESTINATION_TITLE, "balance");


        startActivityWithFlag(intent);
    }

    private void openMiniStatment() {
        Intent intent;
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_FAVORITE_MENU, AnalyticsParameters.KEY_STATEMENT_MINI_MENU);
        intent = new Intent(getApplicationContext(), ViewStatementActivity.class);
        intent.putExtra(ViewStatementActivity.DESTINATION_TITLE, "mini");
        startActivityWithFlag(intent);
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

    public class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                Logger.v("On");
                checkBalance();
                //code
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                //code
                Logger.v("off");
            }
        }
    }

    private void setNavigationDrawerMenu() {
        NavMenuAdapter adapter = new NavMenuAdapter(this, getMenuList(), this);
        RecyclerView navMenuDrawer = (RecyclerView) findViewById(R.id.main_nav_menu_recyclerview);
        navMenuDrawer.setAdapter(adapter);
        navMenuDrawer.setLayoutManager(new LinearLayoutManager(this));
        navMenuDrawer.setAdapter(adapter);
//
////        INITIATE SELECT MENU
//        adapter.selectedItemParent = menu.get(0).menuTitle;
//        onMenuItemClick(adapter.selectedItemParent);
//        adapter.notifyDataSetChanged();
    }

    private List<TitleMenu> getMenuList() {
        List<TitleMenu> list = new ArrayList<>();

        menu = Constant.getMenuNavigasi(getApplicationContext());
        for (int i = 0; i < menu.size(); i++) {
            ArrayList<SubTitle> subMenu = new ArrayList<>();
            if (menu.get(i).subMenu.size() > 0) {
                for (int j = 0; j < menu.get(i).subMenu.size(); j++) {
                    subMenu.add(new SubTitle(menu.get(i).subMenu.get(j).subMenuTitle));
                }
            }

            list.add(new TitleMenu(menu.get(i).menuTitle, subMenu, menu.get(i).menuIconDrawable));
        }

        return list;
    }


    @Override
    public void onMenuItemClick(String itemString) {
        for (int i = 0; i < menu.size(); i++) {
            if (itemString.equals(menu.get(i).menuTitle)) {

                handleNavMemuClick(itemString);

                break;
            } else {
                for (int j = 0; j < menu.get(i).subMenu.size(); j++) {
                    if (itemString.equals(menu.get(i).subMenu.get(j).subMenuTitle)) {

                        Log.e("", "");

                        if (itemString.equals(Constant.KEY_home_statement_mini)) {
                            openMiniStatment();
                        } else if (itemString.equals(Constant.KEY_home_statement_balance)) {
                            openBalanceStatment();
                        } else if (itemString.equals(Constant.KEY_home_statement_sales)) {
                            openSalesStatement();
                        } else if (itemString.equals(Constant.KEY_home_statement_transaction)) {
                            openTransactionStatement();
                        } else if (itemString.equals(Constant.KEY_home_settings_change_pin)) {
                            openChangePinNumber();
                        } else if (itemString.equals(Constant.KEY_home_tutorial)) {
                            startActivity(new Intent(getApplicationContext(), HelpMainActivity.class));
                        }


                        break;
                    }
                }
            }
        }

        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void openChangePinNumber() {
        AnalyticsManager.sendEvent(AnalyticsParameters.KEY_SETTINGS_MENU, AnalyticsParameters.KEY_SETTINGS_CHANGE_PIN_MENU);
        startActivity(new Intent(this, ChangePinActivity.class));
    }

    private void handleNavMemuClick(String menuName) {
        if (menuName.equals(Constant.KEY_logout_text)) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_LOGOUT);
            logout();
        } else if (menuName.equals(Constant.KEY_about)) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_ABOUT_MENU);
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        } else if (menuName.equals(Constant.KEY_nav_terms_and_conditions)) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_TERMS_MENU);
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, TermsActivity.class));
            }
        } else if (menuName.equals(Constant.KEY_nav_policy_statement)) {
            AnalyticsManager.sendEvent(AnalyticsParameters.KEY_DASHBOARD, AnalyticsParameters.KEY_NAV_PRIVACY_MENU);
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(AllUrl.privacy));
                startActivity(browserIntent);
            }
        } else if (menuName.equals(Constant.KEY_faq_text)) {
            startFAQActiivty();

        } else if (menuName.equals(Constant.KEY_home_tutorial)) {

            startActivity(new Intent(getApplicationContext(), HelpMainActivity.class));

        }
    }

    private void startFAQActiivty() {
        Intent intent = new Intent(this, PostLoginFAQActivity.class);
        intent.putExtra("isAfterLogin", false);
        startActivity(intent);
    }


}
