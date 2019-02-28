package com.cloudwell.paywell.services.activity.eticket.airticket

import android.os.Bundle
import android.view.Menu
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseActivity
import kotlinx.android.synthetic.main.contant_flight_details.*

class FlightDetailsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details)

        setToolbar(getString(R.string.title_booking_and_review))
//
//        val parcelable = getIntent().getExtras().getParcelable<Result>("object") as Result
//        Log.v("", "");


        ivUpDown.setOnClickListener {
            val expanded = expandable_layout_2.isExpanded
            if (expanded) {
                expandable_layout_2.collapse()
            } else {
                expandable_layout_2.expand()
            }
        }

        ivUpDown2.setOnClickListener {
            val expanded = expandable_layout1.isExpanded
            if (expanded) {
                expandable_layout1.collapse()
            } else {
                expandable_layout1.expand()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
