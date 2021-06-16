package com.cloudwell.paywell.services.activity.eticket.airticket

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import kotlinx.android.synthetic.main.activity_air_ticket_main.*

open class AirTicketMainActivity : AirTricketBaseActivity() {

    companion object {
        var item: Datum = Datum()
        fun newIntent(context: Context, item: Datum): Intent {
            val intent = Intent(context, AirTicketMainActivity::class.java)
            this.item = item
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_air_ticket_main)

        assert(supportActionBar != null)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(getString(R.string.search_flights))
            supportActionBar!!.elevation = 0f
            supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#189d49")));
        }

        val fragmentAdapter = SearchFlightAdapter(supportFragmentManager, item)
        viewpager_main.adapter = fragmentAdapter

        tabLayout_main.setupWithViewPager(viewpager_main)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        item = Datum()
        finish()
        super.onBackPressed()
    }
}
