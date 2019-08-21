package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import kotlinx.android.synthetic.main.fragment_flight_fare.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */
class FlightFareDialogFragment() : DialogFragment() {

    companion object {
        var fare = Fare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fare = arguments?.getParcelable<Fare>("object") as Fare

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_flight_fare, container, false)
        v.tvFare.text = "${fare.baseFare}"
        v.tvTax.text = "${fare.tax}"
        v.tvDepartTime.text = "${fare.currency}"
        v.tvArrivalTime.text = "${fare.otherCharges}"
        v.tvAirportName.text = "${fare.discount}"
        v.tvPaxType.text = "${fare.paxType}"
        v.tvPassengerCount.text = "${fare.passengerCount}"
        v.tvServiceFee.text = "${fare.serviceFee}"


        v.tvOk.setOnClickListener {
            dismiss()
        }

        v.tvCencel.setOnClickListener {
            dismiss()
        }


        return v
    }


}
