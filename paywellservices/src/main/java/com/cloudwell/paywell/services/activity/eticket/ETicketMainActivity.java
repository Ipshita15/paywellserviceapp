package com.cloudwell.paywell.services.activity.eticket;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.AppLoadingActivity;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.activity.eticket.busticket.BusMainActivity;
import com.cloudwell.paywell.services.activity.eticket.trainticket.TrainMainActivity;

import java.util.List;

/**
 * Created by android on 1/26/2016.
 */
public class ETicketMainActivity extends AppCompatActivity {

    private static final String BUS_TICKET = "Bus Ticket";
    private static final String TRAIN_TICKET = "Train Ticket";
    private RelativeLayout relativeLayout;
    boolean hasBusService = false;
    boolean hasTrainService = false;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eticket_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_eticket);
        relativeLayout = (RelativeLayout) findViewById(R.id.eticketRelativeLayout);
        initView();
    }

    private void initView() {
        List<String> serviceList = AppLoadingActivity.getETicketService();
        for (int i = 0; i < serviceList.size(); i++) {
            if (BUS_TICKET.equalsIgnoreCase(serviceList.get(i))) {
                hasBusService = true;
            } else if (TRAIN_TICKET.equalsIgnoreCase(serviceList.get(i))) {
                hasTrainService = true;
            }
        }
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
        Intent intent = new Intent(ETicketMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Button click handler on Main activity
     *
     * @param v
     */
    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.homeBtnBusTicket:
                if (hasBusService) {
                    startActivity(new Intent(this, BusMainActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            case R.id.homeBtnTrainTicket:
                if (hasTrainService) {
                    startActivity(new Intent(this, TrainMainActivity.class));
                    finish();
                } else {
                    Snackbar snackbar = Snackbar.make(relativeLayout, R.string.services_off_msg, Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                    snackbar.show();
                }
                break;
            default:
                break;
        }
    }
}
