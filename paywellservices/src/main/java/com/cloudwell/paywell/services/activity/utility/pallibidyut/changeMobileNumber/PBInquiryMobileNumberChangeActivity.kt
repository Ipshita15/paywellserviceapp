package com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.adapter.HeaderRVSectionForLog
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.MessageEventMobileNumberChange
import com.cloudwell.paywell.services.activity.utility.pallibidyut.changeMobileNumber.model.ResMobileChangeLog
import com.cloudwell.paywell.services.analytics.AnalyticsManager
import com.cloudwell.paywell.services.analytics.AnalyticsParameters
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.squareup.otto.Subscribe
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import java.text.SimpleDateFormat

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 17/1/19.
 */
class PBInquiryMobileNumberChangeActivity : AppCompatActivity() {
    private var mAppHandler: AppHandler? = null
    private var mRelativeLayout: RelativeLayout? = null


    companion object {

        var TRANSLOG_TAG = "TRANSLOGTXT"
    }

    @Subscribe
    fun onTransrationItemClick(event: MessageEventMobileNumberChange) {
        showFullInfo(event);

    }

    public override fun onStart() {
        super.onStart()
        GlobalApplicationBus.getBus().register(this)
    }

    public override fun onStop() {
        super.onStop()
        GlobalApplicationBus.getBus().unregister(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utility_inquiry_mobile_number_change)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(R.string.home_utility_pb_bill_status_inquiry_title)
        }
        mAppHandler = AppHandler.getmInstance(applicationContext)
        mRelativeLayout = findViewById(R.id.relativeLayout)

        initView()

        AnalyticsManager.sendScreenView(AnalyticsParameters.KEY_UTILITY_POLLI_BIDDUT_MOBILE_NUMBER_CHANGE_INQUIRY)


    }

    private fun initView() {

        try {

            val response = TRANSLOG_TAG
            val resMobileChangeLogs = Gson().fromJson(response, Array<ResMobileChangeLog>::class.java)
            if (resMobileChangeLogs.size < 0) {
                showNoDataFoundMessage()
                return
            }

            var sectionAdapter = SectionedRecyclerViewAdapter()
            val allHeaderSet = HashSet<String>()
            val allData = kotlin.collections.mutableMapOf<String, List<ResMobileChangeLog>>()
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat = SimpleDateFormat("dd MMM yyyy")

            val isEnglish = mAppHandler?.getAppLanguage().equals("en", ignoreCase = true)


            for (i in resMobileChangeLogs.indices) {
                val resMobileChangeLog = resMobileChangeLogs[i]
                val dateFormatedFirst = inputFormat.parse(resMobileChangeLog.requestDatetime)
                val outputDateStr = outputFormat.format(dateFormatedFirst)

                allHeaderSet.add(outputDateStr);
            }


            for (s in allHeaderSet) {
                val tempData = ArrayList<ResMobileChangeLog>()
                for (i in resMobileChangeLogs.indices) {
                    val date = resMobileChangeLogs[i].requestDatetime
                    val dateFormatedFirst = inputFormat.parse(date)
                    val outputDateStr = outputFormat.format(dateFormatedFirst)

                    if (outputDateStr == s) {
                        tempData.add(resMobileChangeLogs[i])
                    }
                }
                allData[s] = tempData

            }

            sectionAdapter = SectionedRecyclerViewAdapter()

            for ((index, value) in allHeaderSet.withIndex()) {

                val sectionData = HeaderRVSectionForLog(index, value, allData.get(value), isEnglish)
                sectionAdapter.addSection(sectionData)
            }
            var linearLayoutManager = LinearLayoutManager(this)

            val sectionHeader = findViewById<RecyclerView>(R.id.listviewLog) as RecyclerView
            sectionHeader.setLayoutManager(linearLayoutManager)
            sectionHeader.setHasFixedSize(true)
            sectionHeader.setAdapter(sectionAdapter)
            sectionHeader.isNestedScrollingEnabled = false;
        } catch (e: Exception) {
            showNoDataFoundMessage()
        }


    }

    private fun showNoDataFoundMessage() {
        val snackbar = Snackbar.make(mRelativeLayout!!, R.string.no_data_msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.parseColor("#ffffff"))
        val snackBarView = snackbar.view
        snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
        snackbar.show()
        Handler().postDelayed({ onBackPressed() }, 2000)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            this.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }

    private fun showFullInfo(obj: MessageEventMobileNumberChange) {
        val o = obj.resMobileChangeLog;
        val context = AppController.getContext();

        var message = context.getString(R.string.acc_no_des) + o.customerAccNo + "\n" +
                context.getString(R.string.phone_no_des) + o.customerPhn + "\n" +
                context.getString(R.string.trx_id_des) + o.trxId + "\n"
        context.getString(R.string.date_des) + o.requestDatetime + "\n"

        if (o.responseDetails != null) {
            if (!o.responseDetails.equals("")) {
                message = message + "\n \n" + context.getString(R.string.details)
            }
        }


        val builder = AlertDialog.Builder(this@PBInquiryMobileNumberChangeActivity)

        if (o.statusCode.equals("200")) {
            builder.setTitle(Html.fromHtml("<font color='#008000'>Result Successful</font>"));
        } else if (o.statusCode.equals("100")) {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result To Be Process</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#ff0000'>Result Failed</font>"));
        }
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.okay_btn)) { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show();
    }


}
