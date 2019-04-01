package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatuViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.adapter.TransitionRVSectionAdapter
import com.cloudwell.paywell.services.app.AppHandler
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

class AirThicketTranslationLogActivity : AirTricketBaseActivity() {

    lateinit var tag: String

    private lateinit var viewMode: BookingStatuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_transtionlog)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.transaction_log))


        val bundle = intent.extras
        val limit = bundle.getInt(AirTicketMenuActivity.KEY_LIMIT)

        initViewModel(limit)
    }

    private fun initViewModel(limit: Int) {

        viewMode = ViewModelProviders.of(this).get(BookingStatuViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


        viewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        viewMode.responseList.observe(this, Observer {
            it?.let { it1 -> setupList(it1) }
        })



        viewMode.getBookingStatus(isInternetConnection, limit)


    }

    private fun setupList(it1: BookingList) {

        val groupBy = it1.data.groupBy {
            it.firstRequestDateTime?.split(" ")?.get(0)
        }

        val isEnglish = AppHandler.getmInstance(applicationContext)?.getAppLanguage().equals("en", ignoreCase = true)

        var sectionAdapter = SectionedRecyclerViewAdapter()

        groupBy.forEach {
            val transitionRVSectionAdapter = TransitionRVSectionAdapter(it.key.toString(), it.value, isEnglish)
            sectionAdapter.addSection(transitionRVSectionAdapter)

        }

        var linearLayoutManager = LinearLayoutManager(this)

        val sectionHeader = findViewById<RecyclerView>(R.id.listviewLog) as RecyclerView
        sectionHeader.setLayoutManager(linearLayoutManager)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)
        sectionHeader.isNestedScrollingEnabled = false;

        Log.e("", "");


//        val customAdapter = AirThicketTranslationLogAdatper(this, it1.data)
//        listBookingList.adapter = customAdapter
//
//
//        val mLayoutManager = LinearLayoutManager(applicationContext)
//        listBookingList.setLayoutManager(mLayoutManager)
//        listBookingList.setItemAnimator(DefaultItemAnimator())
//
//        tvSerialNumber.typeface = Typeface.DEFAULT_BOLD
//        tvBookingId.typeface = Typeface.DEFAULT_BOLD
//        tvDate.typeface = Typeface.DEFAULT_BOLD
//        tvBookingStatus.typeface = Typeface.DEFAULT_BOLD
//        tvAction.typeface = Typeface.DEFAULT_BOLD
//        tvAction.text = "Ticket Price"


//        TransitionRVSectionAdapter()


    }

    private fun handleViewStatus(it: BookingStatuViewStatus) {
        if (it.isShowProcessIndicatior) {
            showProgressDialog()
        } else {
            dismissProgressDialog()
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
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
