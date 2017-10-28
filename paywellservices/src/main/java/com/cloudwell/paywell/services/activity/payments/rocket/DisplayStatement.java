package com.cloudwell.paywell.services.activity.payments.rocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.app.AppController;

/**
 * Created by Naima Gani on 12/19/2016.
 */

public class DisplayStatement extends AppCompatActivity {

    private TextView mTvErrorMsg;
    private ProgressBar mPbLoading;
    ListView listView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_statement);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_payment_transactions);


        mTvErrorMsg = (TextView) findViewById(R.id.tvErrorMessage);
        mTvErrorMsg.setTypeface(AppController.getInstance().getRobotoRegularFont());
        mPbLoading = (ProgressBar) findViewById(R.id.pbLoading);

        listView = (ListView) findViewById(R.id.listView);
        InquiryStatementTextListAdapter adapter = new InquiryStatementTextListAdapter(this);
        listView.setAdapter(adapter);
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
        Intent intent = new Intent(DisplayStatement.this, RocketInquiryActivity.class);
        startActivity(intent);
        finish();
    }
}