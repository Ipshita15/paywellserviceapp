package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Passenger
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo.adapter.AdapterForPassengersBookingInfo
import com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo.adapter.AirportListBookingInfoAdapter
import kotlinx.android.synthetic.main.contant_view_booking_info.*

class ViewBookingInfoActivity : AirTricketBaseActivity() {
    companion object {
        lateinit var item: Datum
        fun newIntent(context: Context, item: Datum): Intent {
            val intent = Intent(context, ViewBookingInfoActivity::class.java)
            this.item = item
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_booking_info)
        setToolbar(getString(R.string.booking_info))

        initializationView()
    }

    private fun initializationView() {
        val trips = item.trips

//        val airline = resposeAirPriceSearch.data?.results?.get(0)?.segments?.get(0)?.airline


        val airportList = mutableSetOf<Airport>()

        trips.forEach {

            val originAirport = Airport()
            originAirport.airportCode = it.originAirportCode
            originAirport.airportName = it.originAirportName
            originAirport.terminal = it.originTerminal
            originAirport.countryName = it.originCountry
            originAirport.cityName = it.originCity
            airportList.add(originAirport)


            val destinationAirport = Airport()
            destinationAirport.airportCode = it.destinationAirportCode
            destinationAirport.airportName = it.destinationAirportName
            destinationAirport.terminal = it.destinationTerminal
            destinationAirport.countryName = it.destinationCountry
            destinationAirport.cityName = it.destinationCity



            airportList.add(destinationAirport)
        }

        recyclerViewAirports.setNestedScrollingEnabled(false)
        recyclerViewAirports.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewAirports.setLayoutManager(mLayoutManager)
        recyclerViewAirports.setItemAnimator(DefaultItemAnimator())


        val recyclerListAdapter = AirportListBookingInfoAdapter(applicationContext, airportList.toList())
        recyclerViewAirports.adapter = recyclerListAdapter




        item.passengers?.toMutableList()?.let { handlePassengerList(it) }

//
//
//
//
//        tvAirlineCode.text = getString(com.cloudwell.paywell.services.R.string.airline_code) + " ${airline?.airlineCode}"
//        tvAirlesscode.text = getString(com.cloudwell.paywell.services.R.string.airport_name) + " ${airline?.airlineName}"
//        tvFlghtNumber.text = getString(com.cloudwell.paywell.services.R.string.flight_number) + " ${airline?.flightNumber}"
//        tvBookingClass.text = getString(com.cloudwell.paywell.services.R.string.booking_class) + " ${airline?.bookingClass}"
//        tvOperatorCarrier.text = getString(com.cloudwell.paywell.services.R.string.operating_carrier) + " ${airline?.operatingCarrier}"
//        tvCabinClass.text = getString(com.cloudwell.paywell.services.R.string.cabin_class) + " ${airline?.cabinClass}"
//
//        val segments = resposeAirPriceSearch.data?.results?.get(0)?.segments
//        if (segments!!.size!! > 1) {
//            val arrTime = segments?.get(0)?.destination?.arrTime
//            val depTime = segments?.get(segments.size - 1)?.origin?.depTime
//
//            val arrTimeSplit = arrTime?.split("T")
//            val depTimeSplit = depTime?.split("T")
//
//            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
//            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])
//
//            tveDpartureTime.text = getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
//            tvArrivalTime.text = getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]
//        } else {
//
//
//            val arrTime = segments?.get(0)?.destination?.arrTime
//            val depTime = segments?.get(0)?.origin?.depTime
//            val arrTimeSplit = arrTime?.split("T")
//            val depTimeSplit = depTime?.split("T")
//
//            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
//            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])
//
//            tveDpartureTime.text = getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
//            tvArrivalTime.text = getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]
//
//        }
//
//
//        tvBaggage.text = getString(com.cloudwell.paywell.services.R.string.baggage) + resposeAirPriceSearch.data?.results?.get(0)?.segments?.get(0)?.baggage + " " + getString(com.cloudwell.paywell.services.R.string.kg_per_adult)

    }

    private fun handlePassengerList(it: MutableList<Passenger>) {
        val recyclerView = findViewById(R.id.recycleviewForPassenger) as RecyclerView


        val glm = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = glm
        glm.isAutoMeasureEnabled = true

        recyclerView.layoutManager = glm


        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);


        val recyclerListAdapter = AdapterForPassengersBookingInfo(this, it)

        recyclerView.adapter = recyclerListAdapter


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home)
            onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        finish()
    }
}
