package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.adapter.HeaderAirportRecyclerViewSection
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.viewModel.AirportSerachViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter


class AirportsSearchActivity : AppCompatActivity() {

    // view

    lateinit var sectionAdapter: SectionedRecyclerViewAdapter;
    private lateinit var mAirTicketBaseViewMode: AirportSerachViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_city_search)
        initViewModel()

    }


    private fun initViewModel() {
        mAirTicketBaseViewMode = ViewModelProviders.of(this).get(AirportSerachViewModel::class.java)

        mAirTicketBaseViewMode.allAirportHashMap.observe(this, Observer {
            handleDisplayData(it)
        })


//        mViewModelAir.baseViewStatus.observe(this, Observer {
//            handleViewCommonStatus(it)
//        })

        //  mAirTicketBaseViewMode.getData(isInternetConnection);

    }

    private fun handleDisplayData(allAirportsMap: MutableMap<String, List<Airport>>?) {

        sectionAdapter = SectionedRecyclerViewAdapter()

        allAirportsMap?.forEach { (k, v) ->

            val sectionData = HeaderAirportRecyclerViewSection(k, v)
            sectionAdapter.addSection(sectionData)
        }


//        for ((index, value) in allAirportsMap.withIndex()) {
//            val sectionData = HeaderAirportRecyclerViewSection(index, value, allAirportsMap.get(value))
//            sectionAdapter.addSection(sectionData)
//        }

        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        val columns: Int;
        if (dpWidth > 320) {
            columns = 2
        } else {
            columns = 2
        }

        val glm = GridLayoutManager(applicationContext, columns)
        glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (sectionAdapter.getSectionItemViewType(position)) {
                    SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER ->
                        return columns
                    else ->
                        return 1
                }
            }
        }

        val sectionHeader = findViewById<RecyclerView>(com.cloudwell.paywell.services.R.id.recycviewContryAndAirport) as RecyclerView
        sectionHeader.setLayoutManager(glm)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)
        sectionHeader.isNestedScrollingEnabled = false;
    }


}
