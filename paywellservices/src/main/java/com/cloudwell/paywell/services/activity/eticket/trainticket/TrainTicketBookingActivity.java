package com.cloudwell.paywell.services.activity.eticket.trainticket;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;
import com.cloudwell.paywell.services.utils.MyHttpClient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by android on 5/24/2016.
 */
public class TrainTicketBookingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RelativeLayout mRelativeLayout;
    private EditText mEtMobileNo;
    private Button mBtnSearchSeat;
    private ConnectionDetector mCd;
    private String mTrainCode;
    private String mClassCode;
    private String mTrainName;
    private String mClassName;
    private String mMobileNo;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        getSupportActionBar().setTitle(R.string.purchase_ticket_msg);
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        Spinner mSpinnerTrainName = (Spinner) findViewById(R.id.spinnerTrainName);
        Spinner mSpinnerClassType = (Spinner) findViewById(R.id.spinnerClassType);
        mEtMobileNo = (EditText) findViewById(R.id.etMobileNo);
        mBtnSearchSeat = (Button) findViewById(R.id.btnSearchSeat);

        ((TextView) mRelativeLayout.findViewById(R.id.tvSelectTrain)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvSelectClass)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvMobileNo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtMobileNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSearchSeat.setTypeface(AppController.getInstance().getRobotoRegularFont());

        // Spinner click listener
        assert mSpinnerTrainName != null;
        mSpinnerTrainName.setOnItemSelectedListener(this);
        assert mSpinnerClassType != null;
        mSpinnerClassType.setOnItemSelectedListener(this);

        // Button click listener
        mBtnSearchSeat.setOnClickListener(this);

        mAppHandler = new AppHandler(this);
        // Creating adapter for spinner
        assert mSpinnerTrainName != null;
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAppHandler.getTrainNames()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTypeface(AppController.getInstance().getRobotoRegularFont());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        ((TextView) view).setAllCaps(false);
                    }
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setTypeface(AppController.getInstance().getRobotoRegularFont());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ((TextView) view).setAllCaps(false);
                }
                return view;
            }
        };
        // Drop down layout style - list terms_and_conditions_format with radio button
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinnerTrainName.setAdapter(destinationAdapter);

        assert mSpinnerClassType != null;
        ArrayAdapter<String> passengerTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAppHandler.getClassTypes()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    ((TextView) view).setTypeface(AppController.getInstance().getRobotoRegularFont());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        ((TextView) view).setAllCaps(false);
                    }
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                ((TextView) view).setTypeface(AppController.getInstance().getRobotoRegularFont());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ((TextView) view).setAllCaps(false);
                }
                return view;
            }
        };
        // Drop down layout style - list terms_and_conditions_format with radio button
        passengerTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinnerClassType.setAdapter(passengerTypeAdapter);

        mCd = new ConnectionDetector(getApplicationContext());

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinnerTrainName:
                mTrainCode = mAppHandler.getTrainCodes().get(position);
                mTrainName = mAppHandler.getTrainNames().get(position);
                break;
            case R.id.spinnerClassType:
                mClassCode = mAppHandler.getClassTypeCodes().get(position);
                mClassName = mAppHandler.getClassTypes().get(position);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSearchSeat) {
            mMobileNo = mEtMobileNo.getText().toString().trim();
            if (mMobileNo.length() < 11) {
                mEtMobileNo.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
            } else {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    try {
                        new TrainSeatAsync().execute(getResources().getString(R.string.train_source_url),
                                "imei_no=" + mAppHandler.getImeiNo(),
                                "&mode=cost_info",
                                "&source_name=" + URLEncoder.encode(mAppHandler.getSourceStation(), "UTF-8"),
                                "&source_code=" + URLEncoder.encode(mAppHandler.getSourceStationCode(), "UTF-8"),
                                "&destination_name=" + URLEncoder.encode(mAppHandler.getDestinationStation(), "UTF-8"),
                                "&destination_code=" + URLEncoder.encode(mAppHandler.getDestinationStationCode(), "UTF-8"),
                                "&train_name=" + URLEncoder.encode(mTrainName, "UTF-8"),
                                "&train_code=" + URLEncoder.encode(mTrainCode, "UTF-8"),
                                "&train_class_name=" + URLEncoder.encode(mClassName, "UTF-8"),
                                "&class_code=" + URLEncoder.encode(mClassCode, "UTF-8"),
                                "&number_of_seat=" + URLEncoder.encode(mAppHandler.getNumberOfPassenger(), "UTF-8"),
                                "&passanger_code=" + URLEncoder.encode(mAppHandler.getPassengerCode(), "UTF-8"),
                                "&age=" + URLEncoder.encode(mAppHandler.getAgeOfPassenger(), "UTF-8"),
                                "&journey_date=" + URLEncoder.encode(mAppHandler.getJourneyDate(), "UTF-8"),
                                "&passenger_phone=" + URLEncoder.encode(mMobileNo, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent(TrainTicketBookingActivity.this, SearchTrainActivity.class);
        startActivity(intent);
        finish();
    }

    private class TrainSeatAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        private String ticketId;
        private String ticketFare;
        private String pin;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TrainTicketBookingActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8] + params[9] + params[10] + params[11] + params[12] + params[13] + params[14] + params[15]);
            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 1000000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 1000000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient httpClient = new MyHttpClient(httpParameters, getApplicationContext());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = httpClient.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                if (result.startsWith("200")) {
                    String[] spiltArray = result.split("@");
                    ticketId = spiltArray[1];
                    ticketFare = spiltArray[2];

                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainTicketBookingActivity.this);
                    builder.setTitle(getString(R.string.amount_des) + " " + ticketFare + getString(R.string.tk));

                    final EditText pinNoET = new EditText(TrainTicketBookingActivity.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    pinNoET.setGravity(Gravity.CENTER_HORIZONTAL);
                    pinNoET.setLayoutParams(lp);
                    pinNoET.setHint(R.string.pin_no_title_msg);
                    pinNoET.setInputType(InputType.TYPE_CLASS_NUMBER |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pinNoET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    builder.setView(pinNoET);

                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            InputMethodManager inMethMan = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inMethMan.hideSoftInputFromWindow(pinNoET.getWindowToken(), 0);

                            pin = pinNoET.getText().toString().trim();
                            if (pin.length() == 0) {
                                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG);
                                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                                View snackBarView = snackbar.getView();
                                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                                snackbar.show();
                            } else {
                                if (!mCd.isConnectingToInternet()) {
                                    AppHandler.showDialog(getSupportFragmentManager());
                                } else {
                                    try {
                                        new PurchaseTicketAsync().execute(getResources().getString(R.string.train_source_url),
                                                "imei_no=" + mAppHandler.getImeiNo(),
                                                "&pin_code=" + URLEncoder.encode(pin, "UTF-8"),
                                                "&mode=purchase_ticket",
                                                "&id=" + URLEncoder.encode(ticketId, "UTF-8"));
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, result.split("@")[1].toString(), Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }

    private class PurchaseTicketAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(TrainTicketBookingActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4]);
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                responseTxt = client.execute(request, responseHandler);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseTxt;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.cancel();
            if (result != null) {
                if (result.startsWith("200")) {
                    String[] spiltArray = result.split("@");
                    String stationFrom = spiltArray[1];
                    String stationTo = spiltArray[2];
                    String journeyDate = spiltArray[3];
                    String ticketCollectDateline = spiltArray[4];
                    String departureTime = spiltArray[5];
                    String trainName = spiltArray[6];
                    String trainNo = spiltArray[7];
                    String coachNo = spiltArray[8];
                    String seatNo = spiltArray[9];
                    String trxId = spiltArray[10];
                    String blTrainTrxId = spiltArray[11];
                    String fare = spiltArray[12];
                    String totalFare = spiltArray[13];
                    String serviceCharge = spiltArray[14];
                    String totalSum = spiltArray[15];
                    String customerMSISDN = spiltArray[16];
                    String pnr = spiltArray[17];

                    Intent intent = new Intent(TrainTicketBookingActivity.this, TrainTicketBookingStatusActivity.class);
                    intent.putExtra(TrainTicketBookingStatusActivity.STATION_FROM, stationFrom);
                    intent.putExtra(TrainTicketBookingStatusActivity.STATION_TO, stationTo);
                    intent.putExtra(TrainTicketBookingStatusActivity.JOURNEY_DATE, journeyDate);
                    intent.putExtra(TrainTicketBookingStatusActivity.TICKET_COLLECT_DATELINE, ticketCollectDateline);
                    intent.putExtra(TrainTicketBookingStatusActivity.DEPARTURE_TIME, departureTime);
                    intent.putExtra(TrainTicketBookingStatusActivity.TRAIN_NAME, trainName);
                    intent.putExtra(TrainTicketBookingStatusActivity.TRAIN_NO, trainNo);
                    intent.putExtra(TrainTicketBookingStatusActivity.COACH_NO, coachNo);
                    intent.putExtra(TrainTicketBookingStatusActivity.SEAT_NO, seatNo);
                    intent.putExtra(TrainTicketBookingStatusActivity.TRX_ID, trxId);
                    intent.putExtra(TrainTicketBookingStatusActivity.BL_TRAIN_TRX_ID, blTrainTrxId);
                    intent.putExtra(TrainTicketBookingStatusActivity.FARE, fare);
                    intent.putExtra(TrainTicketBookingStatusActivity.TOTAL_FARE, totalFare);
                    intent.putExtra(TrainTicketBookingStatusActivity.SERVICE_CHARGE, serviceCharge);
                    intent.putExtra(TrainTicketBookingStatusActivity.TOTAL_SUM, totalSum);
                    intent.putExtra(TrainTicketBookingStatusActivity.CELL_NUMBER, mMobileNo);
                    intent.putExtra(TrainTicketBookingStatusActivity.CUSTOMER_MSISDN, customerMSISDN);
                    intent.putExtra(TrainTicketBookingStatusActivity.PNR, pnr);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TrainTicketBookingActivity.this);
                    builder.setTitle("Result");
                    builder.setMessage(result.split("@")[1]);
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(TrainTicketBookingActivity.this, ETicketMainActivity.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                }

            } else {
                Snackbar snackbar = Snackbar.make(mRelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
