package com.cloudwell.paywell.services.activity.eticket.airticket.bookingStatus.fragment

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloudwell.paywell.services.activity.eticket.airticket.booking.model.Datum
import com.cloudwell.paywell.services.app.storage.AppStorageBox
import kotlinx.android.synthetic.main.fragment_tricket_action_menu.view.*


class TicketActionMenuFragment : DialogFragment() {


    lateinit var onClickHandler: OnClickHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    fun setOnClickHandlerTest(onClickHandler: OnClickHandler) {
        this.onClickHandler = onClickHandler
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(com.cloudwell.paywell.services.R.layout.fragment_tricket_action_menu, container, false)
        val model = AppStorageBox.get(requireActivity(), AppStorageBox.Key.BOOKING_STATUS_ITEM) as Datum

        val m = model.message
        if (m.equals("Booked")) {
            v.btActionIssueTicket.visibility = View.VISIBLE
            v.btCencel.visibility = View.VISIBLE

            v.btActionDocsInfoUpdate.visibility = View.GONE
            v.btTicketRefund.visibility = View.GONE
            v.btTicketVoid.visibility = View.GONE
            v.btActionDocsInfoUpdate.visibility = View.GONE
            v.btActionReissue.visibility = View.GONE

        } else if (m.equals("Ticketed")) {

            v.btActionDocsInfoUpdate.visibility = View.VISIBLE
            v.btActionReissue.visibility = View.VISIBLE
            v.btTicketRefund.visibility = View.VISIBLE
            v.btTicketVoid.visibility = View.VISIBLE


            v.btActionIssueTicket.visibility = View.GONE
            v.btCencel.visibility = View.GONE

        }

        v.btActionIssueTicket.setOnClickListener {
            dismiss()
            onClickHandler.onClickIsisThicketButton()
        }



        v.btActionDocsInfoUpdate.setOnClickListener {
            dismiss()
            onClickHandler.onDocsUpdate(model)
        }

        v.btActionReissue.setOnClickListener {
            dismiss()
            onClickHandler.onReschedule(model)
        }

        v.btCencel.setOnClickListener {
            dismiss()
            onClickHandler.onClickCancelButton()
        }

        v.btTicketRefund.setOnClickListener {
            dismiss()
            onClickHandler.onTicketRefund(model)
        }


        v.btTicketVoid.setOnClickListener {
            dismiss()
            onClickHandler.onTicketVoid(model)
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


    interface OnClickHandler {
        fun onClickCancelButton()
        fun onClickIsisThicketButton()
        fun onDocsUpdate(item: Datum)
        fun onReschedule(item: Datum)
        fun onTicketRefund(item: Datum)
        fun onTicketVoid(item: Datum)

    }

}
