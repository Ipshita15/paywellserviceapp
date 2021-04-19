package com.cloudwell.paywell.services.activity.healthInsurance

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.education.PaywellPinDialog
import com.cloudwell.paywell.services.activity.healthInsurance.dialog.HelthPINdialog
import com.cloudwell.paywell.services.activity.healthInsurance.model.MembershipPackagesItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
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

       // Toast.makeText(this, packageobject.name, Toast.LENGTH_SHORT ).show()
        Toast.makeText(this, packageobject.packageImage, Toast.LENGTH_SHORT ).show()
        amount.setText("\u09F3"+packageobject.amount)
        cashback_amout_txt.setText(packageobject.cashBackAmount+" "+packageobject.cashBackMessage)
        member_msg.setText(packageobject.memberMessage)
        additional_txt.setText(packageobject.aditional.get(0).amount + " "+packageobject.aditional.get(0).message)
        package_detailsText.setText(packageobject.details)

       // Glide.with(this).load(packageobject.packageImage).into(banner_image)
        Picasso.get().load(packageobject.packageImage).into(banner_image)



        active_btn.setOnClickListener(View.OnClickListener {

            showPINdialog(packageobject)

        })


    }

    private fun showPINdialog(packageobject: MembershipPackagesItem) {


        val askingPinDialog = HelthPINdialog(object : PaywellPinDialog.IonClickInterface {
            override fun onclick(pin: String) {


            }
        })
        askingPinDialog.show(supportFragmentManager, "Pin Dialog")
    }


}
