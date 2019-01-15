package com.cloudwell.paywell.services.activity.statements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;

public class StatementMainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_statement);
        }

        mAppHandler = new AppHandler(this);

        TextView btnMiniStatement = findViewById(R.id.mini_statement_btn);
        TextView btnBalanceStatement = findViewById(R.id.balance_statement_btn);
        TextView btnSalesStatement = findViewById(R.id.sales_statement_btn);
        TextView btnTransactionStatement = findViewById(R.id.trx_statement_btn);

        if (mAppHandler.getAppLanguage().equalsIgnoreCase("en")) {
            btnMiniStatement.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnBalanceStatement.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnSalesStatement.setTypeface(AppController.getInstance().getOxygenLightFont());
            btnTransactionStatement.setTypeface(AppController.getInstance().getOxygenLightFont());
        } else {
            btnMiniStatement.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnBalanceStatement.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnSalesStatement.setTypeface(AppController.getInstance().getAponaLohitFont());
            btnTransactionStatement.setTypeface(AppController.getInstance().getAponaLohitFont());
        }

        btnMiniStatement.setOnClickListener(this);
        btnBalanceStatement.setOnClickListener(this);
        btnSalesStatement.setOnClickListener(this);
        btnTransactionStatement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mini_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_MINI_MENU);
                ViewStatementActivity.title = "mini";
                ViewStatementActivity.url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.balance_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_BALANCE_MENU);
                ViewStatementActivity.title = "balance";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/balanceStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.sales_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_SALES_MENU);
                ViewStatementActivity.title = "sales";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/salesStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.trx_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_TRX_MENU);
                ViewStatementActivity.title = "trx";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/getAllTransactionStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            default:
                break;
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
        Intent intent = new Intent(StatementMainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
