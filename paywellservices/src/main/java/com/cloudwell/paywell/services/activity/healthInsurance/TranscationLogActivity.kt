package com.cloudwell.paywell.services.activity.healthInsurance

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.education.PaywellPinDialog
import com.cloudwell.paywell.services.activity.healthInsurance.adapter.TrxLogAdapter
import com.cloudwell.paywell.services.activity.healthInsurance.dialog.ReactivationDialog
import com.cloudwell.paywell.services.activity.healthInsurance.model.ActivePakage
import com.cloudwell.paywell.services.activity.healthInsurance.model.ActiveResponse
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.TrxResponse
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_transcation_log.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TranscationLogActivity : HealthInsuranceBaseActivity(), TrxLogAdapter.trxOnClick {

    var responseDetails:  List<TransactionDataItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcation_log)

        setToolbar(getString(R.string.trx_details))


        val data = intent.getStringExtra(getString(R.string.health_trx_tag))
        val response = Gson().fromJson(data, TrxResponse::class.java)
        responseDetails = response.transactionData



        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        health_trx_recycler.layoutManager = linearLayoutManager

        val adapter = TrxLogAdapter(applicationContext, responseDetails)
        health_trx_recycler.adapter = adapter
        adapter.setClickListener(this)




    }

    override fun onclick(item: TransactionDataItem) {

        val dialog = ReactivationDialog(item, object : ReactivationDialog.reactiveInterface{
            override fun onclick(packageItem: TransactionDataItem) {
                askPin(item)
            }
        })
        dialog.show(supportFragmentManager, "reactive Dialog")


    }

    private fun askPin(item: TransactionDataItem) {
        //show pin dialog
        val askingPinDialog = PaywellPinDialog("", object : PaywellPinDialog.IonClickInterface {
            override fun onclick(pin: String) {
              val   payWellPin = pin.toInt()
            //    callActive(item)

            }
        })
        askingPinDialog.show(supportFragmentManager, "Pin Dialog")
    }


//
//    private fun callActive( item : TransactionDataItem) {
//        showProgressDialog()
//
//        val activePakage = ActivePakage()
//
//        activePakage.dateOfBirth = item.birth_date
//        activePakage.gender = gender
//        activePakage.identificationType = idType
//        activePakage.memberName = memberName
//        activePakage.mobile = mobileNumber
//        activePakage.nomineePhoneNumber = otherNumber
//        activePakage.packagesId = healthPackage
//        activePakage.password = payWellPin
//        activePakage.username = mAppHandler.userName
//        activePakage.identificationNumber = memberidcard
//
//
//
//        ApiUtils.getAPIServiceV2().activatePackage(activePakage).enqueue(object : Callback<ActiveResponse> {
//            override fun onResponse(call: Call<ActiveResponse>, response: Response<ActiveResponse>) {
//                dismissProgressDialog()
//
//                if (response.isSuccessful){
//
//
//
//                    val body = response.body()
//                    val msg  = response.message()
//
//                    if(response.body()?.statusCode == 200){
//                        showSuccessDialog("Health Insurence", response.body()!!.message)
//                    }else{
//                        showErrorCallBackMessagev1(response.body()?.message)
//
//                    }
//
//
//
//
//                }else{
//                    showErrorMessagev1(response.message())
//                }
//
//
//
//            }
//
//            override fun onFailure(call: Call<ActiveResponse>, t: Throwable) {
//                dismissProgressDialog()
//                showErrorMessagev1(t.message)
//            }
//        })
//
//
//
//
//    }


}