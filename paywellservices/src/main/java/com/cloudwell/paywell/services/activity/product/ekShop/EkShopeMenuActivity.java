package com.cloudwell.paywell.services.activity.product.ekShop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cloudwell.paywell.services.R;
import com.cloudwell.paywell.services.activity.base.ProductEecommerceBaseActivity;
import com.cloudwell.paywell.services.activity.product.ekShop.report.ReportMainActivity;
import com.cloudwell.paywell.services.analytics.AnalyticsManager;
import com.cloudwell.paywell.services.analytics.AnalyticsParameters;

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-06-24.
 */
public class EkShopeMenuActivity extends ProductEecommerceBaseActivity {

    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ek_shope_main);
        assert getSupportActionBar() != null;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.home_title_ek_shope);
        }

        relativeLayout = findViewById(R.id.linearLayout);
    }

    public void onButtonClicker(View v) {

        switch (v.getId()) {
            case R.id.et_shop_visit:
                AnalyticsManager.sendEvent(AnalyticsParameters.KEY_PRODUCT_MENU, AnalyticsParameters.KEY_PRODUCT_EKSHOPE_DEAL_PAGE);
                startActivity(new Intent(getApplicationContext(), EKShopActivity.class));
                break;
            case R.id.et_ek_shop_report:
                startActivity(new Intent(getApplicationContext(), ReportMainActivity.class));
                break;

            default:
                break;
        }
    }


}
