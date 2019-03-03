package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1

import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.DummayData
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter.FlightSequenceAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import com.cloudwell.paywell.services.utils.DateUtils.differenceDate
import com.google.gson.Gson
import kotlinx.android.synthetic.main.contant_flight_details.*
import java.text.SimpleDateFormat
import java.util.*


class FlightDetailsActivity : AirTricketBaseActivity() {

    var rotationAngle = 180;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_flight_details)

        setToolbar(getString(com.cloudwell.paywell.services.R.string.title_booking_and_review))
//
//        val parcelable = getIntent().getExtras().getParcelable<Result>("object") as Result

        val result = Gson().fromJson(DummayData().multipSegmentData, Array<Result>::class.java)
        val get = result.get(0)



        Log.v("", "");


//        ivUpDown.setOnClickListener {
//            handleUpDownClick()
//        }

        viewDeartSectionWithoutColor.setOnClickListener {
            handleUpDownClick()
        }


        diplayHumanDate(get)

        //generator of segemnt view

        val segmentsList = mutableListOf<FlightSequenceAdapter.MyItem>()

        get.segments.forEach {
            val origin = it.origin
            val split = origin?.depTime?.split("T")

            val airlineName = it.airline?.airlineName


            segmentsList.add(FlightSequenceAdapter.MyItem(true, origin?.airport?.airportName!!, split!![0], split[1], airlineName, "", ""))


            segmentsList.add(FlightSequenceAdapter.MyItem(true, "", "", "", "", "", "1 hours 30"))


            val destination = it.destination
            val split1 = destination?.arrTime?.split("T")
            val cityName = it.destination?.airport?.cityName

            segmentsList.add(FlightSequenceAdapter.MyItem(true, destination?.airport?.airportName!!, split1!![0], split[1], "", cityName!!, ""))

        }

        sequenceLayout.setAdapter(FlightSequenceAdapter(segmentsList))

//        ivUpDown2.setOnClickListener {
//            val expanded = expandable_layout1.isExpanded
//            if (expanded) {
//                expandable_layout1.collapse()
//            } else {
//                expandable_layout1.expand()
//            }
//        }

    }

    private fun handleUpDownClick() {
        val expanded = expandable_layout_2.isExpanded
        if (expanded) {

            expandable_layout_2.collapse()
            rotatedUpUp()

        } else {

            expandable_layout_2.expand()
            rotatedDown()

        }
    }

    private fun rotatedDown() {
//        val animRotateAclk = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_down)
//        ivUpDown.startAnimation(animRotateAclk)
        ivUpDown.clearAnimation()
        ivUpDown.setRotation(180f)
    }

    private fun rotatedUpUp() {
//        val animRotateAclk = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_up)
//        ivUpDown.startAnimation(animRotateAclk)
        ivUpDown.clearAnimation()
        ivUpDown.setRotation(180f)
    }

    private fun diplayHumanDate(get: Result) {
        val depTime = get.segments.get(0).origin?.depTime?.split("T")
        val fdepTimeFormatDate = SimpleDateFormat("yyyy-mm-dd").parse(depTime?.get(0)) as Date

        val humanReadAbleDate = SimpleDateFormat("EE, MMM DD").format(fdepTimeFormatDate)
        tvNameOfDate.text = humanReadAbleDate


        val split = get.segments.get(0).origin?.depTime.toString().split("T");
        val date = split.get(0) + " " + split.get(1)
        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date)


        val split1 = get.segments.get(0).destination?.arrTime.toString().split("T");
        val date1 = split1.get(0) + " " + split1.get(1)
        val secoundDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date1)


        val differenceDate = differenceDate(fistDate, secoundDate)
        tvTotalDepartTime.text = differenceDate
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(com.cloudwell.paywell.services.R.menu.airticket_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
