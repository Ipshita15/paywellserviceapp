package com.cloudwell.paywell.services.activity.refill.nagad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.refill_log_details, null);
        RefillInquiryViewHolder mh = new RefillInquiryViewHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(@NonNull RefillInquiryViewHolder holder, int i) {


//        ImageView refill_calenderIV = holder.itemView.findViewById(R.id.refill_calenderIV);
        TextView refill_dateTV = holder.itemView.findViewById(R.id.refill_dateTV);
        TextView refill_transactionTV = holder.itemView.findViewById(R.id.refill_transactionTV);
        TextView refill_mobileTV = holder.itemView.findViewById(R.id.refill_mobileTV);
        TextView refill_amountTV = holder.itemView.findViewById(R.id.refill_amountTV);
        TextView successMsgTV = holder.itemView.findViewById(R.id.successMsgTV);

        refill_dateTV.setText(trxList.get(i).getTrxTimestamp());
        refill_transactionTV.setText("TRX ID : "+trxList.get(i).getTrxId());
        refill_mobileTV.setText("Mobile : "+trxList.get(i).getSender());
        refill_amountTV.setText("Tk."+trxList.get(i).getAmount());
        successMsgTV.setText("Successful");






    }

    @Override
    public int getItemCount() {
        return (null != trxList ? trxList.size() : 0);

    }

    public class RefillInquiryViewHolder extends RecyclerView.ViewHolder {

        protected TextView refill_dateTV;
        protected TextView refill_transactionTV;
        protected TextView refill_mobileTV;
        protected TextView refill_amountTV;
        protected TextView successMsgTV;





        public RefillInquiryViewHolder(@NonNull View itemView) {
            super(itemView);

            this.refill_dateTV = itemView.findViewById(R.id.refill_dateTV);
            this.refill_transactionTV = itemView.findViewById(R.id.refill_transactionTV);
            this.refill_mobileTV = itemView.findViewById(R.id.refill_mobileTV);
            this.refill_amountTV = itemView.findViewById(R.id.refill_amountTV);
            this.successMsgTV = itemView.findViewById(R.id.successMsgTV);



        }
    }
}
