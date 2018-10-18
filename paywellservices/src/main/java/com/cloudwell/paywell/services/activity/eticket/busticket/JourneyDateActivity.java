package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.applandeo.materialcalendarview.CalendarView;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class JourneyDateActivity extends AppCompatActivity {

    private String mSelectedDate;
    private AppHandler mAppHandler;
    private boolean flag = false;
    RelativeLayout mRelativeLayout;
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String date_full, selected_date_full;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.calendar_ticket_msg);
        mAppHandler = new AppHandler(JourneyDateActivity.this);

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        if(mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")){
            Configuration config = new Configuration();
            config.locale = Locale.ENGLISH;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            flag = true;
        }
        //Initialize CustomCalendarView from layout
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendar_view);

        //Initialize calendar with date
        Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show monday as first date of week
//        calendarView.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
//        calendarView.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the terms_and_conditions_format
//        calendarView.refreshCalendar(currentCalendar);

        //Handling custom calendar events
//        calendarView.setCalendarListener(this);

        try {
            Date date_today = new Date();
            date_full = dateFormat.format(date_today);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//    @SuppressWarnings("deprecation")
//    @Override
//    public void onDateSelected(Date date) {
//        if (flag) {
//            Configuration config = new Configuration();
//            config.locale = Locale.FRANCE;
//            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//        }
//        flag = false;
//        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.ENGLISH);
//        selected_date_full = dateFormat.format(date);
//        mSelectedDate = df.format(date);
//
//        try {
//            if (dateFormat.parse(selected_date_full).before(dateFormat.parse(date_full))) {
//                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.previous_journey_date_select_error_msg, Snackbar.LENGTH_LONG);
//                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
//                View snackBarView = snackbar.getView();
//                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
//                snackbar.show();
//            } else {
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result_calender", mSelectedDate);
//                setResult(RESULT_OK, returnIntent);
//                finish();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onMonthChanged(Date date) {
//
//    }

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
        Intent intent = new Intent(JourneyDateActivity.this, BusMainActivity.class);
        startActivity(intent);
        finish();
    }
}
