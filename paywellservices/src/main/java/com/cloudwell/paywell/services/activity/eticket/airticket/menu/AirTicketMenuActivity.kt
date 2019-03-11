package com.cloudwell.paywell.services.activity.eticket.airticket.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.BookingCancelActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.TransactionLogActivity
import kotlinx.android.synthetic.main.activity_air_tricket_menu.*

class AirTicketMenuActivity : AirTricketBaseActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_tricket_menu)
        setToolbar(getString(R.string.home_eticket_air))
        btSerach.setOnClickListener(this)
        btCencel.setOnClickListener(this)
        btTransationLog.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btSerach -> {
                startActivity(Intent(applicationContext, AirTicketMainActivity::class.java))
            }

            R.id.btCencel -> {
                startActivity(Intent(applicationContext, BookingCancelActivity::class.java))
            }

            R.id.btTransationLog -> {
                startActivity(Intent(applicationContext, TransactionLogActivity::class.java))
            }
        }
    }

}
