package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.viewModel.FlightSearchViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.adapter.FlightAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.view.SeachViewStatus
import com.cloudwell.paywell.services.customView.horizontalDatePicker.MyDatePickerTimeline
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import kotlinx.android.synthetic.main.activity_search_view.*


class FlightSearchViewActivity : AirTricketBaseActivity(), IDatePicker {
    internal lateinit var myDatePickerTimeline: MyDatePickerTimeline
    private lateinit var mViewModelFlight: FlightSearchViewModel


    override fun onSetDate(year: Int, month: Int, day: Int) {
        val mMonth = month + 1;

        val date = "$year-$mMonth-$day"

        mViewModelFlight.onSetDate(isInternetConnection, date)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_search_view)
        setToolbar(getString(R.string.title_serach_view))

        myDatePickerTimeline = findViewById<View>(com.cloudwell.paywell.services.R.id.myDateTimelineView) as MyDatePickerTimeline
        myDatePickerTimeline.setFirstDate(2019, 0, 5)
        myDatePickerTimeline.setOnDateChangeLincher(this)

        initializationView();
        initViewModel()

    }

    private fun initializationView() {


    }


    private fun initViewModel() {
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

        mViewModelFlight.init(isInternetConnection)


    }

    private fun setAdapter(it: List<Result>?) {
        shimmer_recycler_view.showShimmerAdapter()
        shimmer_recycler_view.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        shimmer_recycler_view.adapter = it?.let { it1 -> FlightAdapter(it1, applicationContext) }
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
                progressBar2.visibility = View.INVISIBLE
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
