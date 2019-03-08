package com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.adapter.HeaderAirportRecyclerViewSection
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.serach.citySerach.viewModel.AirportSerachViewModel
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_city_search.*
import kotlinx.android.synthetic.main.content_airport_serach.*


class AirportsSearchActivity : AirTricketBaseActivity() {

    // view
    lateinit var sectionData: HeaderAirportRecyclerViewSection
    lateinit var sectionAdapter: SectionedRecyclerViewAdapter;
    private lateinit var mAirTicketBaseViewMode: AirportSerachViewModel

    lateinit var allAirports: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.cloudwell.paywell.services.R.layout.activity_city_search)

        initViewInitialization()
        initViewModel()

    }

    private fun initViewInitialization() {
        allAirports = ArrayList()

        ivClose.setOnClickListener {
            finish()
        }

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allAirports)
        searchListView.adapter = adapter
        searchListView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            Log.e("logTag", adapter.getItem(position).toString())
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                    recycviewContryAndAirport.visibility = View.GONE
                    searchListView.visibility = View.VISIBLE
                    adapter.filter.filter(s)
                } else {
                    recycviewContryAndAirport.visibility = View.VISIBLE
                    searchListView.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
    }

    private fun initViewModel() {
        mAirTicketBaseViewMode = ViewModelProviders.of(this).get(AirportSerachViewModel::class.java)

        mAirTicketBaseViewMode.allAirportHashMap.observe(this, Observer {
            handleDisplayData(it)
        })


        mAirTicketBaseViewMode.getData(isInternetConnection);

    }

    private fun handleDisplayData(allAirportsMap: MutableMap<String, List<Airport>>?) {

        sectionAdapter = SectionedRecyclerViewAdapter()

        var i = 0
        allAirportsMap?.forEach { (k, v) ->


            sectionData = HeaderAirportRecyclerViewSection(k, v)
            sectionAdapter.addSection(sectionData)
            i++
        }

        for (air in allAirportsMap!!.iterator())
            for (name in air.value) {
                allAirports.add(name.airportName)
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
        sectionHeader.isNestedScrollingEnabled = false

//        sectionHeader.setOnClickListener { object: AdapterView.OnItemClickListener() }
    }

}
