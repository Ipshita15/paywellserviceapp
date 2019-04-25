package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter

import android.content.Context
import android.graphics.Typeface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import kotlinx.android.synthetic.main.layout_test.view.*

class FareListAdapter(var context: Context, var items: List<Fare>) : RecyclerView.Adapter<FareListAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FareListAdapter.ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.layout_test, parent, false)
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
        holder.tvConvenienceFee.text = "" + model.convenienceFee
        holder.tvAmount.text = "" + model.amount


        if (items.lastIndex == position) {
            holder.tvLAmount.text = "Total Amount"
            holder.tvLAmount.setTypeface(null, Typeface.BOLD);
            holder.tvAmount.text = "" + model.amount
            holder.tvAmount.setTypeface(null, Typeface.BOLD);


            holder.tableRow1.visibility = View.GONE
            holder.tableRow2.visibility = View.GONE
            holder.tableRow3.visibility = View.GONE
            holder.tableRow4.visibility = View.GONE
            holder.tableRow5.visibility = View.GONE
            holder.tableRow6.visibility = View.GONE
            holder.tableRow7.visibility = View.GONE
            holder.tableRow8.visibility = View.GONE


//            holder.tvLBaseFare.visibility = View.GONE
//            holder.tvLaTax.visibility = View.GONE
//            holder.tvLCurrency.visibility = View.GONE
//            holder.tvLOtherCharge.visibility = View.GONE
//            holder.tvLPaxType.visibility = View.GONE
//            holder.tvLPassengerCounter.visibility = View.GONE
//            holder.tvLService.visibility = View.GONE
//            holder.tvLconvercencFree.visibility = View.GONE
//
//
//
//            holder.tvFareBaseFare.visibility = View.GONE
//            holder.tvTax.visibility = View.GONE
//            holder.tvCurrency.visibility = View.GONE
//            holder.tvOtherCarge.visibility = View.GONE
//            holder.tvPaxType.visibility = View.GONE
//            holder.tvPassengerCount.visibility = View.GONE
//            holder.tvServiceFee.visibility = View.GONE
//            holder.tvConvenienceFee.visibility = View.GONE


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
        val tvLaTax = v.tvLaTax
        val tvLCurrency = v.tvLCurrency
        val tvLOtherCharge = v.tvLOtherCharge
        val tvLPaxType = v.tvLPaxType
        val tvLPassengerCounter = v.tvLPassengerCounter
        val tvLService = v.tvLService
        val tvLconvercencFree = v.tvLconvercencFree
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
