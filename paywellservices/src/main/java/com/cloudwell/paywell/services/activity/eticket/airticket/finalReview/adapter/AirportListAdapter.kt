package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Airport
import kotlinx.android.synthetic.main.item_airport_summay.view.*

class AirportListAdapter(var context: Context, var items: List<Airport>) : RecyclerView.Adapter<AirportListAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = LayoutInflater.from(context).inflate(R.layout.item_airport_summay, parent, false)
        return ViewHolder(view)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val airport = items.get(position)

        val counter = position + 1
        holder.tvAirport.text = context.getString(R.string.airport) + " " + counter
        holder.tvAirportCode.text = context.getString(com.cloudwell.paywell.services.R.string.airport_code) + " ${airport?.airportCode}"
        holder.tvAirportName.text = context.getString(com.cloudwell.paywell.services.R.string.airport_name) + " ${airport?.airportName}"
        holder.tvTerminal.text = context.getString(com.cloudwell.paywell.services.R.string.terminal) + " ${airport?.terminal}"
        holder.tvCityCode.text = context.getString(com.cloudwell.paywell.services.R.string.city_code) + " ${airport?.cityCode}"
        holder.tvCityName.text = context.getString(com.cloudwell.paywell.services.R.string.city_name) + " ${airport?.cityName}"
        holder.tvCountryCode.text = context.getString(com.cloudwell.paywell.services.R.string.country_code) + " ${airport?.countryCode}"
        holder.tvCountryName.text = context.getString(com.cloudwell.paywell.services.R.string.country_name) + " ${airport?.countryName}"


    }


    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvAirport = v.tvAirport
        val tvAirportCode = v.tvAirportCode
        val tvAirportName = v.tvAirportName
        val tvTerminal = v.tvTerminal
        val tvCityCode = v.tvCityCode
        val tvCityName = v.tvCityName
        val tvCountryCode = v.tvCountryCode
        val tvCountryName = v.tvCountryName


    }

}

