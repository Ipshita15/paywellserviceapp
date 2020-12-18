package com.cloudwell.paywell.services.activity.healthInsurance

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity


class PackageDetailsActivity : HealthInsuranceBaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)

        setToolbar(getString(R.string.package_list_digital_health))


    }


}
