package com.cloudwell.paywell.services.activity.eticket.airticket.booking

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.BookingList
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.BookingStatusListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.ItemClickListener
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.activity_booking_main.*
import kotlinx.android.synthetic.main.item_booking_status.*


class BookingStatusActivity : AirTricketBaseActivity() {

    lateinit var responseList: BookingList
    lateinit var tag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_booking_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = getString(com.cloudwell.paywell.services.R.string.booking_status_menu)
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#189d49")));
        }
        responseList = AppStorageBox.get(applicationContext, AppStorageBox.Key.AIRTICKET_BOOKING_RESPONSE) as BookingList

        val bundle = intent.extras
        if (!bundle.isEmpty) {
            tag = bundle.getString("tag")
        }
        initViewInitialization()
    }

    private fun initViewInitialization() {
        val customAdapter = BookingStatusListAdapter(responseList, this.applicationContext, object : ItemClickListener {
            override fun onItemClick(datum: Datum) {
                datum.invoiceUrl = "notify.paywellonline.com\\/haltripFiles\\/HTS19032310529.pdf"
                showTricketIntentPopupMessage(datum)
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

    private fun showTricketIntentPopupMessage(datum: Datum) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.please_seleted_a_option))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.view_tricket), DialogInterface.OnClickListener { dialog, id ->

                })
                .setNegativeButton(getString(R.string.email), DialogInterface.OnClickListener { dialog, id ->

                })
        val alert = builder.create()
        alert.show()

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
