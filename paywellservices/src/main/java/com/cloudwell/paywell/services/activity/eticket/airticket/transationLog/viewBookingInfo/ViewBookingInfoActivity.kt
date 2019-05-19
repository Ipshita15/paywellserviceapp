package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo

import android.annotation.SuppressLint
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

    @SuppressLint("SetTextI18n")
    private fun initializationView() {
        val trips = item.trips

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

        recyclerViewAirports.isNestedScrollingEnabled = false
        recyclerViewAirports.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerViewAirports.layoutManager = mLayoutManager
        recyclerViewAirports.itemAnimator = DefaultItemAnimator()


        val recyclerListAdapter = AirportListBookingInfoAdapter(applicationContext, airportList.toList())
        recyclerViewAirports.adapter = recyclerListAdapter

        item.passengers?.toMutableList()?.let { handlePassengerList(it) }

        tvBaggage.text = getString(R.string.baggage) + trips[0].baggage + " " + getString(R.string.kg_per_adult)

        var text = ""


        for ((i, value) in trips.withIndex()) {
            val arrivalTime = value.arrivalTime
            val departureTime = value.departureTime

            text = text + "Departure Time " + (i + 1) + ": " + departureTime + "\n"
            text = text + "Arrival Time " + (i + 1) + ": " + arrivalTime + "\n\n"

        }

        text = text.substring(0, text.length - 2)
        tveDpartureTime.text = "" + text


        val trip = item.trips[0]
        tvAirlineCode.text = getString(R.string.airline_code) + " ${trip.airlineCode}"
        tvAirlesscode.text = getString(R.string.airport_name) + " ${trip.airlineName}"
        tvFlghtNumber.text = getString(R.string.flight_number) + " ${trip.flightNumber}"
        tvBookingClass.text = getString(R.string.booking_class) + " ${trip.bookingClass}"
        tvOperatorCarrier.text = getString(R.string.operating_carrier) + " ${trip.operatingCareer}"
        tvCabinClass.text = getString(R.string.cabin_class) + " ${trip.cabinClass}"

    }

    private fun handlePassengerList(it: MutableList<Passenger>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recycleviewForPassenger)

        val recyclerListAdapter = AdapterForPassengersBookingInfo(this, it)

        val glm = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = glm
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.layoutManager = glm
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
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
