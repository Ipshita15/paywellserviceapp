package com.cloudwell.paywell.services.activity.eticket.busticketNew.search;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Bus;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;

import java.util.ArrayList;
import java.util.List;

public class BusSelectActivity extends BusTricketBaseActivity {

    private Spinner busListSpinner;
    private ArrayAdapter<String> busListAdapter;
    private ArrayList<String> busNameList = new ArrayList<>();
    private ArrayList<Bus> busList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_select);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        busListSpinner = findViewById(R.id.busListSP);
        busListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, busNameList);
        busListSpinner.setAdapter(busListAdapter);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        new BusTicketRepository().getBusList().observeForever(new Observer<List<Bus>>() {
            @Override
            public void onChanged(@Nullable List<Bus> buses) {
                busList = (ArrayList<Bus>) buses;
                if (buses != null && buses.size() > 0) {
                    for (int a = 0; a < buses.size(); a++) {
                        busNameList.add(buses.get(a).getBusname());
                    }
                    busListAdapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        });
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

    public void goToSearchBusTicket(View view) {
        startActivity(new Intent(this, BusCitySearchActivity.class));
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
