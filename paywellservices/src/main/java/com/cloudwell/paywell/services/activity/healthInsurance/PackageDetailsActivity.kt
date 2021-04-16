package com.cloudwell.paywell.services.activity.healthInsurance

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.healthInsurance.model.MembershipPackagesItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.RespseMemberShipPackage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_package_details.*


class PackageDetailsActivity : HealthInsuranceBaseActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)

        setToolbar(getString(R.string.package_list_digital_health))


        val gson = Gson()
        val packageobject: MembershipPackagesItem = gson.fromJson(intent.getStringExtra(getString(R.string.selected_healthmart_package)), MembershipPackagesItem::class.java)

        package_name.setText(packageobject.name)
        package_duration.setText(packageobject.validity+" "+getString(R.string.validity))

        Toast.makeText(this, packageobject.name, Toast.LENGTH_SHORT ).show()

    }


}
