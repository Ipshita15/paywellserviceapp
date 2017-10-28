package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BusMainActivity extends AppCompatActivity {

    private static final int CITY_CODE_FROM = 1;
    private static final int CITY_CODE_TO = 2;
    private static final int CODE_CALENDAR = 3;
    private LinearLayout mLinearlayout;
    private TextView _tvCityFrom;
    private TextView _tvCityTo;
    private TextView _tvDate;
    private TextView _tvToday;
    private TextView _tvHiddenDate;
    private ConnectionDetector cd;
    private String defaultDate;
    private String cityIdFrom;
    private String cityIdTo;
    private AppHandler mAppHandler;
    private Button _btnSearch;
    LinearLayout llCityFrom, llCityTo, llCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_eticket_bus);

        mAppHandler = new AppHandler(BusMainActivity.this);

        mLinearlayout = (LinearLayout) findViewById(R.id.linearLayout);
        llCityFrom = (LinearLayout) findViewById(R.id.llFrom);
        llCityTo = (LinearLayout) findViewById(R.id.llTo);
        llCalendar = (LinearLayout) findViewById(R.id.llCalendar);

        ((TextView) findViewById(R.id.tvFrom)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) findViewById(R.id.tvTo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) findViewById(R.id.tvJourneyDate)).setTypeface(AppController.getInstance().getRobotoRegularFont());

        _tvCityFrom = (TextView) findViewById(R.id.tvCityFrom);
        _tvCityTo = (TextView) findViewById(R.id.tvCityTo);
        _tvDate = (TextView) findViewById(R.id.tvDate);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _tvCityFrom.setText(mAppHandler.getCityFrom());
            cityIdFrom = mAppHandler.getCityIdFrom();
            _tvCityTo.setText(mAppHandler.getCityTo());
            cityIdTo = mAppHandler.getCityIdTo();
        }
        initView();
    }

    private void initView() {
        _tvToday = (TextView) findViewById(R.id.tvToday);
        _tvHiddenDate = (TextView) findViewById(R.id.tvHiddenDate);
        _btnSearch = (Button) findViewById(R.id.btnSearch);

        _tvCityFrom.setTypeface(AppController.getInstance().getRobotoRegularFont());
        _tvCityTo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        _tvDate.setTypeface(AppController.getInstance().getRobotoRegularFont());
        _tvToday.setTypeface(AppController.getInstance().getRobotoRegularFont());
        _tvHiddenDate.setTypeface(AppController.getInstance().getRobotoRegularFont());
        _btnSearch.setTypeface(AppController.getInstance().getRobotoRegularFont());

        cd = new ConnectionDetector(BusMainActivity.this.getApplicationContext());
        llCityFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Intent intent = new Intent(BusMainActivity.this, FromCityListActivity.class);
                    startActivityForResult(intent, CITY_CODE_FROM);
                    _tvCityTo.setText("");
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });

        llCityTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    if (cityIdFrom != null) {
                        Intent intent = new Intent(BusMainActivity.this, ToCityListActivity.class);
                        intent.putExtra(ToCityListActivity.CITY_ID_FROM, cityIdFrom);
                        startActivityForResult(intent, CITY_CODE_TO);
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.journey_date_select_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }

        });

        llCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet()) {
                    Intent intnt = new Intent();
                    intnt.setClass(BusMainActivity.this, JourneyDateActivity.class);
                    startActivityForResult(intnt, CODE_CALENDAR);
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });

        GregorianCalendar today_month = (GregorianCalendar) GregorianCalendar.getInstance();
        DateFormat df = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
        defaultDate = df.format(today_month.getTime());
        populateDate(defaultDate);
        _tvHiddenDate.setText(defaultDate);

        _btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityIdFrom != null && cityIdTo != null && (_tvHiddenDate.getText() != null) && _tvCityFrom.getText().toString() != null && _tvCityTo.getText().toString() != null) {
                    String date = "";
                    try {
                        String str_input = _tvHiddenDate.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat(getResources().getString(R.string.date_format), Locale.US);
                        SimpleDateFormat formatTarget = new SimpleDateFormat(getResources().getString(R.string.time_format), Locale.US);
                        date = formatTarget.format(format.parse(str_input));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (cd.isConnectingToInternet()) {
                        mAppHandler.setCityFrom(_tvCityFrom.getText().toString());
                        mAppHandler.setCityIdFrom(cityIdFrom);
                        mAppHandler.setCityTo(_tvCityTo.getText().toString());
                        mAppHandler.setCityIdTo(cityIdTo);
                        mAppHandler.setJourneyDate(date);
                        new BusMainActivity.GetSearchedBusesAsync().execute(getResources().getString(R.string.bus_ticket_url),
                                "imei=" + mAppHandler.getImeiNo(),
                                "&mode=businformation",
                                "&from_id=" + mAppHandler.getCityIdFrom(),
                                "&to_id=" + mAppHandler.getCityIdTo(),
                                "&jdate=" + mAppHandler.getJourneyDate(),
                                "&format=json",
                                "&securityKey=" + mAppHandler.getTicketToken());
                    } else {
                        Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                        View snackBarView = snackbar.getView();
                        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                        snackbar.show();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(mLinearlayout, R.string.selection_error_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
            }
        });
    }

    private class GetSearchedBusesAsync extends AsyncTask<String, Void, String> implements DialogInterface.OnDismissListener {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(BusMainActivity.this, "", getString(R.string.connection_error_msg), true);
            if (!isFinishing())
                progressDialog.show();
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
            progressDialog.cancel();
            mAppHandler.setSearchBus(result);
            Intent intent = new Intent(BusMainActivity.this, SearchBusActivity.class);
            startActivity(intent);

        }

    }

    private void populateDate(String date) {
        _tvDate.setText(date);
        if (date.equals(defaultDate)) {
            _tvToday.setText(R.string.today_msg);
            _tvToday.setVisibility(View.VISIBLE);
        } else {
            _tvToday.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CITY_CODE_FROM) { //from City
                String cityFrom = data.getStringExtra(FromCityListActivity.CITY_NAME);
                _tvCityFrom.setText(cityFrom);
                cityIdFrom = data.getStringExtra(FromCityListActivity.CITY_ID);

            } else if (requestCode == CITY_CODE_TO) { //To city
                String cityTo = data.getStringExtra(ToCityListActivity.CITY_NAME);
                _tvCityTo.setText(cityTo);
                cityIdTo = data.getStringExtra(ToCityListActivity.CITY_ID);
            } else if (requestCode == CODE_CALENDAR) { //calender
                String calenderDate = data.getStringExtra("result_calender");
                _tvHiddenDate.setText(calenderDate);
                populateDate(data.getStringExtra("result_calender"));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("My_fromCity", _tvCityFrom.getText().toString());
        savedInstanceState.putString("My_toCity", _tvCityTo.getText().toString());
        savedInstanceState.putString("My_calenderResult", _tvHiddenDate.getText().toString());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(BusMainActivity.this, ETicketMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
