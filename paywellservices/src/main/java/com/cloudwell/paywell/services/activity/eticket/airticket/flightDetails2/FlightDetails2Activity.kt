package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.AllSummaryActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment.BaggageAndPoliciesActiivty
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment.FlightFareDialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.ResposeAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter.AdapterForPassengers
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.viewmodel.FlightDetails2ViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerAdd.AddPassengerActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.passengerList.PassengerListActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.RecyclerItemClickListener
import kotlinx.android.synthetic.main.contant_flight_details_2.*
import kotlinx.android.synthetic.main.review_bottom_sheet.*


class FlightDetails2Activity : AirTricketBaseActivity() {


    private lateinit var viewMode: FlightDetails2ViewModel
    lateinit var touchHelper: ItemTouchHelper
    lateinit var adapterForPassengers: AdapterForPassengers

    lateinit var resposeAirPriceSearch: ResposeAirPriceSearch

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_details_2)
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


        tvTotalPrice.text = resposeAirPriceSearch.data?.results?.get(0)?.totalFare.toString()

        tvTotalPrice.setOnClickListener {

            val dialogFragment = FlightFareDialogFragment()
            val get = resposeAirPriceSearch.data?.results?.get(0)?.fares?.get(0)

            val args = Bundle()
            args.putParcelable("object", get)

            dialogFragment.arguments = args
            dialogFragment.show(supportFragmentManager, "dialog")
        }


        viewReview.setOnClickListener {

            var totalSeletedCounter = 0
            var totalPassenger = 0
            var passengerString = ""

            val requestAirSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestAirSearch


            totalPassenger = (requestAirSearch.adultQuantity + requestAirSearch.childQuantity + requestAirSearch.infantQuantity).toInt();


            viewMode.mListMutableLiveDPassengers.value?.forEach {
                val passengerSleted = it.isPassengerSleted
                if (passengerSleted) {
                    passengerString = "$passengerString +${it.id},"
                    totalSeletedCounter = totalSeletedCounter + 1;
                }
            }

            if (totalSeletedCounter == totalPassenger) {
                AppStorageBox.put(applicationContext, AppStorageBox.Key.SELETED_PASSENGER_IDS, passengerString)
                startActivity(Intent(applicationContext, AllSummaryActivity::class.java))
            } else {
                showWarringForMissMax(requestAirSearch)
            }
        }


    }

    private fun showWarringForMissMax(requestAirSearch: RequestAirSearch) {


        var adultQuantityMessage = ""
        if (requestAirSearch.adultQuantity != 0L) {
            adultQuantityMessage = "${requestAirSearch.adultQuantity} adult"
        }


        var childQuantityMessage = ""
        if (requestAirSearch.childQuantity != 0L) {
            childQuantityMessage = ", ${requestAirSearch.childQuantity} child"
        }


        var infantQuantityMessage = ""
        if (requestAirSearch.infantQuantity != 0L) {
            infantQuantityMessage = ", ${requestAirSearch.infantQuantity} infant"
        }


        var message = "This price is only available for ${adultQuantityMessage} ${childQuantityMessage} ${infantQuantityMessage}.\nPlease provide all passenger information "

        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setCancelable(true)
                .setPositiveButton(getString(R.string.ok), DialogInterface.OnClickListener { dialog, id ->


                })
        val alert = builder.create()
        alert.show()

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
            resposeAirPriceSearch = AppStorageBox.get(applicationContext, AppStorageBox.Key.ResposeAirPriceSearch) as ResposeAirPriceSearch

            val shortDepartArriveTime = AppStorageBox.get(applicationContext, AppStorageBox.Key.ShortDepartArriveTime)
            val orignAirportAnddestinationairportCode = AppStorageBox.get(applicationContext, AppStorageBox.Key.orignAirportAnddestinationairportCode) as String
            val totalJourney_time = AppStorageBox.get(applicationContext, AppStorageBox.Key.totalJourney_time) as String
            val humanReadAbleDate = AppStorageBox.get(applicationContext, AppStorageBox.Key.humanReadAbleDate) as String

            tvNameOfDate.text = humanReadAbleDate + " " + shortDepartArriveTime
            tvOrginAndDestinationAirportCode.text = orignAirportAnddestinationairportCode
            tvShortDepartArriveTime.text = totalJourney_time

        } catch (e: Exception) {

        }


        tvPoliciesAndBaggageAllowance.setOnClickListener {

            val get = resposeAirPriceSearch.data?.results?.get(0)?.fares?.get(0) as Fare
//            var get = Fare()
//            get.baseFare = 34344343;
//            get.tax = 10;
//            get.currency = "Tk";
//            get.discount = 33;
//            get.otherCharges = 2000;
//            get.serviceFee = 33;


            AppStorageBox.put(applicationContext, AppStorageBox.Key.FARE_DATA, get)

            val s = Intent(this.applicationContext, BaggageAndPoliciesActiivty::class.java)
            startActivity(s)
        }




    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
