package com.cloudwell.paywell.services.activity.healthInsurance

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import kotlinx.android.synthetic.main.activity_entertainment_main.*


class HealthInsuranceMainActivity : HealthInsuranceBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_insurance_main)
        setToolbar(getString(R.string.home_insurance))

        inilizzeData()
    }

    private fun inilizzeData() {

        entertainmentBtnBongo.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        })

    }
}
