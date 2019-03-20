package com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.fragment

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cloudwell.paywell.services.activity.eticket.airticket.AirTicketMainActivity
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResAirPreBooking
import com.cloudwell.paywell.services.activity.eticket.airticket.finalReview.model.ResBookingAPI
import com.cloudwell.paywell.services.activity.eticket.airticket.flightDetails1.model.Fare
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_booking_success.view.*


/**
 * Created by Kazi Md. Saidul Email: Kazimdsaidul@gmail.com  Mobile: +8801675349882 on 4/3/19.
 */

class BookingSuccessDialog : DialogFragment() {
    lateinit var resBookingAPI: ResBookingAPI
    lateinit var bookingDialogListener: BookingDialogListener

    companion object {
        var fare = Fare()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        isCancelable = false

        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_booking_success, container, false)
        resBookingAPI = AppStorageBox.get(activity?.applicationContext, AppStorageBox.Key.AIR_BOOKKING) as ResBookingAPI

        v.tvFare.text = resBookingAPI.bookingID
        v.tvMessage.text = resBookingAPI.message

        v.tvFare.setOnClickListener {
            val cm = activity?.application?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            cm.setText(v.tvFare.getText())
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
        }





        v.btBooking.setOnClickListener {
            val intent = Intent(activity, AirTicketMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent)
        }
        return v
    }

    interface BookingDialogListener {
        fun onBooking(resAirPreBooking: ResAirPreBooking)
    }


}
