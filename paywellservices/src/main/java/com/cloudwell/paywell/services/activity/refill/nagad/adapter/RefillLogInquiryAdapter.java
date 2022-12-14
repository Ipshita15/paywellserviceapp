package com.cloudwell.paywell.services.activity.refill.nagad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.TrxDetail;

import java.util.List;

public class RefillLogInquiryAdapter extends RecyclerView.Adapter<RefillLogInquiryAdapter.RefillInquiryViewHolder> {

    private List<TrxDetail> trxList;
    private Context context;


    public RefillLogInquiryAdapter(Context context, List<TrxDetail> trxList) {
        this.context = context;
        this.trxList = trxList;

    }
    @Override
    public RefillInquiryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.refill_log_details, viewGroup,false);
        RefillInquiryViewHolder mh = new RefillInquiryViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull RefillInquiryViewHolder holder, int i) {

        TextView refill_dateTV = holder.itemView.findViewById(R.id.refill_dateTV);
        TextView refill_transactionNoTV = holder.itemView.findViewById(R.id.refill_transactionNoTV);
        TextView refill_mobileNoTV = holder.itemView.findViewById(R.id.refill_mobileNoTV);
        TextView refill_amountNoTV = holder.itemView.findViewById(R.id.refill_amountNoTV);
        TextView successMsgTV = holder.itemView.findViewById(R.id.successMsgTV);

        refill_dateTV.setText(trxList.get(i).getTrxTimestamp());
        refill_transactionNoTV.setText(trxList.get(i).getTrxId());
        refill_mobileNoTV.setText(trxList.get(i).getSender());
        refill_amountNoTV.setText("Tk. "+trxList.get(i).getAmount());
        successMsgTV.setText("Successful");

    }

    @Override
    public int getItemCount() {
        return (null != trxList ? trxList.size() : 0);

    }

    public class RefillInquiryViewHolder extends RecyclerView.ViewHolder {


        public RefillInquiryViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
