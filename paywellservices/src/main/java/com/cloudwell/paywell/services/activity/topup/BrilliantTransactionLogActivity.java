package com.cloudwell.paywell.services.activity.topup;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.topup.brilliant.model.APIBrilliantTRXLog;
import com.cloudwell.paywell.services.activity.topup.brilliant.model.BrilliantTRXLogModel;
import com.cloudwell.paywell.services.activity.topup.brilliant.model.Datum;
import com.cloudwell.paywell.services.app.AppHandler;


import java.util.ArrayList;

public class BrilliantTransactionLogActivity extends AppCompatActivity {
    private ListView trxLogLV;
    private ArrayList<BrilliantTRXLogModel> brilliantTRXLogModels;
    private ArrayList allDataList;
    private CustomAdapter customAdapter;
    private String date="";
    private String limit="";
    private LinearLayout brilliantTrxLogLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brilliant_transaction_log);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.brilliant_trx_log);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        brilliantTrxLogLL=findViewById(R.id.brilliantTrxLogLL);
        limit=getIntent().getStringExtra(BrilliantTopupActivity.LIMIT_STRING);
        trxLogLV=findViewById(R.id.trxLogLV);
        brilliantTRXLogModels=new ArrayList<>();
        allDataList=new ArrayList<>();
        customAdapter =new CustomAdapter(BrilliantTransactionLogActivity.this,allDataList);
        trxLogLV.setAdapter(customAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (limit!=null){
            if (!limit.isEmpty()){
                getBrilliantTrxLogData(new AppHandler(BrilliantTransactionLogActivity.this).getImeiNo(),limit);
            }
        }
    }
    private void getBrilliantTrxLogData(final String userName, String limitNumber){
        allDataList.clear();
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AndroidNetworking.get("https://api.paywellonline.com/PayWellBrilliantSystem/transactionLog?")
                .addQueryParameter("username",userName)
//                .addQueryParameter("username","cwntcl")
                .addQueryParameter("number", limitNumber)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(APIBrilliantTRXLog.class, new ParsedRequestListener<APIBrilliantTRXLog>() {
                    @Override
                    public void onResponse(APIBrilliantTRXLog users) {
                        if (users.getStatusCode()==200){
                            for (Datum datum: users.getData()){
                                String sub_date_comp = datum.getAddDatetime().substring(0,10);
                                if (!date.equals(sub_date_comp)) {
                                    date = sub_date_comp;
                                    allDataList.add(date);
                                }
                                BrilliantTRXLogModel brilliantTRXLogModel=new BrilliantTRXLogModel(datum.getPaywellTrxId()
                                        ,datum.getBriliantTrxId(),datum.getBrilliantMobileNumber(),datum.getAmount(),datum.getStatusName(),datum.getAddDatetime());
                                allDataList.add(brilliantTRXLogModel);
                            }
                            customAdapter.notifyDataSetChanged();
                        }else {
                            Snackbar snackbar = Snackbar.make(brilliantTrxLogLL, users.getMessage(), Snackbar.LENGTH_LONG);
                            snackbar.setActionTextColor(Color.parseColor("#ffffff"));
                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"));
                            snackbar.show();
                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        anError.printStackTrace();
                        progressDialog.dismiss();
                    }
                });
    }

    public class CustomAdapter extends BaseAdapter {
        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        ArrayList arrayList;
        private LayoutInflater mInflater;

        public CustomAdapter(Context context,ArrayList arrayList) {
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList=arrayList;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            BrilliantTransactionLogActivity.CustomAdapter.ViewHolder holder;
            int rowType;
            if (arrayList.get(position) instanceof  BrilliantTRXLogModel) {
                rowType = 0;
            } else {
                rowType = 1;
            }
            if (convertView == null) {
                holder = new BrilliantTransactionLogActivity.CustomAdapter.ViewHolder();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        String dateAndTimeString=(String) arrayList.get(position);
                        String dateString=dateAndTimeString.substring(0,10);
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(dateString);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_topup_trx_log, parent, false);
                        holder.phnNo = convertView.findViewById(R.id.phnNo);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.date = convertView.findViewById(R.id.date);
                        holder.status = convertView.findViewById(R.id.status);
                        convertView.setTag(holder);

                        BrilliantTRXLogModel brilliantTRXLogModel= (BrilliantTRXLogModel) arrayList.get(position);

                        String amountStr = brilliantTRXLogModel.getAmount();
                        holder.phnNo.setText(brilliantTRXLogModel.getBrilliantNum().toString());
                        holder.amount.setText(getBaseAmount(amountStr));
                        holder.date.setText(brilliantTRXLogModel.getDate().substring(11,brilliantTRXLogModel.getDate().length()));
                        holder.status.setText(brilliantTRXLogModel.getStatus());

                        if (brilliantTRXLogModel.getStatus()!=null){
                            if (brilliantTRXLogModel.getStatus().equalsIgnoreCase("Successful")) {
                                holder.status.setTextColor(Color.parseColor("#008000"));
                            } else {
                                holder.status.setTextColor(Color.parseColor("#ff0000"));
                            }
                        }
                        break;
                }
            } else {
                holder = (BrilliantTransactionLogActivity.CustomAdapter.ViewHolder) convertView.getTag();
                switch (rowType) {
                    case TYPE_SEPARATOR:
                        String dateAndTimeString=(String) arrayList.get(position);
                        String dateString=dateAndTimeString.substring(0,10);
                        convertView = mInflater.inflate(R.layout.dialog_both_header, parent, false);
                        holder.textView = convertView.findViewById(R.id.header);
                        convertView.setTag(holder);
                        holder.textView.clearComposingText();
                        holder.textView.setText(dateString);
                        break;
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.dialog_topup_trx_log, parent, false);
                        holder.phnNo = convertView.findViewById(R.id.phnNo);
                        holder.amount = convertView.findViewById(R.id.amount);
                        holder.date = convertView.findViewById(R.id.date);
                        holder.status = convertView.findViewById(R.id.status);
                        convertView.setTag(holder);

                        BrilliantTRXLogModel brilliantTRXLogModel= (BrilliantTRXLogModel) arrayList.get(position);

                        String amountStr =brilliantTRXLogModel.getAmount();
                        holder.phnNo.setText(brilliantTRXLogModel.getBrilliantNum().toString());
                        holder.amount.setText(getBaseAmount(amountStr));
                        holder.date.setText(brilliantTRXLogModel.getDate().substring(11,brilliantTRXLogModel.getDate().length()));
                        holder.status.setText(brilliantTRXLogModel.getStatus());

                        if (brilliantTRXLogModel.getStatus()!=null){
                            if (brilliantTRXLogModel.getStatus().equalsIgnoreCase("Successful")) {
                                holder.status.setTextColor(Color.parseColor("#008000"));
                            } else {
                                holder.status.setTextColor(Color.parseColor("#ff0000"));
                            }
                        }
                        break;
                }
            }
            return convertView;
        }
        public class ViewHolder {
            TextView textView, phnNo, amount, date, status;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String getBaseAmount(String amount){
        double amountDouble=Double.parseDouble(amount);
        int taka= (int) amountDouble;
        return "Tk."+String.valueOf(taka);
    }


}
