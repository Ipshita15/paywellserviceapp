package com.cloudwell.paywell.services.activity.eticket.busticketNew.transportSelect;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketStatusFragment;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.Transport;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.BusCitySearchActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.transportSelect.view.IBusSeletedView;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.transportSelect.viewmodel.BusSelectedViewModel;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus;
import com.cloudwell.paywell.services.eventBus.model.MessageToBottom;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class TransportSelectActivity extends BusTricketBaseActivity implements View.OnClickListener, IBusSeletedView {

    private Spinner busListSpinner;
    private ArrayAdapter<String> busListAdapter;
    private ArrayList<String> busNameList = new ArrayList<>();
    private ArrayList<Transport> mTransportList = new ArrayList<>();
    CardView cardLayout;
    mehdi.sakout.fancybuttons.FancyButton btn_next;
    private BusTicketRepository mBusTicketRepository;
    private BusSelectedViewModel viewMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_select);
        setToolbar("Transport");

        cardLayout = findViewById(R.id.cardLayout);
        btn_next = findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        busListSpinner = findViewById(R.id.boothList);
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
                AppStorageBox.put(TransportSelectActivity.this, AppStorageBox.Key.SELETED_BUS_INFO, mTransportList.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        initViewModel();

    }

    private void initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusSelectedViewModel.class);
        viewMode.setView(this);

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
        mBusTicketRepository.getBusList().observeForever(new Observer<List<Transport>>() {
            @Override
            public void onChanged(@Nullable List<Transport> transports) {
                mTransportList = (ArrayList<Transport>) transports;
                if (transports != null && transports.size() > 0) {
                    for (int a = 0; a < transports.size(); a++) {
                        busNameList.add(transports.get(a).getBusname());
                    }
                    busListAdapter.notifyDataSetChanged();
                    dismissProgressDialog();
                }
            }
        });

    }

    public void goToSearchBusTicket() {
        AppStorageBox.put(TransportSelectActivity.this, AppStorageBox.Key.SELETED_BUS_INFO, mTransportList.get(busListSpinner.getSelectedItemPosition()));
        Transport transport = (Transport) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.SELETED_BUS_INFO);

        viewMode.getSchudle(isInternetConnection(), transport);


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

    @Override
    public void onClick(View v) {
        goToSearchBusTicket();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void openNextActivity() {
        Intent intent = new Intent(getApplicationContext(), BusCitySearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorMessage(@org.jetbrains.annotations.Nullable String status) {
        BusTicketStatusFragment busTicketStatusFragment = new BusTicketStatusFragment();
        BusTicketStatusFragment.message = status;
        busTicketStatusFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hiddenProgress() {
        dismissProgressDialog();
    }
}
