package com.cloudwell.paywell.services.activity.eticket.airticket

import android.os.Bundle
import android.view.Menu
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity

class FlightDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        setToolbar(getString(R.string.title_booking_and_review))
//
//        val parcelable = getIntent().getExtras().getParcelable<Result>("object") as Result
//        Log.v("", "");

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
