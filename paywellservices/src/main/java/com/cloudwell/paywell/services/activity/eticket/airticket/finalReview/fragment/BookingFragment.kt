package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
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

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_booking, container, false)

        resAirPreBooking = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING) as ResAirPreBooking


        val segments = resAirPreBooking.data?.results

        v.tvFare.text = activity?.getString(R.string.total_fare_text) + ": TK. ${resAirPreBooking.data?.results?.get(0)?.totalFare} "

        if (resAirPreBooking.data?.results?.get(0)?.isRefundable!!) {
            v.tvTax.text = getString(com.cloudwell.paywell.services.R.string.refundable) + ": Yes"
        } else {
            v.tvTax.text = getString(com.cloudwell.paywell.services.R.string.non_refundable) + ": No"
        }

        if (segments?.size!! > 1) {
            val arrTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.destination?.arrTime
            val depTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(segments.size - 1)?.origin?.depTime

            val arrTimeSplit = arrTime?.split("T")
            val depTimeSplit = depTime?.split("T")


            v.tvCurrency.text = activity?.getString(R.string.depart_time_) + " " + arrTimeSplit!![0] + "" + arrTimeSplit[1]
            v.tvOtherCarge.text = activity?.getString(R.string.arrival_time) + ": " + depTimeSplit!![0] + "" + depTimeSplit[1]

        } else {
            val arrTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.destination?.arrTime
            val depTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.origin?.depTime
            val arrTimeSplit = arrTime?.split("T")
            val depTimeSplit = depTime?.split("T")

            v.tvCurrency.text = activity?.getString(R.string.depart_time_) + " " + arrTimeSplit!![0] + "" + arrTimeSplit[1]
            v.tvOtherCarge.text = activity?.getString(R.string.arrival_time) + ": " + depTimeSplit!![0] + "" + depTimeSplit[1]
        }

        v.tvDiscount.text = activity?.getString(R.string.airport_name) + resAirPreBooking.data?.results?.get(0)?.segments?.get(0)?.airline?.airlineName



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
