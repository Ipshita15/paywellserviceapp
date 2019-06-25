package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;

public class BookingCancelActivity extends AirTricketBaseActivity {
    private ArrayAdapter bookingCancelAdapter;
    private ArrayList<String> cancelReasonList = new ArrayList<>();
    private EditText bookingIdET;
    private Spinner bookingCancelReasonSPNR;
    private FancyButton cancelBookingBtn;
    private ConnectionDetector cd;
    private String PIN_NO = "unknown";
    private ConstraintLayout cancelMainLayout;
    private AppHandler mAppHandler;
    public static String KEY_BOOKING_ID = "Booking_Id";
    private String bookingCancelId = new String();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cencel_booking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.tile_cencel_booking);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent().getStringExtra(KEY_BOOKING_ID) != null) {
            bookingCancelId = getIntent().getStringExtra(KEY_BOOKING_ID);
        }
        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        cancelMainLayout = findViewById(R.id.cancelMainLayout);
        bookingIdET = findViewById(R.id.bookingIdET);
        bookingCancelReasonSPNR = findViewById(R.id.cancelReasonSPNR);
        cancelBookingBtn = findViewById(R.id.cancelBookingBtn);
        cancelReasonList.add("Select your reason");
        cancelReasonList.add("Have another work");
        cancelReasonList.add("Travelling reason accomplished");
        cancelReasonList.add("Have to travel another city");
        cancelReasonList.add("Other");
        bookingCancelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cancelReasonList);
        bookingCancelReasonSPNR.setAdapter(bookingCancelAdapter);
        bookingIdET.setText(bookingCancelId);
        cancelBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!bookingIdET.getText().toString().isEmpty() && !bookingCancelReasonSPNR.getSelectedItem().toString().equals("Select your reason")) {
                    String userName = mAppHandler.getImeiNo();
                    String reason = bookingCancelReasonSPNR.getSelectedItem().toString();

                    // askForPin(bookingIdET.getText().toString(), bookingCancelReasonSPNR.getSelectedItem().toString());

                    callCancelMapping(userName, bookingIdET.getText().toString(), reason, KEY_CANCEL);
                } else {

                    hideUserKeyboard();

                    Snackbar snackbar = Snackbar.make(cancelMainLayout, "Please Enter all the fields first", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
