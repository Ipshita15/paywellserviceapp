package com.cloudwell.paywell.services.activity.eticket.airticket.ticketCencel

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.Snackbar
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.utils.ConnectionDetector
import kotlinx.android.synthetic.main.activity_ticket_cencel.*
import mehdi.sakout.fancybuttons.FancyButton


class TricketCancelActivity : AirTricketBaseActivity() {
    private var bookingCancelAdapter: ArrayAdapter<*>? = null
    private val cancelReasonList = ArrayList<String>()

    private var bookingCancelReasonSPNR: Spinner? = null
    private var cancelBookingBtn: FancyButton? = null
    private var cd: ConnectionDetector? = null
    private var PIN_NO = "unknown"
    private var cancelMainLayout: ConstraintLayout? = null
    var mAppHandler: AppHandler? = null
    private var bookingId = String()
    private var title = String()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_cencel)

        if (supportActionBar != null) {

        }
        if (intent.getStringExtra(KEY_BOOKING_ID) != null) {
            bookingId = intent.getStringExtra(KEY_BOOKING_ID)
        }

        if (intent.getStringExtra(KEY_TITLE) != null) {
            title = intent.getStringExtra(KEY_TITLE)
            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        cd = ConnectionDetector(applicationContext)
        mAppHandler = AppHandler.getmInstance(applicationContext)

        cancelMainLayout = findViewById(R.id.cancelMainLayout)
        bookingCancelReasonSPNR = findViewById(R.id.cancelReasonSPNR)


        cancelBookingBtn = findViewById(R.id.cancelBookingBtn)
        cancelReasonList.add("Select your reason")
        cancelReasonList.add("Have another work")
        cancelReasonList.add("Travelling reason accomplished")
        cancelReasonList.add("Have to travel another city")
        cancelReasonList.add("Other")
        bookingCancelAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, cancelReasonList)
        bookingCancelReasonSPNR!!.adapter = bookingCancelAdapter


        bookingIdET.setText(model.bookingId)
        bookingIdET.setKeyListener(null)

        cancelBookingBtn!!.setText(title)
        cancelBookingBtn!!.setOnClickListener {
            if (!bookingIdET!!.text.toString().isEmpty() && bookingCancelReasonSPNR!!.selectedItem.toString() != "Select your reason") {

                val mAppHandler = AppHandler.getmInstance(applicationContext)
                val userName = mAppHandler.getImeiNo()

                val reason = bookingCancelReasonSPNR!!.selectedItem.toString()

                callCancelMapping(userName, bookingIdET.getText().toString(), reason, AirTricketBaseActivity.KEY_ticket_cancel, Datum())


            } else {

                hideUserKeyboard()

                val snackbar = Snackbar.make(cancelMainLayout!!, "Please Enter all the fields first", Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }

    }


    private fun showMsg(msg: String) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg

        priceChangeFragment.show(supportFragmentManager, "dialog")

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

    companion object {
        var KEY_BOOKING_ID = "Booking_Id"
        var KEY_TITLE = "KEY_TITLE"
        lateinit var model: Datum
    }

}
