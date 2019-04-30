package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.service.CalculationHelper
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.adapter.FareListAdapter
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_flight_fare_fragment.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class FlightFareFragment : Fragment() {
    var fare = mutableListOf<Fare>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fare = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.FARE_DATA) as MutableList<Fare>

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_flight_fare_fragment, container, false)



        v.rvFareList.setNestedScrollingEnabled(false)
        val mLayoutManager = LinearLayoutManager(activity!!.applicationContext)
        v.rvFareList.setLayoutManager(mLayoutManager)
        v.rvFareList.setItemAnimator(DefaultItemAnimator())


        val temp = mutableListOf<Fare>()

        for (fare in fare) {
            val calculatorFare = CalculationHelper.getFare(fare)
            temp.add(calculatorFare)
        }


        val total = CalculationHelper.getTotalFareDetati(fare)
        val fare1 = Fare()
        fare1.amount = total
        temp.add(fare1)

        val recyclerListAdapter = FareListAdapter(activity?.applicationContext!!, temp)
        v.rvFareList.adapter = recyclerListAdapter

        val dividerItemDecoration = DividerItemDecoration(v.rvFareList.getContext(), mLayoutManager.getOrientation())
        v.rvFareList.addItemDecoration(dividerItemDecoration);

        return v
    }


}
