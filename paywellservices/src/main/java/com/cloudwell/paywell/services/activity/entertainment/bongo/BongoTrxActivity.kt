package com.cloudwell.paywell.services.activity.entertainment.bongo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import com.cloudwell.paywell.services.activity.entertainment.bongo.adapter.BongoTrxLogAdapter
import com.cloudwell.paywell.services.activity.entertainment.bongo.model.*
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bongo_trx.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BongoTrxActivity : BaseActivity() {

    private var mAppHandler: AppHandler? = null
    var responseDetails: ArrayList<ResponseDetailsItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bongo_trx)

        hideToolbar()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.parseColor("#b31f2b"))
        };

        val data = intent.getStringExtra(getString(R.string.bongo_trx_tag))
        val response = Gson().fromJson(data, BongoTRXresponse::class.java)
        responseDetails = response.responseDetails

        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL,false)
        bongo_transaction_log.layoutManager = linearLayoutManager

        val adapter = BongoTrxLogAdapter(applicationContext, responseDetails)
        bongo_transaction_log.adapter = adapter

        bongo_trx_back.setOnClickListener(View.OnClickListener {
            finish()
        })

    }


}
