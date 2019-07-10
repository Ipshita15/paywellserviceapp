package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransactionLog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusTransactionModel;

import java.util.ArrayList;
import java.util.Arrays;

public class BusTransactionLogActivity extends AppCompatActivity {

    RecyclerView busTransactionRV;
    ArrayList allDataArrayList = new ArrayList();
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_transaction_log);
        busTransactionRV = findViewById(R.id.busTransactionLogRV);
        busTransactionRV.setLayoutManager(new LinearLayoutManager(this));
        BusTransactionModel busTransactionModel = new BusTransactionModel("2343345", "booked", "123", "male", "Rahim", "11-12-2019", "Dhaka", "Barisal", new ArrayList<>(Arrays.asList("A1", "A2", "A3")), "1200", "Green Line");
        ArrayList<BusTransactionModel> busTransactionModels = new ArrayList<>();
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);
        busTransactionModels.add(busTransactionModel);

        for (BusTransactionModel datum : busTransactionModels) {
            if (!date.equals(datum.getDepartureDate())) {
                date = datum.getDepartureDate();
                allDataArrayList.add(date);
            }
            allDataArrayList.add(datum);
        }

        busTransactionRV.setAdapter(new BusTransactionLogAdapter(allDataArrayList, BusTransactionLogActivity.this));

    }
}
