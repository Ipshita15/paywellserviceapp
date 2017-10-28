package com.cloudwell.paywell.services.activity.statements;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.MainActivity;
import com.cloudwell.paywell.services.app.AppHandler;

public class StatementMainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView btnMiniStatement, btnBalanceStatement, btnSalesStatement, btnTransactionStatement;
    private AppHandler mAppHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.home_statement);

        mAppHandler = new AppHandler(this);

        btnMiniStatement = (TextView) findViewById(R.id.mini_statement_btn);
        btnBalanceStatement = (TextView) findViewById(R.id.balance_statement_btn);
        btnSalesStatement = (TextView) findViewById(R.id.sales_statement_btn);
        btnTransactionStatement = (TextView) findViewById(R.id.trx_statement_btn);

        btnMiniStatement.setOnClickListener(this);
        btnBalanceStatement.setOnClickListener(this);
        btnSalesStatement.setOnClickListener(this);
        btnTransactionStatement.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mini_statement_btn:
                ViewStatementActivity.title = "mini";
                ViewStatementActivity.url = "https://api.paywellonline.com/AndroidWebViewController/StatementInquiry?username="
                                    + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.balance_statement_btn:
                ViewStatementActivity.title = "balance";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/balanceStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.sales_statement_btn:
                ViewStatementActivity.title = "sales";
                ViewStatementActivity.url = "http://api.cloudwell.co/AndroidWebViewController/salesStatement?username="
                        + mAppHandler.getImeiNo() + "&language=" + mAppHandler.getAppLanguage();
                startActivity(new Intent(StatementMainActivity.this, ViewStatementActivity.class));
                break;
            case R.id.trx_statement_btn:
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
