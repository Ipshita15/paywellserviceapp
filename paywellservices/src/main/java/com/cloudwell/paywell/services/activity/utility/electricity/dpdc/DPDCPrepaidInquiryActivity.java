package com.cloudwell.paywell.services.activity.utility.electricity.dpdc;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class DPDCPrepaidInquiryActivity extends AppCompatActivity {

    public static String TRANSLOG_TAG = "TRANSLOGTXT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpdc_prepaid_inquiry);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_topup_trx_log);
        }

        initializeView();
    }

    private void initializeView() {
        TableLayout mTableLayout = findViewById(R.id.tableLayout);
        try {
            JSONArray jsonArray = new JSONArray(TRANSLOG_TAG);

            for (int i = 0; i < jsonArray.length(); i++) {
                TableRow newRow = new TableRow(this);
                JSONObject object = jsonArray.getJSONObject(i);

                Iterator<String> keys = object.keys();
                while (keys.hasNext()) {
                    TextView column = new TextView(this);
                    String keyValue = keys.next();

                    column.setText(object.getString(keyValue));
                    column.setTextColor(Color.parseColor("#000000"));
                    column.setGravity(Gravity.CENTER);
                    column.setBackgroundResource(R.drawable.row_border);

                    newRow.addView(column);
                }
                mTableLayout.addView(newRow, new TableLayout.LayoutParams());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
        Intent intent = new Intent(DPDCPrepaidInquiryActivity.this, DPDCMainActivity.class);
        startActivity(intent);
        finish();
    }
}
