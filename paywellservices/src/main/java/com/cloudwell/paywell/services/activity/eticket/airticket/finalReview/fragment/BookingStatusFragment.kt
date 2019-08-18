package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import com.cloudwell.paywell.services.utils.CalculationHelper
import com.cloudwell.paywell.services.utils.DateUtils
import kotlinx.android.synthetic.main.fragment_booking.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class BookingStatusFragment : DialogFragment() {
    lateinit var resAirPreBooking: ResAirPreBooking
    lateinit var bookingDialogListener: BookingDialogListener

    companion object {
        var fare = Fare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_booking, container, false)

        resAirPreBooking = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIR_PRE_BOOKKING) as ResAirPreBooking

        val fareType = resAirPreBooking.data?.results?.get(0)?.fareType
        if (fareType.equals("InstantTicketing")) {
            v.btBooking2.text = getString(R.string.issue_ticket)
        } else {
            v.btBooking2.text = getString(R.string.booking_now)
        }


        val segments = resAirPreBooking.data?.results

        val AIRLINE_CODE = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIRLINE_CODE) as String
        val totalFare = resAirPreBooking.data?.results?.get(0)?.fares?.let { CalculationHelper.getTotalFareDetati(it, AIRLINE_CODE) }

        v.tvFare.text = activity?.getString(R.string.total_fare_text) + ": TK. " + totalFare

        if (resAirPreBooking.data?.results?.get(0)?.isRefundable!!) {
            v.tvTax.text = getString(R.string.refundable) + ": Yes"
        } else {
            v.tvTax.text = getString(R.string.refundable) + ": No"
        }

        if (segments?.size!! > 1) {
            val arrTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.destination?.arrTime
            val depTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(segments.size - 1)?.origin?.depTime

            val arrTimeSplit = arrTime?.split("T")
            val depTimeSplit = depTime?.split("T")

            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])

            v.tvDepartTime.text = activity?.getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
            v.tvArrivalTime.text = activity?.getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]


        } else {
            val arrTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.destination?.arrTime
            val depTime = resAirPreBooking.data?.results?.get(0)!!.segments?.get(0)?.origin?.depTime
            val arrTimeSplit = arrTime?.split("T")
            val depTimeSplit = depTime?.split("T")

            val departDate = DateUtils.getFormatDate(depTimeSplit!![0])
            val arrivalDate = DateUtils.getFormatDate(arrTimeSplit!![0])

            v.tvDepartTime.text = activity?.getString(R.string.depart_time_) + " " + departDate + ", " + depTimeSplit[1]
            v.tvArrivalTime.text = activity?.getString(R.string.arrival_time) + " " + arrivalDate + ", " + arrTimeSplit[1]
        }

        v.tvAirportName.text = activity?.getString(R.string.airless_name) + " " + resAirPreBooking.data?.results?.get(0)?.segments?.get(0)?.airline?.airlineName



        v.btCancel.setOnClickListener {
            dismiss()
        }
        v.btBooking2.setOnClickListener {
            bookingDialogListener.onBooking(resAirPreBooking)
            dismiss()
        }


        return v
    }

    interface BookingDialogListener {
        fun onBooking(resAirPreBooking: ResAirPreBooking)
    }


}
