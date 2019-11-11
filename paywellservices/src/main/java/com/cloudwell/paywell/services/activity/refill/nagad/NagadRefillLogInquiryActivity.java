package com.cloudwell.paywell.services.activity.refill.nagad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.BaseActivity;
import com.cloudwell.paywell.services.activity.refill.nagad.adapter.RefillLogInquiryAdapter;
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.RefillLog;
import com.cloudwell.paywell.services.activity.refill.nagad.model.refill_log.TrxDetail;
import com.google.gson.Gson;

import java.util.List;

public class NagadRefillLogInquiryActivity extends BaseActivity {
    List<TrxDetail> trxDetails ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill_log_inquiry);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.nagad_refill_log_inquiry);
        }

        String data = getIntent().getStringExtra("data");
        RefillLog refillLog = new Gson().fromJson(data, RefillLog.class);
        trxDetails = refillLog.getTrxDetails();
        RefillLogInquiryAdapter refillLogInquiryAdapter = new RefillLogInquiryAdapter(getApplicationContext(), trxDetails);

        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.refill_inquiryRV);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        my_recycler_view.setAdapter(refillLogInquiryAdapter);



    }

}
