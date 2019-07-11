package com.cloudwell.paywell.services.activity.eticket.busticketNew

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResBusSeatCheckAndBlock
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus_booth_departure.*
import org.json.JSONObject

class BusPassengerBoothDepartureActivity : BusTricketBaseActivity(), IbusTransportListView {
    override fun showSeatCheckAndBookingRepose(it: ResBusSeatCheckAndBlock) {

    }

    override fun showErrorMessage(meassage: String) {

        showDialogMesssage(meassage)
    }

    override fun showNoTripFoundUI() {


    }

    override fun setAdapter(it1: List<TripScheduleInfoAndBusSchedule>) {

    }

    override fun showProgress() {
        showProgressDialog()

    }

    override fun hiddenProgress() {
        dismissProgressDialog()
    }

    private lateinit var busListAdapter: Any
    private val allBoothInfo = mutableListOf<BoothInfo>()
    private val allBoothNameInfo = mutableListOf<String>()


    private lateinit var boothList: Spinner

    lateinit var viewMode: BusTransportViewModel
    internal lateinit var model: TripScheduleInfoAndBusSchedule
    internal lateinit var requestBusSearch: RequestBusSearch
    internal var totalAPIValuePrices: String = ""
    var seatLevel = ""
    var seatId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_booth_departure)
        setToolbar(getString(R.string.title_bus_passenger_booth), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))


        val stringExtra = intent.getStringExtra("jsonData")
        val requestBusSearchJson = intent.getStringExtra("requestBusSearch")
        seatLevel = intent.getStringExtra("seatLevel")
        seatId = intent.getStringExtra("seatId")
        val totalPrices = intent.getStringExtra("totalPrices")
        totalAPIValuePrices = intent.getStringExtra("totalAPIValuePrices")



        requestBusSearch = Gson().fromJson(requestBusSearchJson, RequestBusSearch::class.java)
        model = Gson().fromJson(stringExtra, TripScheduleInfoAndBusSchedule::class.java)

        val boothDepartureInfo = model.busSchedule?.booth_departure_info


        busSeatsTV.text = seatLevel
        tvTotalPrice.text = "TK." + totalPrices


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


        hiddenSoftKeyboard()

        initViewModel()

        btn_search.setOnClickListener {
            handleBookingContinueBooking()
        }


    }

    private fun initViewModel() {
        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)

    }

    private fun handleBookingContinueBooking() {
        val fullName = fullNameTV.text.toString()
        val mobileNumber = mobileNumberTV.text.toString()
        val age = ageTV.text.toString()

        if (fullName.equals("")) {
            textInputLayoutFirstName.error = "Invalid full name"
            return
        } else {
            textInputLayoutFirstName.error = null
        }


        if (mobileNumber.equals("")) {
            textInputLayoutmobileNumber.error = "Invalid mobile number"
            return
        } else {
            textInputLayoutmobileNumber.error = null
        }

        if (age.equals("")) {
            textInputLayoutAge.error = "Invalid age"
            return
        } else {
            textInputLayoutAge.error = null
        }

        val boothInfo = allBoothInfo.get(boothList.selectedItemPosition)



        viewMode.seatCheck(isInternetConnection, model, requestBusSearch, boothInfo, seatLevel, seatId, totalAPIValuePrices)


    }
}
