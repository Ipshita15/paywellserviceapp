package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Activity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter.FlightAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.viewModel.FlightSearchViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.view.SeachViewStatus
import com.cloudwell.paywell.services.app.AppController
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import com.cloudwell.paywell.services.utils.RecyclerItemClickListener
import com.facebook.drawee.backends.pipeline.Fresco
import kotlinx.android.synthetic.main.activity_search_view.*
import java.text.SimpleDateFormat
import java.util.*


class FlightSearchViewActivity : AirTricketBaseActivity(), IDatePicker {


    override fun onSetNewDate(year: Int, month: Int, day: Int) {

        val mMonth = month + 1;

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


        val split = humanReadAbleDate.split("-")
        val month = split[1].toInt() - 1
        myDateTimelineView.setNewDate(split[0].toInt(), month, split[2].toInt())
        myDateTimelineView.setOnDateChangeLincher(this)

    }

    //    internal lateinit var myDatePickerTimeline: MyDatePickerTimeline
    private lateinit var mViewModelFlight: FlightSearchViewModel


    override fun onSetDate(year: Int, month: Int, day: Int) {
        val mMonth = month + 1;

        val date = "$year-$mMonth-$day"
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(date) as Date
        val humanReadAbleDate = SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).format(fdepTimeFormatDate)


        mViewModelFlight.onSetDate(isInternetConnection, humanReadAbleDate)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_search_view)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_serach_view))

        val requestAirSearch = AppStorageBox.get(AppController.getContext(), AppStorageBox.Key.REQUEST_AIR_SERACH) as RequestAirSearch

        if (requestAirSearch.journeyType.equals("Oneway")) {
            val split = requestAirSearch.segments.get(0).departureDateTime.split("-")
            val month = split[1].toInt() - 1
            myDateTimelineView.setFirstDate(split[0].toInt(), month, split[2].toInt())
            myDateTimelineView.setOnDateChangeLincher(this)
        } else {
            myDateTimelineView.visibility = View.GONE
        }

//        myDatePickerTimeline = findViewById<View>(com.cloudwell.paywell.services.R.id.myDateTimelineView) as MyDatePickerTimeline


        initializationView();
        initViewModel(requestAirSearch)

    }

    private fun initializationView() {
        btSerachAgain.setOnClickListener {
            finish()
        }

    }


    private fun initViewModel(requestAirSearch: RequestAirSearch) {
        mViewModelFlight = ViewModelProviders.of(this).get(FlightSearchViewModel::class.java)

        mViewModelFlight.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        mViewModelFlight.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })


        mViewModelFlight.mListMutableLiveDataFlightData.observe(this, Observer {
            setAdapter(it)

        })

        mViewModelFlight.init(isInternetConnection, requestAirSearch)


    }

    private fun setAdapter(it: List<Result>?) {
        Fresco.initialize(applicationContext);

        shimmer_recycler_view.showShimmerAdapter()
        shimmer_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = it?.let { it1 ->
            FlightAdapter(it1, applicationContext)

        }

        shimmer_recycler_view.addOnItemTouchListener(
                RecyclerItemClickListener(applicationContext, shimmer_recycler_view, object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        // do whatever
                        val get = mViewModelFlight.mListMutableLiveDataFlightData.value?.get(position)
                        val mSearchId = mViewModelFlight.mSearchId.value
                        val resultID = get?.resultID


                        val intent = Intent(applicationContext, FlightDetails1Activity::class.java)
                        intent.putExtra("mSearchId", mSearchId)
                        intent.putExtra("resultID", resultID)
                        startActivity(intent)

                    }

                    override fun onLongItemClick(view: View, position: Int) {
                        // do whatever
                    }
                })
        )

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
}
