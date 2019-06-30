package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class BusCitySearchActivity extends BusTricketBaseActivity implements FullScreenDialogBus.OnCitySet {


    public static final String FullSCREEN_DIALOG_HEADER = "header";
    public static final String TO_STRING_ = "Going To";
    private ImageView textSwitchIV;
    private FancyButton searchButton;
    public static final String FROM_STRING = "Leaving From";
    TextSwitcher fromTS, toTS;
    private String fromString, toString;
    private LinearLayout dateLinearLayout, fromLL, toLL;
    private Calendar myCalender;
    private TextView dateTV, monthTV, dayTV;
    private SimpleDateFormat simpleDateFormat;
    private String dateString = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_city_search);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#268472'>Search Bus Ticket</font>"));
            getSupportActionBar().setElevation(0f);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_tab_background_bus)));
        }

        dateTV = findViewById(R.id.dateTV);
        monthTV = findViewById(R.id.monthTV);
        dayTV = findViewById(R.id.dayTV);
        myCalender = Calendar.getInstance();
        dateLinearLayout = findViewById(R.id.dateLL);
        searchButton = findViewById(R.id.btn_search);
        textSwitchIV = findViewById(R.id.textSwitchIV);
        fromTS = findViewById(R.id.busFromCityTS);
        toTS = findViewById(R.id.busToCityTS);
        fromLL = findViewById(R.id.fromLL);
        toLL = findViewById(R.id.toLL);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        AppStorageBox.put(this, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat.format(myCalender.getTimeInMillis()));
        dateTV.setText(String.valueOf(myCalender.get(Calendar.DAY_OF_MONTH)));
        monthTV.setText(new DateFormatSymbols().getMonths()[myCalender.get(Calendar.MONTH)]);
        dayTV.setText(new DateFormatSymbols().getWeekdays()[myCalender.get(Calendar.DAY_OF_WEEK)]);
        fromTS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getApplicationContext());
                switcherTextView.setTextSize(18);
                switcherTextView.setTextColor(Color.BLACK);
                return switcherTextView;
            }
        });
        if ((String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY)) != null)
                && !(String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY)).isEmpty())) {
            fromString = String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY));
        } else {
            fromString = "Leaving From";
        }

        if ((String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY)) != null)
                && !(String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY)).isEmpty())) {
            toString = String.valueOf(AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY));
        } else {
            toString = "Going To";
        }
        String fromData = (String) AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY);
        String toData = (String) AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY);
        if (fromData != null && !(fromData.isEmpty())) {
            fromTS.setText(fromData);
        } else {
            fromTS.setText(fromString);
        }
        toTS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getApplicationContext());
                switcherTextView.setTextSize(18);
                switcherTextView.setTextColor(Color.BLACK);
                return switcherTextView;
            }
        });
        if (toData != null && !(toData.isEmpty())) {
            toTS.setText(toData);
        } else {
            toTS.setText(toString);
        }

        Animation inAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation outAnim = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        inAnim.setDuration(200);
        outAnim.setDuration(200);
        fromTS.setInAnimation(inAnim);
        fromTS.setOutAnimation(outAnim);
        toTS.setInAnimation(inAnim);
        toTS.setOutAnimation(outAnim);
        textSwitchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTS.setText(fromString);
                fromTS.setText(toString);
                String from = toString;
                toString = fromString;
                fromString = from;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = ((TextView) fromTS.getCurrentView()).getText().toString();
                String to = ((TextView) toTS.getCurrentView()).getText().toString();
                if (!(from.isEmpty() && !(from.equals(FROM_STRING))) && !(to.isEmpty() && !(to.equals(TO_STRING_)))) {

                    Toast.makeText(BusCitySearchActivity.this, "Service will available soon", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(BusCitySearchActivity.this, "Please Enter All the data first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fromLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullScreenDialogBus dialog = new FullScreenDialogBus();
                Bundle b = new Bundle();
                b.putString(FullSCREEN_DIALOG_HEADER, FROM_STRING);
                dialog.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialogBus.TAG);
            }
        });
        toLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FullScreenDialogBus dialog = new FullScreenDialogBus();
                Bundle b = new Bundle();
                b.putString(FullSCREEN_DIALOG_HEADER, TO_STRING_);
                dialog.setArguments(b);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialog.show(ft, FullScreenDialogBus.TAG);
            }
        });

        dateLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(BusCitySearchActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        myCalender.set(i, i1, i2);
                        dateTV.setText(String.valueOf(myCalender.get(Calendar.DAY_OF_MONTH)));
                        monthTV.setText(new DateFormatSymbols().getMonths()[myCalender.get(Calendar.MONTH)]);
                        dayTV.setText(new DateFormatSymbols().getWeekdays()[myCalender.get(Calendar.DAY_OF_WEEK)]);
                        AppStorageBox.put(BusCitySearchActivity.this, AppStorageBox.Key.BUS_JOURNEY_DATE, simpleDateFormat.format(myCalender.getTimeInMillis()));

                    }
                }, myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void setCityData(String cityName, String toOrFrom) {
        if (toOrFrom.equals(TO_STRING_)) {
            toTS.setText(cityName);
            toString = cityName;
        } else if (toOrFrom.equals(FROM_STRING)) {
            fromTS.setText(cityName);
            fromString = cityName;
        } else {
            Toast.makeText(BusCitySearchActivity.this, "Internal Error Occurred!!!", Toast.LENGTH_SHORT).show();
        }
    }

}
