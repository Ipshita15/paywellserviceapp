package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.View
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.DummayData
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter.FlightSequenceAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Result
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Segment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.viewModel.FlightDetails1ViewModel
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.RequestAirSearch
import com.cloudwell.paywell.services.utils.DateUtils.differenceDate
import com.google.gson.Gson
import kotlinx.android.synthetic.main.contant_flight_details.*
import java.text.SimpleDateFormat
import java.util.*


class FlightDetails1Activity : AirTricketBaseActivity() {


    var totalJourneyTimeString = ""

    private lateinit var mFlightDetails1ViewModel: FlightDetails1ViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_flight_details)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_booking_and_review))

        initializationView()
        // initViewModel()
//
//        val parcelable = getIntent().getExtras().getParcelable<Result>("object") as Result
        val results = Gson().fromJson(DummayData().multipSegmentData, Array<com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Result>::class.java)


        displayData(results.toList())

    }

    private fun initViewModel() {
        mFlightDetails1ViewModel = ViewModelProviders.of(this).get(FlightDetails1ViewModel::class.java)

        mFlightDetails1ViewModel.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewCommonStatus(it)
        })

        mFlightDetails1ViewModel.mViewStatus.observe(this, android.arch.lifecycle.Observer {
            handleViewStatus(it)
        })


        mFlightDetails1ViewModel.mListMutableLiveDataResults.observe(this, android.arch.lifecycle.Observer {


            it?.let { it1 -> displayData(it1) }

        })


        val requestAirPriceSearch = RequestAirPriceSearch()
        requestAirPriceSearch.searchId = "effd3517-ee7c-4363-87ad-59333cd5f365"
        requestAirPriceSearch.resultID = "3e55bbb0-2897-49ad-bbf2-f04b465d0b05"



        mFlightDetails1ViewModel.callAirPriceSearch(requestAirSearch = requestAirPriceSearch)
    }

    private fun handleViewStatus(status: FlightDetails1Status?) {
        status.let {

            if (it?.isShowProcessIndicator == true) {
                showProgressDialog()
            } else {
                dismissProgressDialog()
            }

            if (!it?.noSerachFoundMessage.equals("")) {
//                shimmer_recycler_view.visibility = View.INVISIBLE
//                layoutNoSerachFound.visibility = View.VISIBLE
                showSnackMessageWithTextMessage(it?.noSerachFoundMessage)
                // finish()
            } else {
//                shimmer_recycler_view.visibility = View.VISIBLE
//                layoutNoSerachFound.visibility = View.INVISIBLE
                // finish()
            }

        }

    }

    private fun displayData(it: List<Result>) {
        val requestAirSearch = RequestAirSearch(journeyType = "local")


        val segments = it?.get(0)?.segments
        val segment = it.get(0)?.segments?.get(0)
        val result = it.get(0);


        //generator of segemnt view

        segments?.let { it1 -> setupTimelineExpanableView(it1) }

        if (segments?.size == 1) {
            val orignAirtportCode = segments.get(0).origin?.airport?.airportCode
            val destinationairportCode = segments.get(0).destination?.airport?.airportCode
            tvOrginAndDestinationAirportCode.text = orignAirtportCode + " - " + destinationairportCode
            displayHumanDate(segments)

            val orign = segments.get(0);
            val orignTime = orign.origin?.depTime.toString().split("T").get(1).toString()

            val destination = segments.get(0);
            val destinationTime = destination.destination?.arrTime.toString().split("T").get(1).toString()

            tvShortDepartArriveTime.text = orignTime + "-" + destinationTime


        } else {

            val orignAirtportCode = segments?.get(0)?.origin?.airport?.airportCode
            val destinationairportCode = segments?.get(segments.size - 1)?.destination?.airport?.airportCode
            tvOrginAndDestinationAirportCode.text = orignAirtportCode + " - " + destinationairportCode
            segments?.let { it1 -> displayHumanDate(it1) }


            val orign = segments?.get(0);
            var orignTime = orign?.origin?.depTime.toString().split("T")?.get(1).toString()
            orignTime = orignTime.substring(0, orignTime.length - 3)

            val destination = segments?.get(segments.size - 1);
            var destinationTime = destination?.destination?.arrTime.toString().split("T")?.get(1).toString()
            destinationTime = destinationTime.substring(0, destinationTime.length - 3)


            tvShortDepartArriveTime.text = orignTime + "-" + destinationTime

        }

        tvTotalFair.text = result.fares?.get(0)?.baseFare.toString()
        tvClass.text = requestAirSearch.journeyType

        if (result.isRefundable) {
            tvNonRefundable.text = getString(R.string.refundable)
        } else {
            tvNonRefundable.text = getString(R.string.non_refundable)

        }

        tvBaggage.text = "Baggage : " + segment?.baggage + " KG per adult ticket "

    }

    private fun setupTimelineExpanableView(segments: List<Segment>) {
        val segmentsList = mutableListOf<FlightSequenceAdapter.MyItem>()

        segments.forEach {
            // parse depDate
            val origin = it.origin
            val depDate = origin?.depTime?.split("T")?.get(0).toString()
            var depTime = origin?.depTime?.split("T")?.get(1).toString()
            depTime = depTime.substring(0, depTime.length - 3)
            val airlineName = it.airline?.airlineName
            segmentsList.add(FlightSequenceAdapter.MyItem(true, origin?.airport?.airportName!!, depDate, depTime, airlineName, "", ""))


            // parse journey time
            val split = origin?.depTime.toString().split("T");
            val date = split.get(0) + " " + split.get(1)
            val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date)

            val split1 = it.destination?.arrTime.toString().split("T");
            val date1 = split1.get(0) + " " + split1.get(1)
            val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date1)
            val differenceDate = differenceDate(fistDate, secondDate)
            segmentsList.add(FlightSequenceAdapter.MyItem(true, "", "", "", "", "", differenceDate))

            storeTotalJourneyTime(differenceDate)

            // parse destination
            val destination = it.destination
            val arrDate = destination?.arrTime?.split("T")?.get(0).toString()
            var arrTime = destination?.arrTime?.split("T")?.get(1).toString()
            arrTime = arrTime.substring(0, arrTime.length - 3)
            val cityName = it.destination?.airport?.cityName
            segmentsList.add(FlightSequenceAdapter.MyItem(true, destination?.airport?.airportName!!, arrDate, arrTime, "", cityName!!, ""))

        }

        sequenceLayout.setAdapter(FlightSequenceAdapter(segmentsList))
    }

    private fun storeTotalJourneyTime(differenceDate: String) {
        if (totalJourneyTimeString.equals("")) {
            totalJourneyTimeString = differenceDate;
        } else {
            totalJourneyTimeString = totalJourneyTimeString + "|" + differenceDate
        }
    }

    private fun initializationView() {
        ivUpDown.switchState()
        tvOrginAndDestinationAirportCode.visibility = View.GONE
        tvShortDepartArriveTime.visibility = View.GONE
        lineView.visibility = View.GONE


        ivUpDown.setOnClickListener {
            handleUpDownClick()
        }

        viewDeartSectionWithoutColor.setOnClickListener {
            handleUpDownClick()
        }

    }

    private fun handleUpDownClick() {
        val expanded = expandable_layout_2.isExpanded
        if (expanded) {

            expandable_layout_2.collapse()
            rotatedUpUp()

        } else {

            expandable_layout_2.expand()
            rotatedUpUp()

        }
    }

    private fun rotatedUpUp() {
        ivUpDown.switchState()

        if (tvOrginAndDestinationAirportCode.visibility == View.VISIBLE) {
            tvOrginAndDestinationAirportCode.visibility = View.GONE
            tvShortDepartArriveTime.visibility = View.GONE
            lineView.visibility = View.GONE
        } else {
            tvOrginAndDestinationAirportCode.visibility = View.VISIBLE
            tvShortDepartArriveTime.visibility = View.VISIBLE
            lineView.visibility = View.VISIBLE
        }


    }


    private fun displayHumanDate(outputSegment: List<Segment>) {

        val outputSegment = outputSegment.get(0);
        val depTime = outputSegment.origin?.depTime?.split("T")
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd").parse(depTime?.get(0)) as Date

        val humanReadAbleDate = SimpleDateFormat("EE, MMM DD").format(fdepTimeFormatDate)
        tvNameOfDate.text = humanReadAbleDate


//        // show different time ..
//        val split = outputSegment.origin?.depTime.toString().split("T");
//        val date = split.get(0) + " " + split.get(1)
//        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date)
//
//        val split1 = outputSegment.destination?.arrTime.toString().split("T");
//        val date1 = split1.get(0) + " " + split1.get(1)
//        val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date1)
//
//        val differenceDate = differenceDate(fistDate, secondDate)
        tvTotalDepartTime.text = totalJourneyTimeString
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
