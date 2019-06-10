package com.cloudwell.paywell.services.activity.eticket.busticketNew.search

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.cloudwell.paywell.services.eventBus.model.MessageToBottom
import com.squareup.otto.Subscribe

class BusSearchActivity : BusTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_search)


//        BusTicketRepository(this).loadAPIData();


        showProgressDialog()

        BusTicketRepository(this).getBusListData().observeForever {

            if (it == true) {
                dismissProgressDialog()
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
