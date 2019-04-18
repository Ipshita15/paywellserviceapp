package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.service.CalculationHelper
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.RequestAirSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.model.Result
import kotlinx.android.synthetic.main.flight_list_item_new.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class FlightAdapterNew(val items: List<Result>, val requestAirSearch: RequestAirSearch, val context: Context, val onClickListener: OnClickListener) : RecyclerView.Adapter<ViewHolderNew>() {


    override fun onBindViewHolder(holder: ViewHolderNew, position: Int) {


        val model = items.get(position)

        holder.tvAirlessName.text = model.segments.get(0).airline?.airlineName
        
        if (position == 0) {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_low)
            val parseColor = Color.parseColor("#f15a24")
            holder.tvPrices.setTextColor(parseColor)
        } else {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_normal)
            val parseColor = Color.BLACK
            holder.tvPrices.setTextColor(parseColor)
        }

        val fares = model.fares
        val totalPrice = CalculationHelper.getTotalFare(fares)
        holder.tvPrices.text = totalPrice


        holder.tvAdult.text = "${requestAirSearch.adultQuantity}  Adult"
        holder.tvchildAndInfant.text = "${requestAirSearch.childQuantity}  Child, ${requestAirSearch.infantQuantity} Infant"


        val recyclerViewLayoutManager = GridLayoutManager(context, 2)


        holder.recyclerView.setLayoutManager(recyclerViewLayoutManager)

        holder.recyclerView.adapter = FlightRecycleViewAdapter(context, items.get(position).segments, requestAirSearch)


        val airlineCode = model.segments.get(0).airline?.airlineCode


        val url = "https://notify.paywellonline.com/airlines/images_airline/${airlineCode}_40x35.png"
        com.orhanobut.logger.Logger.v("Airless logo Url " + url)
        val imageUri = Uri.parse(url)

        holder.airlineSerachIcon.setImageURI(imageUri)

        holder.btDetails.setOnClickListener {
            onClickListener.onClick(position)
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNew {
        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.flight_list_item_new, parent, false)
        return ViewHolderNew(view)
    }


    override fun getItemCount(): Int {
        return items.size
    }


}

interface OnClickListener {

    fun onClick(position: Int)
}


class ViewHolderNew(view: View) : RecyclerView.ViewHolder(view) {
    val tvAirlessName = view.tvAirlessName
    val tvPrices = view.tvPrices
    val btDetails = view.btDetails
    val ivTake = view.ivTake
    val tvAdult = view.tvAdult
    val tvchildAndInfant = view.tvchildAndInfant
    val recyclerView = view.recyclerView
    val airlineSerachIcon = view.airlineSerachIcon


}
