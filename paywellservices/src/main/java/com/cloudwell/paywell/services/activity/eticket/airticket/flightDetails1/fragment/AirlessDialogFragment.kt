package com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Airline
import kotlinx.android.synthetic.main.fragment_airless_info.view.*


class AirlessDialogFragment : DialogFragment() {
    lateinit var airline: Airline
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        airline = arguments?.getParcelable<Airline>("object") as Airline

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_airless_info, container, false)
        v.tvAirlineCode.text = airline.airlineCode
        v.tvAirlineName.text = airline.airlineName
        v.tvBookingClass.text = airline.bookingClass
        v.tvCabinClass.text = airline.cabinClass
        v.tvFlightNumber.text = airline.flightNumber
        v.tvOperatingCarrier.text = airline.operatingCarrier

        v.tvOk.setOnClickListener {
            dismiss()
        }

        v.tvCencel.setOnClickListener {
            dismiss()
        }


        return v
    }

}
