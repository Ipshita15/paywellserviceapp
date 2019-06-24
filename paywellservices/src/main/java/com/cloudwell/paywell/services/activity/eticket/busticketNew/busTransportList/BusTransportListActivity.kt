package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Activity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.viewModel.FlightSearchViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.BusTicketRepository
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.BusTripListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_bus_transport_list.*
import java.text.SimpleDateFormat
import java.util.*


class BusTransportListActivity : BusTricketBaseActivity(), IDatePicker, IbusTransportListView {
    lateinit var viewMode: BusTransportViewModel
    lateinit var requestBusSearch: RequestBusSearch
    var isReSchuduler = false

    var items = mutableListOf<TripScheduleInfoAndBusSchedule>()

    var busTripListAdapter: BusTripListAdapter? = null

    override fun showNoTripFoundUI() {
        shimmer_recycler_view.visibility = View.INVISIBLE
        layoutNoSerachFound.visibility = View.VISIBLE
    }

    override fun setAdapter(it: List<TripScheduleInfoAndBusSchedule>) {

        Fresco.initialize(applicationContext);

        items = it.toMutableList()

        shimmer_recycler_view.visibility = View.VISIBLE
        shimmer_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = it.let { it1 ->


            val busTicketRepository = BusTicketRepository()

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


    override fun showProgress() {
        progressBar2.visibility = View.VISIBLE
        shimmer_recycler_view.showShimmerAdapter()

    }

    override fun hiddenProgress() {
        progressBar2.visibility = View.GONE
        shimmer_recycler_view.hideShimmerAdapter()
    }


    override fun onSetNewDate(year: Int, month: Int, day: Int) {

        val mMonth = month + 1

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


        val split = humanReadAbleDate.split("-")
        val month = split[1].toInt() - 1
        myDateTimelineView.setNewDate(split[0].toInt(), month, split[2].toInt())
        // myDateTimelineView.setOnDateChangeLincher(this)

        // mViewModelFlight.onSetDate(isInternetConnection, humanReadAbleDate, requestBusSearch)


    }

    private lateinit var mViewModelFlight: FlightSearchViewModel


    override fun onSetDate(year: Int, month: Int, day: Int) {
        val mMonth = month + 1;

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)

    }

    private fun initViewModel() {

        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)


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

        initViewModel()

        viewMode.callLocalSearch(requestBusSearch)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}
