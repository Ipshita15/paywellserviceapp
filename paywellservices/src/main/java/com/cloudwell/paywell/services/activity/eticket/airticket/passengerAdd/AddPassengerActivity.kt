package com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.viewmodel.AddPassengerViewModel


class AddPassengerActivity : AirTricketBaseActivity() {


    private lateinit var viewMode: AddPassengerViewModel
    lateinit var touchHelper: ItemTouchHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_add_passenger)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_add_passenger))

        initializationView()


        initViewModel()


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(AddPassengerViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })


    }

    private fun handleViewStatus(it: List<Passenger>) {


    }


    private fun initializationView() {


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_passenger, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.add_passenger -> {

                addPassenger();

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addPassenger() {


    }

}
