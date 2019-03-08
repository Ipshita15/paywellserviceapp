package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails2.model.Passenger
import kotlinx.android.synthetic.main.flight_list_item.view.*
import kotlinx.android.synthetic.main.passenger_list_item.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 7/3/19.
 */
class AdapterForPassengers(var context: Context, var items: List<Passenger>) : RecyclerView.Adapter<AdapterForPassengers.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(com.cloudwell.paywell.services.R.layout.passenger_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items.get(position)
        if (model.isDefault) {
            holder.ivAddPassenger.visibility = View.VISIBLE
            holder.tvAddEdit.visibility = View.VISIBLE
        } else {
            holder.ivAddPassenger.visibility = View.INVISIBLE
            holder.tvAddEdit.visibility = View.INVISIBLE
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tvStringTime = view.tvStringTime
        val ivAddPassenger = view.ivAddPassenger
        val tvAddEdit = view.tvAddEdit

    }
}