package com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import mehdi.sakout.fancybuttons.FancyButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingCancelActivity extends AppCompatActivity {
    private ArrayAdapter bookingCancelAdapter;
    private ArrayList<String> cancelReasonList = new ArrayList<>();
    private EditText bookingIdET;
    private Spinner bookingCancelReasonSPNR;
    private FancyButton cancelBookingBtn;
    private ConnectionDetector cd;
    private String PIN_NO = "unknown";
    private ConstraintLayout cancelMainLayout;
    private AppHandler mAppHandler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cencel_booking);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.tile_cencel_booking);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cd = new ConnectionDetector(getApplicationContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        cancelMainLayout = findViewById(R.id.cancelMainLayout);
        bookingIdET = findViewById(R.id.bookingIdET);
        bookingCancelReasonSPNR = findViewById(R.id.cancelReasonSPNR);
        cancelBookingBtn = findViewById(R.id.cancelBookingBtn);
        cancelReasonList.add("Select your reason");
        cancelReasonList.add("have another work");
        cancelReasonList.add("Travelling reason accomplished");
        cancelReasonList.add("Have to travel another city");
        cancelReasonList.add("Other");
        bookingCancelAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, cancelReasonList);
        bookingCancelReasonSPNR.setAdapter(bookingCancelAdapter);
        cancelBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (!bookingIdET.getText().toString().isEmpty() &&
                        !bookingCancelReasonSPNR.getSelectedItem().toString().equals("Select your reason")) {
                    askForPin(bookingIdET.getText().toString(), bookingCancelReasonSPNR.getSelectedItem().toString());
                } else {
                    Snackbar snackbar = Snackbar.make(cancelMainLayout, "Please Enter all the fields first", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });

    }

    private void submitCancelRequest(String userName, String pass, String bookingId, String cancelReason, String apiFormat) {

        final ProgressDialog progressDialog = new ProgressDialog(BookingCancelActivity.this);
        progressDialog.setMessage("Loading!!! Please wait..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiUtils.getAPIService().cancelBooking(userName, pass, bookingId, cancelReason, apiFormat).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {

                    if (response.isSuccessful()) {
                        JsonObject jsonObject = response.body();
                        String message = jsonObject.get("message").getAsString();
                        if (jsonObject.get("status").getAsInt() == 200) {
                            showMsg("Request Successfully Submitted");
                        } else {
                            showMsg(message);
                        }

                    }
                }
                progressDialog.hide();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(BookingCancelActivity.this, "Network error!!!", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        });
    }

    private void askForPin(String bookingId, String cancelReason) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.pin_no_title_msg);

        final EditText pinNoET = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
        pinNoET.setLayoutParams(lp);
        pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        builder.setView(pinNoET);

        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                if (pinNoET.getText().toString().length() != 0) {
                    dialogInterface.dismiss();
                    PIN_NO = pinNoET.getText().toString();
                    if (cd.isConnectingToInternet()) {
                        submitCancelRequest("cwntcl", PIN_NO, bookingId, cancelReason, "json");
                    } else {
                        Snackbar snackbar = Snackbar.make(cancelMainLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(cancelMainLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showMsg(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Booking Cancel");
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
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
