package com.cloudwell.paywell.services.activity.entertainment.bongo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoActivePkgPojo
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.BongoActiveResponse
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.PackagesItem
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.AskMobileNumberDialog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.AskMobileNumberDialog.getNumberClickInterface
import com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog.AskPinDialog
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_packege_selected.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PackegeSelectedActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_packege_selected)
        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };

        mAppHandler = AppHandler.getmInstance(applicationContext)

        val gson = Gson()
        val selected_package : PackagesItem = gson.fromJson(intent.getStringExtra("selected_package"), PackagesItem::class.java)
        Log.e("ov", selected_package.toString())


        bongo_title.text = selected_package.title
        bongo_package_title.text = selected_package.title
        bongo_price.text = selected_package.currency+" "+selected_package.price
        bongo_payment_btn.setOnClickListener(View.OnClickListener {

            if (isInternetConnection){
                askMobileNumberDialog(selected_package)
            }else{
                showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
            }

        })
        bongo_sub_back.setOnClickListener(View.OnClickListener {
            finish()
        })
    }

    fun askMobileNumberDialog(packagesItem: PackagesItem) {
        val mobileNumberDialog = AskMobileNumberDialog(object : getNumberClickInterface {
            override fun onclick(mobileNumber: String) {

                askPinDialog(packagesItem, mobileNumber)

            }
        })
        mobileNumberDialog.show(supportFragmentManager, "askMobileNumberDialog")
    }


    fun askPinDialog(packagesItem: PackagesItem, mobile: String) {

        val pinDialog = AskPinDialog(object : AskPinDialog.getPinInterface{
            override fun onclick(pinNumber: String) {
                if(isInternetConnection){
                    activeBongoPackge(packagesItem, mobile, pinNumber)

                }else{
                    showErrorCallBackMessagev1(getString(R.string.no_internet_connection_please_check_your_internet_connection))
                }
            }
        })

        pinDialog.show(supportFragmentManager, "askMobileNumberDialog")
    }


    private fun activeBongoPackge(packagesItem: PackagesItem, mobile:String, pin : String) {
        showProgressDialog()

        var bongoActivePkgPojo = BongoActivePkgPojo()
        bongoActivePkgPojo.code = packagesItem.code.toString()
        bongoActivePkgPojo.locale = mAppHandler?.appLanguage
        bongoActivePkgPojo.mobile = mAppHandler?.mobileNumber
        bongoActivePkgPojo.pin_no = pin
        bongoActivePkgPojo.username = mAppHandler?.userName


        ApiUtils.getAPIServiceV2().getBongoActivePackgeList(bongoActivePkgPojo).enqueue(object : Callback<BongoActiveResponse> {
            override fun onFailure(call: Call<BongoActiveResponse>, t: Throwable) {
                dismissProgressDialog()
            }

            override fun onResponse(call: Call<BongoActiveResponse>, response: Response<BongoActiveResponse>) {
                dismissProgressDialog()
                if (response.isSuccessful){
                    val activeResponse : BongoActiveResponse = response.body()!!
                    if (activeResponse.status == 200){

                        showSuccessDialog(getString(R.string.success_msg), getString(R.string.subscription_success))

                    }else{
                        showErrorMessagev1(activeResponse.message)
                    }

                }else{
                    showErrorMessagev1(getString(R.string.try_again_msg))
                }


            }

        })
    }

}
