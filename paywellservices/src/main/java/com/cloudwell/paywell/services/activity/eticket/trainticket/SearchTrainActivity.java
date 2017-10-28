package com.cloudwell.paywell.services.activity.eticket.trainticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
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

public class SearchTrainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RelativeLayout mRelativeLayout;
    private TextView mTvNoOfPassenger;
    private EditText mEtPassengerAge;
    private Button mBtnSearchTrain;
    private String mNoOfPassenger;
    private ConnectionDetector mCd;
    private AppHandler mAppHandler;

    private String mDestCode;
    private String mPassengerCode;
    private LinearLayout mLLAgeOfPassenger;
    private String mDestinationName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_train);
        getSupportActionBar().setTitle(R.string.home_eticket_train);
        initView();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        Spinner mSpinnerDestination = (Spinner) findViewById(R.id.spinnerDestination);
        Spinner mSpinnerPassengerType = (Spinner) findViewById(R.id.spinnerPassengerType);
        mTvNoOfPassenger = (TextView) findViewById(R.id.tvNoOfPassenger);
        mLLAgeOfPassenger = (LinearLayout) findViewById(R.id.llPassengerAge);
        mEtPassengerAge = (EditText) findViewById(R.id.etPassengerAge);
        mBtnSearchTrain = (Button) findViewById(R.id.btnSearchTrain);

        ((TextView) mRelativeLayout.findViewById(R.id.tvStationTo)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvPassengerType)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvPassenger)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mTvNoOfPassenger.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) mRelativeLayout.findViewById(R.id.tvPassengerAge)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        mEtPassengerAge.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mBtnSearchTrain.setTypeface(AppController.getInstance().getRobotoRegularFont());
        // Spinner click listener
        assert mSpinnerDestination != null;
        mSpinnerDestination.setOnItemSelectedListener(this);
        assert mSpinnerPassengerType != null;
        mSpinnerPassengerType.setOnItemSelectedListener(this);
        // Button click listener
        mBtnSearchTrain.setOnClickListener(this);

        mAppHandler = new AppHandler(this);
        // Creating adapter for spinner
        assert mSpinnerDestination != null;
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAppHandler.getDestinationStations()) {
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
        mSpinnerDestination.setAdapter(destinationAdapter);

        assert mSpinnerPassengerType != null;
        ArrayAdapter<String> passengerTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mAppHandler.getPassengerTypes()) {
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
        mSpinnerPassengerType.setAdapter(passengerTypeAdapter);

        mCd = new ConnectionDetector(getApplicationContext());
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent(SearchTrainActivity.this, TrainTicketActivity.class);
        intent.putExtra("back","back");
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.spinnerDestination:
                mDestCode = mAppHandler.getDestinationStationCodes().get(position);
                mDestinationName = mAppHandler.getDestinationStations().get(position);
                break;
            case R.id.spinnerPassengerType:
                mNoOfPassenger = mAppHandler.getPassengers().get(position);
                mPassengerCode = mAppHandler.getPassengerCodes().get(position);
                mTvNoOfPassenger.setText(mNoOfPassenger);
                if (mNoOfPassenger.equalsIgnoreCase("1"))
                    mLLAgeOfPassenger.setVisibility(View.VISIBLE);
                else
                    mLLAgeOfPassenger.setVisibility(View.GONE);
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
        if (v == mBtnSearchTrain) {
            if (!mCd.isConnectingToInternet()) {
                AppHandler.showDialog(getSupportFragmentManager());
            } else {
                try {
                    new TrainAsync().execute(getResources().getString(R.string.train_source_url),
                            "imei_no=" + mAppHandler.getImeiNo(),
                            "&mode=train_list",
                            "&source_code=" + URLEncoder.encode(mAppHandler.getSourceStationCode(), "UTF-8"),
                            "&destination=" + URLEncoder.encode(mAppHandler.getDestinationName(), "UTF-8"),
                            "&destination_code=" + URLEncoder.encode(mDestCode, "UTF-8")); //  imei_no=a10000289bbb5d&pin_code=1234&mode=train_list&source_code=DA$DHAKA%20KAMALAPUR&destination=CHI&destination_code=CTG$CHITTAGONG
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class TrainAsync extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(SearchTrainActivity.this, "", getString(R.string.loading_msg), true);
            if (!isFinishing())
                progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseTxt = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5]);
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
            ArrayList<String> trainNames = new ArrayList<>();
            ArrayList<String> trainCodes = new ArrayList<>();
            ArrayList<String> classTypes = new ArrayList<>();
            ArrayList<String> classCode = new ArrayList<>();
            if (result != null) {
                if (result.startsWith("200")) {
                    String[] spiltArray = result.split("200");
                    String[] subSpiltArray = spiltArray[1].split("@");
                    String[] subSpiltArray2 = spiltArray[2].split("@");
                    for (int i = 2; i < subSpiltArray.length; i++) {
                        String trainArray = subSpiltArray[i];
                        if (i % 2 != 0)
                            trainNames.add(trainArray);
                        else
                            trainCodes.add(trainArray);
                    }
                    for (int j = 2; j < subSpiltArray2.length; j++) {
                        String classArray = subSpiltArray2[j];
                        if (j % 2 != 0) {
                            classTypes.add(classArray);
                        } else {
                            if (!classArray.contains("###"))
                                classCode.add(classArray);
                        }
                    }

                    mAppHandler.setDestinationStation(mDestinationName);
                    mAppHandler.setDestinationStationCode(mDestCode);
                    mAppHandler.setTrainNames(trainNames);
                    mAppHandler.setTrainCodes(trainCodes);
                    mAppHandler.setClassTypes(classTypes);
                    mAppHandler.setClassTypeCodes(classCode);
                    mAppHandler.setNoOfPassenger(mNoOfPassenger);
                    mAppHandler.setAgeOfPassenger(mEtPassengerAge.getText().toString().trim());
                    mAppHandler.setPassengerCode(mPassengerCode);
                    Intent intent = new Intent(SearchTrainActivity.this, TrainTicketBookingActivity.class);
                    startActivity(intent);
                } else {
                    Snackbar snackbar = Snackbar.make(mRelativeLayout, result.split("@")[1], Snackbar.LENGTH_LONG);
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
}
