package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Result
import com.cloudwell.paywell.services.utils.DateUtils
import kotlinx.android.synthetic.main.flight_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class FlightAdapter(val items: List<Result>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var destinationTime = ""
        var originAirport = ""
        var destinationAirport = ""
        var airlineName = ""
        var airlineCode = ""
        var stop = ""

        var totalJourneyTimeString = 0L


        val model = items.get(position)
        if (position == 0) {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_low)
            val parseColor = Color.parseColor("#f15a24")
            holder.tvPrices.setTextColor(parseColor)
        } else {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_normal)
            val parseColor = Color.parseColor("#666666")
            holder.tvPrices.setTextColor(parseColor)
        }

        val orign = model.segments.get(0);
        var orignTime = orign.origin?.depTime.toString().split("T").get(1).toString()
        orignTime = orignTime.substring(0, orignTime.length - 3)

        originAirport = model.segments.get(0).origin?.airport?.airportCode!!

        airlineName = model.segments.get(0).airline?.airlineName ?: ""
        airlineCode = model.segments.get(0).airline?.airlineCode ?: ""


        if (model.segments.size <= 1) {

            val destination = model.segments.get(0);
            destinationTime = destination.destination?.arrTime.toString().split("T").get(1).toString()
            destinationTime = destinationTime.substring(0, destinationTime.length - 3)

            destinationAirport = model.segments.get(0).destination?.airport?.airportCode ?: ""

            stop = "Nonstop"


            val split = model.segments.get(0).origin?.depTime.toString().split("T");
            val date = split.get(0) + " " + split.get(1)
            val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

            val split1 = model.segments.get(0).destination?.arrTime.toString().split("T");
            val date1 = split1.get(0) + " " + split1.get(1)
            val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)

            val durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)

            holder.tvDurationAndKilometer.text = context.getString(R.string.duration_tiime) + durtingJounaryTimeNew + " | " + stop

        } else {

            val destination = model.segments?.get(model.segments.size - 1);
            destinationTime = destination?.destination?.arrTime.toString().split("T")?.get(1).toString()
            destinationTime = destinationTime.substring(0, destinationTime.length - 3)

            destinationAirport = model.segments.get(model.segments.size - 1).destination?.airport?.airportCode
                    ?: ""

            stop = "${model.segments.size - 1} stop in " + (destination.origin?.airport?.airportCode
                    ?: "")


            val split = model.segments.get(0).origin?.depTime.toString().split("T");
            val date = split.get(0) + " " + split.get(1)
            val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date)

            val split1 = model.segments.get(model.segments.size - 1).destination?.arrTime.toString().split("T");
            val date1 = split1.get(0) + " " + split1.get(1)
            val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.ENGLISH).parse(date1)

            val durtingJounaryTimeNew = DateUtils.getDurtingJounaryTimeNew(fistDate, secondDate)

            holder.tvDurationAndKilometer.text = context.getString(R.string.duration_tiime) + durtingJounaryTimeNew + " | " + stop


        }

        holder.tvStringTime.text = orignTime
        holder.tvEndTime.text = destinationTime
        holder.tvPrices.text = "${model.totalFare}"
        holder.tvOriginAirportCode.text = originAirport
        holder.tvDestinationAirportCode.text = destinationAirport
        holder.tvAirlinesName.text = airlineName


//        model.segments.forEach {
//
//            val split = it.origin?.depTime.toString().split("T");
//            val date = split.get(0) + " " + split.get(1)
//            val fistDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date)
//
//            val split1 = it.destination?.arrTime.toString().split("T");
//            val date1 = split1.get(0) + " " + split1.get(1)
//            val secondDate = SimpleDateFormat("yyyy-mm-dd HH:mm:ss").parse(date1)
//
//
//            val differenceMilliSecond = DateUtils.differenceMilliSecond(fistDate, secondDate)
//            totalJourneyTimeString = totalJourneyTimeString + differenceMilliSecond
//
//        }
//
//        val durtingJounaryTime = DateUtils.getDurtingJounaryTime(totalJourneyTimeString)


        val imageUri = Uri.parse("https://notify.paywellonline.com/airlines/images/${airlineCode}_350_100_r.png")

        holder.airlineSerachIcon.setImageURI(imageUri)

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
    val tvStringTime = view.tvStringTime
    val tvEndTime = view.tvEndingTime
    val tvPrices = view.tvPrices
    val ivTake = view.ivTake
    val tvOriginAirportCode = view.tvSrocesAddress
    val tvDestinationAirportCode = view.tvDestinationAddress
    val tvDurationAndKilometer = view.tvDurationAndKilometer
    val tvAirlinesName = view.tvAirlinesName
    val btDetails = view.btDetails
    val airlineSerachIcon = view.airlineSerachIcon


}
