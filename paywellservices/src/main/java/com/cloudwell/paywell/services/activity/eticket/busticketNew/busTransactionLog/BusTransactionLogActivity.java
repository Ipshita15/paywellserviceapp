package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransactionLog;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.menu.BusTicketMenuActivity;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusTransactionModel;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog.Datum;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.transactionLog.TransactionLogDetailsModel;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.app.storage.AppStorageBox;
import com.cloudwell.paywell.services.retrofit.ApiUtils;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusTransactionLogActivity extends BusTricketBaseActivity {

    private static final String TAG = BusTransactionLogActivity.class.getName();
    RecyclerView busTransactionRV;
    ArrayList allDataArrayList = new ArrayList();
    BusTransactionLogAdapter adapter;
    int limit = 0;
    private String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_transaction_log);


        setToolbar(getString(R.string.bus_transction_log), getResources().getColor(R.color.bus_ticket_toolbar_title_text_color));

        limit = getIntent().getIntExtra(BusTicketMenuActivity.Companion.getKEY_LIMIT(), -1);
        busTransactionRV = findViewById(R.id.busTransactionLogRV);
        busTransactionRV.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BusTransactionLogAdapter(allDataArrayList, BusTransactionLogActivity.this);
        busTransactionRV.setAdapter(adapter);
        String userName = AppHandler.getmInstance(this).getUserName();
        String skey = ApiUtils.KEY_SKEY;
        if (limit > 0) {
            getTransactionLog(userName, skey, String.valueOf(limit));
        } else {
            Toast.makeText(this, "Internal error!!! limit can't be less than 5", Toast.LENGTH_SHORT).show();
        }
    }

    void getTransactionLog(String userName, String skey, String limit) {


        showProgressDialog();
        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());

        boolean isBusTicket = (Boolean) AppStorageBox.get(getApplicationContext(), AppStorageBox.Key.IS_BUS_Ticket_USER_FLOW);


        RequestBusTranstionLog m = new RequestBusTranstionLog();
        m.setLimit(limit);
        m.setUsername(userName);
        if (isBusTicket) {
            m.setTransportType("1");
        } else {
            m.setTransportType("1");
        }


        ApiUtils.getAPIServiceV2().getBusTransactionLogFromServer(m).enqueue(new Callback<TransactionLogDetailsModel>() {
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


                            try {
                                BusTransactionModel busTransactionModel = new BusTransactionModel(
                                        transactionDate,
                                        datum.getTrxId(),
                                        datum.getTicketInfo().getBookingInfoId(),
                                        datum.getStatusMessage(),
                                        datum.getStatusMessageForConfirm(),
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

                            } catch (Exception e) {
                                Log.d(TAG, "onResponse:");
                            }


                        }
                    } else {
                        showNoDataFound();
                    }
                } else {
                    showNoDataFound();
                }
                dismissProgressDialog();
            }

            @Override
            public void onFailure(Call<TransactionLogDetailsModel> call, Throwable t) {
                Toast.makeText(BusTransactionLogActivity.this, getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }
        });

    }

    private void showNoDataFound() {
        ImageView imageView = findViewById(R.id.ivForNodataFoundBusTrans);
        imageView.setVisibility(View.VISIBLE);
        TextView textView = findViewById(R.id.tvNoDataFoundBusTrans);
        textView.setVisibility(View.VISIBLE);
        Toast.makeText(BusTransactionLogActivity.this, R.string.no_data_found, Toast.LENGTH_SHORT).show();
    }
}
