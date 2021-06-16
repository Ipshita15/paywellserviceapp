package com.cloudwell.paywell.services.activity.eticket.airticket.transationLog.viewBookingInfo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Airport
import kotlinx.android.synthetic.main.item_airport_booking_info.view.*

/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 2019-05-13.
 */
class AirportListBookingInfoAdapter(var context: Context, var items: List<Airport>) : RecyclerView.Adapter<AirportListBookingInfoAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.item_airport_booking_info, parent, false)
        return ViewHolder(view)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val airport = items.get(position)

        val counter = position + 1
        holder.tvAirport.text = context.getString(R.string.airport) + " " + counter
        holder.tvAirportCode.text = context.getString(com.cloudwell.paywell.services.R.string.airport_code) + " ${airport?.airportCode}"
        holder.tvAirportName.text = context.getString(com.cloudwell.paywell.services.R.string.airport_name) + " ${airport?.airportName}"
        holder.tvTerminal.text = context.getString(com.cloudwell.paywell.services.R.string.terminal) + " ${airport?.terminal}"
        holder.tvCityName.text = context.getString(com.cloudwell.paywell.services.R.string.city_name) + " ${airport?.cityName}"

        holder.tvCountryName.text = context.getString(com.cloudwell.paywell.services.R.string.country_name) + " ${airport?.countryName}"


    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvAirport = v.tvAirport
        val tvAirportCode = v.tvAirportCode
        val tvAirportName = v.tvAirportName
        val tvTerminal = v.tvTerminal
        //        val tvCityCode = v.tvCityCode
        val tvCityName = v.tvCityName
        //        val tvCountryCode = v.tvCountryCode
        val tvCountryName = v.tvCountryName


    }

}