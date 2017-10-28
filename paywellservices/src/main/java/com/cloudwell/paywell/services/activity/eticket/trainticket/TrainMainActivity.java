package com.cloudwell.paywell.services.activity.eticket.trainticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.gnas.customcalendarview.CalendarListener;
import com.gnas.customcalendarview.CustomCalendarView;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TrainMainActivity extends AppCompatActivity implements View.OnClickListener, CalendarListener {

    private ConnectionDetector mCd;
    private RelativeLayout mRelativeLayout;
    private Button mBtnConfirmDate;
    private String mSelectedDate;
    private AppHandler mAppHandler;
    private boolean flag = false;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String date_full, selected_date_full;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_eticket_train);

        mCd = new ConnectionDetector(TrainMainActivity.this);
        mAppHandler = new AppHandler(TrainMainActivity.this);

        initViews();
    }

    @SuppressWarnings("deprecation")
    private void initViews() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        mBtnConfirmDate = (Button) findViewById(R.id.btnConfirmCalendarDate);
        mBtnConfirmDate.setOnClickListener(this);

        ((TextView) findViewById(R.id.tvJourneyDate)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnConfirmDate.setTypeface(AppController.getInstance().getRobotoRegularFont());

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")) {
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            flag = true;
        }
        //Initialize CustomCalendarView from layout
        CustomCalendarView calendarView = (CustomCalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the terms_and_conditions_format
        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        calendarView.setCalendarListener(this);

//        today = currentCalendar.get(Calendar.DATE);
//        month = currentCalendar.get(Calendar.MONTH);
//        month++;

        try {
            Date date_today = new Date();
            date_full = dateFormat.format(date_today);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onDateSelected(Date date) {
        selected_date_full = dateFormat.format(date);
        DateFormat df = new SimpleDateFormat("dd", Locale.ENGLISH);
        mSelectedDate = df.format(date);

        //DateFormat df_for_validate = new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.ENGLISH);
        //String validate_month = df_for_validate.format(date);
        //mSelectedMonth = validate_month.substring(5, 7);
    }

    @Override
    public void onMonthChanged(Date time) {

    }

    @SuppressWarnings("deprecation")
    @Override
    public void onClick(View v) {
        if (v == mBtnConfirmDate) {
            try {
                if (flag) {
                    Configuration config = new Configuration();
                    config.locale = Locale.FRANCE;
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                    if (mSelectedDate == null) {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.journey_date_select_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                        snackbar.show();
                    } else if (dateFormat.parse(selected_date_full).before(dateFormat.parse(date_full))) {
                        //if((Integer.parseInt(mSelectedDate) < today) && (Integer.parseInt(mSelectedMonth) < month)) {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.previous_journey_date_select_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                        snackbar.show();
                    } else {
                        if (!mCd.isConnectingToInternet()) {
                            AppHandler.showDialog(TrainMainActivity.this.getSupportFragmentManager());
                        } else {
                            flag = false;
                            new TrainMainActivity.SourceAsync().execute(getResources().getString(R.string.train_source_url),
                                    "imei_no=" + mAppHandler.getImeiNo(),
                                    "&mode=source_list",
                                    "&journey_date=" + mSelectedDate);
                        }
                    }
                } else {
                    if (mSelectedDate == null) {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.journey_date_select_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                        snackbar.show();
                    } else if (dateFormat.parse(selected_date_full).before(dateFormat.parse(date_full))) {
                        //if ((Integer.parseInt(mSelectedDate) < today) && (Integer.parseInt(mSelectedMonth) < month)) {
                        Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.previous_journey_date_select_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                        snackbar.show();
                    } else {
                        if (!mCd.isConnectingToInternet()) {
                            AppHandler.showDialog(TrainMainActivity.this.getSupportFragmentManager());
                        } else {
                            new TrainMainActivity.SourceAsync().execute(getResources().getString(R.string.train_source_url),
                                    "imei_no=" + mAppHandler.getImeiNo(),
                                    "&mode=source_list",
                                    "&journey_date=" + mSelectedDate);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class SourceAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TrainMainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3]);
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            ArrayList<String> mSources = new ArrayList<>();
            ArrayList<String> mSourceCodes = new ArrayList<>();

            if (result != null) {
                if (result.startsWith("200")) {
                    String[] spiltArray = result.split("@");
                    for (int i = 2; i < spiltArray.length; i++) {
                        String subSpiltArray = spiltArray[i];
                        if (i % 2 != 0) {
                            mSources.add(subSpiltArray);
                        } else {
                            if (!subSpiltArray.contains("###"))
                                mSourceCodes.add(subSpiltArray);

                        }
                    }
                    mAppHandler.setSourceCodes(mSourceCodes);
                    mAppHandler.setSources(mSources);
                    mAppHandler.setJourneyDate(mSelectedDate);
                    Intent intent = new Intent(TrainMainActivity.this, TrainTicketActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, result.split("@")[1], Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onBackPressed() {
        if (flag) {
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        flag = false;
        Intent intent = new Intent(TrainMainActivity.this, ETicketMainActivity.class);
        startActivity(intent);
        finish();
    }
}
