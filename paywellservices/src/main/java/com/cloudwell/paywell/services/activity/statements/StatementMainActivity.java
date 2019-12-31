package com.cloudwell.paywell.services.activity.statements;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;
import com.cloudwell.paywell.services.app.AppController;
import com.cloudwell.paywell.services.app.AppHandler;
import com.cloudwell.paywell.services.utils.UniqueKeyGenerator;

import androidx.appcompat.app.AppCompatActivity;

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

        mAppHandler = AppHandler.getmInstance(getApplicationContext());

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

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_STATEMENT_MENU);
    }

    @Override
    public void onClick(View v) {

        String uniqueKey = UniqueKeyGenerator.getUniqueKey(AppHandler.getmInstance(getApplicationContext()).getRID());
        switch (v.getId()) {

            case R.id.mini_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_MINI_MENU);

                Intent intent=new Intent(StatementMainActivity.this, ViewStatementActivity.class);
                intent.putExtra(ViewStatementActivity.DESTINATION_TITLE,"mini");
                intent.putExtra(ViewStatementActivity.DESTINATION_URL, "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage()+ "&ref_id=" + uniqueKey );
                startActivity(intent);
                break;
            case R.id.balance_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_BALANCE_MENU);


                Intent intent1=new Intent(StatementMainActivity.this, ViewStatementActivity.class);
                intent1.putExtra(ViewStatementActivity.DESTINATION_TITLE,"balance");
                intent1.putExtra(ViewStatementActivity.DESTINATION_URL, "https://api.paywellonline.com/AndroidWebViewController/balanceStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage()+ "&ref_id=" + uniqueKey );
                startActivity(intent1);

                break;
            case R.id.sales_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_SALES_MENU);


                Intent intent2=new Intent(StatementMainActivity.this, ViewStatementActivity.class);
                intent2.putExtra(ViewStatementActivity.DESTINATION_TITLE,"sales");
                intent2.putExtra(ViewStatementActivity.DESTINATION_URL, "https://api.paywellonline.com/AndroidWebViewController/salesStatementForhttps?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage()+ "&ref_id=" + uniqueKey );
                startActivity(intent2);
                break;
            case R.id.trx_statement_btn:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_STATEMENT_MENU, AnalyticsParameters.KEY_STATEMENT_TRX_MENU);

                Intent intent3=new Intent(StatementMainActivity.this, ViewStatementActivity.class);
                intent3.putExtra(ViewStatementActivity.DESTINATION_TITLE,"trx");
                intent3.putExtra(ViewStatementActivity.DESTINATION_URL, "https://api.paywellonline.com/AndroidWebViewController/getAllTransactionStatementForHttps?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage()+ "&ref_id=" + uniqueKey );
                startActivity(intent3);
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
        finish();
    }
}
