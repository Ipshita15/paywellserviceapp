package com.cloudwell.paywell.services.customView.horizontalDatePicker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 27/12/18.
 */
public class MyDatePickerTimelineForBus extends LinearLayout implements View.OnClickListener, MyDateTimelineViewRecyclerViewForBus.OnDateSelectedListener {
    IDatePicker iDatePicker;

    //
    int startYear;
    int startMonth;
    int startDay;
    private MyDateTimelineViewRecyclerViewForBus myDateTimelineViewRecyclerView;


    public MyDatePickerTimelineForBus(Context context) {
        this(context, null);
    }

    public MyDatePickerTimelineForBus(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyDatePickerTimelineForBus(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {

        final View view = inflate(getContext(), R.layout.main_bus, this);


        myDateTimelineViewRecyclerView = view.findViewById(R.id.myDateTimelineViewRecyclerView);
        myDateTimelineViewRecyclerView.setFirstDate(2018, 12, 27);
        myDateTimelineViewRecyclerView.setOnDateSelectedListener(this);

        // store default value
        startYear = 2018;
        startMonth = 12;
        startDay = 27;

        myDateTimelineViewRecyclerView.setLastDate(2019, 01, 31);

        TextView ivMore = view.findViewById(R.id.textView4);
        ivMore.setOnClickListener(this);

        view.findViewById(R.id.imageView).setOnClickListener(this);


    }

    public void setFirstDate(int startYear, int startMonth, int startDay) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        myDateTimelineViewRecyclerView.setFirstDate(startYear, startMonth, startDay);

    }


    public void setNewDate(int startYear, int startMonth, int startDay) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        myDateTimelineViewRecyclerView.setNewDate(startYear, startMonth, startDay);

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.textView4) {
            showDate();

        }
        if (i == R.id.imageView) {
            showDate();
        }
    }

    //get date
    private void showDate() {

        Calendar calendar = Calendar.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int thismonth = calendar.get(Calendar.MONTH);
        final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        calendar.set(year, thismonth, dayOfMonth);


        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                if (iDatePicker != null) {
                    iDatePicker.onSetNewDate(year, month, day);
                }
            }
        }, year, thismonth, dayOfMonth);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            String validateDate = (String) AppStorageBox.get(getContext(), AppStorageBox.Key.BUS_VALIDATE_DATE);

            Date date = null;
            date = sdf.parse(validateDate);
            long millis = date.getTime();
            datePickerDialog.getDatePicker().setMaxDate(millis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePickerDialog.show();

        datePickerDialog.show();
    }


    public void setOnDateChangeLincher(AppCompatActivity mainActivity) {
        this.iDatePicker = (IDatePicker) mainActivity;
    }


    @Override
    public void onDateSelected(int selectedYear, int selectedMonth, int selectedDay, int selectedPosition) {
        if (iDatePicker != null) {
            iDatePicker.onSetDate(selectedYear, selectedMonth, selectedDay);
        }
    }


}