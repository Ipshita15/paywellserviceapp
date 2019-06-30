package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
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

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import mehdi.sakout.fancybuttons.FancyButton;

public class BusCitySearchActivity extends BusTricketBaseActivity implements FullScreenDialogBus.OnCitySet {


    public static final String FullSCREEN_DIALOG_HEADER = "header";
    public static final String TO_STRING_ = "To";
    private ImageView textSwitchIV;
    private FancyButton searchButton;
    public static final String FROM_STRING = "From";
    TextSwitcher fromTS, toTS;
    private String fromString, toString;
    private LinearLayout dateLinearLayout, fromLL, toLL;
    private Calendar myCalender;
    private TextView dateTV, monthTV, dayTV;
    private SimpleDateFormat simpleDateFormat;

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
        simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");
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
        fromString = "Leaving From";
        fromTS.setText(fromString);
        toTS.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                TextView switcherTextView = new TextView(getApplicationContext());
                switcherTextView.setTextSize(18);
                switcherTextView.setTextColor(Color.BLACK);
                return switcherTextView;
            }
        });
        toString = "Going To";
        toTS.setText(toString);

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
                Intent intent = new Intent(getApplicationContext(), BusSearchActivity.class);
                startActivity(intent);

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
