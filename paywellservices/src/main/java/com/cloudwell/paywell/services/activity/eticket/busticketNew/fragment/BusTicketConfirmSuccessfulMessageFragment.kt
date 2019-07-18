package com.cloudwell.paywell.services.activity.eticket.busticketNew.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.ResPaymentBookingAPI
import kotlinx.android.synthetic.main.fragment_bus_ticket_confirm_successfull_fragment.view.*


class BusTicketConfirmSuccessfulMessageFragment : DialogFragment() {

    companion object {
        lateinit var model: ResPaymentBookingAPI
    }

    private var onClicklistener: MyClickListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickListener(onClicklistener: MyClickListener) {
        this.onClicklistener = onClicklistener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_bus_ticket_confirm_successfull_fragment, container, false)
        v.tvMessage.text = model.meassage
        v.tvInfoTicketNo.text = model.ticketInfo?.ticketNo
        v.tvBoardingPointName.text = model.ticketInfo?.boardingPointName
        v.tvTotalAmount.text = model.ticketInfo?.totalAmount
        v.tvWebBookingId.text = model.ticketInfo?.webBookingInfoId
        v.tvBookingId.text = model.ticketInfo?.bookingInfoId


        v.tvOk.setOnClickListener {
            onClicklistener?.onClick()
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

    interface MyClickListener {
        fun onClick()

    }


}




