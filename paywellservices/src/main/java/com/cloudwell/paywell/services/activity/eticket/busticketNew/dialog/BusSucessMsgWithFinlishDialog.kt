package com.cloudwell.paywell.services.activity.utility.pallibidyut.bill.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.R
import com.cloudwell.paywell.services.activity.base.BaseDialogFragment
import com.cloudwell.paywell.services.activity.eticket.busticketNew.model.new_v.ticket_confirm.ResposeTicketConfirm
import kotlinx.android.synthetic.main.bus_ticket_success_msg_dialog.view.*


class BusSucessMsgWithFinlishDialog(val it: ResposeTicketConfirm, val isRetrunTriple: Boolean) : BaseDialogFragment() {
    interface MyClickListener {
        fun onClick()
    }

    lateinit var myClickListener: MyClickListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = layoutInflater.inflate(R.layout.bus_ticket_success_msg_dialog, null)
        isCancelable = false



        if (!isRetrunTriple) {
            view?.ConstLayoutRetrun?.visibility = View.GONE
            view?.contLayoutTicketReturn?.visibility = View.GONE
        } else {

            val retrun = it.ticketData?.get(1)

            view?.tvAddressRetrun?.text = "${retrun?.journeyRoute}"
            view?.tvBusNameRetrun?.text = "${retrun?.transportName}"
            view?.tvSeatNumberRetrun?.text = "${retrun?.seatLbls}"
            view?.tvboadingPointRount?.text = "${retrun?.boardingPointName}"
            view?.tvRetrunTicketNo?.text = "Ticket no:${retrun?.ticketNo}"

        }

        val single = it.ticketData?.get(0)

        view?.tvAddressOneWay?.text = "Journey route: ${single?.journeyRoute}"
        view?.tvBusNameOneWay?.text = "Transport name: ${single?.transportName}"
        view?.tvSeatNumberOneWay?.text = "Seat no: ${single?.seatLbls}"
        view?.tvBoardingPoint?.text = "Boarding Point name: ${single?.boardingPointName}"
        view?.tvTotalAAmont?.text = "Ticket no:${single?.ticketNo}"

        view?.btnOtpErrorCall?.setOnClickListener {
            myClickListener.onClick()
        }

        view?.tvMessage?.text = "${it.message}"

        return view

    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }

    fun setOnClickListener(myClickListener: MyClickListener) {
        this.myClickListener = myClickListener

    }


}