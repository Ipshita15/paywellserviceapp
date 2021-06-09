package com.cloudwell.paywell.services.activity.healthInsurance

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.healthInsurance.adapter.DocConsultationAdapter
import com.cloudwell.paywell.services.activity.healthInsurance.dialog.Documentdialog
import com.cloudwell.paywell.services.activity.healthInsurance.dialog.HelthMobiledialog
import com.cloudwell.paywell.services.activity.healthInsurance.model.MembershipPackagesItem
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_package_details.*
import org.sufficientlysecure.htmltextview.HtmlFormatter
import org.sufficientlysecure.htmltextview.HtmlFormatterBuilder
import org.sufficientlysecure.htmltextview.HtmlResImageGetter


class PackageDetailsActivity : HealthInsuranceBaseActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_package_details)

        setToolbar(getString(R.string.package_list_digital_health))





        val gson = Gson()
        val packageobject: MembershipPackagesItem = gson.fromJson(intent.getStringExtra(getString(R.string.selected_healthmart_package)), MembershipPackagesItem::class.java)

        package_name.setText(packageobject.name)
        package_duration.setText(packageobject.validity + " "+ getString(R.string.month_validity))

        amount.setText("\u09F3" + packageobject.amount)
        cashback_amout_txt.setText(packageobject.cashBackAmount + " " + packageobject.cashBackMessage)
        member_msg.setText(packageobject.memberMessage)
        additional_txt.setText(packageobject.aditional.get(0).amount + " " + packageobject.aditional.get(0).message)

        val formattedHtml = HtmlFormatter.formatHtml(HtmlFormatterBuilder().setHtml(packageobject.details).setImageGetter(HtmlResImageGetter(package_detailsText.getContext())))
        package_detailsText.setText(formattedHtml)

        Picasso.get().load(packageobject.packageImage).into(banner_image)

        rvDoctorConsultation
        val adapter = DocConsultationAdapter(packageobject.doctorConsultation)
        rvDoctorConsultation.layoutManager = GridLayoutManager(this, 2)
        rvDoctorConsultation.adapter = adapter


        active_btn.setOnClickListener(View.OnClickListener {

            showPINdialog(packageobject)

        })


    }

    private fun showPINdialog(packageobject: MembershipPackagesItem) {

        val mobileDialog = HelthMobiledialog(object : HelthMobiledialog.MobileInterface{
            override fun onclick(mobile : String) {

                showDocumentDialog(mobile, packageobject)

            }
        })

        mobileDialog.show(supportFragmentManager, "Mobile Dialog")

    }


    private fun showDocumentDialog(mobile: String, hPackage :MembershipPackagesItem){

        val documentDialog = Documentdialog(object : Documentdialog.DocClickInterface {

            override fun onclick(document: String) {

                Intent(this@PackageDetailsActivity, HelthInfoActivity::class.java).also {
                    it.putExtra(getString(R.string.healthmobile), mobile)
                    it.putExtra(getString(R.string.document), document)
                    it.putExtra(getString(R.string.selected_helth_p), hPackage.packageId)
                    startActivity(it)
                }


            }
        })

        documentDialog.show(supportFragmentManager, "Doc Dialog")


    }


}
