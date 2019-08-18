package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.activity.eticket.airticket.base.TransitionLogBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.BookingStatusListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.ItemClickListener
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.model.BookingStatuViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.viewModel.BookingStatsViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.menu.AirTicketMenuActivity
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.item_booking_status.*


class BookingStatusActivity : TransitionLogBaseActivity() {
    lateinit var tag: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_booking_main)
        setToolbar(getString(com.cloudwell.paywell.services.R.string.booking_status_menu))

        val bundle = intent.extras
        limit = bundle.getInt(AirTicketMenuActivity.KEY_LIMIT)

        initViewModel(limit)
    }


    private fun initViewModel(limit: Int) {

        mViewMode = ViewModelProviders.of(this).get(BookingStatsViewModel::class.java)

        mViewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


        mViewMode.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        mViewMode.responseList.observe(this, Observer {
            it?.let { it1 -> setupList(it1) }
        })



        mViewMode.getBookingStatus(isInternetConnection, limit)


    }

    private fun handleViewStatus(it: BookingStatuViewStatus) {
        if (it.isShowProcessIndicatior) {
            showProgressDialog()
        } else if (!it.isShowProcessIndicatior) {
            dismissProgressDialog()
        }

        if (!it.successMessageTricketStatus.equals("")) {
            showMsg(it.successMessageTricketStatus)
        }

        if (it.modelPriceChange != null) {
            showTicketPriceChangeDialog(it.modelPriceChange!!)
        }
    }


    fun setupList(responseList: BookingList) {
        val customAdapter = BookingStatusListAdapter(responseList, this.applicationContext, object : ItemClickListener {
            override fun onItemClick(datum: Datum) {
                showThicketIntentPopupMessage(datum)
            }

            override fun onActionButtonClick(position: Int, model: Datum) {
                showActionMenuPopupMessate(model)
            }

        })

        listBookingList.adapter = customAdapter

        val mLayoutManager = LinearLayoutManager(applicationContext)
        listBookingList.setLayoutManager(mLayoutManager)
        listBookingList.setItemAnimator(DefaultItemAnimator())

        tvSerialNumber.typeface = Typeface.DEFAULT_BOLD
        tvBookingId.typeface = Typeface.DEFAULT_BOLD
        tvDate.typeface = Typeface.DEFAULT_BOLD
        tvBookingStatus.typeface = Typeface.DEFAULT_BOLD
        tvAction.typeface = Typeface.DEFAULT_BOLD
    }


    private fun submitBookingListRequest(limit: Int, tag: String) {
        showProgressDialog()

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
