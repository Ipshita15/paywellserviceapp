package com.cloudwell.paywell.services.activity.topup;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.model.ResponseDetailsItem;

import java.util.ArrayList;
import java.util.List;

public class TransLogActivity extends AppCompatActivity {

    public static ArrayList<ResponseDetailsItem> responseDetailsItems = new ArrayList<>();
    public  static List<ResponseDetailsItem> responseDetailsItemList;

    private RelativeLayout relativeLayout;
    private ListView listView;
    public static String[] mPhn = null;
    public static String[] mAmount = null;
    public static String[] mDate = null;
    public static String[] mStatus = null;
    public static String[] mTrx = null;
    public static String length;
    public static String TRANSLOG_TAG = "TRANSLOGTXT";
    private String date = "";

    RecyclerView responseRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trx_log);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_topup_trx_log);
        }

        relativeLayout = findViewById(R.id.relativeLayout);
        responseRecyclerView = findViewById(R.id.recycler_view_trsnscationLog);
        responseRecyclerView.setHasFixedSize(true);
        responseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(responseDetailsItemList, TransLogActivity.this);
        responseRecyclerView.setAdapter(adapter);


    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

        private List<ResponseDetailsItem> responseDetailsItemList;
        private Context context;
        private LayoutInflater mInflater;

        public RecyclerViewAdapter(List<ResponseDetailsItem> responseDetailsItemList, Context context) {
            this.responseDetailsItemList = responseDetailsItemList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = mInflater.inflate(R.layout.dialog_topup_trx_log_new, parent, false);
//            return new ViewHolder(view);

            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.dialog_topup_trx_log_new, parent, false);
            ViewHolder viewHolder = new ViewHolder(listItem);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                String sub_date = responseDetailsItemList.get(position).getRequestTrxDate();
                String time = responseDetailsItemList.get(position).getRequestDate().substring(11, 19);
            if (!date.equals(sub_date)) {
                date = sub_date;
                //go for header
                holder.headerLayout.setVisibility(View.VISIBLE);
                holder.header.setText(date);
            }else {
                //go without header
                holder.headerLayout.setVisibility(View.GONE);
            }
            holder.amount.setText("Tk."+responseDetailsItemList.get(position).getAmount());
            holder.date.setText(time);
            holder.phnNo.setText(responseDetailsItemList.get(position).getRecipientMsisdn());
            String status = responseDetailsItemList.get(position).getStatusName();
            if (status.equals("Successful")){
                holder.status.setText(status);
                holder.status.setTextColor(Color.parseColor("#008000"));
            }else {
                holder.status.setText(status);
                holder.status.setTextColor(Color.parseColor("#ff0000"));
            }

        }

        @Override
        public int getItemCount() {
            return responseDetailsItemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout headerLayout;
            TextView header,phnNo,amount,date,status;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);


                headerLayout = itemView.findViewById(R.id.hader_layout);
                header = itemView.findViewById(R.id.header);
                phnNo = itemView.findViewById(R.id.phnNo);
                amount = itemView.findViewById(R.id.amount);
                date = itemView.findViewById(R.id.date);
                status = itemView.findViewById(R.id.status);



            }
        }
    }

}
