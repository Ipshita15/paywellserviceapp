package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import com.cloudwell.paywell.services.utils.DateUtils
import kotlinx.android.synthetic.main.flight_list_item.view.*
import java.text.SimpleDateFormat


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class FlightAdapter(val items: List<Result>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var destinationTime = "";
        var originAirport = ""
        var destinationAirport = ""
        var airlineName = ""
        var stop = "";

        var totalJourneyTimeString = 0L


        val model = items.get(position)
        if (position == 0) {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_low)
            val parseColor = Color.parseColor("#f15a24")
            holder.tvPrices.setTextColor(parseColor)
        } else {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_normal)
        }

        val orign = model.segments.get(0);
        var orignTime = orign.origin?.depTime.toString().split("T").get(1).toString()
        orignTime = orignTime.substring(0, orignTime.length - 3)

        originAirport = model.segments.get(0).origin?.airport?.airportCode!!

        airlineName = model.segments.get(0).airline?.airlineName ?: ""


        if (model.segments.size <= 1) {

            val destination = model.segments.get(0);
            destinationTime = destination.destination?.arrTime.toString().split("T").get(1).toString()
            destinationTime = destinationTime.substring(0, destinationTime.length - 3)

            destinationAirport = model.segments.get(0).destination?.airport?.airportCode ?: ""

            stop = "Nonstop"

        } else {

            val destination = model.segments?.get(model.segments.size - 1);
            destinationTime = destination?.destination?.arrTime.toString().split("T")?.get(1).toString()
            destinationTime = destinationTime.substring(0, destinationTime.length - 3)

            destinationAirport = model.segments.get(model.segments.size - 1).destination?.airport?.airportCode
                    ?: ""

            stop = "${model.segments.size - 1} stop in " + (destination.origin?.airport?.airportCode
                    ?: "")


        }

        holder.tvStringTime.text = orignTime
        holder.tvEndTime.text = destinationTime
        holder.tvPrices.text = "${model.totalFare}"
        holder.tvOriginAirportCode.text = originAirport
        holder.tvDestinationAirportCode.text = destinationAirport
        holder.tvAirlinesName.text = airlineName


        model.segments.forEach {

            val split = it.origin?.depTime.toString().split("T");
            val date = split.get(0) + " " + split.get(1)
            val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date)

            val split1 = it.destination?.arrTime.toString().split("T");
            val date1 = split1.get(0) + " " + split1.get(1)
            val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date1)


            val differenceMilliSecond = DateUtils.differenceMilliSecond(fistDate, secondDate)
            totalJourneyTimeString = totalJourneyTimeString + differenceMilliSecond

        }

        val durtingJounaryTime = DateUtils.getDurtingJounaryTime(totalJourneyTimeString)


        holder.tvDurationAndKilometer.text = "Duration: ${durtingJounaryTime} | $stop"


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.flight_list_item, parent, false)
        return ViewHolder(view)
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

}


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvStringTime = view.tvStringTime
    val tvEndTime = view.tvEndingTime
    val tvPrices = view.tvPrices
    val ivTake = view.ivTake
    val tvOriginAirportCode = view.tvSrocesAddress
    val tvDestinationAirportCode = view.tvDestinationAddress
    val tvDurationAndKilometer = view.tvDurationAndKilometer
    val tvAirlinesName = view.tvAirlinesName
    val btDetails = view.btDetails


}
