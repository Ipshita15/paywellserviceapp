package com.cloudwell.paywell.services.activity.eticket.airticket.serach

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.adapter.FlightAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.view.SeachViewStatus
import com.cloudwell.paywell.services.customView.horizontalDatePicker.MyDatePickerTimeline
import com.cloudwell.paywell.services.customView.horizontalDatePicker.commincation.IDatePicker
import com.cooltechworks.views.shimmer.ShimmerRecyclerView


class SearchViewActivity : AirTricketBaseActivity(), IDatePicker {
    internal lateinit var myDatePickerTimeline: MyDatePickerTimeline
    private lateinit var viewModel: SearchNotificationViewModel


    override fun onSetDate(year: Int, month: Int, day: Int) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_search_view)

        myDatePickerTimeline = findViewById<View>(com.cloudwell.paywell.services.R.id.myDateTimelineView) as MyDatePickerTimeline
        myDatePickerTimeline.setFirstDate(2019, 0, 5)
        myDatePickerTimeline.setOnDateChangeLincher(this)


        initViewModel()

    }


    private fun initViewModel() {

        viewModel = ViewModelProviders.of(this).get(SearchNotificationViewModel::class.java)

        viewModel.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        viewModel.mViewStatus.observe(this, Observer {
            handleViewStatus(it)
        })


        viewModel.mListMutableLiveDataFlightData.observe(this, Observer {
            setAdapter(it)

        })


        viewModel.init(isInternetConnection)


    }

    private fun setAdapter(it: List<Result>?) {
        val shimmerRecycler = findViewById<View>(com.cloudwell.paywell.services.R.id.shimmer_recycler_view) as ShimmerRecyclerView
        shimmerRecycler.layoutManager = LinearLayoutManager(this)
        shimmerRecycler.adapter = it?.let { it1 -> FlightAdapter(it1, applicationContext) }
        shimmerRecycler.showShimmerAdapter()

    }

    private fun handleViewStatus(status: SeachViewStatus?) {
        when (status) {

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
