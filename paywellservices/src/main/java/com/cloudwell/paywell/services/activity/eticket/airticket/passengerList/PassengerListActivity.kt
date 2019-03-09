package com.cloudwell.paywell.services.activity.eticket.airticket.passengerList

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerList.viewModel.PassengerListViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.contant_passenger_list.*


class PassengerListActivity : AirTricketBaseActivity() {


    private lateinit var viewMode: PassengerListViewModel
    lateinit var touchHelper: ItemTouchHelper

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_passenger_list_activity)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_booking_and_review))

        initializationView()


        initViewModel()


    }

    override fun onResume() {
        super.onResume()
        viewMode.getAllPassengers();
    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(PassengerListViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        viewMode.mListMutableLiveDPassengers.observe(this, android.arch.lifecycle.Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })




    }

    private fun handleViewStatus(it: List<Passenger>) {

        val recyclerView = findViewById(R.id.recyclerViewPassenger) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val columns = 1;

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        val recyclerListAdapter = AdapterForPassengersEdit(this, it, object : AdapterForPassengersEdit.OnClickListener {

            override fun onDeleted(model: Passenger, position: Int) {


            }

            override fun onEditClick(model: Passenger, position: Int) {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRTRICKET_EDIT_PASSENGER, model)
                val intent = Intent(applicationContext, AddPassengerActivity::class.java)
                intent.putExtra("isEditFlag", true)
                startActivity(intent)

            }

        })


        recyclerView.layoutManager = glm
        recyclerView.adapter = recyclerListAdapter;
        recyclerView.isNestedScrollingEnabled = false;




    }


    private fun initializationView() {
        layoutAddPassenger.setOnClickListener {
            startActivity(Intent(applicationContext, AddPassengerActivity::class.java))
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
