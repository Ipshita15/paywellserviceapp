package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import kotlinx.android.synthetic.main.list_item_fare_data.view.*
import java.text.NumberFormat

class FareListAdapter(var context: Context, var items: List<Fare>) : RecyclerView.Adapter<FareListAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.list_item_fare_data, parent, false)
        return ViewHolder(view)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = items.get(position)

        holder.tvFareBaseFare.text = "" + NumberFormat.getInstance().format(model.baseFare)
        holder.tvTax.text = "" + NumberFormat.getInstance().format(model.tax)
        holder.tvCurrency.text = "" + model.currency
        holder.tvOtherCarge.text = "" + NumberFormat.getInstance().format(model.otherCharges)
        holder.tvPaxType.text = "" + model.paxType
        holder.tvPassengerCount.text = "" + NumberFormat.getInstance().format(model.passengerCount)
        holder.tvServiceFee.text = "" + NumberFormat.getInstance().format(model.serviceFee)
        holder.tvConvenienceFee.text = "" + NumberFormat.getInstance().format(model.convenienceFee)
        holder.tvAmount.text = "" + NumberFormat.getInstance().format((model.amount.toDouble()))



        holder.tableRow1.visibility = View.VISIBLE
        holder.tableRow2.visibility = View.VISIBLE
        holder.tableRow3.visibility = View.VISIBLE
        holder.tableRow4.visibility = View.VISIBLE
        holder.tableRow5.visibility = View.VISIBLE
        holder.tableRow6.visibility = View.VISIBLE
        holder.tableRow7.visibility = View.VISIBLE
        holder.tableRow8.visibility = View.VISIBLE

        holder.tvLAmount.visibility = View.VISIBLE

        if (items.lastIndex == position) {

            holder.tableRow9.visibility = View.GONE
            holder.tableRow2.visibility = View.GONE
            holder.tableRow3.visibility = View.GONE
            holder.tableRow4.visibility = View.GONE
            holder.tableRow5.visibility = View.GONE
            holder.tableRow6.visibility = View.GONE
            holder.tableRow7.visibility = View.GONE
            holder.tableRow8.visibility = View.GONE

            holder.tvLBaseFare.text = context.getString(R.string.total_amount)
            holder.tvLBaseFare.setTypeface(null, Typeface.BOLD);

            holder.tvFareBaseFare.text = "" + model.amount
            holder.tvFareBaseFare.setTypeface(null, Typeface.BOLD);


        }


    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvFareBaseFare = v.tvFare
        val tvTax = v.tvTax
        val tvCurrency = v.tvDepartTime
        val tvOtherCarge = v.tvArrivalTime
        val tvPaxType = v.tvPaxType
        val tvPassengerCount = v.tvPassengerCount
        val tvServiceFee = v.tvServiceFee
        val tvConvenienceFee = v.tvConvenienceFee
        val tvAmount = v.tvAmount


        val tvLBaseFare = v.tvLBaseFare
        val tvLAmount = v.tvLAmount


        val tableRow1 = v.tableRow1
        val tableRow2 = v.tableRow2
        val tableRow3 = v.tableRow3
        val tableRow4 = v.tableRow4
        val tableRow5 = v.tableRow5
        val tableRow6 = v.tableRow6
        val tableRow7 = v.tableRow7
        val tableRow8 = v.tableRow8
        val tableRow9 = v.tableRow9


    }


}
