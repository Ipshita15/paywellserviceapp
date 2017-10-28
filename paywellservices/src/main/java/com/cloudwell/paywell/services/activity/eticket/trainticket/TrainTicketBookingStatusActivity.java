package com.cloudwell.paywell.services.activity.eticket.trainticket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.ETicketMainActivity;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class TrainTicketBookingStatusActivity extends AppCompatActivity {

    public static final String STATION_FROM = "stationFrom";
    public static final String STATION_TO = "stationTo";
    public static final String JOURNEY_DATE = "journeyDate";
    public static final String TICKET_COLLECT_DATELINE = "ticketCollectDateline";
    public static final String DEPARTURE_TIME = "departureTime";
    public static final String TRAIN_NAME = "trainName";
    public static final String TRAIN_NO = "trainNo";
    public static final String COACH_NO = "coachNo";
    public static final String SEAT_NO = "seatNo";
    public static final String TRX_ID = "trxId";
    public static final String BL_TRAIN_TRX_ID = "blTrainTrxId";
    public static final String FARE = "fare";
    public static final String TOTAL_FARE = "totalFare";
    public static final String SERVICE_CHARGE = "serviceCharge";
    public static final String CUSTOMER_MSISDN = "customerMSISDN";
    public static final String TOTAL_SUM = "totalSum";
    public static final String CELL_NUMBER = "cellNumber";
    public static final String PNR = "pnr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_booking);
        initView();
    }

    private void initView() {
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.linearLayout);
        assert relativeLayout != null;
        ((TextView) relativeLayout.findViewById(R.id.tvPwHeader)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvDate = (TextView) findViewById(R.id.tvDate);
        assert tvDate != null;
        tvDate.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTime = (TextView) findViewById(R.id.tvTime);
        assert tvTime != null;
        tvTime.setTypeface(AppController.getInstance().getRobotoRegularFont());
        ((TextView) relativeLayout.findViewById(R.id.tvTrainTicketHeader)).setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTrxId = (TextView) findViewById(R.id.tvTrxId);
        assert tvTrxId != null;
        tvTrxId.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvSourceStation = (TextView) findViewById(R.id.tvSourceStation);
        assert tvSourceStation != null;
        tvSourceStation.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvDestinationStation = (TextView) findViewById(R.id.tvDestStation);
        assert tvDestinationStation != null;
        tvDestinationStation.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvJourneyDate = (TextView) findViewById(R.id.tvJourneyDate);
        assert tvJourneyDate != null;
        tvJourneyDate.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvDepartureTime = (TextView) findViewById(R.id.tvDepTime);
        assert tvDepartureTime != null;
        tvDepartureTime.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTrainNameAndNo = (TextView) findViewById(R.id.tvTrainNameAndNo);
        assert tvTrainNameAndNo != null;
        tvTrainNameAndNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvCoachNo = (TextView) findViewById(R.id.tvCoachNo);
        assert tvCoachNo != null;
        tvCoachNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvSeatNo = (TextView) findViewById(R.id.tvSeatNo);
        tvSeatNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvFare = (TextView) findViewById(R.id.tvFare);
        assert tvFare != null;
        tvFare.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTotalFare = (TextView) findViewById(R.id.tvTotalFare);
        assert tvTotalFare != null;
        tvTotalFare.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvServiceCharge = (TextView) findViewById(R.id.tvServiceCharge);
        assert tvServiceCharge != null;
        tvServiceCharge.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        assert tvTotalAmount != null;
        tvTotalAmount.setTypeface(AppController.getInstance().getRobotoRegularFont());

        TextView tvCellNo = (TextView) findViewById(R.id.tvCellNo);
        assert tvCellNo != null;
        tvCellNo.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvETicketNumber = (TextView) findViewById(R.id.tvETicketNo);
        assert tvETicketNumber != null;
        tvETicketNumber.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvTicketCollectDateline = (TextView) findViewById(R.id.tvTicketCollectDateline);
        assert tvTicketCollectDateline != null;
        tvTicketCollectDateline.setTypeface(AppController.getInstance().getRobotoRegularFont());
        TextView tvHotLine = (TextView) findViewById(R.id.tvPwHotLine);
        assert tvHotLine != null;
        tvHotLine.setTypeface(AppController.getInstance().getRobotoRegularFont());

        Bundle bundle = getIntent().getExtras();
        String stationFrom = null;
        String stationTo = null;
        String journeyDate = null;
        String ticketCollectDateline = null;
        String departureTime = null;
        String trainName = null;
        String trainNo = null;
        String coachNo = null;
        String seatNo = null;
        String trxId = null;
        String fare = null;
        String totalFare = null;
        String serviceCharge = null;
        String totalSum = null;
        String cellNumber = null;
        String pnr = null;
        if (bundle != null) {
            stationFrom = bundle.getString(STATION_FROM);
            stationTo = bundle.getString(STATION_TO);
            journeyDate = bundle.getString(JOURNEY_DATE);
            ticketCollectDateline = bundle.getString(TICKET_COLLECT_DATELINE);
            departureTime = bundle.getString(DEPARTURE_TIME);
            trainName = bundle.getString(TRAIN_NAME);
            trainNo = bundle.getString(TRAIN_NO);
            coachNo = bundle.getString(COACH_NO);
            seatNo = bundle.getString(SEAT_NO);
            trxId = bundle.getString(TRX_ID);
            fare = bundle.getString(FARE);
            totalFare = bundle.getString(TOTAL_FARE);
            serviceCharge = bundle.getString(SERVICE_CHARGE);
            totalSum = bundle.getString(TOTAL_SUM);
            cellNumber = bundle.getString(CELL_NUMBER);
            pnr = bundle.getString(PNR);
        }

        tvDate.setText(String.format("%s %s", getString(R.string.date_des) + " ", AppHandler.getCurrentDate()));
        tvTime.setText(String.format("%s %s", getString(R.string.time_des) + " ", AppHandler.getCurrentTime()));
        tvTrxId.setText(String.format("%s %s", getString(R.string.trx_id_des) + " ", trxId));
        tvSourceStation.setText(String.format("%s %s", getString(R.string.from_city_ticket_des) + " ", stationFrom));
        tvDestinationStation.setText(String.format("%s %s", getString(R.string.to_city_ticket_des) + " ", stationTo));

        tvJourneyDate.setText(String.format("%s %s", getString(R.string.journey_date_ticket_des) + " ", journeyDate));
        tvDepartureTime.setText(String.format("%s %s", getString(R.string.boarding_des) + " ", departureTime));

        tvTrainNameAndNo.setText(String.format("%s %s/%s", getString(R.string.train_name_and_number_des) + " ", trainName, trainNo));
        tvCoachNo.setText(String.format("%s %s", getString(R.string.coach_number_des) + " ", coachNo));
        tvSeatNo.setText(String.format("%s %s", getString(R.string.selected_seat_des) + " ", seatNo));
        tvFare.setText(String.format("%s %s %s", getString(R.string.amount_des) + " ", fare, getString(R.string.tk)));
        tvTotalFare.setText(String.format("%s %s %s", getString(R.string.total_fare_des) + " ", totalFare, getString(R.string.tk)));
        tvServiceCharge.setText(String.format("%s %s %s", getString(R.string.service_carge_des) + " ", serviceCharge, getString(R.string.tk)));
        tvTotalAmount.setText(String.format("%s %s %s", getString(R.string.total_des) + " ", totalSum, getString(R.string.tk)));
        tvCellNo.setText(String.format("%s %s", getString(R.string.phone_no_des) + " ", cellNumber));
        tvETicketNumber.setText(String.format("%s %s", getString(R.string.ticket_des) + " ", pnr));
        tvTicketCollectDateline.setText(String.format("%s %s", getString(R.string.collect_before_des) + " ", ticketCollectDateline));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        Intent intent = new Intent(TrainTicketBookingStatusActivity.this, ETicketMainActivity.class);
        startActivity(intent);
        finish();
    }
}
