package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection

class HeaderAirportRecyclerViewSectionSection() {

}

class HeaderAirportRecyclerViewSection(var key: String, var list: List<Airport>) : StatelessSection(R.layout.header_airport_country, R.layout.item_airport) {


    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val iHolder = holder as AirportItemViewHolder
        val favoriteMenu = list.get(position)

        iHolder.itemContent.text = favoriteMenu.airportName

    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        return AirportItemViewHolder(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val hHolder = holder as CountryNameHeaderViewHHolder
        hHolder.tvCountryName.setText(key)

    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        return CountryNameHeaderViewHHolder(view!!)
    }
}


class CountryNameHeaderViewHHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvCountryName: TextView

    init {
        tvCountryName = view.findViewById(R.id.tvCountryName) as TextView
    }
}

class AirportItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var itemContent: TextView


    init {
        itemContent = view.findViewById(R.id.tvAirportName) as TextView
    }
}
