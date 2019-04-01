package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.FlightDetails1Status
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter.ExpandableListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.viewModel.FlightDetails1ViewModel
import kotlinx.android.synthetic.main.fragment_roles.view.*


class RolesFragment : Fragment() {

    private lateinit var mFlightDetails1ViewModel: FlightDetails1ViewModel


    // view
    var listAdapter: ExpandableListAdapter? = null
    var expListView: ExpandableListView? = null
    var listDataHeader = mutableListOf<String>()
    var listDataChild = mutableMapOf<String, ArrayList<String>>()
    lateinit var lvExp: ExpandableListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFlightDetails1ViewModel = ViewModelProviders.of(activity!!).get(FlightDetails1ViewModel::class.java!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_roles, container, false)


        // preparing list data
        lvExp = v.lvExp as ExpandableListView

        mFlightDetails1ViewModel.baseViewStatus.observe(this, android.arch.lifecycle.Observer {
            val baggageAndPoliciesActiivty = activity as BaggageAndPoliciesActiivty
            baggageAndPoliciesActiivty.handleViewCommonStatus(it)

        })


        mFlightDetails1ViewModel.mViewStatus.observe(this, Observer {
            it?.let { it1 -> handleViewStatus(it1) }
        })


        mFlightDetails1ViewModel.mListMutableLiveDataAirRules.observe(this, android.arch.lifecycle.Observer {

            it?.let { it1 -> displayData(it1) }

        })

        val requestAirPriceSearch = RequestAirPriceSearch()
//        requestAirPriceSearch.searchId = "effd3517-ee7c-4363-87ad-59333cd5f365"
//        requestAirPriceSearch.resultID = "3e55bbb0-2897-49ad-bbf2-f04b465d0b05"

        mFlightDetails1ViewModel.callAirRolesAPI(requestAirPriceSearch)


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


        it1.forEach {

            listDataHeader.add(it.ruleType)
//            val ruleDetails = mutableListOf<String>().add(it.ruleDetails)
            val arrayListOf = arrayListOf<String>()
            arrayListOf.add(it.ruleDetails)


            listDataChild.put(it.ruleType, arrayListOf)

        }


        listAdapter = ExpandableListAdapter(activity?.applicationContext!!, listDataHeader, listDataChild)

        // setting list mAdapter
        lvExp.setAdapter(listAdapter)

        listAdapter?.notifyDataSetChanged()

    }

    private fun prepareListData() {


        // Adding child data
        (listDataHeader as ArrayList<String>).add("Top 250")
        // Adding child data
        val top250 = ArrayList<String>()
        top250.add("The Shawshank Redemption")



        listDataChild!![listDataHeader.get(0)] = top250 // Header, Child data

    }


}
