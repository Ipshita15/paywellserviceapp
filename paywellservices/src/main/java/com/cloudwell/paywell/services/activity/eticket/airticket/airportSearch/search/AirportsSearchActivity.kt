package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter.AirportSerachAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter.HeaderAirportRecyclerViewSection
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter.HeaderAirportRecyclerViewSectionFilter
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.viewModel.AirportSerachViewModel
import com.cloudwell.paywell.services.app.AppHandler
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.orhanobut.logger.Logger
import com.squareup.otto.Subscribe
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_city_search.*
import kotlinx.android.synthetic.main.content_airport_serach.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


class AirportsSearchActivity : AirTricketBaseActivity() {

    // view

    lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    lateinit var mAirTicketBaseViewMode: AirportSerachViewModel


    var CITY_NAME = "cityName"
    var AIRPORT_NAME = "airport"
    var IS_TO = "isTo"
    var isIndian = false

    var VALUE_FROM = "from"
    var fromValue = ""
    var isTo = false

    lateinit var mAdapter: AirportSerachAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_search)

        getSupportActionBar()?.hide();

        isTo = intent.extras.getBoolean(IS_TO, false)
        if (!isTo) {
            tvToOrFrom.text = getString(R.string.from)
        } else {
            tvToOrFrom.text = getString(R.string.to)
        }


        isIndian = intent.extras.getBoolean("indian", false)


        fromValue = intent.extras.getString(VALUE_FROM, "")

        initViewInitialization()
        initViewModel()


        hiddenSoftKeyboard()

    }


    private fun initViewInitialization() {

        ivClose.setOnClickListener {
            finish()
        }

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


            }

            override fun afterTextChanged(s: Editable) {
                Logger.v("" + s.toString())
                if (s.isNotEmpty()) {
                    Logger.v("---" + s.toString())
                    setupFilterList(s)

                } else {
                    setupAirportList(mAirTicketBaseViewMode.allAirportHashMap.value, false)

                }
            }
        })
    }

    private fun setupFilterList(s: CharSequence) {

        doAsync {
            var groupBy: Map<String, List<Airport>> = mutableMapOf()
            if (!mAirTicketBaseViewMode.airports.isNullOrEmpty()) {
                val filter = filter(mAirTicketBaseViewMode.airports, s.toString())

                groupBy = filter.groupBy {
                    it.country

                }

            }

            uiThread {


                setupAirportList(groupBy.toMutableMap(), true)
            }
        }


    }

    private fun addToRecentSearch(airport: Airport) {
//        airport.status = "recent"
//        mAirTicketBaseViewMode.addRecentSearch(airport)

    }

    private fun backResult(single: Airport) {
        AppStorageBox.put(applicationContext, AppStorageBox.Key.AIRPORT, single)

        val intent = Intent()
        intent.putExtra("from", fromValue)
        intent.putExtra("isTo", isTo)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initViewModel() {
        mAirTicketBaseViewMode = ViewModelProviders.of(this).get(AirportSerachViewModel::class.java)


        mAirTicketBaseViewMode.baseViewStatus.observe(this, Observer {
            handleViewCommonStatus(it)
        })

        mAirTicketBaseViewMode.allAirportHashMap.observe(this, Observer {
            setupAirportList(it, false)
        })
        mAirTicketBaseViewMode.mViewStatus.observe(this, Observer {

            it?.let { it1 -> handleStatus(it1) }
        })


        mAirTicketBaseViewMode.getData(isInternetConnection, isIndian, AppHandler.getmInstance(applicationContext));


    }

    private fun handleStatus(it: AirportSeachStatus) {

        if (it.isShowProcessIndicatior) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }


    }

    private fun setupAirportList(allAirportsMap: MutableMap<String, List<Airport>>?, isFilter: Boolean) {

        sectionAdapter = SectionedRecyclerViewAdapter()

        var i = 0
        allAirportsMap?.forEach { (k, v) ->
            if (isFilter) {
                val sectionData = HeaderAirportRecyclerViewSectionFilter(k, v)
                sectionAdapter.addSection(sectionData)
            } else {
                val sectionData = HeaderAirportRecyclerViewSection(k, v)
                sectionAdapter.addSection(sectionData)
            }
            i++
        }


        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        var columns: Int;
        if (dpWidth > 320) {
            columns = 2
        } else {
            columns = 2
        }

        if (isFilter) {
            columns = 1
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


        val sectionHeader = findViewById<RecyclerView>(R.id.recycviewContryAndAirport)
        sectionHeader.setLayoutManager(glm)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)
        sectionHeader.isNestedScrollingEnabled = false
        sectionAdapter.notifyDataSetChanged()


        val verticalDecoration = DividerItemDecoration(sectionHeader.getContext(), DividerItemDecoration.HORIZONTAL)
        val verticalDivider = ContextCompat.getDrawable(applicationContext, R.drawable.vertical_divider_new)
        verticalDecoration.setDrawable(verticalDivider!!)


        if (etSearch.text.isEmpty()) {
            setupAirportListDefalt(mAirTicketBaseViewMode.allAirportHashMap.value)

        }


    }


    private fun setupAirportListDefalt(allAirportsMap: MutableMap<String, List<Airport>>?) {

        sectionAdapter = SectionedRecyclerViewAdapter()

        var i = 0
        allAirportsMap?.forEach { (k, v) ->

            val sectionData = HeaderAirportRecyclerViewSection(k, v)
            sectionAdapter.addSection(sectionData)

            i++
        }


        val display = this.getWindowManager().getDefaultDisplay()
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = resources.displayMetrics.density
        val dpWidth = outMetrics.widthPixels / density
        var columns: Int;
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


        val sectionHeader = findViewById<RecyclerView>(R.id.recycviewContryAndAirport)
        sectionHeader.setLayoutManager(glm)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)
        sectionHeader.isNestedScrollingEnabled = false
        sectionAdapter.notifyDataSetChanged()


        val verticalDecoration = DividerItemDecoration(sectionHeader.getContext(), DividerItemDecoration.HORIZONTAL)
        val verticalDivider = ContextCompat.getDrawable(applicationContext, R.drawable.vertical_divider_new)
        verticalDecoration.setDrawable(verticalDivider!!)


    }

    @Subscribe
    fun onFavoriteItemAdd(airport: Airport) {


        //  com.orhanobut.logger.Logger.e("'" + airport)

        addToRecentSearch(airport)
        backResult(airport)

    }

    public override fun onResume() {
        super.onResume()
        GlobalApplicationBus.getBus().register(this)
    }

    public override fun onPause() {
        super.onPause()
        GlobalApplicationBus.getBus().unregister(this)
    }

    private fun filter(items: List<Airport>, query: String): List<Airport> {
        val charSequence = query.toString().toLowerCase()

        // We implement here the filter logic
        if (charSequence == null || charSequence.length == 0) {
            // No filter implemented we return all the list
            return items

        } else {
            // We perform filtering operation
            val nPlanetList = ArrayList<Airport>()

            for (p in items) {
                if (p.airportName.toLowerCase().startsWith(charSequence) || p.iata.toLowerCase().startsWith(charSequence) || p.country.toLowerCase().startsWith(charSequence) || p.city.toLowerCase().startsWith(charSequence) || p.iso.toLowerCase().startsWith(charSequence)) {
                    nPlanetList.add(p)
                }
            }
            return nPlanetList
        }

    }


}
