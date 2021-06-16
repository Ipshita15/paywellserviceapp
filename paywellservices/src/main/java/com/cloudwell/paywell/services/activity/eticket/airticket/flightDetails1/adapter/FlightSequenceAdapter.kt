package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter

import com.cloudwell.paywell.services.libaray.SequenceAdapter
import com.cloudwell.paywell.services.libaray.SequenceStep
import kotlinx.android.synthetic.main.sequence_step.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 3/3/19.
 */
class FlightSequenceAdapter(private val items: List<MyItem>, var onFlightClick: OnFlightClick) : SequenceAdapter<FlightSequenceAdapter.MyItem>() {


    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): MyItem {
        return items[position]
    }

    override fun bindView(sequenceStep: SequenceStep, item: MyItem) {
        with(sequenceStep) {
            setActive(item.isActive)
            setDate(item.date)
            setTime(item.time)
            setTitle(item.airportName)


            if (!item.airlineName.equals("")) {
                setSubtitle(item.airlineName)
            } else {
                setSubtitle("Stop " + item.cityName)
            }

            if (item.totalTime.equals("")) {
                hiddenFlightBackgroud(false)
                hiddenFlightInfoIncon(false)
            } else {

                // first row....
                hiddenFlightBackgroud(true)
                hiddenFlightInfoIncon(true)
                setTotalJounaryTime(item.totalTime)
            }

            sequenceStep.vFlightBackgroud.setOnClickListener {
                onFlightClick.onclick(item.airlineName)
            }
        }


    }

    interface OnFlightClick {
        fun onclick(airlineName: String?)
    }


    data class MyItem(val isActive: Boolean,
                      val airportName: String,
                      val date: String,
                      val time: String,
                      var airlineName: String?,
                      val cityName: String = "",
                      val totalTime: String)
}
