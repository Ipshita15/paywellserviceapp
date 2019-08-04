package com.cloudwell.paywell.services.activity.eticket.busticket;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticket.adapter.SeatListAdapter;
import com.cloudwell.paywell.services.activity.eticket.busticket.model.SeatView;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SeatListActivity extends AppCompatActivity {
    public static final String OPTION_ID = "optionId";
    public static final String COMPANY_NAME = "companyName";
    public static final String ARRIVAL_TIME = "arrivalTime";
    public static final String COACH_TYPE = "coachType";
    public static final String AC = "AC";
    public static final String NON_AC = "NonAC";
    public static final String ALL = "All";
    public static final String FRAGMENT_KEY = "fragmentKey";
    private TextView _totalSeatSelect;
    private TextView _date;
    private TextView _totalPrice;
    private Button _btnResult;
    private String _optionId;
    private AppHandler mAppHandler;
    private ProgressBar _progressBar;
    GridView gridView;

    ArrayList<String> sortedKey;
    JSONObject seatDetails;

    ArrayList<SeatView> seatList = new ArrayList<>();
    SeatView seatView;
    private RelativeLayout _upperLL;
    private RelativeLayout _middleLL;
    private TextView _busName;
    private String _coachType;
    private String _fragmentKey;
    private String _companyName;
    private String _arrivalTime;
    private RelativeLayout _relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_list);
        getSupportActionBar().setTitle(R.string.seat_list_ticket_msg);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        _relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        _upperLL = (RelativeLayout) findViewById(R.id.upperLL);
        _middleLL = (RelativeLayout) findViewById(R.id.middleLL);
        _progressBar = (ProgressBar) findViewById(R.id.progressbar);

        _date = (TextView) findViewById(R.id.tvJourneyDate);
        _date.setTypeface(AppController.getInstance().getOxygenLightFont());

        _busName = (TextView) findViewById(R.id.tvBusName);
        _busName.setTypeface(AppController.getInstance().getOxygenLightFont());

        ((TextView) _relativeLayout.findViewById(R.id.tvSeat)).setTypeface(AppController.getInstance().getOxygenLightFont());
        _totalSeatSelect = (TextView) findViewById(R.id.tvTotalSeatSelect);
        _totalSeatSelect.setTypeface(AppController.getInstance().getOxygenLightFont());

        ((TextView) _relativeLayout.findViewById(R.id.tvPrice)).setTypeface(AppController.getInstance().getOxygenLightFont());
        _totalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        _totalPrice.setTypeface(AppController.getInstance().getOxygenLightFont());

        _btnResult = (Button) findViewById(R.id.btnResult);
        _btnResult.setTypeface(AppController.getInstance().getOxygenLightFont());

        gridView = (GridView) findViewById(R.id.gridSeat);
        gridView.setTextFilterEnabled(true);

        _upperLL.setVisibility(View.GONE);
        gridView.setVisibility(View.GONE);
        _middleLL.setVisibility(View.GONE);
        _btnResult.setVisibility(View.GONE);
        _progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            _optionId = bundle.getString(OPTION_ID);
            _companyName = bundle.getString(COMPANY_NAME);
            _arrivalTime = bundle.getString(ARRIVAL_TIME);
            _coachType = bundle.getString(COACH_TYPE);
            _fragmentKey = bundle.getString(FRAGMENT_KEY);
            _date.setText(getString(R.string.time_des) + " " + _arrivalTime);
            _busName.setText(_companyName);
        }
        mAppHandler = AppHandler.getmInstance(getApplicationContext());
        new GetSeatsAsync().execute(getResources().getString(R.string.bus_ticket_url),
                "imei=" + mAppHandler.getImeiNo(),
                "&option_id=" + _optionId,
                "&mode=seatdetails",
                "&format=json",
                "&securityKey=" + mAppHandler.getTicketToken());
    }

    private class GetSeatsAsync extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonStr = null;
            HttpGet request = new HttpGet(params[0] + params[1] + params[2] + params[3] + params[4] + params[5]);
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
                    parseJsonObjects(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            _progressBar.setVisibility(View.GONE);
            _upperLL.setVisibility(View.VISIBLE);
            gridView.setVisibility(View.VISIBLE);
            _middleLL.setVisibility(View.VISIBLE);
            _btnResult.setVisibility(View.VISIBLE);
        }
    }

    private void parseJsonObjects(String response) {

        sortedKey = new ArrayList<>();
        try {
            JSONObject jsonObj = new JSONObject(response);
            String str_noCols = jsonObj.getString("column_no");
            gridView.setNumColumns(Integer.valueOf(str_noCols) - 1);
            seatDetails = jsonObj.getJSONObject("seatstructure_details");

            Iterator keys = seatDetails.keys();
            while (keys.hasNext()) {
                String _key = (String) keys.next();
                sortedKey.add(_key);
            }
            Collections.sort(sortedKey);
            sortedKey.size();
            for (String k : sortedKey) {
                JSONObject jsonObject = seatDetails.getJSONObject(k);
                seatView = new SeatView();
                seatView.setSeatName(k);
                seatView.setX_axis(jsonObject.getString("x_axis"));
                seatView.setY_axis(jsonObject.getString("y_axis"));
                seatView.setSeat_type_id(jsonObject.getString("seat_type_id"));
                seatView.setStatus(jsonObject.getString("status"));
                seatView.setSeat_structure_id(jsonObject.getString("seat_structure_id"));
                seatView.setFare(jsonObject.getString("fare"));
                seatView.setSeat_no(jsonObject.getString("seat_no"));
                seatView.setSeatid(jsonObject.getString("seatid"));

                seatList.add(seatView);
            }
            SeatListAdapter adapter = new SeatListAdapter(SeatListActivity.this, seatList, _totalSeatSelect, _totalPrice, mAppHandler);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickDoneButton(View view) {
        String selectedSeat = mAppHandler.getNumberOfSeat();
        if (selectedSeat.equalsIgnoreCase("unknown") || selectedSeat.equalsIgnoreCase("0")) {
            Snackbar snackbar = Snackbar.make(view, R.string.seat_select_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
            return;
        }

        if (Integer.parseInt(selectedSeat) > 4) {
            Snackbar snackbar = Snackbar.make(view, R.string.seat_limit_select_error_msg, Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
            snackbar.show();
            return;
        }

        String date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTarget = new SimpleDateFormat(getResources().getString(R.string.date_format));
            date = formatTarget.format(format.parse(mAppHandler.getJourneyDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SeatListActivity.this);
        builder.setTitle(R.string.confirm_title_msg);

        builder.setMessage(_busName.getText()
                + "\n" + getString(R.string.journey_date_ticket_des) + " " + date + " , " + _date.getText()
                + "\n" + getString(R.string.coach_des) + " " + _coachType
                + "\n" + getString(R.string.selected_seat_des) + " " + "\n"
                + mAppHandler.getSelectedSeatAndPrice()
                + "\n" + getString(R.string.total_des) + " " + _totalPrice.getText().toString() + "\n");
        builder.setPositiveButton(R.string.okay_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                dialogInterface.dismiss();
                Intent intent = new Intent(SeatListActivity.this, BoardingNDropping.class);
                intent.putExtra(BoardingNDropping.OPTION_ID, _optionId);
                intent.putExtra(BoardingNDropping.COMPANY_NAME, _busName.getText().toString());
                intent.putExtra(BoardingNDropping.ARRIVAL_TIME, _date.getText().toString());
                intent.putExtra(BoardingNDropping.COACH_TYPE, _coachType);
                startActivity(intent);
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
        Intent intent = new Intent(SeatListActivity.this, SearchBusActivity.class);

        if (_fragmentKey.equalsIgnoreCase(NON_AC))
            intent.putExtra("tab_position", 2);
        else if (_fragmentKey.equalsIgnoreCase(AC))
            intent.putExtra("tab_position", 1);
        else
            intent.putExtra("tab_position", 0);
        startActivity(intent);
        finish();
    }
}

