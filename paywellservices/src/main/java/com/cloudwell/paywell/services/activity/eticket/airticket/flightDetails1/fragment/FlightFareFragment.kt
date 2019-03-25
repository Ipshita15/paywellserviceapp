package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_flight_fare.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class FlightFareFragment() : Fragment() {
    lateinit var fare: Fare

    companion object {
        var fare = Fare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fare = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.FARE_DATA) as Fare


        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_flight_fare_fragment, container, false)
        v.tvFare.text = "${fare.baseFare}"
        v.tvRefunableForBooking.text = "${fare.tax}"
        v.tvDepartTime.text = "${fare.currency}"
        v.tvArrivalTIme.text = "${fare.otherCharges}"
        v.tvAirlinesName.text = "${fare.discount}"
        v.tvPaxType.text = "${fare.paxType}"
        v.tvPassengerCount.text = "${fare.passengerCount}"
        v.tvServiceFee.text = "${fare.serviceFee}"


        return v
    }


}
