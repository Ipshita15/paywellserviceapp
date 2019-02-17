package com.cloudwell.paywell.services.activity.eticket.airticket

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.cloudwell.paywell.services.R

class RoundTripFragment : Fragment() {

    lateinit var tvClass: TextView
    lateinit var tvAdult: TextView
    lateinit var tvKid: TextView
    lateinit var tvInfant: TextView
    lateinit var llPassenger: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_round_trip, container, false)

        tvClass = view.findViewById(R.id.airTicketClass)
        llPassenger = view.findViewById(R.id.llPsngr)
        tvAdult = view.findViewById(R.id.airTicketAdult)
        tvKid = view.findViewById(R.id.airTicketKid)
        tvInfant = view.findViewById(R.id.airTicketInfant)

        tvClass.setOnClickListener {

            val b = Bundle()
            b.putString("myClassName", tvClass.text.toString())

            val bottomSheet = ClassBottomSheetDialog()
            bottomSheet.arguments = b
            bottomSheet.show(fragmentManager, "classBottomSheet")
        }

        llPassenger.setOnClickListener {

            val b = Bundle()
            b.putString("myAdult", tvAdult.text.toString())
            b.putString("myKid", tvKid.text.toString())
            b.putString("myInfant", tvInfant.text.toString())

            val passengerBottomSheet = PassengerBottomSheetDialog()
            passengerBottomSheet.arguments = b
            passengerBottomSheet.show(fragmentManager, "psngrBottomSheet")
        }
        return view
    }

    fun onClassTextChange(text: String) {
        tvClass.setText(text)
    }

    fun onAdultPsngrTextChange(text: String) {
        tvAdult.setText(text)
    }

    fun onKidPsngrTextChange(text: String) {
        tvKid.setText(text)
    }

    fun onInfantPsngrTextChange(text: String) {
        tvInfant.setText(text)
    }
}
