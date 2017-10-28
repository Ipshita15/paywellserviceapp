package com.cloudwell.paywell.services.activity.payments.rocket;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.gnas.customcalendarview.CalendarListener;
import com.gnas.customcalendarview.CustomCalendarView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Naima Gani on 12/19/2016.
 */

public class DateFromActivity extends AppCompatActivity implements CalendarListener {

    private String mSelectedDate;
    private AppHandler mAppHandler;
    private boolean flag = false;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_date);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Calendar");
        mAppHandler = new AppHandler(DateFromActivity.this);

        if(mAppHandler.getAppLanguage().equalsIgnoreCase("bn") || mAppHandler.getAppLanguage().equalsIgnoreCase("unknown")){
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
    }


    @SuppressWarnings("deprecation")
    @Override
    public void onDateSelected(Date date) {
        if (flag) {
            Configuration config = new Configuration();
            config.locale = Locale.FRANCE;
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        }
        flag = false;
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
        mSelectedDate = df.format(date);
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result_calender", mSelectedDate);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onMonthChanged(Date date) {
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
        Intent intent = new Intent(DateFromActivity.this, RocketInquiryActivity.class);
        startActivity(intent);
        finish();
    }
}
