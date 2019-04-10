package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketHelper
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.OutputSegment
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.utils.DateUtils
import kotlinx.android.synthetic.main.simple_list_item_segment.view.*
import java.text.SimpleDateFormat
import java.util.*


class FlightRecycleViewAdapter(val mContext: Context, val mSegments: List<OutputSegment>, val mRequestAirSearch: RequestAirSearch) : RecyclerView.Adapter<FlightRecycleViewAdapter.VHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {

        val view = LayoutInflater.from(mContext).inflate(com.cloudwell.paywell.services.R.layout.simple_list_item_segment, parent, false)
        return VHolder(view)
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        displayDataNew(holder, mSegments, position)
    }

    private fun displayDataNew(holder: VHolder, segments: List<OutputSegment>, position: Int) {
        val segment = segments.get(position)


        if (mRequestAirSearch.journeyType.equals("MultiStop")) {
            val counter = position + 1
            holder.tvTitle.text = "Flight " + counter
        } else {
            holder.tvTitle.text = segment.tripIndicator
        }


        // show
        val split = segments.get(position).origin?.depTime.toString().split("T");
        val date = split.get(0) + " " + split.get(1)
        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

        val split1 = segments.get(position).destination?.arrTime.toString().split("T");
        val date1 = split1.get(0) + " " + split1.get(1)
        val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)

        val durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)

        holder.tvDurationAndStopCounter.text = durtingJounaryTimeNew


        val formatDepTime = segment.origin?.depTime?.let { AirTicketHelper.getFormatDepTime(it) }
        holder.tvDepTime.text = formatDepTime

        holder.tvOrganAirportCode.text = segment.origin?.airport?.airportCode.toString()
        holder.tvDestinationAirportCode.text = segment.destination?.airport?.airportCode.toString()
        holder.tvOrginTime.text = segment.origin?.depTime?.let { AirTicketHelper.getFormatTime(it) }


        val differenceDays = DateUtils.getDifferenceDays(fistDate, secondDate)
        var differenceDaysString = ""
        if (differenceDays == 1 || differenceDays == 0) {
            differenceDaysString = ""
        } else {
            differenceDaysString = " (+" + differenceDays + ")"
        }

        holder.tvDestinationTime.text = segment.destination?.arrTime?.let { AirTicketHelper.getFormatTime(it) } + differenceDaysString

    }


    override fun getItemCount(): Int {
        return mSegments.size
    }

    class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tvTitle
        val tvDepTime = view.tvDepTime
        val tvOrganAirportCode = view.tvOrginAirportCode
        val tvDestinationAirportCode = view.tvDestinationAirportCode
        val tvOrginTime = view.tvOrginTime
        val tvDestinationTime = view.tvDestinationTime
        val tvDurationAndStopCounter = view.tvDurationAndStopCounter
    }

}
