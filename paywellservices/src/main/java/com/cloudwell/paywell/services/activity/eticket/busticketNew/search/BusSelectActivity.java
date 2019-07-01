package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.eventBus.model.MessageToBottom;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class BusSelectActivity extends BusTricketBaseActivity {

    private Spinner busListSpinner;
    private ArrayAdapter<String> busListAdapter;
    private ArrayList<String> busNameList = new ArrayList<>();
    private ArrayList<Bus> busList = new ArrayList<>();
    private BusTicketRepository mBusTicketRepository;

    CardView cardLayout;
    mehdi.sakout.fancybuttons.FancyButton btn_next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_select);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        cardLayout = findViewById(R.id.cardLayout);
        btn_next = findViewById(R.id.btn_next);

        busListSpinner = findViewById(R.id.busListSP);
        busListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, busNameList);
        busListSpinner.setAdapter(busListAdapter);

        showProgressDialog();

        if (isInternetConnection()) {
            callGetBusListAPI();
        } else {
            showNoInternetConnectionFound();
        }

        busListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AppStorageBox.put(BusSelectActivity.this, AppStorageBox.Key.BUS_ID, busList.get(i).getBusid());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalApplicationBus.getBus().register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        GlobalApplicationBus.getBus().unregister(this);
    }

    private void callGetBusListAPI() {
        mBusTicketRepository = new BusTicketRepository();
        mBusTicketRepository.getBusList().observeForever(new Observer<List<Bus>>() {
            @Override
            public void onChanged(@Nullable List<Bus> buses) {
                busList = (ArrayList<Bus>) buses;
                if (buses != null && buses.size() > 0) {
                    for (int a = 0; a < buses.size(); a++) {
                        busNameList.add(buses.get(a).getBusname());
                    }
                    busListAdapter.notifyDataSetChanged();
                    dismissProgressDialog();
                }
            }
        });

    }

    public void goToSearchBusTicket(View view) {

        String trasnportID = (String) AppStorageBox.get(BusSelectActivity.this, AppStorageBox.Key.BUS_ID);
        showProgressDialog();
        mBusTicketRepository.getBusScheduleDate(trasnportID).observeForever(aBoolean -> {
            dismissProgressDialog();
            if (aBoolean) {
                startActivity(new Intent(getApplicationContext(), BusCitySearchActivity.class));
            }
        });


    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Subscribe
    public void getAuthError(MessageToBottom messageToBottom) {

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog();
                cardLayout.setVisibility(View.INVISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                showDialogMesssageWithFinished(getString(R.string.services_alert_msg));
            }
        });


    }
}
