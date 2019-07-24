package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransactionLog;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusTransactionModel;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog.Datum;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog.TransactionLogDetailsModel;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.retrofit.ApiUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusTransactionLogActivity extends BusTricketBaseActivity {

    RecyclerView busTransactionRV;
    ArrayList allDataArrayList = new ArrayList();
    BusTransactionLogAdapter adapter;
    int limit = 0;
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_transaction_log);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#268472'>Booking Log</font>"));
            getSupportActionBar().setElevation(0f);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.color_tab_background_bus)));
        }

        limit = getIntent().getIntExtra(BusTicketMenuActivity.Companion.getKEY_LIMIT(), -1);
        busTransactionRV = findViewById(R.id.busTransactionLogRV);
        busTransactionRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusTransactionLogAdapter(allDataArrayList, BusTransactionLogActivity.this);
        busTransactionRV.setAdapter(adapter);
        String userName = AppHandler.getmInstance(this).getImeiNo();
        String skey = ApiUtils.KEY_SKEY;
        if (limit > 0) {
            getTransactionLog(userName, skey, String.valueOf(limit));
        } else {
            Toast.makeText(this, "Internal error!!! limit can't be less than 5", Toast.LENGTH_SHORT).show();
        }
    }

    void getTransactionLog(String userName, String skey, String limit) {

        showProgressDialog();

        ApiUtils.getAPIServicePHP7().getBusTransactionLogFromServer(userName, skey, limit).enqueue(new Callback<TransactionLogDetailsModel>() {
            @Override
            public void onResponse(Call<TransactionLogDetailsModel> call, Response<TransactionLogDetailsModel> response) {
                if (response.isSuccessful()) {
                    TransactionLogDetailsModel transactionLogDetailsModel = response.body();
                    if (transactionLogDetailsModel.getStatus() == 200) {
                        if (transactionLogDetailsModel.getData().size() != 0) {
                            ImageView imageView = findViewById(R.id.ivForNodataFoundBusTrans);
                            imageView.setVisibility(View.GONE);
                            TextView textView = findViewById(R.id.tvNoDataFoundBusTrans);
                            textView.setVisibility(View.GONE);
                        }


                        for (Datum datum : transactionLogDetailsModel.getData()) {
                            String transactionDate = datum.getTransactionDateTime().split(" ")[0];
                            if (!date.equals(transactionDate)) {
                                date = transactionDate;
                                allDataArrayList.add(date);
                            }
                            BusTransactionModel busTransactionModel = new BusTransactionModel(
                                    transactionDate,
                                    datum.getTrxId(),
                                    datum.getTicketInfo().getBookingInfoId(),
                                    datum.getStatusMessage(),
                                    datum.getTicketInfo().getWebBookingInfoId(),
                                    datum.getTicketInfo().getTotalAmount(),
                                    datum.getCustomerInfo().getCustomerName(),
                                    datum.getCustomerInfo().getCusTomerGenger(),
                                    datum.getCustomerInfo().getCustomerPhone(),
                                    datum.getCustomerInfo().getCustomerAddress(),
                                    datum.getCustomerInfo().getCustomerEmail(),
                                    datum.getTicketInfo().getTicketNo(),
                                    datum.getTicketInfo().getBoardingPointName(),
                                    datum.getTicketInfo().getDepartureDate(),
                                    datum.getTicketInfo().getDepartureTime(),
                                    datum.getTicketInfo().getSeatLbls(),
                                    datum.getBusInfo().getCoachNo(),
                                    datum.getBusInfo().getBusName(),
                                    datum.getTicketInfo().getJourneyRoute().split("-")[0],
                                    datum.getTicketInfo().getJourneyRoute().split("-")[1]);
                            allDataArrayList.add(busTransactionModel);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        ImageView imageView = findViewById(R.id.ivForNodataFoundBusTrans);
                        imageView.setVisibility(View.VISIBLE);
                        TextView textView = findViewById(R.id.tvNoDataFoundBusTrans);
                        textView.setVisibility(View.VISIBLE);
                        Toast.makeText(BusTransactionLogActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
                    }
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TransactionLogDetailsModel> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(BusTransactionLogActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });

    }
}
