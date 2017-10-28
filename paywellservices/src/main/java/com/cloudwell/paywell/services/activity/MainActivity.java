package com.cloudwell.paywell.services.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.about.AboutActivity;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.activity.mfs.MFSMainActivity;
import com.cloudwell.paywell.services.activity.notification.NotificationActivity;
import com.cloudwell.paywell.services.activity.notification.NotificationAllList;
import com.cloudwell.paywell.services.activity.payments.PaymentsMainActivity;
import com.cloudwell.paywell.services.activity.product.ProductMenuActivity;
import com.cloudwell.paywell.services.activity.refill.RefillBalanceMainActivity;
import com.cloudwell.paywell.services.activity.scan.QRCodeActivity;
import com.cloudwell.paywell.services.activity.settings.SettingsActivity;
import com.cloudwell.paywell.services.activity.statements.StatementMainActivity;
import com.cloudwell.paywell.services.activity.terms.TermsActivity;
import com.cloudwell.paywell.services.activity.topup.TopupMainActivity;
import com.cloudwell.paywell.services.activity.utility.UtilityMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.UpdateChecker;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.revesoft.revechatsdk.Utility.ReveChat;
import com.revesoft.revechatsdk.ui.ReveChatActivity;
import com.revesoft.revechatsdk.visitor.VisitorInfo;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private boolean doubleBackToExitPressedOnce = false;
    private CoordinatorLayout mCoordinateLayout;
    private AppHandler mAppHandler;
    private UpdateChecker mUpdateChecker;
    public static final long UPDATE_INTERVAL = 1 * 24 * 60 * 60;// 1 week
    private TextView mToolbarHeading;
    private ConnectionDetector mCd;
    public static int mNumOfNotification;
    private TextView mNotification = null;
    private Toolbar mToolbar;
    private boolean mIsNotificationShown;
    private static final String TAG_RESPONSE_STATUS = "status";
    private static final String TAG_RESPONSE_TOTAL_UREAD_MSG = "unread_message";
    private static final String TAG_RESPONSE_MSG_SUBJECT = "message_sub";
    private static final String TAG_RESPONSE_MSG_ID = "message_id";
    private static final String TAG_RESPONSE_MSG_ARRAY = "detail_message";
    private static final String TAG_RESPONSE_MESSAGE = "message";
    private static final String TAG_RESPONSE_DATE = "added_datetime";
    private static final String TAG_RESPONSE_IMAGE = "image_url";
    NavigationView navigationView;
    boolean checkNotificationFlag = false;
    PayWellBalanceAsync pwBalanceCheck;

    private Button home_topup, home_utility, home_payments, home_eticket, home_mfs, home_product_catalog, home_statement, home_refill_balance, home_settings;
    ImageView msgImageView;

    private static final int PERMISSIONS_FOR_QR_CODE_SCAN = 100;
    private static final int PERMISSIONS_FOR_QR_CODE_GENERATION = 101;
    private static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 102;

    public final static int QRCodeWidth = 500;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        mToolbarHeading = ((TextView) mToolbar.findViewById(R.id.txtHeading));
        mCoordinateLayout = (CoordinatorLayout) findViewById(R.id.coordinateLayout);

        mCd = new ConnectionDetector(getApplicationContext());
        mAppHandler = new AppHandler(this);

        /*Buttons Initialization*/
        home_topup = (Button) findViewById(R.id.homeBtnTopup);
        home_utility = (Button) findViewById(R.id.homeBtnUtility);
        home_payments = (Button) findViewById(R.id.homeBtnPayments);
        home_eticket = (Button) findViewById(R.id.homeBtnEticket);
        home_mfs = (Button) findViewById(R.id.homeBtnMFS);
        home_product_catalog = (Button) findViewById(R.id.homeBtnProductCategory);
        home_statement = (Button) findViewById(R.id.homeBtnMiniStatement);
        home_refill_balance = (Button) findViewById(R.id.homeBtnRefillBalance);
        home_settings = (Button) findViewById(R.id.homeBtnSettings);

        msgImageView = (ImageView) findViewById(R.id.homeBtnMessage);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer.setDrawerListener(toggle);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // key board back pressed
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

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            Glide.with(this)
                    .load(R.drawable.ic_home_chat_bn)
                    .asGif()
                    .into(msgImageView);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_home_chat_en)
                    .asGif()
                    .into(msgImageView);
        }
        mToolbarHeading.setText(getString(R.string.balance) + mAppHandler.getPwBalance() + getString(R.string.tk));
        mToolbarHeading.setTypeface(AppController.getInstance().getRobotoRegularFont());

        RefreshStringsOfButton();
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnTopup)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnUtility)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnPayments)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnEticket)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnMFS)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnProductCategory)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnMiniStatement)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnRefillBalance)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((Button) mCoordinateLayout.findViewById(R.id.homeBtnSettings)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        setAutomaticDatetime();
        checkUpdate();
        if (mAppHandler.getQrCodeImagePath().equalsIgnoreCase("unknown")) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // permission has not been granted.
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_FOR_QR_CODE_GENERATION);
            } else {
                generateQRCode();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPayWellBalance();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            Glide.with(this)
                    .load(R.drawable.ic_home_chat_bn)
                    .asGif()
                    .into(msgImageView);
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_home_chat_en)
                    .asGif()
                    .into(msgImageView);
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

        MenuItem menuNotification = _menu.findItem(R.id.menu_notification);
        View notificationView = MenuItemCompat.getActionView(menuNotification);

        //Scanner
        MenuItem menuScan = _menu.findItem(R.id.menu_scanner);

        mNotification = (TextView) notificationView.findViewById(R.id.tvNotification);
        mNotification.setVisibility(View.INVISIBLE);
        if (!mIsNotificationShown) {
            if (!mCd.isConnectingToInternet())
                AppHandler.showDialog(getSupportFragmentManager());
            new NotificationAsync().execute(getResources().getString(R.string.notif_url));
        }
        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (mNumOfNotification != 0) {
                    Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                    mNumOfNotification = 0;
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this, NotificationAllList.class));
                    finish();
                }
            }
        });

        menuScan.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return (scanOnClick());
            }
        });

        TextView _pwId = (TextView) findViewById(R.id.pwId);
        assert _pwId != null;
        String str_rid = getString(R.string.rid) + mAppHandler.getRID();
        _pwId.setText(str_rid);

        return true;
    }


    public boolean scanOnClick() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // permission has not been granted.
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_FOR_QR_CODE_SCAN);
        } else {
            startActivity(new Intent(MainActivity.this, QRCodeActivity.class));
            finish();
        }
        return true;
    }

    // call the updating code on the main thread,
    // so we can call this asynchronously
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
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                // Handle the topup action
                startActivity(new Intent(this, TopupMainActivity.class));
            }
        } else if (id == R.id.nav_utility) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                // Handle the utility action
                startActivity(new Intent(this, UtilityMainActivity.class));
            }
        } else if (id == R.id.nav_payments) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                // Handle the bKash action
                startActivity(new Intent(this, PaymentsMainActivity.class));
            }
        } else if (id == R.id.nav_eticket) {
            // Handle the eTicket action
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                // Handle the utility action
                startActivity(new Intent(this, ETicketMainActivity.class));
            }

        } else if (id == R.id.nav_mfs) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(this, MFSMainActivity.class));
            }
        } else if (id == R.id.nav_product) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                startActivity(new Intent(MainActivity.this, ProductMenuActivity.class));
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

                List<NameValuePair> nameValuePairs = new ArrayList<>(3);
                nameValuePairs.add(new BasicNameValuePair("username", mAppHandler.getImeiNo()));
                nameValuePairs.add(new BasicNameValuePair("mes_type", "all_message"));
                nameValuePairs.add(new BasicNameValuePair("message_status", "all"));
                nameValuePairs.add(new BasicNameValuePair("format", "json"));

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                notifications = httpClients.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return notifications;
        }

        @Override
        protected void onPostExecute(String result) {

            if (result != null) {
                NotificationActivity.mId = new String[result.length()];
                NotificationActivity.mTitle = new String[result.length()];
                NotificationActivity.mMsg = new String[result.length()];
                NotificationActivity.mDate = new String[result.length()];
                NotificationActivity.mImage = new String[result.length()];
                NotificationActivity.mStatus = new String[result.length()];
                NotificationActivity.length = 0;

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString(TAG_RESPONSE_STATUS);

                    if (status.equalsIgnoreCase("200")) {

                        String totalUnreadMsg = jsonObject.getString(TAG_RESPONSE_TOTAL_UREAD_MSG);
                        String totalMsg = jsonObject.getString("total_message");
                        NotificationActivity.length = Integer.parseInt(totalMsg);
                        mNumOfNotification = Integer.parseInt(totalUnreadMsg);

                        JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESPONSE_MSG_ARRAY);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String msg_id = object.getString(TAG_RESPONSE_MSG_ID);
                            String msg_status = object.getString(TAG_RESPONSE_STATUS);
                            String msg_title = object.getString(TAG_RESPONSE_MSG_SUBJECT);
                            String msg = object.getString(TAG_RESPONSE_MESSAGE);
                            String date = object.getString(TAG_RESPONSE_DATE);
                            String image;
                            if (!object.getString(TAG_RESPONSE_IMAGE).isEmpty()) {
                                image = object.getString(TAG_RESPONSE_IMAGE);
                            } else {
                                image = "empty";
                            }
                            NotificationActivity.mId[i] = msg_id;
                            NotificationActivity.mTitle[i] = msg_title;
                            NotificationActivity.mMsg[i] = msg;
                            NotificationActivity.mDate[i] = date;
                            NotificationActivity.mImage[i] = image;
                            NotificationActivity.mStatus[i] = msg_status;

                            if (checkNotificationFlag) {
                                checkNotificationFlag = false;
                                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                                finish();
                            }
                        }

                    } else {
                        mNumOfNotification = 0;
                    }

                    notificationCount(mNumOfNotification);

                } catch (Exception ex) {
                    ex.printStackTrace();
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
        pwBalanceCheck.execute(
                getResources().getString(R.string.pw_bal),
                "imei_no=" + mAppHandler.getImeiNo());
    }

    @SuppressWarnings("deprecation")
    private class PayWellBalanceAsync extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            mToolbarHeading.setText(R.string.balance_checking);
        }

        @Override
        protected String doInBackground(String... params) {

            HttpGet request = new HttpGet(params[0] + params[1]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseTxt = null;
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception ex) {
                ex.printStackTrace();
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
                            mToolbarHeading.setText(getString(R.string.balance) + mAppHandler.getPwBalance() + getString(R.string.tk));
                            if (Integer.parseInt(splitArray[4]) > 0) {
                                notificationCount(Integer.parseInt(splitArray[4]));
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(R.string.home_notification);
                                builder.setMessage(getString(R.string.unread_first_msg) + " "
                                        + splitArray[4] + " " + getString(R.string.unread_second_msg));
                                builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int id) {
                                        dialogInterface.dismiss();
                                        checkNotificationFlag = true;
                                        new NotificationAsync().execute(getResources().getString(R.string.notif_url));
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                                messageText.setGravity(Gravity.CENTER);
                            }
                        } else {
                            Snackbar snackbar = Snackbar.make(mCoordinateLayout, splitArray[1], Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.conn_timeout_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkUpdate() {
        if ((System.currentTimeMillis() / 1000) >= (mAppHandler.getUpdateCheck() + UPDATE_INTERVAL)) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                checkForUpdate();
            }
        }
    }

    private void checkForUpdate() {
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
                if (!mUpdateChecker.isUpdateAvailable()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.no_update_msg, Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
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
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
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
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
    }

    private void checkPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            mUpdateChecker.downloadAndInstall(getResources().getString(R.string.update_check));
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was grantedTask
                    mUpdateChecker.downloadAndInstall(getResources().getString(R.string.update_check));
                } else {
                    // permission denied
                    Snackbar snackbar = Snackbar.make(mCoordinateLayout, R.string.access_denied_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                    checkPermission();
                }
                return;
            }
            case PERMISSIONS_FOR_QR_CODE_SCAN: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    startActivity(new Intent(MainActivity.this, QRCodeActivity.class));
                    finish();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_FOR_QR_CODE_SCAN);
                }
                return;
            }
            case PERMISSIONS_FOR_QR_CODE_GENERATION: {
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Phone permission has been granted
                    generateQRCode();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_FOR_QR_CODE_GENERATION);
                }
                return;
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
                    startActivity(new Intent(this, TopupMainActivity.class));
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
                    startActivity(new Intent(this, UtilityMainActivity.class));
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
                    startActivity(new Intent(this, PaymentsMainActivity.class));
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
                    startActivity(new Intent(this, ETicketMainActivity.class));
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
                    startActivity(new Intent(MainActivity.this, MFSMainActivity.class));
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
                    startActivity(new Intent(MainActivity.this, ProductMenuActivity.class));
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
                    startActivity(new Intent(MainActivity.this, StatementMainActivity.class));
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
                    startActivity(new Intent(this, RefillBalanceMainActivity.class));
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
                    if (mAppHandler.getUserName().equalsIgnoreCase("unknown")) {
                        AskForInfo();
                    } else {
                        ReveChatWindow();
                    }
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

    private void AskForInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.chat_info_title_msg));
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 10, 20, 5);

        final EditText etUserName = new EditText(this);
        etUserName.setHint(getString(R.string.chat_info_username_msg));
        layout.addView(etUserName);

        final EditText etPhn = new EditText(this);
        etPhn.setHint(getString(R.string.phone_no_title_msg));
        etPhn.setRawInputType(InputType.TYPE_CLASS_NUMBER); //for decimal numbers
        layout.addView(etPhn);

        builder.setView(layout);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);

                if (etUserName.getText().toString().length() != 0 && etPhn.getText().toString().length() == 11) {
                    dialogInterface.dismiss();
                    String name = etUserName.getText().toString();
                    String phn = etPhn.getText().toString();
                    if (mCd.isConnectingToInternet()) {
                        mAppHandler.setUserName(name);
                        mAppHandler.setPhoneNumber(phn);
                        ReveChatWindow();
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
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void ReveChatWindow() {
        //Initializing with account id
        //ReveChat.init("account id");
        ReveChat.init("16057");
        //Creating visitor info
        VisitorInfo visitorInfo = new VisitorInfo.Builder()
                .name(mAppHandler.getUserName()).email(mAppHandler.getRID() + "@cloudwell.co")
                .phoneNumber(mAppHandler.getPhoneNumber()).build();

        //Set visitor info
        ReveChat.setVisitorInfo(visitorInfo);

        //Optional
        //If want to Receive push notification from Reve Chat.
        //Add your device token id(registration Id)
        //You also need to do step 4.
        //ReveChat.setDeviceTokenId("deviceTokenId");

        //starting chat window
        startActivity(new Intent(this, ReveChatActivity.class));
    }

    public void generateQRCode() {
        try {
            Bitmap bitmap = TextToImageEncode(mAppHandler.getRID());
            String path = saveToInternalStorage(bitmap);
            mAppHandler.setQrCodeImagePath(path);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRCodeWidth, QRCodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        ContextCompat.getColor(this, android.R.color.black) : ContextCompat.getColor(this, android.R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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

}
