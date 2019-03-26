package com.cloudwell.paywell.services.activity.eticket.airticket.ticketViewer

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/25/19.
 */
class TicketViewerActivity : AirTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_ticket_view)
        setToolbar(getString(R.string.title_ticket_viewe))
        initViewInitialization()


    }

    private fun initViewInitialization() {

//        Uri.fromFile()
//
//        pdfView.fromUri(Uri)
    }
}