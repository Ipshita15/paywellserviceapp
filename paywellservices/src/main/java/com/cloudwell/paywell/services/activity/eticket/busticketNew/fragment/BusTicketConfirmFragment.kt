package com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.TicketInfoSeatBookAndCheck
import kotlinx.android.synthetic.main.fragment_bus_ticket_confirm_fragment.view.*
import kotlinx.android.synthetic.main.fragment_prices_change.view.tvYourSeats


class BusTicketConfirmFragment : DialogFragment() {

    companion object {
        lateinit var ticketInfo: TicketInfoSeatBookAndCheck
    }

    private var onClicklistener: MyClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickListener(onClicklistener: MyClickListener) {
        this.onClicklistener = onClicklistener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bus_ticket_confirm_fragment, container, false)
        v.tvYourSeats.text = "Your Seat : " + ticketInfo.seats
        v.tvTotalPrices.text = "Total Price : " + ticketInfo.paidAmount

        v.tvOk.setOnClickListener {
            dismiss()
            onClicklistener?.onClick()
        }
        v.btCancel.setOnClickListener {
            dismiss()
        }

        return v
    }

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }


}

interface MyClickListener {
    fun onClick()

}


