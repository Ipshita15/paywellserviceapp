package com.cloudwell.paywell.services.activity.eticket.busticketNew.search

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository

class BusSearchActivity : BusTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_search)



        BusTicketRepository(this).getBusListData().observeForever {
            val b: Boolean = it?.status == 200
            if (b) {
                val data = it?.data?.data
                data?.let { it1 -> BusTicketRepository(this).saveBuss(it1) }
            } else {

            }
        }

    }
}
