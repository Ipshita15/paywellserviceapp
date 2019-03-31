package com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.View
import android.widget.AdapterView
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.AirTricketBaseActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter.HeaderAirportRecyclerViewSection
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.adapter.ListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.model.Airport
import com.cloudwell.paywell.services.activity.eticket.airticket.airportSearch.search.viewModel.AirportSerachViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.eventBus.GlobalApplicationBus
import com.squareup.otto.Subscribe
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_city_search.*
import kotlinx.android.synthetic.main.content_airport_serach.*
import java.util.*


class AirportsSearchActivity : AirTricketBaseActivity() {

    // view
    lateinit var sectionData: HeaderAirportRecyclerViewSection
    lateinit var sectionAdapter: SectionedRecyclerViewAdapter
    private lateinit var mAirTicketBaseViewMode: AirportSerachViewModel


    lateinit var allAirports: ArrayList<Airport>
    lateinit var allAirportsCity: ArrayList<String>
    var CITY_NAME = "cityName"
    var AIRPORT_NAME = "airport"
    var IS_TO = "isTo"
    var isIndian = false

    var VALUE_FROM = "from"
    var fromValue = ""
    var isTo = false

    lateinit var adapter: ListAdapter


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
        allAirports = ArrayList()
        allAirportsCity = ArrayList()

        ivClose.setOnClickListener {
            finish()
        }

//        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, allAirports)


        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {


                if (s.length > 0) {
                    recycviewContryAndAirport.visibility = View.GONE
                    searchListView.visibility = View.VISIBLE

                    if (::adapter.isInitialized) {
                        adapter.filter.filter(s)
                    }

                } else {
                    recycviewContryAndAirport.visibility = View.VISIBLE
                    searchListView.visibility = View.GONE
                }

            }

            override fun afterTextChanged(s: Editable) {

            }
        })
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
            handleDisplayData(it)
        })
        mAirTicketBaseViewMode.mViewStatus.observe(this, Observer {

            it?.let { it1 -> handleStatus(it1) }
        })


        mAirTicketBaseViewMode.getData(isInternetConnection, isIndian);

    }

    private fun handleStatus(it: AirportSeachStatus) {

        if (it.isShowProcessIndicatior) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }


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
                allAirports.add(name)
                allAirportsCity.add(name.city)

            }

        adapter = ListAdapter(this, R.layout.custom_layout, allAirports)

        searchListView.adapter = adapter
        searchListView.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->

            val airportName = adapter.getItem(position).airportName
            val single = mAirTicketBaseViewMode.resGetAirports.airports.filter { s -> s.airportName == airportName }.single()

            addToRecentSearch(single)
            backResult(single)
        }


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


        val sectionHeader = findViewById(com.cloudwell.paywell.services.R.id.recycviewContryAndAirport) as RecyclerView
        sectionHeader.setLayoutManager(glm)
        sectionHeader.setHasFixedSize(true)
        sectionHeader.setAdapter(sectionAdapter)
        sectionHeader.isNestedScrollingEnabled = false


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


}
