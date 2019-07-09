package com.cloudwell.paywell.services.activity.eticket.busticketNew

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus_booth_departure.*
import org.json.JSONObject

class BusPassengerBoothDepartureActivity : BusTricketBaseActivity() {

    private lateinit var busListAdapter: Any
    private val allBoothInfo = mutableListOf<BoothInfo>()
    private val allBoothNameInfo = mutableListOf<String>()

    internal lateinit var model: TripScheduleInfoAndBusSchedule
    private lateinit var boothList: Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_booth_departure)
        setToolbar(getString(R.string.title_bus_passenger_booth), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        val stringExtra = intent.getStringExtra("jsonData")
        val seatLevel = intent.getStringExtra("seatLevel")
        val totalPrices = intent.getStringExtra("totalPrices")


        model = Gson().fromJson(stringExtra, TripScheduleInfoAndBusSchedule::class.java)

        val boothDepartureInfo = model.busSchedule?.booth_departure_info


        busSeatsTV.text = seatLevel + " | TK." + totalPrices


        val jsonObject = JSONObject(boothDepartureInfo)
        jsonObject.keys().forEach {
            val model = jsonObject.get(it) as JSONObject
            val boothName = model.getString("booth_name")
            val boothDepartureTime = model.getString("booth_departure_time")
            allBoothInfo.add(BoothInfo(it, boothName, boothDepartureTime))
            allBoothNameInfo.add(boothName)
        }


        boothList = findViewById<Spinner>(R.id.boothList)
        busListAdapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, allBoothNameInfo)
        this.boothList.setAdapter(busListAdapter as ArrayAdapter<String>)

    }
}
