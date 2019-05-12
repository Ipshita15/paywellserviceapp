package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.reschedule

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.base.TransitionLogBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatsViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter.ItemClickListener as ItemClickListener1

class RescheduleActivity : TransitionLogBaseActivity() {

    lateinit var tag: String

    companion object {
        lateinit var item: Datum
        fun newIntent(context: Context, item: Datum): Intent {
            val intent = Intent(context, RescheduleActivity::class.java)
            this.item = item
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reschedule)
        setToolbar(getString(R.string.titel_reschedule))
        setAdapter(item)


    }

    private fun setAdapter(item: Datum) {


    }

    private fun initViewModel(limit: Int) {

        mViewMode = ViewModelProviders.of(this).get(BookingStatsViewModel::class.java)

        mViewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        mViewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })

        mViewMode.getBookingStatus(isInternetConnection, limit)

    }


    private fun handleViewStatus(it: BookingStatuViewStatus) {
        if (it.isShowProcessIndicatior) {
            showProgressDialog()
        } else {
            dismissProgressDialog()
        }

        if (!it.successMessageTricketStatus.equals("")) {
            showMsg(it.successMessageTricketStatus)
        }

        if (it.modelPriceChange != null) {
            showTricketPriceChangeDialog(it.modelPriceChange!!)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }


}
