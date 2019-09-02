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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusTransportListActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResPaymentBookingAPI;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatCheckBookAPI;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.lifecycle.ViewModelProviders;
import mehdi.sakout.fancybuttons.FancyButton;

public class BusCitySearchActivity extends BusTricketBaseActivity implements FullScreenDialogBus.OnCitySet, IbusTransportListView, AdapterView.OnItemSelectedListener {


    public static final String FullSCREEN_DIALOG_HEADER = "header";
    public static final String TO_STRING = "Going To";
    TextSwitcher fromTS, toTS;
    private ImageView textSwitchIV;
    public static final String FROM_STRING = "Leaving From";
    private FancyButton searchButton;
    private String fromString, toString;
    private LinearLayout dateLinearLayout, fromLL, toLL;
    private Calendar myCalender;
    private TextView dateTV, monthTV, dayTV;
    private SimpleDateFormat simpleDateFormat;
    private String dateString = new String();
    Spinner boothList;
    TextView boaringPointLevel;
    View viewBoardingPoint;
    private BusTransportViewModel viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_city_search_new);

        setToolbar(getString(R.string.search_transport_ticket), getApplicationContext().getResources().getColor(R.color.bus_ticket_toolbar_title_text_color));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#268472'>Search Transport Ticket</font>"));
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

        boothList = findViewById(R.id.boothList);
        boaringPointLevel = findViewById(R.id.boaringPointLevel);
        viewBoardingPoint = findViewById(R.id.viewBoardingPoint);

        boothList.setOnItemSelectedListener(this);


        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
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

        fromString = FROM_STRING;
        toString = TO_STRING;

        String fromData = (String) AppStorageBox.get(this, AppStorageBox.Key.BUS_LEAVING_FROM_CITY);
        String toData = (String) AppStorageBox.get(this, AppStorageBox.Key.BUS_GOING_TO_CITY);

        if (fromData != null && !(fromData.isEmpty()) && !fromData.equals("null")) {
            fromTS.setText(fromData);
            fromString = fromData;
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
        if (toData != null && !(toData.isEmpty()) && !toData.equals("null")) {
            toTS.setText(toData);
            toString = toData;
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

                getBoardingPoint();


            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from = ((TextView) fromTS.getCurrentView()).getText().toString();
                String to = ((TextView) toTS.getCurrentView()).getText().toString();
                if (!from.isEmpty() && !(from.equals(FROM_STRING)) && !to.isEmpty() && !(to.equals(TO_STRING))) {

                    if (!from.equals(TO_STRING) && (!to.equals(FROM_STRING))) {

                        if (boothList.getSelectedItem().toString().equals("All")) {
                            openTripListActivity(from, to, "All");
                        } else {
                            openTripListActivity(from, to, boothList.getSelectedItem().toString());
                        }
                    }

                } else {
                    Toast.makeText(BusCitySearchActivity.this, getApplicationContext().getResources().getString(R.string.bus_validation_message), Toast.LENGTH_SHORT).show();
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
        toLL.setOnClickListener(view -> {
            FullScreenDialogBus dialog = new FullScreenDialogBus();
            Bundle b = new Bundle();
            b.putString(FullSCREEN_DIALOG_HEADER, TO_STRING);
            dialog.setArguments(b);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            dialog.show(ft, FullScreenDialogBus.TAG);
        });

        dateLinearLayout.setOnClickListener(view -> {
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

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                String validateDate = (String) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.BUS_VALIDATE_DATE);

                Date date = null;
                date = sdf.parse(validateDate);
                long millis = date.getTime();
                datePickerDialog.getDatePicker().setMaxDate(millis);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            datePickerDialog.show();

        });

        initViewModel();

        getBoardingPoint();


    }

    private void tryToOldBoardingPointSeletion() {
        Integer o = (Integer) AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.BOARDING_POGISTION);
        if (o != null) {
            boothList.setSelection(o);
        }
    }

    private void initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel.class);
        viewMode.setIbusTransportListView(this);
    }

    private void openTripListActivity(String from, String to, String boardingPoint) {
        String date = (String) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.BUS_JOURNEY_DATE);

        RequestBusSearch requestBusSearch = new RequestBusSearch();
        requestBusSearch.setFrom(from);
        requestBusSearch.setTo(to);
        requestBusSearch.setDate(date);
        requestBusSearch.setBoadingPoint(boardingPoint);

        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH, requestBusSearch);

        Intent intent = new Intent(getApplicationContext(), BusTransportListActivity.class);
        startActivity(intent);
    }


    @Override
    public void setCityData(String cityName, String toOrFrom) {
        if (toOrFrom.equals(TO_STRING)) {
            toTS.setText(cityName);
            toString = cityName;
        } else if (toOrFrom.equals(FROM_STRING)) {
            fromTS.setText(cityName);
            fromString = cityName;

        } else {
            Toast.makeText(BusCitySearchActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
        }

        getBoardingPoint();

    }

    private void getBoardingPoint() {

        String from = ((TextView) fromTS.getCurrentView()).getText().toString();
        String to = ((TextView) toTS.getCurrentView()).getText().toString();
        if (!from.isEmpty() && !(from.equals(FROM_STRING)) && !to.isEmpty() && !(to.equals(TO_STRING))) {
            if (!from.equals(TO_STRING) && (!to.equals(FROM_STRING))) {

                String date = (String) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.BUS_JOURNEY_DATE);


                Transport transport = (Transport) AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.SELETED_BUS_INFO);
                RequestBusSearch requestBusSearch = new RequestBusSearch();
                requestBusSearch.setFrom(from);
                requestBusSearch.setTo(to);
                requestBusSearch.setDate(date);

                viewMode.getBoardingPoint(requestBusSearch, transport);
            } else {
                boardingViewStatusChange(View.GONE);
            }

        } else {
            boardingViewStatusChange(View.GONE);
            Toast.makeText(BusCitySearchActivity.this, getApplicationContext().getResources().getString(R.string.bus_validation_message), Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void showNoTripFoundUI() {
        Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.no_boarding_point_found), Toast.LENGTH_LONG).show();
        boardingViewStatusChange(View.GONE);
    }

    private void boardingViewStatusChange(int gone) {
        boothList.setVisibility(gone);
        boaringPointLevel.setVisibility(gone);
        viewBoardingPoint.setVisibility(gone);

    }

    @Override
    public void setAdapter(@NotNull List<TripScheduleInfoAndBusSchedule> it1) {


    }

    @Override
    public void showErrorMessage(@NotNull String message) {

    }

    @Override
    public void showSeatCheckAndBookingRepose(@NotNull ResSeatCheckBookAPI it) {

    }

    @Override
    public void showShowConfirmDialog(@NotNull ResPaymentBookingAPI it) {

    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hiddenProgress() {
        dismissProgressDialog();
    }

    @Override
    public void setBoardingPoint(@NotNull Set<String> allBoothNameInfo) {

        String[] strings = allBoothNameInfo.toArray(new String[allBoothNameInfo.size()]);


        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.bus_spinner_back, strings);
        boothList.setAdapter(stringArrayAdapter);
        boardingViewStatusChange(View.VISIBLE);


        tryToOldBoardingPointSeletion();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        AppStorageBox.put(AppController.getContext(), AppStorageBox.Key.BOARDING_POGISTION, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
