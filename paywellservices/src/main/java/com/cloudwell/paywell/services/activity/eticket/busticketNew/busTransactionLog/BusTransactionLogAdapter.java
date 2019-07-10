package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransactionLog;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BusTransactionModel;
import com.cloudwell.paywell.services.activity.eticket.busticketNew.search.FullScreenDialogBus;
import com.cloudwell.paywell.services.utils.FullScreenDialogPayWell;

import java.util.ArrayList;

/**
 * Created by YASIN on 03,July,2019
 * Email: yasinenubd5@gmail.com
 */
public class BusTransactionLogAdapter extends RecyclerView.Adapter<BusTransactionLogAdapter.TransactionLogViewHolder> {


    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private final Activity context;
    private ArrayList busTransactionModelArrayList;

    public BusTransactionLogAdapter(ArrayList busTransactionModelArrayList, Activity context) {
        this.busTransactionModelArrayList = busTransactionModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SEPARATOR) {
            return new TransactionLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_trnsactionlog_list_item_header, parent, false));
        } else {
            return new TransactionLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_transactionlog_list_item_view, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionLogViewHolder holder, int position) {

        int rowType;
        if (busTransactionModelArrayList.get(position) instanceof BusTransactionModel) {
            rowType = 0;
        } else {
            rowType = 1;
        }

        switch (rowType) {
            case TYPE_SEPARATOR:
                TextView header = holder.itemView.findViewById(R.id.busHeaderTitle);
                header.setText((String) busTransactionModelArrayList.get(position));
                break;
            case TYPE_ITEM:
                TextView bookingId = holder.itemView.findViewById(R.id.bookingIdTV);
                TextView amount = holder.itemView.findViewById(R.id.priceTV);
                TextView status = holder.itemView.findViewById(R.id.statusTV);
                bookingId.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getBookingId());
                amount.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getTicketPrice());
                status.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getBookingStatus());
                LinearLayout linearLayout = holder.itemView.findViewById(R.id.mainClickLL);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FullScreenDialogPayWell fullScreenDialogPayWell = FullScreenDialogPayWell.newInstance("Transaction Details",
                                R.style.FullScreenDialogStyle, new FullScreenDialogPayWell.OnSetFullScreenDialogView() {
                                    @Override
                                    public View setView(LayoutInflater inflater, ViewGroup container, Dialog dialog) {
                                        View view1 = inflater.inflate(R.layout.transaction_details_dialog, container, false);
                                        Toolbar toolbar = view1.findViewById(R.id.toolbar);
                                        TextView bookingId = view1.findViewById(R.id.bookingIdTV);
                                        TextView travelDateTV = view1.findViewById(R.id.travelDateTV);
                                        TextView priceTv = view1.findViewById(R.id.priceTV);
                                        TextView customerNameTV = view1.findViewById(R.id.customerNameTV);
                                        TextView customerGenderTV = view1.findViewById(R.id.customerGenderTV);
                                        TextView departureTV = view1.findViewById(R.id.departureTimeTV);
                                        TextView seatNumTV = view1.findViewById(R.id.seatNumTV);
                                        TextView coachNumTV = view1.findViewById(R.id.coachNumTV);
                                        TextView transportNameTV = view1.findViewById(R.id.transportNameTV);
                                        TextView journeyFromTV = view1.findViewById(R.id.journeyFromTV);
                                        TextView journeyToTV = view1.findViewById(R.id.journeyToTV);
                                        bookingId.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getBookingId());
                                        travelDateTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getDepartureDate());
                                        priceTv.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getTicketPrice());
                                        customerNameTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getCustomerName());
                                        customerGenderTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getCustomerGender());
                                        departureTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getDepartureDate());
                                        seatNumTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getSeatNo().get(0));
                                        coachNumTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getCouchNo());
                                        transportNameTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getTransportName());
                                        journeyFromTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getJourneyFrom());
                                        journeyToTV.setText(((BusTransactionModel) busTransactionModelArrayList.get(position)).getJourneyTo());

                                        toolbar.setNavigationIcon(R.drawable.close);
                                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                dialog.dismiss();
                                            }
                                        });
                                        toolbar.setBackgroundColor(context.getResources().getColor(R.color.color_tab_background_bus));
                                        toolbar.setTitle("Transaction Details");
                                        return view1;
                                    }
                                });
                        FragmentTransaction ft = context.getFragmentManager().beginTransaction();
                        fullScreenDialogPayWell.show(ft, FullScreenDialogBus.TAG);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (busTransactionModelArrayList.get(position) instanceof BusTransactionModel) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return busTransactionModelArrayList.size();
    }

    public interface OnDialogClose {
        void onClose();
    }

    public class TransactionLogViewHolder extends RecyclerView.ViewHolder {
        public TransactionLogViewHolder(View itemView) {
            super(itemView);
        }
    }


}
