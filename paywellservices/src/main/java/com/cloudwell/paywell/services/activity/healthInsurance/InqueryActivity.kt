package com.cloudwell.paywell.services.activity.healthInsurance

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.HealthInsuranceBaseActivity
import com.cloudwell.paywell.services.activity.healthInsurance.adapter.ClaimAdapter
import com.cloudwell.paywell.services.activity.healthInsurance.model.ClaimDataItem
import com.cloudwell.paywell.services.activity.healthInsurance.model.ClaimResponse
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_claim.*

class InqueryActivity : HealthInsuranceBaseActivity() {

    var responseDetails:  List<ClaimDataItem?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_claim)

        setToolbar(getString(R.string.trx_details))


        val data = intent.getStringExtra(getString(R.string.health_claim_tag))
        val response = Gson().fromJson(data, ClaimResponse::class.java)
        responseDetails = response.transactionData



        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        health_claim_recycler.layoutManager = linearLayoutManager

        val adapter = ClaimAdapter(applicationContext, responseDetails)
        health_claim_recycler.adapter = adapter




    }


}