package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.BaggageAndPoliciesActiivty
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Status
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter.ExpandableListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.viewModel.FlightDetails1ViewModel
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_roles.view.*


class RolesFragment : Fragment() {

    private lateinit var mFlightDetails1ViewModel: FlightDetails1ViewModel


    // view
    var listAdapter: ExpandableListAdapter? = null
    var listDataHeader = mutableListOf<String>()
    var listDataChild = mutableMapOf<String, ArrayList<String>>()
    lateinit var lvExp: ExpandableListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFlightDetails1ViewModel = ViewModelProviders.of(activity!!).get(FlightDetails1ViewModel::class.java!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_roles, container, false)


        // preparing list data
        lvExp = v.lvExp as ExpandableListView

        mFlightDetails1ViewModel.baseViewStatus.observe(this, Observer {
            val baggageAndPoliciesActiivty = activity as BaggageAndPoliciesActiivty
            baggageAndPoliciesActiivty.handleViewCommonStatus(it)

        })


        mFlightDetails1ViewModel.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        mFlightDetails1ViewModel.mListMutableLiveDataAirRules.observe(this, Observer {

            it?.let { it1 -> displayData(it1) }

        })

        val serachId = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.SERACH_ID) as String
        val requestID = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.Request_ID) as String


        val requestAirPriceSearch = RequestAirPriceSearch()
        requestAirPriceSearch.resultID = requestID
        requestAirPriceSearch.searchId = serachId


        if (listDataHeader.size == 0) {
            mFlightDetails1ViewModel.callAirRolesAPI(requestAirPriceSearch)
        }



        return v
    }

    private fun handleViewStatus(it: FlightDetails1Status) {

        val baggageAndPoliciesActiivty = activity as BaggageAndPoliciesActiivty

        if (it.isShowProcessIndicator) {
            baggageAndPoliciesActiivty.showProgressDialog()

        } else {
            baggageAndPoliciesActiivty.dismissProgressDialog()

        }
    }

    private fun displayData(it1: List<Datum>) {

        listDataHeader.clear()
        listDataChild.clear()

        it1.forEach {

            listDataHeader.add(it.ruleType)
            val arrayListOf = arrayListOf<String>()
            arrayListOf.add(it.ruleDetails)


            listDataChild.put(it.ruleType, arrayListOf)

        }


        listAdapter = ExpandableListAdapter(activity?.applicationContext!!, listDataHeader, listDataChild)

        // setting list mAdapter
        lvExp.setAdapter(listAdapter)

        listAdapter?.notifyDataSetChanged()

    }


}
