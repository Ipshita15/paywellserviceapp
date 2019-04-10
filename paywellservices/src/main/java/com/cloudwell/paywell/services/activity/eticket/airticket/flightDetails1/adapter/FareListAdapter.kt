package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import kotlinx.android.synthetic.main.list_item_fare_data.view.*

class FareListAdapter(var context: Context, var items: List<Fare>) : RecyclerView.Adapter<FareListAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FareListAdapter.ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.list_item_fare_data, parent, false)
        return FareListAdapter.ViewHolder(view)


    }


    override fun onBindViewHolder(holder: FareListAdapter.ViewHolder, position: Int) {
        val model = items.get(position)

        holder.tvFareBaseFare.text = "" + model.baseFare
        holder.tvTax.text = "" + model.tax
        holder.tvCurrency.text = "" + model.currency
        holder.tvOtherCarge.text = "" + model.otherCharges
        holder.tvPaxType.text = "" + model.paxType
        holder.tvPassengerCount.text = "" + model.passengerCount
        holder.tvServiceFee.text = "" + model.serviceFee


    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvFareBaseFare = v.tvFare
        val tvTax = v.tvTax
        val tvCurrency = v.tvCurrency
        val tvOtherCarge = v.tvOtherCarge
        val tvPaxType = v.tvPaxType
        val tvPassengerCount = v.tvPassengerCount
        val tvServiceFee = v.tvServiceFee

    }


}
