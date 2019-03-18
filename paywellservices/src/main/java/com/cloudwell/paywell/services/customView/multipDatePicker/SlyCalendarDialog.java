package com.cloudwell.paywell.services.customView.multipDatePicker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloudwell.paywell.services.R;

import java.util.Calendar;
import java.util.Date;


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/17/19.
 */
public class SlyCalendarDialog extends DialogFragment implements DialogCompleteListener {


    private SlyCalendarData slyCalendarData = new SlyCalendarData();
    private Callback callback = null;

    public SlyCalendarDialog setStartDate(@Nullable Date startDate) {
        slyCalendarData.setSelectedStartDate(startDate);
        return this;
    }

    public SlyCalendarDialog setEndDate(@Nullable Date endDate) {
        slyCalendarData.setSelectedEndDate(endDate);
        return this;
    }

    public SlyCalendarDialog setSingle(boolean single) {
        slyCalendarData.setSingle(single);
        return this;
    }

    public SlyCalendarDialog setFirstMonday(boolean firsMonday) {
        slyCalendarData.setFirstMonday(firsMonday);
        return this;
    }

    public SlyCalendarDialog setCallback(@Nullable Callback callback) {
        this.callback = callback;
        return this;
    }

    public SlyCalendarDialog setTimeTheme(@Nullable Integer themeResource) {
        slyCalendarData.setTimeTheme(themeResource);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    public Date getCalendarFirstDate() {
        return slyCalendarData.getSelectedStartDate();
    }

    @Nullable
    public Date getCalendarSecondDate() {
        return slyCalendarData.getSelectedEndDate();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SlyCalendarView calendarView = (SlyCalendarView) getActivity().getLayoutInflater().inflate(R.layout.slycalendar_main, container);
        calendarView.setSlyCalendarData(slyCalendarData);
        calendarView.setCallback(callback);
        calendarView.setCompleteListener(this);
        return calendarView;
    }

    @Override
    public void complete() {
        this.dismiss();
    }

    public SlyCalendarDialog setBackgroundColor(Integer backgroundColor) {
        slyCalendarData.setBackgroundColor(backgroundColor);
        return this;
    }

    public SlyCalendarDialog setHeaderColor(Integer headerColor) {
        slyCalendarData.setHeaderColor(headerColor);
        return this;
    }

    public SlyCalendarDialog setHeaderTextColor(Integer headerTextColor) {
        slyCalendarData.setHeaderTextColor(headerTextColor);
        return this;
    }

    public SlyCalendarDialog setTextColor(Integer textColor) {
        slyCalendarData.setTextColor(textColor);
        return this;
    }

    public SlyCalendarDialog setSelectedColor(Integer selectedColor) {
        slyCalendarData.setSelectedColor(selectedColor);
        return this;
    }

    public SlyCalendarDialog setSelectedTextColor(Integer selectedTextColor) {
        slyCalendarData.setSelectedTextColor(selectedTextColor);
        return this;
    }

    public interface Callback {
        void onCancelled();

        void onDataSelected(Calendar firstDate, Calendar secondDate, int hours, int minutes);
    }


}
