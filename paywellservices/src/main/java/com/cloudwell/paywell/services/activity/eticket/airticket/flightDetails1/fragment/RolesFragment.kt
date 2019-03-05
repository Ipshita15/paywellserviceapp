package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.RequestAirPriceSearch
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.airRules.Datum
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.viewModel.FlightDetails1ViewModel
import iammert.com.expandablelib.Section
import kotlinx.android.synthetic.main.fragment_roles.*


class RolesFragment : Fragment() {

    private lateinit var mFlightDetails1ViewModel: FlightDetails1ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFlightDetails1ViewModel = ViewModelProviders.of(activity!!).get(FlightDetails1ViewModel::class.java!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val inflate = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_roles, container, false)


        mFlightDetails1ViewModel.mListMutableLiveDataAirRules.observe(this, android.arch.lifecycle.Observer {


            it?.let { it1 -> displayData(it1) }

        })

        val requestAirPriceSearch = RequestAirPriceSearch()
        requestAirPriceSearch.searchId = "effd3517-ee7c-4363-87ad-59333cd5f365"
        requestAirPriceSearch.resultID = "3e55bbb0-2897-49ad-bbf2-f04b465d0b05"

        mFlightDetails1ViewModel.callAirRolesAPI(requestAirPriceSearch)

        return inflate
    }

    private fun displayData(it1: List<Datum>) {


        it1.forEach {

            val section = Section<String, String>()
            val ruleType = it.ruleType
            val ruleDetails = it.ruleDetails


//defaut is false
            section.expanded = true

            val fruitCategory = ruleType
            val fruit1 = ruleDetails


            section.parent = fruitCategory
            section.children.add(fruit1)

            el.addSection(section)
            el.addChild(fruitCategory, fruit1);


        }

    }


}
