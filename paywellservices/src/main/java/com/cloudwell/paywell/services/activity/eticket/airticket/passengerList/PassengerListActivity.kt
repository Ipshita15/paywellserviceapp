package com.cloudwell.paywell.services.activity.eticket.airticket.passengerList

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter.AdapterForPassengers
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.viewmodel.FlightDetails2ViewModel
import com.cloudwell.paywell.services.utils.RecyclerItemClickListener


class PassengerListActivity : AirTricketBaseActivity() {


    private lateinit var viewMode: FlightDetails2ViewModel
    lateinit var touchHelper: ItemTouchHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_passenger_list_activity)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_booking_and_review))

        initializationView()


        initViewModel()


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(FlightDetails2ViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        viewMode.mListMutableLiveDPassengers.observe(this, android.arch.lifecycle.Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })

        viewMode.getAllPassengers();


    }

    private fun handleViewStatus(it: List<Passenger>) {

        val recyclerView = findViewById(R.id.recyclerViewPassenger) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val columns = 2;

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        val recyclerListAdapter = AdapterForPassengers(this, it)
        recyclerView.layoutManager = glm
        recyclerView.adapter = recyclerListAdapter;
        recyclerView.isNestedScrollingEnabled = false;

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // do whatever
                        val get = viewMode.mListMutableLiveDPassengers.value?.get(position)
                        if (get?.isDefault!!) {
//                            val intent = Intent(applicationContext, )
                        }

                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )


    }


    private fun initializationView() {


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
