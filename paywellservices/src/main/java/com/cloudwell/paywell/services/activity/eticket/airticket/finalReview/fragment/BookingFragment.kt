package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_booking.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class BookingFragment : DialogFragment() {
    lateinit var resAirPreBooking: ResAirPreBooking
    lateinit var bookingDialogListener: BookingDialogListener

    companion object {
        var fare = Fare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        resAirPreBooking = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING) as ResAirPreBooking

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_booking, container, false)
        v.btBooking.setOnClickListener {
            bookingDialogListener.onBooking(resAirPreBooking)
            dismiss()
        }

        return v
    }

    interface BookingDialogListener {
        fun onBooking(resAirPreBooking: ResAirPreBooking)
    }


}
