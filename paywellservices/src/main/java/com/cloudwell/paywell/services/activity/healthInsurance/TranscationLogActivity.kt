package com.cloudwell.paywell.services.activity.healthInsurance

import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.healthInsurance.adapter.TrxLogAdapter
import com.cloudwell.paywell.services.activity.healthInsurance.model.TransactionDataItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.TrxResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_transcation_log.*

class TranscationLogActivity : HealthInsuranceBaseActivity() {

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




    }
}