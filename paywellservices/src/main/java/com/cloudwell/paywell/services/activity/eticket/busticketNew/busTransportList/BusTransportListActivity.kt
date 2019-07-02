package com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BusTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.BusTripListAdapter
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.adapter.OnClickListener
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.view.IbusTransportListView
import com.cloudwell.paywell.services.activity.eticket.busticketNew.busTransportList.viewModel.BusTransportViewModel
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.RequestBusSearch
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResSeatInfo
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TripScheduleInfoAndBusSchedule
import com.cloudwell.paywell.services.activity.eticket.busticketNew.seatLayout.SeatLayoutActivity
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bus_transport_list.*
import java.text.SimpleDateFormat
import java.util.*


class BusTransportListActivity : BusTricketBaseActivity(), IDatePicker, IbusTransportListView {
    lateinit var viewMode: BusTransportViewModel
    lateinit var requestBusSearch: RequestBusSearch
    var isReSchuduler = false

    var items = mutableListOf<TripScheduleInfoAndBusSchedule>()

    var busTripListAdapter: BusTripListAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_transport_list)
        setToolbar(getString(R.string.title_bus_transtport_list), resources.getColor(R.color.bus_ticket_toolbar_title_text_color))

        requestBusSearch = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestBusSearch

//        requestBusSearch = RequestBusSearch()
//        requestBusSearch.to = "Dhaka"
//        requestBusSearch.from = "Kolkata"
//        requestBusSearch.date = "2019-07-01"

        val split = requestBusSearch.date.split("-")
        val month = split[1].toInt() - 1
        myDateTimelineViewBus.setFirstDate(split[0].toInt(), month, split[2].toInt())
        myDateTimelineViewBus.setOnDateChangeLincher(this)

        initViewModel()
        viewMode.callLocalSearch(requestBusSearch)


        btSerachAgain.setOnClickListener {
            finish()
        }


    }

    override fun showNoTripFoundUI() {
        shimmer_recycler_view.visibility = View.INVISIBLE
        layoutNoSerachFound.visibility = View.VISIBLE
    }

    override fun setAdapter(it: List<TripScheduleInfoAndBusSchedule>) {
        layoutNoSerachFound.visibility = View.INVISIBLE
        viewMode.cancelAllRequest()

        Fresco.initialize(applicationContext);

        items = it.toMutableList()

        shimmer_recycler_view.visibility = View.VISIBLE
        shimmer_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = it.let { it1 ->


            val transportID = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.TRANSPORT_ID) as String

            busTripListAdapter = BusTripListAdapter(it1, applicationContext, requestBusSearch, transportID, object : OnClickListener {
                override fun onUpdateData(position: Int, resSeatInfo: ResSeatInfo) {

                    val get = items.get(position)
                    get.resSeatInfo = resSeatInfo
                    items.set(position, get)
                    busTripListAdapter?.notifyItemRangeChanged(position, items.size);
                    busTripListAdapter?.notifyDataSetChanged()

                }


                override fun onClick(position: Int) {
                    // do whatever

                    val model = items.get(position)

                    if (model.resSeatInfo == null) {
                        Toast.makeText(applicationContext, "PLease wailt..", Toast.LENGTH_LONG).show()
                    } else if (model.resSeatInfo?.tototalAvailableSeat == 0) {
                        showDialogMesssage("seat not available")
                    } else {

                        val toJson = Gson().toJson(model)
                        val intent = Intent(applicationContext, SeatLayoutActivity::class.java)
                        intent.putExtra("jsonData", toJson)
                        startActivity(intent)
                    }
                    // AppStorageBox.put(applicationContext, AppStorageBox.Key.SERACH_ID, mSearchId)


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
        myDateTimelineViewBus.setNewDate(split[0].toInt(), month, split[2].toInt())
        myDateTimelineViewBus.setOnDateChangeLincher(this)

        requestBusSearch.date = humanReadAbleDate

        viewMode.callLocalSearch(requestBusSearch)


    }


    override fun onSetDate(year: Int, month: Int, day: Int) {
        val mMonth = month + 1;
        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)
        myDateTimelineViewBus.setOnDateChangeLincher(this)


        requestBusSearch.date = humanReadAbleDate

        viewMode.callLocalSearch(requestBusSearch)

    }

    private fun initViewModel() {

        viewMode = ViewModelProviders.of(this).get(BusTransportViewModel::class.java)
        viewMode.setIbusTransportListView(this)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


}
