package com.cloudwell.paywell.services.customView.horizontalDatePicker;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 26/12/18.
 */
public class MyDateTimelineViewRecyclerView extends RecyclerView {

    private static final String TAG = "TimelineView";

    private static final String[] WEEK_DAYS = DateFormatSymbols.getInstance().getShortWeekdays();
    private static final String[] MONTH = DateFormatSymbols.getInstance().getShortMonths();

    private final Calendar calendar = Calendar.getInstance(Locale.getDefault());

    private TimelineAdapter adapter;
    private LinearLayoutManager layoutManager;
    private OnDateSelectedListener onDateSelectedListener;
//    private MonthView.DateLabelAdapter dateLabelAdapter;

    private int startYear = 2018, startMonth = 12, startDay = 27;
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedPosition = 1;
    private int dayCount = Integer.MAX_VALUE;

    // Day letter
    private int lblDayColor;
    // Day number label
    private int lblDateColor, lblDateSelectedColor;
    // Label
    private int lblLabelColor;

    public MyDateTimelineViewRecyclerView(Context context) {
        super(context);
        init();
    }

    public MyDateTimelineViewRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyDateTimelineViewRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        calendar.setTimeInMillis(System.currentTimeMillis());

        setSelectedDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        resetCalendar();

        setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        adapter = new TimelineAdapter();
        setLayoutManager(layoutManager);
        setAdapter(adapter);
    }

    private void resetCalendar() {
        calendar.set(startYear, startMonth, startDay, 1, 0, 0);
    }

    private void onDateSelected(int position, int year, int month, int day) {
        if (position == selectedPosition) {
            centerOnPosition(selectedPosition);
            return;
        }

        int oldPosition = selectedPosition;
        selectedPosition = position;

        this.selectedYear = year;
        this.selectedMonth = month;
        this.selectedDay = day;

        if (adapter != null && layoutManager != null) {
            adapter.notifyItemChanged(oldPosition);
            adapter.notifyItemChanged(position);

            centerOnPosition(selectedPosition);

            if (onDateSelectedListener != null) {
                onDateSelectedListener.onDateSelected(selectedYear, selectedMonth, selectedDay, selectedPosition);
            }
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    centerOnPosition(selectedPosition);
                }
            });
        }
    }

    public void centerOnPosition(int position) {
        if (getChildCount() == 0) {
            return;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!isLaidOut()) {
                return;
            }
        }
        // Animate scroll
        int offset = getMeasuredWidth() / 2 - getChildAt(0).getMeasuredWidth() / 2;
        layoutManager.scrollToPositionWithOffset(position, offset);
    }

    public void centerOnSelection() {
        centerOnPosition(selectedPosition);
    }

    public void setSelectedDate(int year, int month, int day) {
        if (year == startYear && month == startMonth && day < startDay) {
            day = startDay;
        }

        // Get new selected dayOfYear
        calendar.set(year, month, day, 1, 0, 0);
        final int newDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        final long newTimestamp = calendar.getTimeInMillis();

        // Get current selected dayOfYear
        calendar.set(selectedYear, selectedMonth, selectedDay, 1, 0, 0);
        final int oldDayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        final long oldTimestamp = calendar.getTimeInMillis();

        int dayDifference;
        if (year == selectedYear) {
            dayDifference = newDayOfYear - oldDayOfYear;
        } else {
            // Lazy...
            int dayDifferenceApprox = (int) ((newTimestamp - oldTimestamp) / TimeUnit.DAYS.toMillis(1));
            calendar.add(Calendar.DAY_OF_YEAR, dayDifferenceApprox);
            dayDifference = dayDifferenceApprox + (newDayOfYear - calendar.get(Calendar.DAY_OF_YEAR));
        }

        onDateSelected(selectedPosition + dayDifference, year, month, day);
    }

    public int getSelectedYear() {
        return selectedYear;
    }

    public int getSelectedMonth() {
        return selectedMonth;
    }

    public int getSelectedDay() {
        return selectedDay;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int position) {
        resetCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, position);
        onDateSelected(position, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    //
    public OnDateSelectedListener getOnDateSelectedListener() {
        return onDateSelectedListener;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        this.onDateSelectedListener = onDateSelectedListener;
    }
//
//    public void setDateLabelAdapter(@Nullable MonthView.DateLabelAdapter dateLabelAdapter) {
//        this.dateLabelAdapter = dateLabelAdapter;
//    }

    public void setDayLabelColor(int lblDayColor) {
        this.lblDayColor = lblDayColor;
    }

    public void setDateLabelColor(int lblDateColor) {
        this.lblDateColor = lblDateColor;
    }

    public void setDateLabelSelectedColor(int lblDateSelectedColor) {
        this.lblDateSelectedColor = lblDateSelectedColor;
    }

    public void setLabelColor(int lblLabelColor) {
        this.lblLabelColor = lblLabelColor;
    }

    public int getLblDateColor() {
        return lblDateColor;
    }

    public void setLblDateColor(int lblDateColor) {
        this.lblDateColor = lblDateColor;
    }

    public int getLblDateSelectedColor() {
        return lblDateSelectedColor;
    }

    public void setLblDateSelectedColor(int lblDateSelectedColor) {
        this.lblDateSelectedColor = lblDateSelectedColor;
    }

    public int getLblDayColor() {
        return lblDayColor;
    }

    public void setLblDayColor(int lblDayColor) {
        this.lblDayColor = lblDayColor;
    }

    public int getLblLabelColor() {
        return lblLabelColor;
    }

    public void setLblLabelColor(int lblLabelColor) {
        this.lblLabelColor = lblLabelColor;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setFirstDate(int startYear, int startMonth, int startDay) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;

        selectedYear = startYear;
        selectedMonth = startMonth;
        selectedDay = startDay;
        selectedPosition = 0;
        if (adapter != null) {
            adapter.notifyDataSetChanged();

        }
    }


    public void setNewDate(int startYear, int startMonth, int startDay) {
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;

        selectedYear = startYear;
        selectedMonth = startMonth;
        selectedDay = startDay;
        selectedPosition = 0;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
            onDateSelected(selectedPosition, startYear, startMonth, startDay);

        }



    }

    public void setLastDate(int endYear, int endMonth, int endDay) {
        Calendar firstDate = Calendar.getInstance();
        firstDate.set(startYear, startMonth, startDay);
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(endYear, endMonth, endDay);

        // TODO: might now work for summer time...
        int dayDiff = (int) TimeUnit.DAYS.convert(lastDate.getTimeInMillis() - firstDate.getTimeInMillis(),
                TimeUnit.MILLISECONDS);

        setDayCount(dayDiff + 1);
    }

    void setDayCount(int dayCount) {
        if (this.dayCount == dayCount) {
            return;
        }

        this.dayCount = dayCount;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public interface OnDateSelectedListener {
        void onDateSelected(int selectedYear, int selectedMonth, int selectedDay, int selectedPosition);
    }

    private class TimelineAdapter extends Adapter<MyViewHolder> {

        TimelineAdapter() {

        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mti_item_day_new, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            Log.e("onBindViewHolder", "" + position);
            // Set calendar
            resetCalendar();
            calendar.add(Calendar.DAY_OF_MONTH, position);

            // Get values
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            boolean isToday = DateUtils.isToday(calendar.getTimeInMillis());


            if (position == selectedPosition) {
                holder.linearLayout.setBackgroundResource(R.drawable.layout_round_bg);
                holder.tvDate.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color_white)));
                holder.tvDay.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color_white)));
                holder.tvMinValue.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color_white)));

            } else {
                holder.linearLayout.setBackgroundResource(R.color.white);
                holder.tvDate.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color)));
                holder.tvDay.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color)));
                holder.tvMinValue.setTextColor(AppController.getContext().getResources().getColor((R.color.text_color)));

            }

            holder.bind(position, year, month, day, dayOfWeek, "", position == selectedPosition, isToday);

//            holder.bind(1,2018,12,30,dayOfWeek,"card_background_1", true,false);
        }

        @Override
        public int getItemCount() {
            return dayCount;
        }
    }

    private class MyViewHolder extends ViewHolder {

        private final TextView tvDay;
        private final TextView tvDate;
        private final TextView tvMinValue;
        private final ConstraintLayout linearLayout;

        private int position;
        private int year, month, day;

        MyViewHolder(View root) {
            super(root);

            tvDay = (TextView) root.findViewById(R.id.tvDay);
            tvDate = (TextView) root.findViewById(R.id.tvDate);
            tvMinValue = (TextView) root.findViewById(R.id.tvMinValue);
            linearLayout = root.findViewById(R.id.linearLayout);

//            lblDay.setTextColor(lblDayColor);
//            lblDate.setTextColor(lblDateColor);
//            lblValue.setTextColor(lblLabelColor);

            root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
//                    selectedPosition = position;
                    onDateSelected(position, year, month, day);
                    Log.e(TAG, "onClick: ");
                }
            });
        }

        void bind(int position, int year, int month, int day, int dayOfWeek, CharSequence label, boolean selected, boolean isToday) {
            this.position = position;
            this.year = year;
            this.month = month;
            this.day = day;

            String s = WEEK_DAYS[dayOfWeek].toUpperCase(Locale.US);
            String m = MONTH[month].toUpperCase(Locale.US);


            tvDay.setText(s);
            tvDate.setText(String.valueOf(day));
            tvMinValue.setText(m);


            //selectedPosition = position;
//            tvDay.setBackgroundResource(selected ? R.drawable.mti_bg_lbl_date_selected : (isToday ? R.drawable.mti_bg_lbl_date_today : 0));
//            lblDate.setTextColor(selected || isToday ? lblDateSelectedColor : lblDateColor);

        }
    }

}