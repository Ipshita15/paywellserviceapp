package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
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

        val segment = mSegments.get(position)

        if (mRequestAirSearch.journeyType.equals("MultiStop")) {
            val counter = position + 1
            holder.tvTitle.text = mContext.getString(R.string.flight) + counter
        } else {
            if (segment.tripIndicator.equals("OutBound")) {
                holder.tvTitle.text = mContext.getString(R.string.outbound)
            } else {
                holder.tvTitle.text = mContext.getString(R.string.inbound)
            }
        }

        if (mRequestAirSearch.journeyType.equals("Oneway")) {
            displayOneWay(holder, mSegments, position)
        } else if (mRequestAirSearch.journeyType.equals("Return")) {
            displayDataNew(holder, mSegments, position)
        } else if (mRequestAirSearch.journeyType.equals("MultiStop")) {
            displayDataNew(holder, mSegments, position)
        }

    }

    private fun displayOneWay(holder: VHolder, segments: List<OutputSegment>, position: Int) {
        val segment = segments.get(position)
        var secondDate: Date = Date()
        var split1 = mutableListOf<String>()
        var date1 = ""
        var durtingJounaryTimeNew = ""


        val formatDepTime = segment.origin?.depTime?.let { AirTicketHelper.getFormatDepTime(it) }
        holder.tvDepDate.text = formatDepTime

        // show show date
        val split = segments.get(0).origin?.depTime.toString().split("T");
        val date = split.get(0) + " " + split.get(1)
        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

        split1 = segments.get(segments.size - 1).destination?.arrTime.toString().split("T").toMutableList();


        var stopCount = ""
        if (segments.size > 1) {
            stopCount = "" + ((segments.size) - 1)
        } else {
            stopCount = "0"
        }

        date1 = split1.get(0) + " " + split1.get(1)
        secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)
        durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)
        holder.tvDurationAndStopCounter.text = durtingJounaryTimeNew + ", $stopCount stop"


        val differenceDays = DateUtils.getDifferenceDays(fistDate, secondDate)
        var differenceDaysString = ""
        if (differenceDays == 1 || differenceDays == 0) {
            differenceDaysString = ""
        } else {
            differenceDaysString = " (+" + (differenceDays - 1) + ")"
        }

        if (mRequestAirSearch.journeyType.equals("Oneway")) {
            holder.tvOrganAirportCode.text = segments.get(0).origin?.airport?.airportCode.toString()
            holder.tvDestinationAirportCode.text = segments.get(segments.size - 1).destination?.airport?.airportCode.toString()

            holder.tvDepTime.text = segments.get(0).origin?.depTime?.let { AirTicketHelper.getFormatTime(it) }
            holder.tvArrTime.text = segments.get(segments.size - 1).destination?.arrTime?.let { AirTicketHelper.getFormatTime(it) } + "${differenceDaysString}"
        }

    }


    private fun displayDataNew(holder: VHolder, segments: List<OutputSegment>, position: Int) {
        val segment = segments.get(position)
        var secondDate: Date = Date()
        var split1 = mutableListOf<String>()
        var date1 = ""
        var durtingJounaryTimeNew = ""


        val formatDepTime = segment.origin?.depTime?.let { AirTicketHelper.getFormatDepTime(it) }
        holder.tvDepDate.text = formatDepTime


        // show show date
        val split = segments.get(0).origin?.depTime.toString().split("T");
        val date = split.get(0) + " " + split.get(1)
        val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

        if (mRequestAirSearch.journeyType.equals("Oneway")) {
            split1 = segments.get(segments.size - 1).destination?.arrTime.toString().split("T").toMutableList();
        } else {
            split1 = segments.get(position).destination?.arrTime.toString().split("T").toMutableList();
        }

        var stopCount = ""
        if (segments.size > 1) {
            stopCount = "" + ((segments.size) - 1)
        } else {
            stopCount = "0"
        }


        date1 = split1.get(0) + " " + split1.get(1)
        secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)
        durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)
        holder.tvDurationAndStopCounter.text = durtingJounaryTimeNew + ", $stopCount stop"


        val differenceDays = DateUtils.getDifferenceDays(fistDate, secondDate)
        var differenceDaysString = ""
        if (differenceDays == 1 || differenceDays == 0) {
            differenceDaysString = ""
        } else {
            differenceDaysString = " (+" + (differenceDays - 1) + ")"
        }

        if (mRequestAirSearch.journeyType.equals("Oneway")) {
            holder.tvOrganAirportCode.text = segments.get(0).origin?.airport?.airportCode.toString()
            holder.tvDestinationAirportCode.text = segments.get(segments.size - 1).destination?.airport?.airportCode.toString()

            holder.tvDepTime.text = segments.get(0).origin?.depTime?.let { AirTicketHelper.getFormatTime(it) }
            holder.tvArrTime.text = segments.get(segments.size - 1).destination?.arrTime?.let { AirTicketHelper.getFormatTime(it) } + "${differenceDaysString}"

        } else {
            holder.tvOrganAirportCode.text = segment.origin?.airport?.airportCode.toString()
            holder.tvDestinationAirportCode.text = segment.destination?.airport?.airportCode.toString()
            holder.tvDepTime.text = segment.origin?.depTime?.let { AirTicketHelper.getFormatTime(it) }
            holder.tvArrTime.text = segment.origin?.depTime?.let { AirTicketHelper.getFormatTime(it) } + "${differenceDaysString}"
        }


    }

    override fun getItemCount(): Int {

        if (mRequestAirSearch.journeyType.equals("Oneway")) {
            return 1
        }

        return mSegments.size
    }

    class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle = view.tvTitle
        val tvDepDate = view.tvDepDate
        val tvOrganAirportCode = view.tvOrginAirportCode
        val tvDestinationAirportCode = view.tvDestinationAirportCode
        val tvDepTime = view.tvDepTime
        val tvArrTime = view.tvArrTime
        val tvDurationAndStopCounter = view.tvDurationAndStopCounter
    }

}
