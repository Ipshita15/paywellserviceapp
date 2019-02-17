package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.activity.eticket.busticket.model.BoardingAndDropping;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class BoardingNDropping extends BaseActivity {
    public static final String OPTION_ID = "optionId";
    public static final String COMPANY_NAME = "companyName";
    public static final String ARRIVAL_TIME = "arrivalTime";
    public static final String COACH_TYPE = "coachType";

    private String _optionId;
    private AppHandler mAppHandler;
    private String _companyName;
    private String _arrivalTime;
    private String _coachType;
    private TextView txtCompany;
    private TextView txtDate;
    private TextView txtCoachType;
    private EditText edtName;
    private EditText edtMobile;
    private ProgressBar _progressBar;
    private ConnectionDetector cd;
    private RelativeLayout _relativeLayout;
    private CardView mCardView;
    private CardView mCardfView2;
    private Button mBtnDone;
    private BoardingAndDropping boardingAndDropping;
    private String seatId;
    private String seatNo;
    private String seatFare;
    private String numOfSeat;
    private String name;
    private String phoneNo;
    private Spinner spinnerBoading;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding_droping);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.purchase_ticket_msg);
        cd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _optionId = bundle.getString(OPTION_ID);
            _companyName = bundle.getString(COMPANY_NAME);
            _arrivalTime = bundle.getString(ARRIVAL_TIME);
            _coachType = bundle.getString(COACH_TYPE);
        }
        initView();
        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boardingAndDropping = (BoardingAndDropping) spinnerBoading.getSelectedItem();
                seatId = removeComa(mAppHandler.getSeatId()); //multiple with coma
                seatNo = removeComa(mAppHandler.getSeatNo()); //multiple with coma
                seatFare = removeComa(mAppHandler.getFare()); //multiple with coma
                numOfSeat = removeComa(mAppHandler.getNumberOfSeat());
                name = edtName.getText().toString();
                phoneNo = edtMobile.getText().toString();
                if (name.length() == 0) {
                    edtName.setError(Html.fromHtml("<font color='red'>" + getString(R.string.ticket_name_error_msg) + "</font>"));
                    return;
                }
                if (phoneNo.length() < 11) {
                    edtMobile.setError(Html.fromHtml("<font color='red'>" + getString(R.string.phone_no_error_msg) + "</font>"));
                    return;
                }
                if (cd.isConnectingToInternet()) {
                    try {
                        new GetPreConfirmDataAsync().execute(getResources().getString(R.string.bus_ticket_url),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&mode=temporaryseats",
                                "&option_id=" + _optionId,
                                "&seatid=" + seatId,
                                "&seat_no=" + seatNo,
                                "&no_of_seat=" + numOfSeat,
                                "&boardingpointsid=" + boardingAndDropping.getReportingBranchID(),
                                "&seatfare=" + seatFare,
                                "&mobilenumber=" + phoneNo,
                                "&name=" + URLEncoder.encode(name, "UTF-8"),
                                "&format=json",
                                "&securityKey=" + mAppHandler.getTicketToken());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(_relativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });

        if (cd.isConnectingToInternet()) {
            new GetAPIDataAsync().execute(getResources().getString(R.string.bus_ticket_url),
                    "imei=" + mAppHandler.getImeiNo(),
                    "&mode=boardingpoints",
                    "&option_id=" + _optionId,
                    "&fromname=" + mAppHandler.getCityFrom(),
                    "&toname=" + mAppHandler.getCityTo(),
                    "&format=json",
                    "&securityKey=" + mAppHandler.getTicketToken());

        } else {
            Snackbar snackbar = Snackbar.make(_relativeLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    public void initView() {
        _relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        txtCompany = (TextView) findViewById(R.id.companyName);
        txtDate = (TextView) findViewById(R.id.tvDepartureTime);
        txtCoachType = (TextView) findViewById(R.id.coachType);

        edtName = (EditText) findViewById(R.id.etName);
        edtMobile = (EditText) findViewById(R.id.etMobileNo);
        spinnerBoading = (Spinner) findViewById(R.id.spinnerBoardingPoint);
        mBtnDone = (Button) findViewById(R.id.btnDone);

        _progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mCardView = (CardView) findViewById(R.id.cardView);
        mCardfView2 = (CardView) findViewById(R.id.cardView2);
        txtCompany.setTypeface(AppController.getInstance().getOxygenLightFont());
        txtDate.setTypeface(AppController.getInstance().getOxygenLightFont());
        txtCoachType.setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) _relativeLayout.findViewById(R.id.tvHeaderInfo)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) _relativeLayout.findViewById(R.id.tvName)).setTypeface(AppController.getInstance().getOxygenLightFont());
        edtName.setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) _relativeLayout.findViewById(R.id.tvMobile)).setTypeface(AppController.getInstance().getOxygenLightFont());
        edtMobile.setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) _relativeLayout.findViewById(R.id.tvBoardingType)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) _relativeLayout.findViewById(R.id.termsAndConditions)).setTypeface(AppController.getInstance().getOxygenLightFont());
        mBtnDone.setTypeface(AppController.getInstance().getOxygenLightFont());

        _progressBar.setVisibility(View.VISIBLE);
        mCardView.setVisibility(View.GONE);
        mCardfView2.setVisibility(View.GONE);
        mBtnDone.setVisibility(View.GONE);
    }

    public void onCheckboxClicked(View view) {
        // Is the terms_and_conditions_format now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked) {
            // Put some meat on the sandwich
        } else {

        }
    }

    private class GetAPIDataAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            String jsonStr = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            try {
                jsonStr = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    parseJson(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } // end of GetC Ontacts asyncTask


    private void parseJson(String result) {
        ArrayList<BoardingAndDropping> mStartPoints = new ArrayList<>();
        if (result != null) {
            try {
                JSONObject jsonObj = new JSONObject(result);
                String statusJsonString = jsonObj.getString("response_status");
                if (statusJsonString.equals("200")) {
                    JSONObject jsonBoardingInside = jsonObj.getJSONObject("response_data");
                    BoardingAndDropping boardingObj;
                    mStartPoints.clear();

                    Iterator<String> iterator = jsonBoardingInside.keys();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        try {
                            Object value = jsonBoardingInside.get(key);
                            JSONObject jsonObjFromVal = (JSONObject) value;

                            String reportingBranchId = jsonObjFromVal.getString("reporting_branch_id");
                            String counterName = jsonObjFromVal.getString("counter_name");
                            String reportingTime = jsonObjFromVal.getString("reporting_time");
                            String scheduleTime = jsonObjFromVal.getString("schedule_time");

                            boardingObj = new BoardingAndDropping(reportingBranchId, counterName, reportingTime, scheduleTime);
                            mStartPoints.add(boardingObj);
                        } catch (JSONException e) {

                        }
                    }
                    if (mStartPoints.size() > 0) {
                        // Creating adapter for spinner
                        ArrayAdapter<BoardingAndDropping> dataAdapter = new ArrayAdapter<BoardingAndDropping>(this, android.R.layout.simple_spinner_item, mStartPoints) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                if (view instanceof TextView) {
                                    ((TextView) view).setTypeface(AppController.getInstance().getOxygenLightFont());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                        ((TextView) view).setAllCaps(false);
                                    }
                                }
                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                ((TextView) view).setTypeface(AppController.getInstance().getOxygenLightFont());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                                    ((TextView) view).setAllCaps(false);
                                }
                                return view;
                            }
                        };
                        // Drop down layout style - list terms_and_conditions_format with radio button
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        spinnerBoading.setAdapter(dataAdapter);
                    }
                    _progressBar.setVisibility(View.GONE);
                    mCardView.setVisibility(View.VISIBLE);
                    mCardfView2.setVisibility(View.VISIBLE);
                    mBtnDone.setVisibility(View.VISIBLE);

                    txtCompany.setText(_companyName);
                    txtDate.setText(_arrivalTime);
                    txtCoachType.setText(R.string.coach_des + " " + _coachType);
                } else {
                    _progressBar.setVisibility(View.GONE);
                    mCardView.setVisibility(View.GONE);
                    mCardfView2.setVisibility(View.GONE);
                    mBtnDone.setVisibility(View.GONE);

                    /// show the scanner result into dialog box.
                    final AlertDialog.Builder builder = new AlertDialog.Builder(BoardingNDropping.this);
                    builder.setTitle("Result");
                    builder.setMessage(jsonObj.getString("message"));
                    builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int id) {
                            dialogInterface.dismiss();
                            onBackPressed();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    TextView messageText = (TextView) alertDialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Snackbar snackbar = Snackbar.make(_relativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
        }
    }

    private String removeComa(String str) {
        if (str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    // final asyncTask
    private class GetPreConfirmDataAsync extends AsyncTask<String, Void, String> implements OnDismissListener {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            this.cancel(true);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String jsonStr = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8] + params[9] + params[10] + params[11] + params[12]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                jsonStr = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dismissProgressDialog();

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String status = jsonObj.getString("response_status");
                    String msg = jsonObj.getString("message");
                    if (status.equalsIgnoreCase("200")) {
                        JSONObject responseObject = jsonObj.getJSONObject("response_data");
                        String ticketNo = responseObject.getString("ticket_no");
                        String totalAmount = responseObject.getString("total_amount");

                        new GetConfirmDataAsync().execute(getResources().getString(R.string.bus_ticket_url),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&mode=finalseatissue",
                                "&ticket_no=" + ticketNo,
                                "&total_amount=" + totalAmount,
                                "&recieptno=" + mAppHandler.getReceiptNo(),
                                "&format=json",
                                "&securityKey=" + mAppHandler.getTicketToken());
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardingNDropping.this);
                        builder.setTitle("Result");
                        builder.setMessage(msg);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                                onBackPressed();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } // end of GetCOntacts asyncTask


    private class GetConfirmDataAsync extends AsyncTask<String, Void, String> implements OnDismissListener {

        private String mTicketNo;
        private String mPinNO;
        private String mFrom;
        private String mDeparture;
        private String mTo;
        private String mDestination;
        private String mBusName;
        private String mCoachNo;
        private String mCoachType;
        private String mSeatNo;
        private String mRoute;
        private String mJourneyDate;
        private String mTime;
        private String mSeatType;
        private String mTotalFare;
        private String mPurchaseDate;
        private String mPassengerName;
        private String mPhone;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            this.cancel(true);
        }

        @SuppressWarnings("deprecation")
        @Override
        protected String doInBackground(String... params) {
            String jsonStr = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpClient client = AppController.getInstance().getTrustedHttpClient();
            try {
                jsonStr = client.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dismissProgressDialog();
            StringBuilder receipt;

            if (result != null) {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    String str_ticket_status = jsonObj.getString("response_status");
                    JSONArray jsonArray = jsonObj.getJSONArray("response_data");

                    if (str_ticket_status.equals("200")) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            mTicketNo = jo.getString("ticket_no");
                            mPinNO = jo.getString("pin_no");
                            mFrom = jo.getString("from_dist_name");
                            mDeparture = jo.getString("bording_point_title");
                            mTo = jo.getString("to_dist_name");
                            mDestination = jo.getString("droping_point_title");
                            mBusName = jo.getString("company_name");
                            mCoachNo = jo.getString("coach_no");
                            mCoachType = jo.getString("coach_type_title");
                            mSeatNo = jo.getString("seat_number_title");
                            mRoute = jo.getString("route_title");
                            mJourneyDate = jo.getString("journey_date");
                            mTime = jo.getString("schedule_title");
                            mSeatType = jo.getString("seat_type_title");
                            mTotalFare = jo.getString("fare");
                            mPurchaseDate = jo.getString("purchase_date");
                            mPassengerName = jo.getString("buyer_name");
                            mPhone = jo.getString("buyer_mobile");
                        }
                        StringBuilder formatBuilder = new StringBuilder();
                        receipt = formatBuilder.append(mBusName + "\n\n"
                                + getString(R.string.ticket_des) + " " + mTicketNo
                                + "\n" + getString(R.string.pin_no_des) + " " + mPinNO
                                + "\n" + getString(R.string.journey_date_ticket_des) + " " + mJourneyDate
                                + "\n" + getString(R.string.boarding_des) + " " + mTime
                                + "\n" + getString(R.string.passenger_name_des) + " " + mPassengerName
                                + "\n" + getString(R.string.phone_no_des) + " " + mPhone
                                + "\n" + getString(R.string.total_des) + " " + mTotalFare + " " + getString(R.string.tk_with_charge_msg)
                                + "\n" + getString(R.string.from_city_ticket_des) + " " + mFrom
                                + "\n" + getString(R.string.to_city_ticket_des) + " " + mTo
                                + "\n" + getString(R.string.droping_des) + " " + mDeparture
                                + "\n" + getString(R.string.coach_number_des) + " " + mCoachNo
                                + "\n" + getString(R.string.coach_des) + " " + mCoachType
                                + "\n" + getString(R.string.selected_seat_des) + " " + mSeatNo
                                + "\n" + getString(R.string.seat_type_des) + " " + mSeatType
                                + "\n" + getString(R.string.purchase_date_des) + " " + mPurchaseDate);
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardingNDropping.this);
                        builder.setTitle("Result");
                        builder.setMessage(receipt.toString());
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                                startActivity(new Intent(BoardingNDropping.this, ETicketMainActivity.class));
                                finish();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BoardingNDropping.this);
                        builder.setTitle("Result");
                        builder.setMessage(R.string.try_again_msg);
                        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                dialogInterface.dismiss();
                                onBackPressed();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
                        messageText.setGravity(Gravity.CENTER);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } // end of GetCOntacts asyncTask

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
        Intent intent = new Intent(BoardingNDropping.this, ETicketMainActivity.class);
        startActivity(intent);
        finish();
    }
}
