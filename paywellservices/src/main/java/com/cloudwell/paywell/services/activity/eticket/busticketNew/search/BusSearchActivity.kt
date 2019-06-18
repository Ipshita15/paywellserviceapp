package com.cloudwell.paywell.services.activity.eticket.busticketNew.search

import android.content.Intent
import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.BusTransportListActivity
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.cloudwell.paywell.services.eventBus.model.MessageToBottom
import com.squareup.otto.Subscribe

class BusSearchActivity : BusTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_search)

        setToolbar(getString(R.string.title_bus_tickets), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))



        showProgressDialog()



        BusTicketRepository(this).getBusListData().observeForever {
            if (it == true) {
                dismissProgressDialog()
//                startActivity(Intent(this, SeatLayoutActivity::class.java))

                startActivity(Intent(this, BusTransportListActivity::class.java))


            }
        }


    }

    override fun onResume() {
        super.onResume()
        GlobalApplicationBus.getBus().register(this)
    }

    override fun onStop() {
        super.onStop()
        GlobalApplicationBus.getBus().unregister(this);
    }

    @Subscribe
    fun showErrorMessage(messageToBottom: MessageToBottom) {
        showSnackMessageWithTextMessage(messageToBottom.message)

    }


}
