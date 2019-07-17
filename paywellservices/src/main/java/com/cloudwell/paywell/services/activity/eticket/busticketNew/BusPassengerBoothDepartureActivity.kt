package com.cloudwell.paywell.services.activity.eticket.busticketNew

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketConfirmFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.BusTicketStatusFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.MyClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.BoothInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResBusSeatCheckAndBlock
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus_booth_departure.*
import org.json.JSONObject

class BusPassengerBoothDepartureActivity : BusTricketBaseActivity(), IbusTransportListView {
    override fun showSeatCheckAndBookingRepose(it: ResBusSeatCheckAndBlock) {


        val t = BusTicketConfirmFragment()
        it.ticketInfo?.seats = seatLevel
        BusTicketConfirmFragment.ticketInfo = it.ticketInfo!!
        t.show(supportFragmentManager, "dialog")
        t.setOnClickListener(object : MyClickListener, OnClickListener {
            override fun onClick() {

                askForPin()
            }
        })

        val transId = "1234"
        viewMode.callconfirmPayment(isInternetConnection,
                transId,
                fullNameTV.text.toString(),
                mobileNumberTV.text.toString(),
                etAddress.text.toString(),
                etEmail.text.toString(),
                ageTV.text.toString(),
                password

        )

    }

    override fun showErrorMessage(meassage: String) {

        val t = BusTicketStatusFragment()
        BusTicketStatusFragment.message = "Not enough balance"
        t.show(supportFragmentManager, "dialog")

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
    var password = "";


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

    private fun askForPin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.pin_no_title_msg)

        val pinNoET = EditText(this)
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        pinNoET.gravity = Gravity.CENTER_HORIZONTAL
        pinNoET.layoutParams = lp
        pinNoET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pinNoET.transformationMethod = PasswordTransformationMethod.getInstance()
        builder.setView(pinNoET)

        builder.setPositiveButton(R.string.okay_btn) { dialogInterface, id ->
            val inMethMan = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inMethMan.hideSoftInputFromWindow(pinNoET.windowToken, 0)

            if (pinNoET.text.toString().length != 0) {
                dialogInterface.dismiss()
                val PIN_NO = pinNoET.text.toString()
                if (isInternetConnection) {

                    val mAppHandler = AppHandler.getmInstance(application)
                    val userName = mAppHandler.imeiNo

                    val datum = AppStorageBox.get(applicationContext, AppStorageBox.Key.REQUEST_API_reschedule) as Datum

//                    submitRescheduleAPI(userName, PIN_NO, datum.bookingId, cancelReason, mViewModelFlight.mSearchId.value, result.resultID, "json")


                } else {
                    val snackbar = Snackbar.make(rootLayout, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(rootLayout, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
                snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                snackbar.show()
            }
        }
        builder.setNegativeButton(R.string.cancel_btn) { dialogInterface, i -> dialogInterface.dismiss() }
        val alert = builder.create()
        alert.show()
    }
}
