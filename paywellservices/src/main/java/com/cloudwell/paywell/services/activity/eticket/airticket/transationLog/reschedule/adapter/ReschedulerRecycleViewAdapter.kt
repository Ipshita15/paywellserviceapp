package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.reschedule.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Trip
import kotlinx.android.synthetic.main.item_re_scheduler.view.*


class ReschedulerRecycleViewAdapter(val mContext: Context, val mSegments: Datum) : RecyclerView.Adapter<ReschedulerRecycleViewAdapter.VHolder>() {

    var groupBy: Map<String?, List<Trip>>? = null

    init {
        if (mSegments.journeyType.equals("Return")) {
            groupBy = mSegments.trips.groupBy {
                it.tripIndicator
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReschedulerRecycleViewAdapter.VHolder {

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_re_scheduler, parent, false)
        return VHolder(view)
    }

    override fun onBindViewHolder(holder: VHolder, position: Int) {
        if (mSegments.journeyType.equals("Oneway")) {
            displayOneWay(holder, mSegments.trips, position)
        } else if (mSegments.journeyType.equals("Return")) {
            displayRound(holder, groupBy, position)
        } else if (mSegments.journeyType.equals("MultiStop")) {
            displayDataNew(holder, mSegments.trips, position)
        }

    }

    private fun displayRound(holder: VHolder, groupingBy: Map<String?, List<Trip>>?, position: Int) {
        val segments: List<Trip>?
        if (position == 0) {
            segments = groupingBy?.get("OutBound")

            // holder.TvDestinationAndArrivedTime.text = segments?.get(0)?.originAirportCode + "-" + segments?.get(0)?.destinationAirportCode


        } else {
            segments = groupingBy?.get("InBound")
            //  holder.TvDestinationAndArrivedTime.text = segments?.get(0)?.originAirportCode + "-" + segments?.get(0)?.destinationAirportCode

        }

        val firstSegment = segments?.first()
        val lastSegment = segments?.last()

        val split1: MutableList<String>
        var durtingJounaryTimeNew = ""


        val formatDepTime = firstSegment?.departureTime
        holder.tvDepDate.text = formatDepTime


        holder.TvDestinationAndArrivedTime.text = firstSegment?.originAirportCode + "-" + lastSegment?.destinationAirportCode


    }

    private fun displayOneWay(holder: VHolder, segments: List<Trip>, position: Int) {

        // holder.tvTitle.text = mContext.getString(R.string.outbound)

        val segment = segments.get(position)
        var arrTimeSplit1 = mutableListOf<String>()


        val formatDepTime = segment.departureTime
        holder.tvDepDate.text = formatDepTime

        holder.TvDestinationAndArrivedTime.text = segment.originAirportCode + "-" + segment.destinationAirportCode


    }


    private fun displayDataNew(holder: VHolder, segments: List<Trip>, position: Int) {
        val segment = segments.get(position)
        var split1 = mutableListOf<String>()
        var durtingJounaryTimeNew = ""


        val formatDepTime = segment.departureTime
        holder.tvDepDate.text = formatDepTime


        holder.TvDestinationAndArrivedTime.text = segment.originAirportCode + "-" + segment.destinationAirportCode


    }

    override fun getItemCount(): Int {

        if (mSegments.journeyType.equals("Oneway")) {
            return 1
        } else if (mSegments.journeyType.equals("Return")) {

            return 2
        }
        return mSegments.trips.size
    }

    class VHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TvDestinationAndArrivedTime = view.TvDestinationAndArrivedTime
        val tvDepDate = view.tvDepartDate

    }

}
