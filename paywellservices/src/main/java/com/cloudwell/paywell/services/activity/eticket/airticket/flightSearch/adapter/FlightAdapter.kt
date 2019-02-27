package com.cloudwell.paywell.services.activity.eticket.airticket.flightSearch.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.model.Result
import kotlinx.android.synthetic.main.flight_list_item.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 19/2/19.
 */
class FlightAdapter(val items: List<Result>, val context: Context) : RecyclerView.Adapter<ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items.get(position)
        if (position == 0) {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_low)
        } else {
            holder.ivTake.setImageResource(com.cloudwell.paywell.services.R.drawable.ic_tk_symbol_normal)
        }

        holder.tvPrices?.text = "" + model.totalFare
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
    val tvPrices = view.tvPrices
    val ivTake = view.ivTake

}
