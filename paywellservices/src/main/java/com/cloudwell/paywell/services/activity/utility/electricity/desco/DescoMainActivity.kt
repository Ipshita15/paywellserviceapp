package com.cloudwell.paywell.services.activity.utility.electricity.desco

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidBillPayActivity
import com.cloudwell.paywell.services.activity.utility.electricity.desco.postpaid.DESCOPostpaidMainActivity
import com.cloudwell.paywell.services.activity.utility.electricity.desco.prepaid.DescoPrepaidMainActivity
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters

class DescoMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_desco_main2)
    }

    fun onButtonClicker(view: View) {


        when (view.getId()) {
            R.id.descoPostpaidBtn -> {
                startActivity(Intent(this, DESCOPostpaidMainActivity::class.java))
            }
            R.id.descoPrepaidBtn -> {
                startActivity(Intent(this, DescoPrepaidMainActivity::class.java))
            }
            else -> {
            }
        }

    }
}
