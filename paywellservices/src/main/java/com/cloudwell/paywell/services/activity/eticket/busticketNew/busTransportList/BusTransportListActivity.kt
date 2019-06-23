package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.view.SeachViewStatus
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.bookingCencel.fragment.CancellationStatusMessageFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Activity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.viewModel.FlightSearchViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.BusTripListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import com.cloudwell.paywell.services.retrofit.ApiUtils
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_search_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BusTransportListActivity : BusTricketBaseActivity(), IDatePicker {

    lateinit var requestBusSearch: RequestBusSearch
    var isReSchuduler = false

    var items = mutableListOf<TripScheduleInfoAndBusSchedule>()

    var busTripListAdapter: BusTripListAdapter? = null

    override fun onSetNewDate(year: Int, month: Int, day: Int) {

        val mMonth = month + 1

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


        val split = humanReadAbleDate.split("-")
        val month = split[1].toInt() - 1
        myDateTimelineView.setNewDate(split[0].toInt(), month, split[2].toInt())
        myDateTimelineView.setOnDateChangeLincher(this)

        // mViewModelFlight.onSetDate(isInternetConnection, humanReadAbleDate, requestBusSearch)

    }

    private lateinit var mViewModelFlight: FlightSearchViewModel


    override fun onSetDate(year: Int, month: Int, day: Int) {
        val mMonth = month + 1;

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


        // mViewModelFlight.onSetDate(isInternetConnection, humanReadAbleDate, requestBusSearch)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_transport_list)

        setToolbar(getString(R.string.title_bus_transtport_list), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))

//        requestBusSearch = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestBusSearch

        requestBusSearch = RequestBusSearch()
        requestBusSearch.to = "Dhaka"
        requestBusSearch.from = "Kolkata"
        requestBusSearch.date = "2019-07-01"
        initializationView();

        callLocalSearch(requestBusSearch)


    }

    private fun callLocalSearch(requestBusSearch: RequestBusSearch) {


        BusTicketRepository(this).searchTransport(requestBusSearch).observeForever {
            it?.let { it1 -> setAdapter(it1) }

        }


    }

    private fun initializationView() {
//        btSerachAgain.setOnClickListener {
//            finish()
//        }

    }


    private fun setAdapter(it: List<TripScheduleInfoAndBusSchedule>) {
        Fresco.initialize(applicationContext);

        items = it.toMutableList()

        shimmer_recycler_view.showShimmerAdapter()
        shimmer_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = it.let { it1 ->


            val busTicketRepository = BusTicketRepository(this)

            busTripListAdapter = BusTripListAdapter(it1, applicationContext, requestBusSearch, busTicketRepository, object : OnClickListener {
                override fun onUpdateData(position: Int, resSeatInfo: ResSeatInfo) {

                    val get = items.get(position)
                    get.resSeatInfo = resSeatInfo
                    items.set(position, get)
                    busTripListAdapter?.notifyItemChanged(position)
                }


                override fun onClick(position: Int) {
                    // do whatever
                    val get = mViewModelFlight.mListMutableLiveDataFlightData.value?.get(position)
                    val mSearchId = mViewModelFlight.mSearchId.value
                    val resultID = get?.resultID


                    AppStorageBox.put(applicationContext, AppStorageBox.Key.SERACH_ID, mSearchId)
                    AppStorageBox.put(applicationContext, AppStorageBox.Key.Request_ID, resultID)


                    val intent = Intent(applicationContext, FlightDetails1Activity::class.java)
                    intent.putExtra("mSearchId", mSearchId)
                    intent.putExtra("resultID", resultID)
                    startActivity(intent)
                }
            })
            busTripListAdapter
        }
    }


    private fun handleViewStatus(status: SeachViewStatus?) {

        status.let {
            if (it?.isShowShimmerView == true) {
                shimmer_recycler_view.showShimmerAdapter()
            } else {
                shimmer_recycler_view.hideShimmerAdapter();
            }

            if (it?.isShowProcessIndicator == true) {
                progressBar2.visibility = View.VISIBLE
            } else {
                progressBar2.visibility = View.GONE
            }


            if (!it?.noSerachFoundMessage.equals("")) {
                shimmer_recycler_view.visibility = View.INVISIBLE
                layoutNoSerachFound.visibility = View.VISIBLE
            } else {
                shimmer_recycler_view.visibility = View.VISIBLE
                layoutNoSerachFound.visibility = View.INVISIBLE
            }


        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun askForPin(cancelReason: String, result: Result) {
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

                    submitRescheduleAPI(userName, PIN_NO, datum.bookingId, cancelReason, mViewModelFlight.mSearchId.value, result.resultID, "json")


                } else {
                    val snackbar = Snackbar.make(linearLayout3!!, R.string.connection_error_msg, Snackbar.LENGTH_LONG)
                    snackbar.setActionTextColor(Color.parseColor("#ffffff"))
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.parseColor("#4CAF50"))
                    snackbar.show()
                }
            } else {
                val snackbar = Snackbar.make(linearLayout3!!, R.string.pin_no_error_msg, Snackbar.LENGTH_LONG)
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

    private fun submitRescheduleAPI(userName: String, pass: String, bookingId: String?, cancelReason: String, searchId: String?, resultID: String, apiFormat: String) {
        showProgressDialog()


        ApiUtils.getAPIService().reScheduleTicket(userName, pass, bookingId, cancelReason, searchId, resultID, apiFormat).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                dismissProgressDialog()

                if (response.isSuccessful) {

                    if (response.isSuccessful) {
                        val jsonObject = response.body()
                        val message = jsonObject!!.get("message_details").asString
                        if (jsonObject.get("status").asInt == 200) {
                            showMsg(message)
                        } else {
                            showMsg(message)
                        }

                    }
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@BusTransportListActivity, "Network error!!!", Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
            }
        })

    }

    private fun showMsg(msg: String) {

        val priceChangeFragment = CancellationStatusMessageFragment()
        CancellationStatusMessageFragment.message = msg

        priceChangeFragment.show(supportFragmentManager, "dialog")

    }
}
