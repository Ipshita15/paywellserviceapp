package com.cloudwell.paywell.services.activity.eticket.trainticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.ConnectionDetector;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by android on 5/17/2016.
 */
public class TrainTicketActivity extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button mBtnSource;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;
    private String mSourceCode;
    private RelativeLayout mrelativeLayout;
    private String mDestination;
    private String mSource;
    private AutoCompleteTextView mACTVDestination;
    String[] destination_suggession;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_ticket);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.home_eticket_air);
        }

        // Creating adapter for spinner
        mCd = new ConnectionDetector(AppController.getContext());
        mAppHandler = AppHandler.getmInstance(getApplicationContext());

        mACTVDestination = (AutoCompleteTextView) findViewById(R.id.etDestination);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mACTVDestination.setText(mAppHandler.getDestinationName());
        }
        initView();
    }

    private void initView() {
        mrelativeLayout = findViewById(R.id.linearLayout);
        Spinner mSpinnerSources = findViewById(R.id.spinnerSources);
        //mEtDestination = findViewById(R.id.etDestination);
        mBtnSource = findViewById(R.id.btnSource);
        // Spinner click listener
        assert mSpinnerSources != null;
        mSpinnerSources.setOnItemSelectedListener(this);
        // Button click listener
        mBtnSource.setOnClickListener(this);

        ((TextView) mrelativeLayout.findViewById(R.id.tvStationFrom)).setTypeface(AppController.getInstance().getOxygenLightFont());
        ((TextView) mrelativeLayout.findViewById(R.id.tvSourceStation)).setTypeface(AppController.getInstance().getOxygenLightFont());
        //mEtDestination.setTypeface(AppController.getInstance().getRobotoRegularFont());

        mACTVDestination.setTypeface(AppController.getInstance().getOxygenLightFont());
        destination_suggession = getResources().getStringArray(R.array.train_destination_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, destination_suggession);
        mACTVDestination.setAdapter(adapter);

        mBtnSource.setTypeface(AppController.getInstance().getOxygenLightFont());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAppHandler.getSources()) {
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
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                ((TextView) view).setAllCaps(false);
//                }
                return view;
            }
        };
        // Drop down layout style - list terms_and_conditions_format with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        mSpinnerSources.setAdapter(dataAdapter);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
//        String item = parent.getItemAtPosition(position).toString();
        mSourceCode = mAppHandler.getSourceCodes().get(position);
        mSource = mAppHandler.getSources().get(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBackPressed() {
        Intent intent = new Intent(TrainTicketActivity.this, TrainMainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnSource) {
            mDestination = mACTVDestination.getText().toString().trim();
            if (mDestination.length() == 0 || mDestination.length() < 3) {
                mACTVDestination.setError(Html.fromHtml("<font color='red'>" + getString(R.string.ticket_destination_error_msg) + "</font>"));
            } else {
                if (!mCd.isConnectingToInternet()) {
                    AppHandler.showDialog(getSupportFragmentManager());
                } else {
                    try {
                        new DestinationAsync().execute(getResources().getString(R.string.train_source_url),
                                "imei_no=" + mAppHandler.getImeiNo(),
                                "&mode=destination_list",
                                "&source_code=" + URLEncoder.encode(mSourceCode, "UTF-8"),
                                "&destination=" + URLEncoder.encode(mDestination.toUpperCase(), "UTF-8")); //  imei_no=a10000289bbb5d&pin_code=1234&mode=destination_list&source_code=DA$DHAKA%20KAMALAPUR&destination=CHI
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private class DestinationAsync extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            showProgressDialog();
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
            dismissProgressDialog();
            ArrayList<String> destinationStations = new ArrayList<>();
            ArrayList<String> destinationStationCodes = new ArrayList<>();
            ArrayList<String> passengerCodes = new ArrayList<>();
            ArrayList<String> passengerTypes = new ArrayList<>();
            ArrayList<String> passengers = new ArrayList<>();
            if (result != null) {
                if (result.startsWith("200")) {
                    String[] spiltArray = result.split("200");
                    String[] subSpiltArray = spiltArray[1].split("@");
                    String[] subSpiltArray2 = spiltArray[2].split("@");
                    for (int i = 2; i < subSpiltArray.length; i++) {
                        String destinationArray = subSpiltArray[i];
                        if (i % 2 != 0) {
                            destinationStations.add(destinationArray);
                        } else {
                            destinationStationCodes.add(destinationArray);
                        }
                    }

                    // for passenger codes
                    passengerCodes.add(subSpiltArray2[2]);
                    passengerCodes.add(subSpiltArray2[5]);
                    passengerCodes.add(subSpiltArray2[8]);
                    passengerCodes.add(subSpiltArray2[11]);
                    passengerCodes.add(subSpiltArray2[14]);
                    passengerCodes.add(subSpiltArray2[17]);
                    passengerCodes.add(subSpiltArray2[20]);
                    // for passenger types
                    passengerTypes.add(subSpiltArray2[3]);
                    passengerTypes.add(subSpiltArray2[6]);
                    passengerTypes.add(subSpiltArray2[9]);
                    passengerTypes.add(subSpiltArray2[12]);
                    passengerTypes.add(subSpiltArray2[15]);
                    passengerTypes.add(subSpiltArray2[18]);
                    passengerTypes.add(subSpiltArray2[21]);
                    // for passengers
                    passengers.add(subSpiltArray2[4]);
                    passengers.add(subSpiltArray2[7]);
                    passengers.add(subSpiltArray2[10]);
                    passengers.add(subSpiltArray2[13]);
                    passengers.add(subSpiltArray2[16]);
                    passengers.add(subSpiltArray2[19]);
                    passengers.add(subSpiltArray2[22]);

                    mAppHandler.setSourceStation(mSource);
                    mAppHandler.setSourceStationCode(mSourceCode);
                    mAppHandler.setDestinationName(mDestination);
                    mAppHandler.setDestinationStations(destinationStations);
                    mAppHandler.setDestinationStationCodes(destinationStationCodes);
                    mAppHandler.setPassengers(passengers);
                    mAppHandler.setPassengerCodes(passengerCodes);
                    mAppHandler.setPassengerTypes(passengerTypes);
                    Intent intent = new Intent(TrainTicketActivity.this, SearchTrainActivity.class);
                    startActivity(intent);

                } else {
                    Snackbar snackbar = Snackbar.make(mrelativeLayout, result.split("@")[1], Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50")); // snackbar background color
                    snackbar.show();
                }
            } else {
                Snackbar snackbar = Snackbar.make(mrelativeLayout, R.string.try_again_msg, Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                snackbar.show();
            }
        }
    }
}
