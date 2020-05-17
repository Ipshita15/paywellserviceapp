package com.cloudwell.paywell.services.activity.education.bbc

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.education.bbc.adapter.TransactionLogAdapter
import com.cloudwell.paywell.services.activity.education.bbc.model.ResponseDetailsItem
import com.cloudwell.paywell.services.activity.education.bbc.model.TransactionResponsePOjo
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_transcation_list.*

class TranscationListActivity : AppCompatActivity() {

    var responseDetails: List<ResponseDetailsItem?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transcation_list)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.bbc_trx_action_title))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#5aac40")));
        }

        val data = intent.getStringExtra("data")
        val response = Gson().fromJson(data, TransactionResponsePOjo::class.java)
        responseDetails = response.responseDetails


        val linearLayoutManager : LinearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        bbc_transaction_log.layoutManager = linearLayoutManager

        val adapter = TransactionLogAdapter(applicationContext, responseDetails)
        bbc_transaction_log.adapter = adapter


    }
}
