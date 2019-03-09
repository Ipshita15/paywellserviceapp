package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter.AdapterForPassengers
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.viewmodel.FlightDetails2ViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerList.PassengerListActivity
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.contant_flight_details_2.*
import kotlinx.android.synthetic.main.review_bottom_sheet.*




class FlightDetails2Activity : AirTricketBaseActivity() {


    private lateinit var viewMode: FlightDetails2ViewModel
    lateinit var touchHelper: ItemTouchHelper
    lateinit var adapterForPassengers: AdapterForPassengers

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_flight_details_2)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_booking_and_review))

        initializationView()
        initilizationReviewBottomSheet()

        initViewModel()


    }

    private fun initilizationReviewBottomSheet() {
        val bottomSheetBehavior = BottomSheetBehavior.from(reviewBottonSheet)


        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {

                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })


    }

    override fun onResume() {
        super.onResume()
        viewMode.getAllPassengers()
    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(FlightDetails2ViewModel::class.java)

        viewMode.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        viewMode.mListMutableLiveDPassengers.observe(this, android.arch.lifecycle.Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


    }

    private fun handleViewStatus(it: List<Passenger>) {

        val recyclerView = findViewById(com.cloudwell.paywell.services.R.id.recyclerViewPassenger) as RecyclerView
        recyclerView.setHasFixedSize(true)

        val columns = 2

        val glm = GridLayoutManager(applicationContext, columns)
        recyclerView.layoutManager = glm


        adapterForPassengers = AdapterForPassengers(this, it)
        recyclerView.layoutManager = glm
        recyclerView.adapter = adapterForPassengers;
        recyclerView.isNestedScrollingEnabled = false;

        recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext, recyclerView, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // do whatever
                        val get = viewMode.mListMutableLiveDPassengers.value?.get(position)
                        if (get?.isDefault!!) {

                            val get1 = AppStorageBox.get(applicationContext, AppStorageBox.Key.COUNTER_PASSENGER)
                            if (get1 == null) {
                                startActivity(Intent(applicationContext, AddPassengerActivity::class.java))
                            } else {
                                startActivity(Intent(applicationContext, PassengerListActivity::class.java))
                            }
                        } else {

                            if (get.isPassengerSleted) {

                                get.isPassengerSleted = false

                            } else {
                                get.isPassengerSleted = true
                            }

                            viewMode.updatePassenger(get)


                            viewMode.mListMutableLiveDPassengers.value?.set(position, get)
                            adapterForPassengers.notifyDataSetChanged()

                        }

                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )


    }


    private fun initializationView() {
        try {
            val resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch
            val shortDepartArriveTime = AppStorageBox.get(applicationContext, AppStorageBox.Key.ShortDepartArriveTime)

            tvNameOfDate.text = "" + shortDepartArriveTime

        } catch (e: Exception) {

        }



    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
