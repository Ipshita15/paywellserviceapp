package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.CalculationHelper
import kotlinx.android.synthetic.main.fragment_earnings.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class EarningsFragment() : Fragment() {
    var fare = mutableListOf<Fare>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fare = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.FARE_DATA) as MutableList<Fare>
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_earnings, container, false)


        val retailerEaring = CalculationHelper.retailerEarning(fare)


        v.tvEarning.text = retailerEaring


        return v
    }


}
