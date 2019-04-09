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


class FlightRecycleViewAdapter(val context: Context, val segments: List<OutputSegment>, val requestAirSearch: RequestAirSearch) : RecyclerView.Adapter<FlightRecycleViewAdapter.VHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder {

        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.simple_list_item_segment, parent, false)
        return VHolder(view)
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {

        if (requestAirSearch.journeyType.equals("Oneway")) {

            displayOneWayData(holder, position)

        } else if (requestAirSearch.journeyType.equals("Return")) {
            if (position % 2 == 0) {
                val model = segments.get(0)
                holder.tvTitle.text = model.tripIndicator
            } else {
                holder.tvTitle.text = "InBound"
            }

        } else if (requestAirSearch.journeyType.equals("MultiStop")) {

            val flightNumber = position + 1

            holder.tvTitle.text = "Flight " + flightNumber

        }
    }

    private fun displayOneWayData(holder: VHolder, position: Int) {
        holder.tvTitle.text = "OutBound"
        val size = segments.size
//        if (size > 1) {
//            // multi-stop
//
//
//            holder.tvDepTime.text = ""
//
//            holder.tvOrganAirportCode.text = ""
//            holder.tvDestinationAirportCode.text = ""
//            holder.tvOrginTime.text = ""
//            holder.tvDestinationTime.text = ""
//
//        } else {
        val segment = segments.get(0)

        val formatDepTime = segment.origin?.depTime?.let { AirTicketHelper.getFormatDepTime(it) }
        holder.tvDepTime.text = formatDepTime

        holder.tvOrganAirportCode.text = segment.origin?.airport?.airportCode.toString()
        holder.tvDestinationAirportCode.text = segment.destination?.airport?.airportCode.toString()
        holder.tvOrginTime.text = segment.origin?.depTime?.let { AirTicketHelper.getFormatTime(it) }
        holder.tvDestinationTime.text = segment.destination?.arrTime?.let { AirTicketHelper.getFormatTime(it) }


        // show
        val split = segments.get(0).origin?.depTime.toString().split("T");
        val date = split.get(0) + " " + split.get(1)
        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

        val split1 = segments.get(segments.size - 1).destination?.arrTime.toString().split("T");
        val date1 = split1.get(0) + " " + split1.get(1)
        val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)

        val durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)

        var stopCount = ""
        if (segments.size > 1) {
            stopCount = "" + ((segments.size) - 1)
        } else {
            stopCount = "0"

        }
        holder.tvDurationAndStopCounter.text = durtingJounaryTimeNew + " | " + stopCount + " stop"


//        }
    }


    override fun getItemCount(): Int {

        if (requestAirSearch.journeyType.equals("Oneway")) {
            return 1
        }

        return segments.size
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
