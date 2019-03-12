package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog

import android.os.Bundle
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity

class TransactionLogActivity : AirTricketBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cencel_booking)
        setToolbar(getString(R.string.tile_cencel_transaction_log))

    }


}
