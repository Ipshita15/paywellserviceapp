package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.cloudwell.paywell.services.utils.FormatHelper
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection


class HeaderAirportRecyclerViewSectionFilter(var key: String, var list: List<Airport>) : StatelessSection(R.layout.header_airport_country_filter, R.layout.item_airport_filter) {


    override fun getContentItemsTotal(): Int {
        return list.size
    }

    override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        val iHolder = holder as AirportItemViewHolderFilter
        val airport = list.get(position)


        iHolder.SubjectName.text = FormatHelper.formatText(airport.airportName)
        iHolder.tvCity.text = FormatHelper.formatText(airport.city + "/" + airport.country)

        iHolder.layout_airport_name_item.setOnClickListener {
            GlobalApplicationBus.getBus().post(airport)
        }


    }

    override fun getItemViewHolder(view: View?): RecyclerView.ViewHolder {
        return AirportItemViewHolderFilter(view!!)
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
        val hHolder = holder as CountryNameHeaderViewHHolderFilter
        hHolder.tvCountryNameFilter.setText(key)

    }

    override fun getHeaderViewHolder(view: View?): RecyclerView.ViewHolder {
        return CountryNameHeaderViewHHolderFilter(view!!)
    }
}


class CountryNameHeaderViewHHolderFilter(view: View) : RecyclerView.ViewHolder(view) {
    var tvCountryNameFilter: TextView

    init {
        tvCountryNameFilter = view.findViewById(R.id.tvCountryNameFilter) as TextView
    }
}

class AirportItemViewHolderFilter(view: View) : RecyclerView.ViewHolder(view) {
    var SubjectName: TextView
    var tvCity: TextView
    var layout_airport_name_item: ConstraintLayout


    init {
        SubjectName = view.findViewById(R.id.tvName) as TextView
        tvCity = view.findViewById(R.id.tvCity) as TextView
        layout_airport_name_item = view.findViewById(R.id.layout_airport_name_item_filter) as ConstraintLayout

    }


}


